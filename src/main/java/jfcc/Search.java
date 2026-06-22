package jfcc;

import java.io.PrintWriter;
import java.util.Date;

public class Search extends AbstractTool {

	private final String searchToken;

	public Search(String pathOrigen, String searchToken) {
		super(pathOrigen);
		this.searchToken = searchToken;
	}

	@Override
	protected String exportFilePrefix() {
		return "report_search";
	}

	@Override
	protected void writeReport(PrintWriter out) {
		long countFind = 0;

		out.println("Origin Folder:" + this.origin.getPath());
		out.println("Origin Elements:" + this.origin.getNumElements());

		out.println();
		out.println("Search String [" + this.searchToken + "]");
		out.println("================================");

		for (FileResume resume : this.origin.getAll().values()) {
			if (resume.getType() == FileResume.TYPE_FILE && resume.search(this.searchToken)) {
				countFind++;
				out.println(resume.getName());
			}
		}
		out.println("Find:" + countFind);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date oInicio = new Date();

		if (args.length >= 2) {
			String origen = args[0];
			String searchString = args[1];

			Search oSearch = new Search(origen, searchString);
			//add file exceptions
			oSearch.addFileIgnored(".svn"); //svn folder

			oSearch.execute();
			oSearch.report();
			//oSearch.export();

		} else {
			System.out.println("usage: jfcc.Search [origen] [Search String]");
		}

		Date oFin = new Date();
		System.out.println("End proces time " + (oFin.getTime() - oInicio.getTime()) + "ms");
	}

}
