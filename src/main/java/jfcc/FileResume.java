package jfcc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;

public class FileResume {

	private String name = null;
	private int type = 0;
	private String absolutePath = null;
	private long crc32 = 0;

	// Bug 5 fix: static final para que no puedan modificarse desde fuera
	public static final int TYPE_FILE = 1;
	public static final int TYPE_FOLDER = 2;

	public FileResume(String name, int type) {
		this.setName(name);
		this.setType(type);
	}

	/**
	 * Bug 7 fix: leia byte a byte; ahora usa un buffer de 8KB.
	 * La logica es identica (mismo CRC32), solo mas rapido con ficheros grandes.
	 */
	public void refreshCRC32() {
		long result = -1;
		try {
			CRC32 crc = new CRC32();
			byte[] buf = new byte[8192];
			int n;
			try (var in = Files.newInputStream(Path.of(this.absolutePath))) {
				while ((n = in.read(buf)) != -1) {
					crc.update(buf, 0, n);
				}
			}
			result = crc.getValue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.crc32 = result;
	}

	/**
	 * Busca la cadena en el contenido del fichero.
	 * Se lee en UTF-8 para manejar correctamente acentos y caracteres no-ASCII.
	 */
	public boolean search(String searchString) {
		try {
			String content = Files.readString(Path.of(this.absolutePath), StandardCharsets.UTF_8);
			return content.contains(searchString);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Bug 1 fix: usaba replaceAll (regex); ahora usa replace (texto literal).
	 * Bug 2 fix: leia bytes como chars, corrompiendo UTF-8; ahora usa Files.readString/writeString.
	 */
	public boolean replace(String searchString, String replaceString) {
		try {
			Path path = Path.of(this.absolutePath);
			String content = Files.readString(path, StandardCharsets.UTF_8);
			String replaced = content.replace(searchString, replaceString);
			Files.writeString(path, replaced, StandardCharsets.UTF_8);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getExtension() {
		if (this.getType() != FileResume.TYPE_FILE) {
			return null;
		}
		int i = this.absolutePath.lastIndexOf('.');
		return i > 0 ? this.absolutePath.substring(i + 1) : null;
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
