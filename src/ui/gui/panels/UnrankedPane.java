package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import model.models.TierElement;
import ui.gui.settings.UISettings;

public class UnrankedPane extends ScrollPane {

	private FlowPane flowPane;
	
	private TierListController controller;
	private List<ImageView> images;
	
	public UnrankedPane(TierListController controller) {
		this.controller = controller;
		this.flowPane = new FlowPane();
		this.images = loadImages();

		this.initPane();
	}	

	private List<ImageView> loadImages() {
		
		List<TierElement> elements = controller.getUnranked();
		this.images = new ArrayList<>();
		Image image;
		
		for (var element : elements) {
			String imageUrl = element.getImageUrl();
			var imageViewer = new ImageView(image = new Image(imageUrl));
			imageViewer.setUserData(element);
			
			/* Debug: Check if image loaded
			image.progressProperty().addListener((obs, oldVal, newVal) -> {
				System.out.println("Image loading: " + imageUrl + " - Progress: " + newVal);
			});
			
			// Check for errors
			if (image.isError()) {
				System.err.println("Failed to load image: " + imageUrl);
			}
			*/
			
			//----- settings -----//
			imageViewer.setPreserveRatio(true);
			imageViewer.setSmooth(true);
			imageViewer.setCache(true);
			
			imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
			imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);
			//imageViewer.setOnDragEntered(this::handleDragEntered);
			
			images.add(imageViewer);
		}
		return images;
	}
	
	private void handleDragEntered(DragEvent event) {
		
		//----- define transfer mode -----//
		Dragboard dragBoard = ( (ImageView)event.getAcceptingObject() ).startDragAndDrop(TransferMode.MOVE);
		
	
		//----- put image on dragboard -----//
		var content = new ClipboardContent();
		content.putImage(( (ImageView)event.getAcceptingObject() ).getImage());
		dragBoard.setContent(content);
		
		//----- update tier list -----//
		TierElement acceptingElement = (TierElement)( (ImageView)event.getAcceptingObject()).getUserData();
		controller.removeFromUnranked(acceptingElement);
		
		event.consume();
		
	}

	private void initPane() {
		this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setVbarPolicy(ScrollBarPolicy.NEVER);

		this.flowPane.getChildren().addAll(images);
		{
			flowPane.setPrefHeight(UISettings.DEFAULT_CELL_SIZE);
		}		
		
		this.setContent(flowPane);
	}
}
