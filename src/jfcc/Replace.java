package jfcc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Replace {

	private Snapshot origin = null;
	private String searchToken = null;
	private String replaceToken = null;
	private ArrayList<String> filesIgnored = null;
	
	public Replace(String pathOrigen, String searchToken, String replaceToken){
		this.filesIgnored = new ArrayList<String>();

		this.origin = new Snapshot(pathOrigen);
		this.searchToken = searchToken;
		this.replaceToken = replaceToken;
	}
	
	public void addFileIgnored(String newFileIgnored){
		this.filesIgnored.add(newFileIgnored);
	}
	
	public void execute(){
		this.origin.setFilesIgnored(this.filesIgnored);
		this.origin.loadSnapshot();
	}
	
	public void report(){
		long count_find = 0;
		
		System.out.println("Origin Folder:"+this.origin.getPath());
		System.out.println("Origin Elements:"+this.origin.getNumElements());
								
		//check new files
		System.out.println("");
		System.out.println("Replace String ["+searchToken+"]" );
		System.out.println("================================");
		Iterator<String> keySetIterator = this.origin.getAll().keySet().iterator();
		while (keySetIterator.hasNext()) {
			String key = keySetIterator.next();
			FileResume oFileResumeOrigin = this.origin.getAll().get(key);
			if(oFileResumeOrigin.getType()==FileResume.TYPE_FILE){
				if(oFileResumeOrigin.search(searchToken)){
					if(oFileResumeOrigin.replace(this.searchToken, this.replaceToken)){
						count_find+=1;
						System.out.println(oFileResumeOrigin.getName());				
					}
				}
			}
		}		
		System.out.println("Replaced:"+count_find);		
				
	}

	
	public void export(){
		/*
		long count_find = 0;
		
		SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    String timeLog = oSimpleDateFormat.format(new Date());
	    String exportFilename = "report_search_"+timeLog+".txt";
		
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
		
			System.out.println("Origin Folder:"+this.origin.getPath());
			System.out.println("Origin Elements:"+this.origin.getNumElements());
									
			//check new files
			oBufferedWriter.newLine();	
			oBufferedWriter.write("Replace String ["+searchToken+"]" );
			oBufferedWriter.newLine();	
			oBufferedWriter.write("================================");
			oBufferedWriter.newLine();	
			Iterator<String> keySetIterator = this.origin.getAll().keySet().iterator();
			while (keySetIterator.hasNext()) {
				String key = keySetIterator.next();
				FileResume oFileResumeOrigin = this.origin.getAll().get(key);
				if(oFileResumeOrigin.getType()==FileResume.TYPE_FILE){
					if(oFileResumeOrigin.search(searchToken)){
						count_find+=1;
						oBufferedWriter.write(oFileResumeOrigin.getName());	 
						oBufferedWriter.newLine();	
					}
				}
			}		
			oBufferedWriter.write("Find:"+count_find);			
			oBufferedWriter.newLine();		
		
			oBufferedWriter.close();
			oFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date oInicio = new Date();
		
		String origen="";
		String searchString="";
		String replaceString="";
		
		if(args.length >= 3){
			origen = args[0];
			searchString = args[1];
			replaceString = args[2];
			
			Replace oSearch = new Replace(origen, searchString, replaceString);
			//add file exceptions
			oSearch.addFileIgnored(".svn"); //svn folder
			
			oSearch.execute();
			oSearch.report();
			//oSearch.export();
			
		} else {
			System.out.println("usage: jfcc.Replace [origen] [Search String] [Replace String]");
		}
		
		Date oFin = new Date();
		System.out.println("End proces time "+(oFin.getTime()-oInicio.getTime())+"ms");

	}

}
