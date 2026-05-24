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
import ui.gui.settings.UISettings;

public class TierElementsPane extends FlowPane {
	
	private Tier tier;
	private TierListController controller;
	private List<ImageView> images;
	
	public TierElementsPane(TierListController controller, Tier tier) {
		this.controller = controller;
		this.tier = tier;
		this.images = loadImages();
		
		this.initPane();
	}

	private List<ImageView> loadImages() {
		List<TierElement> elements = tier.getElements();
		this.images = new ArrayList<>();
		for (var element : elements) {
			String imageUrl = element.getImageUrl();
			var imageViewer = new ImageView(new Image(imageUrl));
			//----- settings -----//
			imageViewer.setUserData(element);
			imageViewer.setSmooth(true);
			
			imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
			imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);
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
