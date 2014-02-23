package jfcc;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class FileResume {

	private String name = null;
	private int type = 0;
	private String absolutePath = null;
	private long crc32 = 0;
	
	public static int TYPE_FILE = 1;
	public static int TYPE_FOLDER = 2;
	
	public FileResume(String name, int type){
		this.setName(name);
		this.setType(type);
	}
	
	public void refreshCRC32(){
		long result = -1;
		try {
			FileInputStream oFileInputStream = new FileInputStream(this.absolutePath);
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
		
		this.crc32 = result;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public long getCrc32() {
		return crc32;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

}
