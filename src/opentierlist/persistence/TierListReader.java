package opentierlist.persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import opentierlist.model.models.TierList;

public class TierListReader {
	@SuppressWarnings("unused")
	private BufferedReader bufferedReader;
	
	public TierListReader(FileReader reader) {
		this.bufferedReader = new BufferedReader(reader);
	}
	
	public TierList read() throws IOException, ParseException {
		return null;
	}
}
