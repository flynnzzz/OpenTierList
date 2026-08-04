package net.flynn.opentierlist.persistence;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import net.flynn.opentierlist.model.models.TierList;
import net.flynn.opentierlist.ui.manual.ScrollPaneTiers;

import javax.imageio.ImageIO;

public class TierListWriter {
	public static void write(File file, TierList tierList) throws IOException, StreamReadException, DatabindException {
		var tierMapper = new ObjectMapper();
		tierMapper.writeValue(file, tierList);
	}

	private static WritableImage screenshot(ScrollPaneTiers node) {

		final double inboundWidth = node.getContent().getBoundsInLocal().getWidth(),
				inboundHeight = node.getContent().getBoundsInLocal().getHeight();

		final WritableImage image = new WritableImage((int) inboundWidth, (int) inboundHeight);

		final var params = new SnapshotParameters();
		params.setTransform(new Scale(1, 1));

		node.hideEditButtons();
		node.getContent().snapshot(params, image);
		node.showEditButtons();

		return image;
	}

	public static void export(File file, ScrollPaneTiers node) throws  IOException {

		ImageIO.write(SwingFXUtils.fromFXImage(screenshot(node), null), "png", file);

	}
}
