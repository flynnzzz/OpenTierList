package ui.gui.manual.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import controller.controllers.TierListController;
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
import model.enums.TierElementStatus;
import model.models.Tier;
import model.models.TierElement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 2.90
 * @since v1.2.5
 */
public class ElementsFlowPane extends FlowPane {
	
	private TierListController controller;
	private Optional<Tier> tier;
	private List<TierElement> elements;
	
	private List<ImageView> images;
	private BiConsumer<TierElement, TierElement> onDragDropped;
	
	public ElementsFlowPane(TierListController controller, List<TierElement> elements, 
			Optional<Tier> tier, BiConsumer<TierElement, TierElement> onDragDropped) {
		this.controller = controller;
		this.tier = tier;
		this.elements = elements;
		this.onDragDropped = onDragDropped;
		this.images = loadImages();
		
		this.setupPane();
	}
	
	public ElementsFlowPane(TierListController controller, List<TierElement> elements, 
			BiConsumer<TierElement, TierElement> onDragDropped) {
		this.controller = controller;
		this.tier = Optional.empty();
		this.elements = elements;
		this.onDragDropped = onDragDropped;
		this.images = loadImages();
		
		this.setupPane();
	}
	
	public Optional<Tier> getTier() {
		return this.tier;
	}
	
	private void setupImages(ImageView imageViewer) {
		// ----- settings -----//
		imageViewer.setSmooth(false);

		imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
		imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);

		// ----- images drag and drop -----//
		imageViewer.setOnDragDetected(this::handleDragDetectedImage);

		imageViewer.setOnDragOver(event -> {
			if (event.getDragboard().hasImage())
				event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		imageViewer.setOnDragEntered(event -> {
			if (event.getTarget() instanceof ImageView target && event.getGestureSource() instanceof ImageView source &&
					event.getDragboard().hasImage()) {
				target.setBlendMode(BlendMode.RED);
				source.setBlendMode(BlendMode.GREEN);
			}
			event.consume();
		});

		imageViewer.setOnDragExited(event -> {
			if (event.getTarget() instanceof ImageView target && event.getGestureSource() instanceof ImageView source) {
				source.setBlendMode(getBlendMode());
				target.setBlendMode(getBlendMode());
			}
			event.consume();
		});

		imageViewer.setOnDragDropped(this::handleDragDroppedImage);

		imageViewer.setOnDragDone(event -> {
			if (event.getTransferMode() == TransferMode.MOVE)
				this.updateImages();
			event.consume();
		});
	}
	
	private List<ImageView> loadImages() {
		this.images = new ArrayList<>();
		
		elements.forEach( element -> {
			var imageViewer = new ImageView(new Image(element.getImageUrl(),
					UISettings.DEFAULT_CELL_SIZE,
					UISettings.DEFAULT_CELL_SIZE,
					false,
					false));

			imageViewer.setUserData(element);
			setupImages(imageViewer);
			images.add(imageViewer);
		});
		
		return images;
	}

	private void setupPane() {
		this.getChildren().addAll(images);
		this.initFlowPaneBorder();
		//----- settings -----//
		this.setPrefWidth(UISettings.DEFAULT_BAR_WIDTH);
		this.setMaxHeight(UISettings.DEFAULT_BAR_MAX_HEIGHT);
		this.setMinHeight(UISettings.DEFAULT_BAR_MIN_HEIGHT);
		this.setupDragAndDrop();	
	}

	private void setupDragAndDrop() {
		this.setOnDragOver(event -> {
			if (event.getGestureSource() != event.getSource() && event.getDragboard().hasImage())
				event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});

		this.setOnDragEntered(event -> {
			var sourceData = event.getGestureSource();
			if (sourceData instanceof ImageView && event.getDragboard().hasImage())
				this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_HIGHLIGHT_COLOR);
			event.consume();
		});

		this.setOnDragExited(event -> {
			var sourceData = event.getGestureSource();
			if (sourceData instanceof ImageView && event.getDragboard().hasImage())
				this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_BORDER_COLOR);
			event.consume();
		});

		this.setOnDragDropped(this::handleDragDropped);

		this.setOnDragDone(event -> {
			if (event.getTransferMode() == TransferMode.MOVE)
				this.updateImages();
			event.consume();
		});
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
		 
		//----- save image and TierElement hashcode on dragboard -----//
		var content = new ClipboardContent();

		if (event.getSource() instanceof ImageView viewer
				&& viewer.getUserData() instanceof TierElement sourceElement) {

			content.putImage(viewer.getImage());
			content.putString(Integer.valueOf(sourceElement.hashCode()).toString());

			dragBoard.setContent(content);
		}
		event.consume();
	}
	
	private void handleDragDroppedImage(DragEvent event) {

		Dragboard dragBoard = event.getDragboard();

		boolean success = false;

		TierElement sourceData = controller.getElementByHash(dragBoard.getString());
		EventTarget dragAndDropTarget = event.getTarget();

		// ----- update model -----//
		if (dragBoard.hasImage() && dragBoard.hasString() &&
				dragAndDropTarget instanceof ImageView targetImage &&
				!sourceData.equals(targetImage.getUserData())) {

			onDragDropped.accept(sourceData, (TierElement) targetImage.getUserData());

			success = true;

			this.updateImages();
		}
		event.setDropCompleted(success);
		
		event.consume();
	}
	
	private void handleDragDropped(DragEvent event) {
		Dragboard dragBoard = event.getDragboard();

		boolean success = false;

		if (dragBoard.hasImage() && dragBoard.hasString()
				&& event.getTarget() instanceof ElementsFlowPane targetElementPane) {

			TierElement sourceElement = controller.getElementByHash(dragBoard.getString());

			updateModel(sourceElement.status(), sourceElement, targetElementPane);
			updateImages();
			success = true;
		}
		event.setDropCompleted(success);

		event.consume();
	}
	
	private void updateModel(TierElementStatus status, TierElement sourceElement, ElementsFlowPane targetPane) {
		Optional<Tier> targetTier = targetPane.getTier();
		switch (status) {
			case TierElementStatus.RANKED: {
				if (targetTier.isPresent())
					controller.moveTo(sourceElement, targetTier.get());
				else
					controller.unrank(sourceElement);
			}
				break;
			case TierElementStatus.UNRANKED: {
				if (targetTier.isPresent())
					controller.rank(sourceElement, targetPane.getTier().get());
				else
					controller.moveUnranked(sourceElement, controller.getUnranked().getLast());
			}
				break;
			default:
				break;
		}
	}
	private void setTierElementsPaneBorder(String color) {
		var tierElementsPaneBorder = new Border(
				new BorderStroke(
						Paint.valueOf(color),
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY,
						BorderWidths.DEFAULT));
		this.setBorder(tierElementsPaneBorder);
}
	
	public void updateImages() {
		List<TierElement> toUpdate;

		toUpdate = tier.isPresent() ? tier.get().getElements() : controller.getUnranked();
		this.elements = toUpdate;

		this.images = loadImages();
		this.getChildren().clear();
		this.getChildren().addAll(images);
	}
}
