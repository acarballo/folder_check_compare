package jfcc;

import java.io.PrintWriter;

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
	 * OJO: este metodo no es de solo lectura. El reemplazo en disco ocurre
	 * como efecto secundario de generar el informe (ver FileResume.replace()).
	 * Llamar dos veces a report()/export() sobre la misma instancia no
	 * duplicara el reemplazo: la segunda vez ya no se encontrara el texto
	 * buscado y el contador saldra a 0.
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

	public static void main(String[] args) {
		if (args.length >= 3) {
			Replace tool = new Replace(args[0], args[1], args[2]);
			tool.addFileIgnored(".svn");
			runTimed(() -> { tool.execute(); tool.report(); });
		} else {
			System.out.println("usage: jfcc.Replace [origen] [Search String] [Replace String]");
		}
	}

}
