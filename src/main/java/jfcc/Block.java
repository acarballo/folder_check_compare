package jfcc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.CRC32;
//import java.util.Set;

public class Block {

	private String blockPath = null;
	private File blockFolder = null;
	private Hashtable<String, String> blockFiles = null;
	
	public Block(String pblockPath){
		this.blockPath = pblockPath;
		this.blockFolder= new File(this.blockPath);
		this.blockFiles = new Hashtable<String, String>();
	}
	
	public void loadSnapshotp(){
		this.blockFiles = new Hashtable<String, String>();
		this.loadSnapshotpFolder(this.blockFolder,"");
	}
	
	
	public String md5Calculator(String filepath){
		String result="";

		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
		
			FileInputStream oFileInputStream = new FileInputStream(filepath);
			
			//DigestInputStream dis = new DigestInputStream(oFileInputStream, md);
			//byte[] digest = md.digest();
			 byte[] dataBytes = new byte[1024];
			 
		        int nread = 0; 
		        while ((nread = oFileInputStream.read(dataBytes)) != -1) {
		          md.update(dataBytes, 0, nread);
		        };
		        byte[] mdbytes = md.digest();
		        
			
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        result=sb.toString();
			//result = new String(digest, "UTF-8");
			//result = DigestUtils.
			
	        oFileInputStream.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("MD5 "+result);
		//String result = new String(Hex.encodeHex(digest));
		return result;
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
			//new FileInputStream(filepath)
			e.printStackTrace();
		} catch (IOException e) {
			//oInputStream.read()
			e.printStackTrace();
		}
		
		return result;
	}
	
	public long crc2Calculator(String filepath){
		long result = -1;
		
		try {
			RandomAccessFile oRandomAccessFile = new RandomAccessFile(filepath, "r");
			long length = oRandomAccessFile.length();
			CRC32 crc = new CRC32();
			for (long i = 0; i < length; i++) {
				oRandomAccessFile.seek(i);
				int cnt = oRandomAccessFile.readByte();
				crc.update(cnt);
			}
			oRandomAccessFile.close();
			result = crc.getValue();
		} catch (FileNotFoundException e1) {
			//new RandomAccessFile(filepath, "r")
			e1.printStackTrace();
		} catch (IOException e) {
			//randAccfile.length();
			e.printStackTrace();
		}
		
		return result;
	}
	
	public long crc3Calculator(String filepath){
		long result = -1;
		try {
			FileInputStream oFileInputStream = new FileInputStream(filepath);
			FileChannel fileChannel = oFileInputStream.getChannel();
			int len = (int) fileChannel.size();
			MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, len);

			CRC32 crc = new CRC32();
			for (int cnt = 0; cnt < len; cnt++) {
				int i = buffer.get(cnt);
				crc.update(i);
			}
			oFileInputStream.close();
			result = crc.getValue();
		} catch (FileNotFoundException e) {
			//new FileInputStream(filepath)
			e.printStackTrace();
		} catch (IOException e) {
			//oInputStream.read()
			e.printStackTrace();
		}
		
		return result;
	}
	
	private void loadSnapshotpFolder(final File folder, String relativePath){
		String keyFolder = folder.getAbsolutePath().replaceFirst(blockFolder.getAbsolutePath(), "");
		this.blockFiles.put(keyFolder, "");
		
		relativePath += "/" + folder.getName();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	loadSnapshotpFolder(fileEntry,relativePath);
	        } else {
	    		String keyFile = fileEntry.getAbsolutePath().replaceFirst(blockFolder.getAbsolutePath(), "");
	    		long crc32 = crcCalculator(fileEntry.getAbsolutePath());
	    		// md5Calculator(fileEntry.getAbsolutePath());
	        	this.blockFiles.put(keyFile, Long.toString(crc32));
	        	//System.out.println(fileEntry.getAbsolutePath());
	        }
	    }	
	}
	
	public void show(){
		//Set<String> keySet = this.blockFiles.keySet();
		//Iterator<String> keySetIterator = keySet.iterator();
		Iterator<String> keySetIterator = this.blockFiles.keySet().iterator();
		while (keySetIterator.hasNext()) {
		   String key = keySetIterator.next();
		   System.out.println("key: " + key + " value: " + this.blockFiles.get(key));
		}
		System.out.println(this.blockFiles.size());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//"/home/you/Desktop"
		Date oInicio = new Date();
		
		//String sPath = ".";
		String sPath = "/Users/Andres/Documents/workspace/csvfix";
		//String sPath = "/Users/Andres/Documents/workspace";
		Block oBlock = new Block(sPath);
		oBlock.loadSnapshotp();
		oBlock.show();
		
		Date oFin = new Date();
		System.out.println(oFin.getTime()-oInicio.getTime());
		
		
//crc 16
//crc2 34	
//crc3 40
	}

}
