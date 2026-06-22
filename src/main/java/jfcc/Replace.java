package jfcc;

import java.io.PrintWriter;
import java.util.Date;

public class Replace extends AbstractTool {

	private final String searchToken;
	private final String replaceToken;

	public Replace(String pathOrigen, String searchToken, String replaceToken) {
		super(pathOrigen);
		this.searchToken = searchToken;
		this.replaceToken = replaceToken;
	}

	@Override
	protected String exportFilePrefix() {
		return "report_replace";
	}

	/**
	 * OJO: este metodo no es de solo lectura. Igual que en el codigo
	 * original, el reemplazo en disco ocurre como efecto secundario de
	 * generar el informe (ver FileResume.replace()). Por eso llamar dos
	 * veces a report()/export() sobre la misma instancia no duplicara el
	 * reemplazo: la segunda vez ya no se encontrara el texto buscado y el
	 * contador saldra a 0. Si se necesita "informe" y "ejecucion" como
	 * pasos independientes, este es el sitio a separar en un futuro
	 * refactor.
	 */
	@Override
	protected void writeReport(PrintWriter out) {
		long countReplaced = 0;

		out.println("Origin Folder:" + this.origin.getPath());
		out.println("Origin Elements:" + this.origin.getNumElements());

		out.println();
		out.println("Replace String [" + this.searchToken + "]");
		out.println("================================");

		for (FileResume resume : this.origin.getAll().values()) {
			if (resume.getType() == FileResume.TYPE_FILE && resume.search(this.searchToken)) {
				if (resume.replace(this.searchToken, this.replaceToken)) {
					countReplaced++;
					out.println(resume.getName());
				}
			}
		}
		out.println("Replaced:" + countReplaced);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date oInicio = new Date();

		if (args.length >= 3) {
			String origen = args[0];
			String searchString = args[1];
			String replaceString = args[2];

			Replace oReplace = new Replace(origen, searchString, replaceString);
			//add file exceptions
			oReplace.addFileIgnored(".svn"); //svn folder

			oReplace.execute();
			oReplace.report();
			//oReplace.export();

		} else {
			System.out.println("usage: jfcc.Replace [origen] [Search String] [Replace String]");
		}

		Date oFin = new Date();
		System.out.println("End proces time " + (oFin.getTime() - oInicio.getTime()) + "ms");
	}

}
