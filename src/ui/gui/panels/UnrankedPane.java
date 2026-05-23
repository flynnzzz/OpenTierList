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

public class UnrankedPane extends ScrollPane {

	private FlowPane flowPane;
	private TierListController controller;
	private List<ImageView> images;
	
	public UnrankedPane(TierListController controller) {
		this.controller = controller;
		this.images = loadImages();
		this.flowPane = new FlowPane();

		this.initPane();
	}	

	private List<ImageView> loadImages() {
		List<TierElement> elements = controller.getUnranked();
		this.images = new ArrayList<>();
		for (var element : elements) {
			String path = element.getImagePath();
			var imageViewer = new ImageView(new Image(path));
			//----- settings -----//
			imageViewer.setUserData(element);
			imageViewer.setSmooth(true);
			imageViewer.setFitHeight(flowPane.getHeight());
			imageViewer.setFitWidth(flowPane.getWidth());
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
			flowPane.setPrefSize(USE_COMPUTED_SIZE, BASELINE_OFFSET_SAME_AS_HEIGHT);
		}		
		
		this.setContent(flowPane);
	}
}
