package jfcc;

import java.io.PrintWriter;

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

	public static void main(String[] args) {
		if (args.length >= 2) {
			Search tool = new Search(args[0], args[1]);
			tool.addFileIgnored(".svn");
			runTimed(() -> { tool.execute(); tool.report(); });
		} else {
			System.out.println("usage: jfcc.Search [origen] [Search String]");
		}
	}

}
