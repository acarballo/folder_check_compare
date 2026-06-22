package jfcc;

import java.io.PrintWriter;
import java.util.Date;

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

		//check updated files
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

		//check new files
		out.println();
		out.println("Check new elements (Only Origin)");
		out.println("================================");
		for (String key : this.origin.getAll().keySet()) {
			FileResume originResume = this.origin.getAll().get(key);
			FileResume destinationResume = this.destination.getAll().get(key);
			if (destinationResume == null) {
				countNew++;
				out.println(originResume.getName());
			}
		}
		out.println("new elements:" + countNew);

		//check deleted files
		out.println();
		out.println("Check deleted elements (Only Destination)");
		out.println("=========================================");
		for (String key : this.destination.getAll().keySet()) {
			FileResume destinationResume = this.destination.getAll().get(key);
			FileResume originResume = this.origin.getAll().get(key);
			if (originResume == null) {
				countDeleted++;
				out.println(destinationResume.getName());
			}
		}
		out.println("deleted elements:" + countDeleted);
		out.println();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date oInicio = new Date();

		if (args.length >= 2) {
			String origen = args[0];
			String destino = args[1];

			Comparator oComparator = new Comparator(origen, destino);
			//add file exceptions
			oComparator.addFileIgnored(".svn"); //svn folder

			oComparator.execute();
			oComparator.report();
			//oComparator.export();

		} else {
			System.out.println("usage: jfcc.Comparator [origen] [destino]");
		}

		Date oFin = new Date();
		System.out.println("End proces time " + (oFin.getTime() - oInicio.getTime()) + "ms");
	}

}
