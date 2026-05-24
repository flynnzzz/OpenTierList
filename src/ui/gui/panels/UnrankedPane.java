package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.models.TierElement;
import ui.gui.settings.UISettings;

/**
 * 
 * @version 2.00
 * @since v1.2.5
 */
public class UnrankedPane extends ScrollPane {

	private FlowPane flowPane;
	
	private TierListController controller;
	private List<ImageView> images;
	
	private TierElement dragAndDropTarget;
	
	public UnrankedPane(TierListController controller) {
		this.controller = controller;
		this.flowPane = new FlowPane();
		this.images = loadImages();

		this.initPane();
	}	

	private List<ImageView> loadImages() {
		
		List<TierElement> elements = controller.getUnranked();
		this.images = new ArrayList<>();
		
		for (var element : elements) {
			String imageUrl = element.getImageUrl();
			var image = new Image(imageUrl);
			var imageViewer = new ImageView(new Image(imageUrl));
			
			//TODO: Alert
			if(image.isError()) {
				System.err.println("Failed to load image at url: " + image.getUrl());
			}
			
			imageViewer.setUserData(element);
			
			//----- settings -----//

			imageViewer.setPreserveRatio(false);
			imageViewer.setSmooth(true);
			imageViewer.setCache(true);
			
			imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
			imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);
			
			
			//----- drag and drop -----//
			imageViewer.setOnDragDetected(this::handleDragDetected);
			imageViewer.setOnDragOver(this::handleDragOver);
			imageViewer.setOnDragEntered(this::handleDragEntered);
			imageViewer.setOnDragExited(this::handleDragExited);
			imageViewer.setOnDragDropped(this::handleDragDropped);
			imageViewer.setOnDragDone(this::handleDragDone);
			
			images.add(imageViewer);
		}
		return images;
	}
	

	private void initPane() {
		this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

		this.flowPane.getChildren().addAll(images);
		{

			this.setFlowPaneBorder();
			
			//var backgroundColor = Paint.valueOf(Color.LIGHTGRAY.toString());
			//var flowPaneBackground = Background.fill(backgroundColor);
			//flowPane.setBackground(flowPaneBackground);
			
			//TODO: organize later
			flowPane.setPrefWidth(8*UISettings.DEFAULT_CELL_SIZE);
			flowPane.setMinWidth(2*UISettings.DEFAULT_CELL_SIZE);

			flowPane.setMaxHeight(3*UISettings.DEFAULT_CELL_SIZE);
			flowPane.setMinHeight(1*UISettings.DEFAULT_CELL_SIZE);
			
			flowPane.setAlignment(Pos.BASELINE_LEFT);
		}		
		
		this.setContent(flowPane);
		
		//TODO: organize later
		this.setPrefWidth(8*UISettings.DEFAULT_CELL_SIZE);
		this.setMaxHeight(3*UISettings.DEFAULT_CELL_SIZE);
		this.setMinHeight(2*UISettings.DEFAULT_CELL_SIZE);
		
		//TODO: organize later
		this.setPadding(new Insets(40, 20, 20, 30 + UISettings.DEFAULT_CELL_SIZE));
	}
	
	private void setFlowPaneBorder() {
		var borderColor = Paint.valueOf(Color.DIMGRAY.toString());
		var flowPaneBorder = new Border(  
				new BorderStroke(
						borderColor, 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
					)
			);
		flowPane.setBorder(flowPaneBorder);
	}
	
	private void handleDragDetected(MouseEvent event) {
		
		//----- define transfer mode -----//
		Dragboard dragBoard = ( (ImageView)event.getSource() ).startDragAndDrop(TransferMode.MOVE);
		 
	
		//----- put image on dragboard -----//
		var content = new ClipboardContent();
		var viewer = (ImageView)event.getSource();
		var image = viewer.getImage();
		content.putImage(image);
		dragBoard.setContent(content);
		
		var source = (TierElement)((ImageView)event.getSource()).getUserData();
		IO.println("Drag detected, source: " + source);
		
		event.consume();
	}
	
	//TODO: debug throughly with different images
	private void handleDragOver(DragEvent event) {
		
		if (event.getGestureSource() != event.getSource() && event.getDragboard().hasImage())
			event.acceptTransferModes(TransferMode.MOVE);
		
		//----- store -----//
		if (event.getTarget() != null)
			this.dragAndDropTarget = (TierElement)((ImageView)event.getTarget()).getUserData();
		
		event.consume();
	}
	
	private void handleDragEntered(DragEvent event) {
		
		var target = (ImageView) event.getTarget();
        if (event.getGestureSource() != target &&
                event.getDragboard().hasImage()) {
        	target.setBlendMode(BlendMode.ADD);
        }
               
		event.consume();
	}
	
	private void handleDragExited(DragEvent event) {
		
		var target = (ImageView) event.getTarget();
			target.setBlendMode(getBlendMode());
		
		event.consume();
	}
	
	private void handleDragDropped(DragEvent event) {
		
		var dragBoard = event.getDragboard();
		
		boolean success = false;
		
		if (dragBoard.hasImage()) {
			success = true;
		}
		event.setDropCompleted(success);
		
		event.consume();
	}
	
	private void handleDragDone(DragEvent event) {
		
		var source = (TierElement)((ImageView) event.getGestureSource()).getUserData();

		IO.println("Source and target: " + source.toString() + ", " + this.dragAndDropTarget.toString());
		if (event.getTransferMode() == TransferMode.MOVE) {
			this.controller.moveUnranked(source, this.dragAndDropTarget);
			IO.println("Element " + source +  " moved successfully to " + this.dragAndDropTarget);
			IO.println(controller.getUnranked());
		}
		this.updateImages();
		
		event.consume();
	}
	
	private void updateImages() {
		this.images = loadImages();
		this.flowPane.getChildren().clear();
		this.flowPane.getChildren().addAll(images);
	}

}
