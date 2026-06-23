package jfcc;

import java.io.PrintWriter;

public class Comparator extends AbstractTool {

	private final Snapshot destination;

	public Comparator(String pathOrigen, String pathDestino) {
		super(pathOrigen);
		this.destination = new Snapshot(pathDestino);
	}

	@Override
	public void execute() {
		super.execute();
		loadSnapshot(this.destination);
		this.origin.refreshSnapshotCRC32();
		this.destination.refreshSnapshotCRC32();
	}

	@Override
	protected String exportFilePrefix() {
		return "report";
	}

	@Override
	protected void writeReport(PrintWriter out) {
		long countModified = 0;
		long countNew = 0;
		long countDeleted = 0;

		out.println("Origin Folder:" + this.origin.getPath());
		out.println("Origin Elements:" + this.origin.getNumElements());
		out.println("Destination Folder:" + this.destination.getPath());
		out.println("Destination Elements:" + this.destination.getNumElements());

		out.println();
		out.println("Check modified elements");
		out.println("=======================");
		for (String key : this.origin.getAll().keySet()) {
			FileResume originResume = this.origin.getAll().get(key);
			if (originResume.getType() == FileResume.TYPE_FILE) {
				FileResume destinationResume = this.destination.getAll().get(key);
				if (destinationResume != null && originResume.getCrc32() != destinationResume.getCrc32()) {
					countModified++;
					out.println(originResume.getName());
				}
			}
		}
		out.println("modified elements:" + countModified);

		out.println();
		out.println("Check new elements (Only Origin)");
		out.println("================================");
		for (String key : this.origin.getAll().keySet()) {
			FileResume originResume = this.origin.getAll().get(key);
			if (!this.destination.getAll().containsKey(key)) {
				countNew++;
				out.println(originResume.getName());
			}
		}
		out.println("new elements:" + countNew);

		out.println();
		out.println("Check deleted elements (Only Destination)");
		out.println("=========================================");
		for (String key : this.destination.getAll().keySet()) {
			FileResume destinationResume = this.destination.getAll().get(key);
			if (!this.origin.getAll().containsKey(key)) {
				countDeleted++;
				out.println(destinationResume.getName());
			}
		}
		out.println("deleted elements:" + countDeleted);
		out.println();
	}

	public static void main(String[] args) {
		if (args.length >= 2) {
			Comparator tool = new Comparator(args[0], args[1]);
			tool.addFileIgnored(".svn");
			runTimed(() -> { tool.execute(); tool.report(); });
		} else {
			System.out.println("usage: jfcc.Comparator [origen] [destino]");
		}
	}

}
