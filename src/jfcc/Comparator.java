package jfcc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Comparator {

	private Snapshot origin = null;
	private Snapshot destination = null;
	private ArrayList<String> filesExceptions = null;
	
	public Comparator(String pathOrigen, String pathDestino){
		this.filesExceptions = new ArrayList<String>();

		this.origin = new Snapshot(pathOrigen);
		this.destination = new Snapshot(pathDestino);
	}
	
	public void addFileException(String newException){
		this.filesExceptions.add(newException);
	}
	
	public void execute(){
		this.origin.setFileExceptions(this.filesExceptions);
		this.origin.loadSnapshot();
		this.origin.refreshSnapshotCRC32();
		this.destination.setFileExceptions(this.filesExceptions);
		this.destination.loadSnapshot();
		this.destination.refreshSnapshotCRC32();
	}
	
	public void report(){
		long count_modified = 0;
		long count_new = 0;
		long count_deleted = 0;
		
		System.out.println("Origin Folder:"+this.origin.getPath());
		System.out.println("Origin Elements:"+this.origin.getNumElements());
		System.out.println("Destination Folder:"+this.destination.getPath());
		System.out.println("Destination Elements:"+this.destination.getNumElements());
				
		//check updated files
		System.out.println("");
		System.out.println("Check modified elements");
		System.out.println("=======================");
		Iterator<String> keySetIterator = this.origin.getAll().keySet().iterator();
		while (keySetIterator.hasNext()) {
			String key = keySetIterator.next();
			FileResume oFileResumeOrigin = this.origin.getAll().get(key);
			if(oFileResumeOrigin.getType()==FileResume.TYPE_FILE){
				FileResume oFileResumeDestination = this.destination.getAll().get(key);
				if(oFileResumeDestination!=null && 
					   oFileResumeOrigin.getCrc32()!=oFileResumeDestination.getCrc32()){
					count_modified+=1;
					System.out.println(oFileResumeOrigin.getName());	   
				}
			}
		}
		System.out.println("modified elements:"+count_modified);
				
		//check new files
		System.out.println("");
		System.out.println("Check new elements (Only Origin)");
		System.out.println("================================");
		keySetIterator = this.origin.getAll().keySet().iterator();
		while (keySetIterator.hasNext()) {
			String key = keySetIterator.next();
			FileResume oFileResumeOrigin = this.origin.getAll().get(key);
			FileResume oFileResumeDestination = this.destination.getAll().get(key);
			if(oFileResumeDestination==null){
				count_new+=1;
				System.out.println(oFileResumeOrigin.getName());	   
			}
		}		
		System.out.println("new elements:"+count_new);		
				
		//check deleted files
		System.out.println("");
		System.out.println("Check deleted elements (Only Destination)");
		System.out.println("=========================================");
		keySetIterator = this.destination.getAll().keySet().iterator();
		while (keySetIterator.hasNext()) {
			String key = keySetIterator.next();
			FileResume oFileResumeDestination = this.destination.getAll().get(key);
			FileResume oFileResumeOrigin = this.origin.getAll().get(key);
			if(oFileResumeOrigin==null){
				count_deleted+=1;
				System.out.println(oFileResumeDestination.getName());	   
			}
		}		
		System.out.println("deleted elements:"+count_deleted);
		System.out.println("");
		
	}
	
	public void export(){
		long count_modified = 0;
		long count_new = 0;
		long count_deleted = 0;
		
		SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    String timeLog = oSimpleDateFormat.format(new Date());
	    String exportFilename = "report_"+timeLog+".txt";
	    
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
			
			//Head
			oBufferedWriter.write("Origin Folder:"+this.origin.getPath());
			oBufferedWriter.newLine();
			oBufferedWriter.write("Origin Elements:"+this.origin.getNumElements());
			oBufferedWriter.newLine();
			oBufferedWriter.write("Destination Folder:"+this.destination.getPath());
			oBufferedWriter.newLine();
			oBufferedWriter.write("Destination Elements:"+this.destination.getNumElements());
			oBufferedWriter.newLine();
			
			//check updated files
			oBufferedWriter.newLine();
			oBufferedWriter.write("Check modified elements");
			oBufferedWriter.newLine();
			oBufferedWriter.write("=======================");
			oBufferedWriter.newLine();
			Iterator<String> keySetIterator = this.origin.getAll().keySet().iterator();
			while (keySetIterator.hasNext()) {
				String key = keySetIterator.next();
				FileResume oFileResumeOrigin = this.origin.getAll().get(key);
				if(oFileResumeOrigin.getType()==FileResume.TYPE_FILE){
					FileResume oFileResumeDestination = this.destination.getAll().get(key);
					if(oFileResumeDestination!=null && 
						   oFileResumeOrigin.getCrc32()!=oFileResumeDestination.getCrc32()){
						count_modified+=1;
						oBufferedWriter.write(oFileResumeOrigin.getName());	
						oBufferedWriter.newLine();
					}
				}
			}
			oBufferedWriter.write("modified elements:"+count_modified);
			oBufferedWriter.newLine();
					
			//check new files
			oBufferedWriter.newLine();
			oBufferedWriter.write("Check new elements (Only Origin)");
			oBufferedWriter.newLine();
			oBufferedWriter.write("================================");
			oBufferedWriter.newLine();
			keySetIterator = this.origin.getAll().keySet().iterator();
			while (keySetIterator.hasNext()) {
				String key = keySetIterator.next();
				FileResume oFileResumeOrigin = this.origin.getAll().get(key);
				FileResume oFileResumeDestination = this.destination.getAll().get(key);
				if(oFileResumeDestination==null){
					count_new+=1;
					oBufferedWriter.write(oFileResumeOrigin.getName());
					oBufferedWriter.newLine();
				}
			}		
			oBufferedWriter.write("new elements:"+count_new);
			oBufferedWriter.newLine();
					
			//check deleted files
			oBufferedWriter.newLine();
			oBufferedWriter.write("Check deleted elements (Only Destination)");
			oBufferedWriter.newLine();
			oBufferedWriter.write("=========================================");
			oBufferedWriter.newLine();
			keySetIterator = this.destination.getAll().keySet().iterator();
			while (keySetIterator.hasNext()) {
				String key = keySetIterator.next();
				FileResume oFileResumeDestination = this.destination.getAll().get(key);
				FileResume oFileResumeOrigin = this.origin.getAll().get(key);
				if(oFileResumeOrigin==null){
					count_deleted+=1;
					oBufferedWriter.write(oFileResumeDestination.getName());
					oBufferedWriter.newLine();
				}
			}		
			oBufferedWriter.write("deleted elements:"+count_deleted);
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
		
		String origen="";
		String destino="";
		
		if(args.length >= 2){
			origen = args[0];
			destino = args[1];
			
			Comparator oComparator = new Comparator(origen, destino);
			//add file exceptions
			oComparator.addFileException(".svn"); //svn folder
			
			oComparator.execute();
			oComparator.report();
			//oComparator.export();
			
		} else {
			System.out.println("usage: jfcc.Comparator [origen] [destino]");
		}
		
		Date oFin = new Date();
		System.out.println("End proces time "+(oFin.getTime()-oInicio.getTime())+"ms");

	}

}
