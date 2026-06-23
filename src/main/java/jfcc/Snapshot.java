package jfcc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Snapshot {

	private final String snapshotPath;
	private final File snapshotFile;
	// Bug 6 fix: Hashtable → LinkedHashMap (no necesita sincronizacion; ademas
	// preserva el orden de insercion, por lo que los informes salen siempre en
	// el mismo orden)
	private Map<String, FileResume> snapshotFiles;
	private ArrayList<String> filesIgnored;

	public Snapshot(String path) {
		this.snapshotPath = path;
		this.snapshotFile = new File(path);
		this.snapshotFiles = new LinkedHashMap<>();
		this.filesIgnored = new ArrayList<>();
	}

	public void setFilesIgnored(ArrayList<String> filesIgnored) {
		this.filesIgnored = filesIgnored;
	}

	public void addFileIgnored(String newFileIgnored) {
		this.filesIgnored.add(newFileIgnored);
	}

	public String getPath() {
		return this.snapshotPath;
	}

	public Map<String, FileResume> getAll() {
		return this.snapshotFiles;
	}

	public long getNumElements() {
		return this.snapshotFiles.size();
	}

	public void loadSnapshot() {
		this.snapshotFiles = new LinkedHashMap<>();
		this.loadSnapshotFolder(this.snapshotFile);
	}

	/**
	 * Bug 4 fix: usaba replaceFirst() interpretando la ruta como regex; una
	 * ruta con parentesis o corchetes (ej. "Proyecto (copia)") romperia el
	 * matching. Ahora usa startsWith/substring, que es puramente textual.
	 *
	 * Nota: se normaliza el separador a "/" para compatibilidad con Windows,
	 * igual que antes.
	 */
	private String getRelativePath(String absolutePath) {
		String base = this.snapshotFile.getAbsolutePath().replace(File.separator, "/");
		String current = absolutePath.replace(File.separator, "/");
		return current.startsWith(base) ? current.substring(base.length()) : current;
	}

	/**
	 * Bug 3 fix: folder.listFiles() puede devolver null si la carpeta no
	 * tiene permisos de lectura; el for-each lanzaria NullPointerException.
	 */
	private void loadSnapshotFolder(final File folder) {
		String keyFolder = getRelativePath(folder.getAbsolutePath());
		FileResume folderResume = new FileResume(keyFolder, FileResume.TYPE_FOLDER);
		folderResume.setAbsolutePath(folder.getAbsolutePath());
		this.snapshotFiles.put(keyFolder, folderResume);

		File[] entries = folder.listFiles();
		if (entries == null) {
			// sin permisos de lectura: se registra la carpeta pero se omite su contenido
			return;
		}

		for (File entry : entries) {
			if (this.filesIgnored.contains(entry.getName())) continue;

			if (entry.isDirectory()) {
				loadSnapshotFolder(entry);
			} else {
				String keyFile = getRelativePath(entry.getAbsolutePath());
				FileResume fileResume = new FileResume(keyFile, FileResume.TYPE_FILE);
				fileResume.setAbsolutePath(entry.getAbsolutePath());
				this.snapshotFiles.put(keyFile, fileResume);
			}
		}
	}

	public void refreshSnapshotCRC32() {
		for (FileResume resume : this.snapshotFiles.values()) {
			if (resume.getType() == FileResume.TYPE_FILE) {
				resume.refreshCRC32();
			}
		}
	}

}
