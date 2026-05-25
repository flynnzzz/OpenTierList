package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.event.ActionEvent;
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
import model.models.Tier;
import model.models.TierElement;
import ui.gui.settings.UISettings;

/**
 * 
 * @version 1.20
 * @since v1.2.5
 */
public class TierElementsPane extends FlowPane {
	
	private Tier tier;
	private TierListController controller;
	private List<ImageView> images;
	
	private TierElement dragAndDropTarget; 
	
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
		this.getChildren().addAll(images);
		
		this.initFlowPaneBorder();
		//TODO: organize later
		this.setPrefWidth(8*UISettings.DEFAULT_CELL_SIZE);
		this.setMaxHeight(5*UISettings.DEFAULT_CELL_SIZE);
		this.setMinHeight(1*UISettings.DEFAULT_CELL_SIZE);
	}
	
	private void initFlowPaneBorder() {
		var borderColor = Paint.valueOf(Color.DIMGRAY.toString());
		var flowPaneBorder = new Border(
				new BorderStroke(
						borderColor, 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
					)
			);
		this.setBorder(flowPaneBorder);
	}
	
	private void handleDragDetected(MouseEvent event) {
		
		//----- define transfer mode -----//
		Dragboard dragBoard = ( (ImageView)event.getSource() ).startDragAndDrop(TransferMode.MOVE);
		 
	
		//----- put image and TierElement hashcode on dragboard -----//
		var content = new ClipboardContent();
		var viewer = (ImageView)event.getSource();
		var sourceElement = (TierElement)viewer.getUserData();
		
		content.putImage(viewer.getImage());
		content.putString(Integer.valueOf(sourceElement.hashCode()).toString());
		
		dragBoard.setContent(content);
		
		event.consume();
	}
	
	private void handleDragOver(DragEvent event) {
		
		if (event.getGestureSource() != event.getSource() && event.getDragboard().hasImage())
			event.acceptTransferModes(TransferMode.MOVE);
		
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
		
		if (dragBoard.hasImage () && dragBoard.hasString()) {
			
			//----- store -----//
			String sourceId = dragBoard.getString();
			TierElement sourceData = controller.getElementByHash(sourceId);
			this.dragAndDropTarget = (TierElement)((ImageView)event.getTarget()).getUserData();

			//----- update model -----//
			if (sourceData != null && dragAndDropTarget != null && !sourceData.equals(dragAndDropTarget)) {
	            var targetTier = controller.getTierByElement(dragAndDropTarget);
	            
	            this.controller.moveTo(sourceData, targetTier, dragAndDropTarget);
	            
	            //IO.println("Ranked element " + sourceData +  " from tier " + tier.getHeader().name()
	            //		+ " moved successfully to " + dragAndDropTarget + " located in " + targetTier.getHeader().name());
	            
	            success = true;
	        }
			this.updateImages();
		}
		event.setDropCompleted(success);
		
		event.consume();
	}
	
	private void handleDragDone(DragEvent event) {
		
		this.dragAndDropTarget = (TierElement)((ImageView)event.getTarget()).getUserData();
		//IO.println(controller);
		this.fireEvent(new ActionEvent(this, null));
		if (event.getTransferMode() == TransferMode.MOVE) {
			this.updateImages();
		}
		event.consume();
	}
	
	private void updateImages() {
		this.images = loadImages();
		this.getChildren().clear();
		this.getChildren().addAll(images);
	}

}
