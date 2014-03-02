package jfcc;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.CRC32;

public class Snapshot {

	private String snapshotPath = null;
	private File snapshotFile = null;
	private Hashtable<String, FileResume>snapshotFiles = null;
	private ArrayList<String> filesExceptions = null;
	
	public Snapshot(String pblockPath){
		this.snapshotPath = pblockPath;
		this.snapshotFile= new File(this.snapshotPath);
		this.snapshotFiles = new Hashtable<String, FileResume>();
		this.filesExceptions = new ArrayList<String>();
	}
	
	public void setFileExceptions(ArrayList<String> fileExceptions){
		this.filesExceptions=fileExceptions;
	}
	
	public void addFileException(String newException){
		this.filesExceptions.add(newException);
	}
	
	public String getPath(){
		return this.snapshotPath;
	}
	
	public Hashtable<String, FileResume> getAll(){
		return this.snapshotFiles;
	}
	
	public long getNumElements(){
		long result = 0;
		if(this.snapshotFiles!=null){
			result = this.snapshotFiles.size();
		}
		return result;
	}
	
	public void loadSnapshot(){
		this.snapshotFiles = new Hashtable<String, FileResume>();
		this.loadSnapshotpFolder(this.snapshotFile);
	}
		
	public long crcCalculator(String filepath){
		long result = -1;
		try {
			FileInputStream oFileInputStream = new FileInputStream(filepath);
			InputStream oInputStream = new BufferedInputStream(oFileInputStream);
			CRC32 crc = new CRC32();
			int cnt;
			while ((cnt = oInputStream.read()) != -1) {
				crc.update(cnt);
			}
			oInputStream.close();
			result = crc.getValue();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * getRelativePath: Get the path relative to the initial snapshot path;
	 * Windows file separator is not compatible with regex operations. file separator is changed for "/"
	 * @param absolutePath
	 * @return String
	 */
	private String getRelativePath(String absolutePath){
		String result = "";
		String pathInitial = this.snapshotFile.getAbsolutePath().replace(File.separator, "/");
		String pathCurrent = absolutePath.replace(File.separator, "/");
		result = pathCurrent.replaceFirst(pathInitial, "");
		return result;
	}
			
	private void loadSnapshotpFolder(final File folder){
		String keyFolder = getRelativePath(folder.getAbsolutePath());
		FileResume oFolderResume = new FileResume(keyFolder, FileResume.TYPE_FOLDER);
		oFolderResume.setAbsolutePath(folder.getAbsolutePath());
		this.snapshotFiles.put(keyFolder, oFolderResume);
		
	    for (final File fileEntry : folder.listFiles()) {
	    	if(filesExceptions.contains(fileEntry.getName())) continue;
	    	
	        if (fileEntry.isDirectory()) {
	        	loadSnapshotpFolder(fileEntry);
	        } else {
	    		String keyFile = getRelativePath(fileEntry.getAbsolutePath());
	    		FileResume oFileResume = new FileResume(keyFile, FileResume.TYPE_FILE);
	    		oFileResume.setAbsolutePath(fileEntry.getAbsolutePath());
	 
	    		this.snapshotFiles.put(keyFile, oFileResume);
	        }
	    }	
	}
	
	public void refreshSnapshotCRC32(){
		Iterator<String> keySetIterator = this.snapshotFiles.keySet().iterator();
		while (keySetIterator.hasNext()) {
		   String key = keySetIterator.next();
		   FileResume oFileResume = this.snapshotFiles.get(key);
		   if(oFileResume.getType()==FileResume.TYPE_FILE){
			   oFileResume.refreshCRC32();
		   }
		}
	}
		
	public void show(){
		Iterator<String> keySetIterator = this.snapshotFiles.keySet().iterator();
		while (keySetIterator.hasNext()) {
		   String key = keySetIterator.next();
		   FileResume oFileResume = this.snapshotFiles.get(key);
		   System.out.println("key: " + key + " value: " + oFileResume.getCrc32());
		}
		System.out.println(this.snapshotFiles.size());
	}
	
	public void export(){
		SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    String timeLog = oSimpleDateFormat.format(new Date());
	    String exportFilename = "snapshot_"+timeLog+".txt";
	    
		File ofile = new File(exportFilename);
		if (!ofile.exists()) {
			try {
				ofile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileWriter oFileWriter = new FileWriter(ofile.getAbsoluteFile());
			BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
			oBufferedWriter.write("[path]="+this.snapshotPath);
			oBufferedWriter.newLine();
			oBufferedWriter.write("[time]="+timeLog);
			oBufferedWriter.newLine();
			oBufferedWriter.write("[start_files_block]");
			oBufferedWriter.newLine();
				
			Iterator<String> keySetIterator = this.snapshotFiles.keySet().iterator();
			while (keySetIterator.hasNext()) {
			   String key = keySetIterator.next();
			   FileResume oFileResume = this.snapshotFiles.get(key);
			   oBufferedWriter.write(oFileResume.getName()+";"+
					   				oFileResume.getType()+";"+
					   				oFileResume.getAbsolutePath()+";"+
					   				oFileResume.getCrc32());
			   oBufferedWriter.newLine();
			}
			
			oBufferedWriter.write("[end_files_block]");
			oBufferedWriter.newLine();
			oBufferedWriter.close();
			oFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Date oInicio = new Date();
		
		//String sPath = ".";
		String sPath = "/Users/Andres/Documents/workspace/csvfix";
		//String sPath = "/Users/Andres/Documents/workspace";
		Snapshot oSnapshot = new Snapshot(sPath);
		oSnapshot.addFileException("build");
		oSnapshot.loadSnapshot();
		oSnapshot.refreshSnapshotCRC32();
		oSnapshot.show();
		oSnapshot.export();
		
		Date oFin = new Date();
		System.out.println(oFin.getTime()-oInicio.getTime());
		
	}

}
