package jfcc;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Hashtable;
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
		Hashtable<String, Integer> fileTypes = new Hashtable<String, Integer>();

		out.println("Origin Folder:" + this.origin.getPath());
		out.println("Origin Elements:" + this.origin.getNumElements());

		out.println();
		out.println("Analizer");
		out.println("================================");

		for (FileResume resume : this.origin.getAll().values()) {
			String ext = resume.getExtension();
			if (ext != null) {
				Integer count = fileTypes.get(ext);
				fileTypes.put(ext, count == null ? 1 : count + 1);
			}
		}

		for (Map.Entry<String, Integer> entry : fileTypes.entrySet()) {
			out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date oInicio = new Date();

		if (args.length >= 1) {
			String origen = args[0];

			Analizer oAnalizer = new Analizer(origen);
			//add file exceptions
			oAnalizer.addFileIgnored(".svn"); //svn folder

			oAnalizer.execute();
			oAnalizer.report();
			//oAnalizer.export();

		} else {
			System.out.println("usage: jfcc.Analizer [origen]");
		}

		Date oFin = new Date();
		System.out.println("End proces time " + (oFin.getTime() - oInicio.getTime()) + "ms");
	}

}
