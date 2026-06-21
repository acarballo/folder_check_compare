package jfcc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class Analizer {

	private Snapshot origin = null;
	private ArrayList<String> filesIgnored = null;
	private Hashtable<String, Integer>fileTypes = null;
	
	public Analizer(String pathOrigen){
		this.filesIgnored = new ArrayList<String>();

		this.origin = new Snapshot(pathOrigen);
	}
	
	public void addFileIgnored(String newFileIgnored){
		this.filesIgnored.add(newFileIgnored);
	}
	
	public void execute(){
		this.origin.setFilesIgnored(this.filesIgnored);
		this.origin.loadSnapshot();
	}
	
	public void report(){	
		fileTypes = new Hashtable<String, Integer>();
		
		System.out.println("Origin Folder:"+this.origin.getPath());
		System.out.println("Origin Elements:"+this.origin.getNumElements());
								
		//check new files
		System.out.println("");
		System.out.println("Analizer" );
		System.out.println("================================");
		Iterator<String> keySetIterator = this.origin.getAll().keySet().iterator();
		while (keySetIterator.hasNext()) {
			String key = keySetIterator.next();
			FileResume oFileResumeOrigin = this.origin.getAll().get(key);
			String ext = oFileResumeOrigin.getExtension();
			
			if(ext!=null){
				if(!fileTypes.containsKey(ext)){
					fileTypes.put(ext, 1);
				} else {
					fileTypes.put(ext, fileTypes.get(ext) + 1);
				}
			}
		}		

		Enumeration<String> e = fileTypes.keys();
		while( e.hasMoreElements() ){
			String skey = e.nextElement();
			System.out.println(skey + ": " + fileTypes.get(skey) );
		}
						
	}

	
	public void export(){
		fileTypes = new Hashtable<String, Integer>();
		
		SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    String timeLog = oSimpleDateFormat.format(new Date());
	    String exportFilename = "report_analizer_"+timeLog+".txt";
		
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
		
			oBufferedWriter.write("Origin Folder:"+this.origin.getPath());
			oBufferedWriter.write("Origin Elements:"+this.origin.getNumElements());
									
			//check new files
			oBufferedWriter.write("Analizer" );
			oBufferedWriter.newLine();	
			oBufferedWriter.write("================================");
			oBufferedWriter.newLine();	
			Iterator<String> keySetIterator = this.origin.getAll().keySet().iterator();
			while (keySetIterator.hasNext()) {
				String key = keySetIterator.next();
				FileResume oFileResumeOrigin = this.origin.getAll().get(key);
				String ext = oFileResumeOrigin.getExtension();
				
				if(ext!=null){
					if(!fileTypes.containsKey(ext)){
						fileTypes.put(ext, 1);
					} else {
						fileTypes.put(ext, fileTypes.get(ext) + 1);
					}
				}
			}		

			Enumeration<String> e = fileTypes.keys();
			while( e.hasMoreElements() ){
				String skey = e.nextElement();
				oBufferedWriter.write(skey + ": " + fileTypes.get(skey) );
				oBufferedWriter.newLine();	
			}	
		
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
		
		if(args.length >= 1){
			origen = args[0];
			
			Analizer oAnalizer = new Analizer(origen);
			//add file exceptions
			oAnalizer.addFileIgnored(".svn"); //svn folder
			
			oAnalizer.execute();
			oAnalizer.report();
			//oAnalizer.export();
			
		} else {
			System.out.println("usage: jfcc.Analizer [origen]");
		}
		
		Date oFin = new Date();
		System.out.println("End proces time "+(oFin.getTime()-oInicio.getTime())+"ms");

	}

}
