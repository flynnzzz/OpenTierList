package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.FlowPane;
import model.models.Tier;
import model.models.TierElement;

public class TierElementsPane extends FlowPane {
	
	private Tier tier;
	private List<ImageView> images;
	
	//TODO: change to pass in controller
	public TierElementsPane(Tier tier) {
		this.images = loadImages();
		this.tier = tier;
		
		this.initPane();
	}

	private List<ImageView> loadImages() {
		List<TierElement> elements = tier.getElements();
		this.images = new ArrayList<>();
		for (var element : elements) {
			String path = element.getImagePath();
			var imageViewer = new ImageView(new Image(path));
			//----- settings -----//
			imageViewer.setUserData(element);
			imageViewer.setSmooth(true);
			imageViewer.setFitHeight(this.getHeight());
			imageViewer.setFitWidth(this.getWidth());
			//imageViewer.setOnDragEntered(this::handleDragEntered);
			
			images.add(imageViewer);
		}
		return images;
	}

	private void handleDragEntered(DragEvent event) {
	}

	private void initPane() {
		this.getChildren().addAll(images);
	}

}
