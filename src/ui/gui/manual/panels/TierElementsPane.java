package ui.gui.manual.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import controller.controllers.TierListController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventTarget;
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
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import model.enums.TierElementStatus;
import model.models.Tier;
import model.models.TierElement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 2.30
 * @since v1.2.5
 */
public class TierElementsPane extends FlowPane {
	
	private TierListController controller;
	private Optional<Tier> tier;
	private List<TierElement> elements;
	
	private List<ImageView> images;
	private BiConsumer<TierElement, TierElement> onDragDropped;
	
	public TierElementsPane(TierListController controller, List<TierElement> elements, 
			Optional<Tier> tier, BiConsumer<TierElement, TierElement> onDragDropped) {
		this.controller = controller;
		this.tier = tier;
		this.elements = elements;
		this.onDragDropped = onDragDropped;
		this.images = loadImages();
		
		this.setupPane();
	}
	
	public TierElementsPane(TierListController controller, List<TierElement> elements, 
			BiConsumer<TierElement, TierElement> onDragDropped) {
		this.controller = controller;
		this.tier = Optional.empty();
		this.elements = elements;
		this.onDragDropped = onDragDropped;
		this.images = loadImages();
		
		this.setupPane();
	}
	
	public void updateElements(List<TierElement> elements) {
		this.elements = elements;
	}
	
	public Optional<Tier> getTier() {
		return this.tier;
	}
	
	private void setupImages(ImageView imageViewer) {
		//----- settings -----//
		imageViewer.setSmooth(true);
		
		imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
		imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);
		//----- drag and drop -----//
		imageViewer.setOnDragDetected(this::handleDragDetectedImage);
		imageViewer.setOnDragOver(this::handleDragOverImage);
		imageViewer.setOnDragEntered(this::handleDragEnteredImage);
		imageViewer.setOnDragExited(this::handleDragExitedImage);
		imageViewer.setOnDragDropped(this::handleDragDroppedImage);
		imageViewer.setOnDragDone(this::handleDragDone);
	}
	
	private List<ImageView> loadImages() {
		this.images = new ArrayList<>();
		
		elements.forEach( element -> {
			var imageViewer = new ImageView(new Image(element.getImageUrl()));
			imageViewer.setUserData(element);
			setupImages(imageViewer);
			images.add(imageViewer);
		});
		
		return images;
	}

	private void setupPane() {
		this.getChildren().addAll(images);
		this.initFlowPaneBorder();
		this.setPrefWidth(UISettings.DEFAULT_BAR_WIDTH);
		this.setMaxHeight(UISettings.DEFAULT_BAR_MAX_HEIGHT);
		this.setMinHeight(UISettings.DEFAULT_BAR_MIN_HEIGHT);
		
		//this.setOnDragDetected(this::handleDragDetected);
		this.setOnDragOver(this::handleDragOver);
		this.setOnDragEntered(this::handleDragEntered);
		this.setOnDragExited(this::handleDragExited);
		this.setOnDragDropped(this::handleDragDropped);
		this.setOnDragDone(this::handleDragDone);
	}
	
	private void initFlowPaneBorder() {
		var flowPaneBorder = new Border(
				new BorderStroke(
						Paint.valueOf(UISettings.DEFAULT_BAR_BORDER_COLOR), 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
					)
			);
		this.setBorder(flowPaneBorder);
	}
	
	private void handleDragDetectedImage(MouseEvent event) {
		
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
	
	private void handleDragOverImage(DragEvent event) {
		
		if (event.getGestureSource() != event.getSource() && event.getDragboard().hasImage())
			event.acceptTransferModes(TransferMode.MOVE);
		
		event.consume();
	}
	
	private void handleDragEnteredImage(DragEvent event) {
		
		ImageView target = (ImageView) event.getTarget();
        if (event.getGestureSource() != target &&
                event.getDragboard().hasImage()) {
        	target.setBlendMode(BlendMode.ADD);
        }
               
		event.consume();
	}
	
	private void handleDragExitedImage(DragEvent event) {
		
		ImageView target = (ImageView) event.getTarget();
			target.setBlendMode(getBlendMode());
		
		event.consume();
	}
	
	private void handleDragDroppedImage(DragEvent event) {
		
		Dragboard dragBoard = event.getDragboard();
		
		boolean success = false;
		
		if (dragBoard.hasImage () && dragBoard.hasString()) {
			
			//----- find source and target -----//
			String sourceId = dragBoard.getString();
			TierElement sourceData = controller.getElementByHash(sourceId);
			EventTarget dragAndDropTarget = event.getTarget();
			
			//----- update model -----//
			if (sourceData != null && dragAndDropTarget != null) {
				
				if (dragAndDropTarget instanceof ImageView targetImage 
						&& !sourceData.equals((TierElement) targetImage.getUserData()))
					onDragDropped.accept(sourceData, (TierElement) targetImage.getUserData());
				}
				
				success = true;
				this.updateImages();
		}
		event.setDropCompleted(success);
		
		event.consume();
	}
	
	private void handleDragOver(DragEvent event) {
		if (event.getGestureSource() != event.getSource() && event.getDragboard().hasImage())
			event.acceptTransferModes(TransferMode.MOVE);
		event.consume();
	}
	
	private void handleDragEntered(DragEvent event) {
		TierElementsPane target = (TierElementsPane) event.getTarget();
        if (event.getGestureSource() != target &&
                event.getDragboard().hasImage()) {
        }
		event.consume();
	}
	
	private void handleDragExited(DragEvent event) {
		TierElementsPane target = (TierElementsPane) event.getTarget();
			target.setBlendMode(getBlendMode());
		event.consume();
	}
	
	private void handleDragDropped(DragEvent event) {
		Dragboard dragBoard = event.getDragboard();

		boolean success = false;
		
		if (dragBoard.hasImage () && dragBoard.hasString()) {
			String sourceId = dragBoard.getString();
			TierElement sourceData = controller.getElementByHash(sourceId);
			EventTarget dragAndDropTarget = event.getTarget();
			
			if (dragAndDropTarget instanceof TierElementsPane targetElementPane) {

				Optional<Tier> targetTier = targetElementPane.getTier();
				switch (sourceData.status()) {
				case TierElementStatus.RANKED: {
					if (targetTier.isPresent())
						controller.moveTo(sourceData, targetTier.get());
					else
						controller.unrank(sourceData);
				} break;
				case TierElementStatus.UNRANKED: {
					if (targetTier.isPresent())
						controller.rank(sourceData, targetElementPane.getTier().get()); 
					else
						controller.moveUnranked(sourceData, controller.getUnranked().getLast());
				} break;
				default: break;
				}
				success = true;
				this.updateImages();
			}
		}
		event.setDropCompleted(success);
		
		event.consume();
	}
	
	private void handleDragDone(DragEvent event) {
		if (event.getTransferMode() == TransferMode.MOVE) {
			this.updateImages();
		}
		event.consume();
	}
	
	public void updateImages() {
		if (tier.isPresent()) 
			this.updateElements(tier.get().getElements());
		else
			this.updateElements(controller.getUnranked());
		
		this.images = loadImages();
		this.getChildren().clear();
		this.getChildren().addAll(images);
		this.updateAnimation();
	}
	
	private void updateAnimation() {
		images.forEach( image -> {
			image.setTranslateX(UISettings.DEFAULT_CELL_SIZE);
			image.setOpacity(0);
			
			var translateTransition = new TranslateTransition(Duration.millis(100), image);
			translateTransition.setFromX(UISettings.DEFAULT_CELL_SIZE);
			translateTransition.setToX(0);
			
			var fadeTransition = new FadeTransition(Duration.millis(100), image);
			fadeTransition.setFromValue(0.0);
			fadeTransition.setToValue(1.0);
			
			new ParallelTransition(image, translateTransition, fadeTransition).play();
		});
	}
}
