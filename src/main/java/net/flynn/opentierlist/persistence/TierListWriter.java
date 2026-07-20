package net.flynn.opentierlist.persistence;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.flynn.opentierlist.model.models.TierList;

public class TierListWriter {
	public static void write(File file, TierList tierList) throws IOException, StreamReadException, DatabindException {
		var tierMapper = new ObjectMapper();
		tierMapper.writeValue(file, tierList);
	}
}
