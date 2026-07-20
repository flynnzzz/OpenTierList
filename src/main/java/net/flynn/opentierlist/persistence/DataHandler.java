package net.flynn.opentierlist.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import net.flynn.opentierlist.model.models.TierList;

public class DataHandler {
	public DataHandler() {}
	
	public void save(File file, TierList tierList) {
		try {
			TierListWriter.write(file, tierList);
		} catch (IOException e) {
			System.err.println("--- Could not save tierlist '" + tierList.getTierListName() +"', aborting ---");
		}
	}
	
	public Optional<TierList> load(File file) {
		Optional<TierList> res = Optional.empty();
		try {
			res = Optional.of(TierListReader.read(file));
		} catch (IOException e) {
			System.err.println("--- Could not load tierlist from path '" + file.getAbsolutePath()  +"', aborting ---");
		}
		return res;
	}
}
