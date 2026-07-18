package opentierlist.persistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import opentierlist.controller.controllers.TierListController;
import opentierlist.model.models.Telement;
import opentierlist.model.models.Tier;

public class TierListWriter {
	private TierListController controller;
	private BufferedWriter bufferedWriter;
	
	public TierListWriter(TierListController controller, FileWriter writer) {
		this.controller = controller;
		this.bufferedWriter = new BufferedWriter(writer);
	}
	public void write() throws IOException {
		bufferedWriter.write('{' + System.lineSeparator());

		bufferedWriter.write("\t\"title\": \"" + controller.getTierListName() + "\"," + System.lineSeparator());
		bufferedWriter.write("\t\"tiers\":" + System.lineSeparator() + "\t[" + System.lineSeparator());

		controller.getTiers().forEach(t -> {
			try {
				writeTier(t);
				if (!controller.getTiers().getLast().equals(t))
					bufferedWriter.write("," + System.lineSeparator());
			} catch (IOException ex) {
				System.err.println("--- IOException, aborting ---");
				System.exit(-1);
			}
		});

		bufferedWriter.write(System.lineSeparator() + "\t]," + System.lineSeparator());

		bufferedWriter.write("\t\"unranked\":" + System.lineSeparator() + "\t[" + System.lineSeparator());
		
		controller.getUnranked().forEach(e -> {
			try {
				writeElement("\t\t", e);
				if (!controller.getUnranked().getLast().equals(e))
					bufferedWriter.write("," + System.lineSeparator());
			} catch (IOException ex) {
				System.err.println("--- IOException, aborting ---");
				System.exit(-1);
			}
		});
		
		bufferedWriter.write(System.lineSeparator() + "\t]" + System.lineSeparator());
		
		bufferedWriter.write('}' + System.lineSeparator());
		bufferedWriter.close();
	}

	private void writeTier(Tier tier) throws IOException {
		bufferedWriter.write("\t\t{" + System.lineSeparator());

		bufferedWriter.write("\t\t\t\"color\": \"" + tier.getColor().toString() + "\"," + System.lineSeparator());
		bufferedWriter.write("\t\t\t\"name\": \"" + tier.getName() + "\"," + System.lineSeparator());
		bufferedWriter.write("\t\t\t\"elements\":" + System.lineSeparator() + "\t\t\t[" + System.lineSeparator());

		tier.getElements().forEach(e -> {
			try {
				writeElement("\t\t\t\t", e);
				if (!tier.getElements().getLast().equals(e)) {
					bufferedWriter.write("," + System.lineSeparator());
				}
			} catch (IOException ex) {
				System.err.println("--- IOException, aborting ---");
				System.exit(-1);
			}
		});

		bufferedWriter.write(System.lineSeparator() + "\t\t\t]" + System.lineSeparator());
		bufferedWriter.write("\t\t}");
	}

	private void writeElement(String indentation, Telement telement) throws IOException {
		bufferedWriter.write(indentation + '{' + System.lineSeparator());

		bufferedWriter.write(indentation + "\t\"name\": \"" + telement.getName() + "\"," + System.lineSeparator());
		bufferedWriter.write(indentation + "\t\"url\": \"" + telement.getImageUrl() + "\"," + System.lineSeparator());
		bufferedWriter.write(indentation + "\t\"status\": \"" + telement.status() + "\"" + System.lineSeparator());

		bufferedWriter.write(indentation + '}');
	}
}
