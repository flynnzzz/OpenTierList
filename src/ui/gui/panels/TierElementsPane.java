package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
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
import model.enums.TierElementStatus;
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
		 
	
		//----- put image on dragboard -----//
		var content = new ClipboardContent();
		var viewer = (ImageView)event.getSource();
		var image = viewer.getImage();
		content.putImage(image);
		dragBoard.setContent(content);
		
		event.consume();
	}
	
	private void handleDragOver(DragEvent event) {
		
		if (event.getGestureSource() != event.getSource() && event.getDragboard().hasImage()) {
			event.acceptTransferModes(TransferMode.MOVE);
		}
		
		//----- store -----//

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
        if (event.getTarget() != null) {
            TierElement targetData = (TierElement)((ImageView)event.getTarget()).getUserData();
            TierElement sourceData = (TierElement)((ImageView)event.getGestureSource()).getUserData();
            
            // Only update if target is NOT the source
            if (!targetData.equals(sourceData)) {
                this.dragAndDropTarget = targetData;
                IO.println(dragAndDropTarget);
                var targetTier = controller.getElementTier(dragAndDropTarget);
                IO.println(targetTier);			
            }
        }
		
		
		event.consume();
	}
	
	private void handleDragDone(DragEvent event) {
		
		var source = (TierElement)((ImageView) event.getGestureSource()).getUserData();

		IO.println("Source and target: " + source.toString() + ", " + this.dragAndDropTarget.toString());
		if (event.getTransferMode() == TransferMode.MOVE) {
			var targetTier = controller.getElementTier(dragAndDropTarget);
			this.controller.moveTo(source, targetTier, dragAndDropTarget);
			IO.println(targetTier);
			
			/*
			switch (dragAndDropTarget.status()) {
			case TierElementStatus.RANKED: {
				this.controller.moveTo(source, targetTier, dragAndDropTarget);
				IO.println("Ranked element " + source +  " from tier " + tier.getHeader().name()
						+ " moved successfully to " + dragAndDropTarget + " located in " + targetTier.getHeader().name());
				IO.println(controller);
				break;
			}
			case TierElementStatus.UNRANKED: {
				this.controller.unrank(controller.getUnranked().indexOf(dragAndDropTarget), source, tier);
				IO.println("Ranked element " + source +  " from tier " + tier.getHeader().name()
						+ " unranked successfully to " + dragAndDropTarget);
				IO.println(controller);
				break;
			}
			default: IO.println("Move request aborted");
			}
			*/
		}
		event.consume();
	}


}
