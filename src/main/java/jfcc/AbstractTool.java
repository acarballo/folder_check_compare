package jfcc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Comportamiento comun a las herramientas de jfcc (Analizer, Comparator,
 * Search, Replace): todas cargan un {@link Snapshot} del directorio origen
 * ignorando ciertos ficheros/carpetas, y generan un informe que se puede
 * mostrar por consola ({@link #report()}) o volcar a un fichero de texto
 * ({@link #export()}).
 *
 * Antes cada herramienta duplicaba el mismo bucle dos veces -una vez
 * escribiendo a System.out y otra a un BufferedWriter-, lo que con el
 * tiempo hizo que report() y export() se desincronizaran (algunas lineas de
 * cabecera no llegaban a escribirse en el fichero exportado). Centralizando
 * la logica en {@link #writeReport(PrintWriter)}, report() y export() usan
 * siempre el mismo codigo y por tanto producen siempre el mismo contenido.
 */
public abstract class AbstractTool {

	protected final Snapshot origin;
	protected final ArrayList<String> filesIgnored = new ArrayList<String>();

	protected AbstractTool(String pathOrigen) {
		this.origin = new Snapshot(pathOrigen);
	}

	public void addFileIgnored(String newFileIgnored) {
		this.filesIgnored.add(newFileIgnored);
	}

	/**
	 * Carga el snapshot del origen. Las subclases que necesiten cargar mas
	 * de un snapshot (p.ej. Comparator, que ademas tiene un destino) deben
	 * sobreescribir este metodo, llamando primero a super.execute() y
	 * reutilizando {@link #loadSnapshot(Snapshot)} para el resto.
	 */
	public void execute() {
		loadSnapshot(this.origin);
	}

	protected void loadSnapshot(Snapshot snapshot) {
		snapshot.setFilesIgnored(this.filesIgnored);
		snapshot.loadSnapshot();
	}

	/**
	 * Escribe el cuerpo del informe (cabecera + resultados). Cada
	 * herramienta implementa aqui su logica concreta; este metodo lo
	 * reutilizan tanto report() como export().
	 */
	protected abstract void writeReport(PrintWriter out);

	/** Prefijo del nombre de fichero que genera export(), p.ej. "report_search". */
	protected abstract String exportFilePrefix();

	public void report() {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out), true);
		printHeader(out);
		writeReport(out);
		out.flush();
	}

	public void export() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeLog = formatter.format(new Date());
		File file = new File(exportFilePrefix() + "_" + timeLog + ".txt");

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			printHeader(out);
			writeReport(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printHeader(PrintWriter out) {
		out.println("jfcc v" + Version.VALUE);
	}

	/**
	 * Mejora 11: el patron de temporizar la ejecucion estaba duplicado en los
	 * cuatro main(). Se centraliza aqui: ejecuta la accion y muestra el tiempo
	 * transcurrido en milisegundos.
	 */
	public static void runTimed(Runnable action) {
		long start = System.currentTimeMillis();
		action.run();
		System.out.println("End proces time " + (System.currentTimeMillis() - start) + "ms");
	}

}

