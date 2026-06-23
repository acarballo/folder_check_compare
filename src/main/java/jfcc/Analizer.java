package jfcc;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Analizer extends AbstractTool {

	public Analizer(String pathOrigen) {
		super(pathOrigen);
	}

	@Override
	protected String exportFilePrefix() {
		return "report_analizer";
	}

	@Override
	protected void writeReport(PrintWriter out) {
		Map<String, Integer> fileTypes = new LinkedHashMap<>();

		out.println("Origin Folder:" + this.origin.getPath());
		out.println("Origin Elements:" + this.origin.getNumElements());

		out.println();
		out.println("Analizer");
		out.println("================================");

		for (FileResume resume : this.origin.getAll().values()) {
			String ext = resume.getExtension();
			if (ext != null) {
				fileTypes.merge(ext, 1, Integer::sum);
			}
		}

		for (Map.Entry<String, Integer> entry : fileTypes.entrySet()) {
			out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	public static void main(String[] args) {
		if (args.length >= 1) {
			Analizer tool = new Analizer(args[0]);
			tool.addFileIgnored(".svn");
			runTimed(() -> { tool.execute(); tool.report(); });
		} else {
			System.out.println("usage: jfcc.Analizer [origen]");
		}
	}

}
