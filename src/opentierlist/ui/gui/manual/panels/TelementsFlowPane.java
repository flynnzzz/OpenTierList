package opentierlist.ui.gui.manual.panels;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import opentierlist.controller.controllers.TierListController;
import opentierlist.model.enums.TelementStatus;
import opentierlist.model.models.Telement;
import opentierlist.model.models.Tier;
import opentierlist.ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 2.90
 * @since v1.2.5
 */
public class TelementsFlowPane extends FlowPane {
	// ----- panels -----//
	private TierListController controller;
	private TiersScrollPane greatparent;
	private ContextMenu imageContextMenu;
	private MenuItem deleteImageMenu, unrankImageMenu;
	
	private Optional<Tier> tier;
	private ObservableList<Telement> elements;
	private ObservableList<ImageView> images;
	private BiConsumer<Telement, Telement> onDragDropped;
	
	public TelementsFlowPane(TiersScrollPane greatparent, TierListController controller, List<Telement> elements, 
			Optional<Tier> tier, BiConsumer<Telement, Telement> onDragDropped) {
		this.controller = controller;
		this.tier = tier;
		this.elements = FXCollections.observableArrayList(elements);
		this.greatparent = greatparent;
		this.onDragDropped = onDragDropped;
		this.images = loadImages();
		this.setupPane();
	}
	
	public TelementsFlowPane(TiersScrollPane greatparent, TierListController controller, List<Telement> elements, 
			BiConsumer<Telement, Telement> onDragDropped) {
		this(greatparent, controller, elements, Optional.empty(), onDragDropped);
	}
	
	public Optional<Tier> getTier() {
		return this.tier;
	}
	
	private void setupImages(ImageView imageViewer) {
		// ----- settings -----//
		imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
		imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);

		imageContextMenu = new ContextMenu();
		deleteImageMenu = new MenuItem("Delete");
		unrankImageMenu = new MenuItem("Unrank");

		imageViewer.setOnMouseClicked( mouseEvent -> {
			if (mouseEvent.getButton() == MouseButton.SECONDARY) {
				deleteImageMenu.setOnAction(_ -> {
					if (imageViewer.getUserData() instanceof Telement telement) {
						controller.removeTelement(telement);
						this.getChildren().remove(imageViewer);
						updateImages();
					}
				});
				
				if (imageViewer.getUserData() instanceof Telement telement && telement.isRanked()) {
					imageContextMenu.getItems().add(unrankImageMenu);
					unrankImageMenu.setOnAction(_ -> {
						controller.unrank(telement);
						greatparent.updateAll();
					});
				}
				imageContextMenu.getItems().add(deleteImageMenu);
				imageContextMenu.show(imageViewer, Side.RIGHT, 0, 0);
			}
		});
		
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
				target.setStyle(
						"-fx-effect: dropshadow(gaussian, rgba(255,64,0,0.95), 6, 0.2, 0, 0);"
				);
				source.setStyle(
						"-fx-effect: dropshadow(gaussian, rgba(0,191,255,0.95), 6, 0.2, 0, 0);"
				);
				
				this.setPadding(new Insets(8));
				this.setHgap(4);
			}
			event.consume();
		});

		imageViewer.setOnDragExited(event -> {
			if (event.getTarget() instanceof ImageView target && event.getGestureSource() instanceof ImageView source) {
				source.setStyle("-fx-effect: null;");
				target.setStyle("-fx-effect: null;");
				
				// TODO: fix flickering
				this.setPadding(Insets.EMPTY);
				this.setHgap(0);
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
	
	public void updateImages() {
		elements = tier.isPresent() 
				? FXCollections.observableArrayList(tier.get().getElements())
				: FXCollections.observableArrayList(controller.getUnranked());
		
		images.clear();

		images = loadImages();
		this.getChildren().clear();
		this.getChildren().addAll(images);
	}
	
	private ObservableList<ImageView> loadImages() {
		this.images = FXCollections.observableArrayList();
		
		elements.forEach( element -> {
			element.updateImagePath();
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
				&& viewer.getUserData() instanceof Telement sourceElement) {

			content.putImage(viewer.getImage());
			content.putString(Integer.valueOf(sourceElement.hashCode()).toString());

			dragBoard.setContent(content);
		}
		event.consume();
	}
	
	private void handleDragDroppedImage(DragEvent event) {

		Dragboard dragBoard = event.getDragboard();

		boolean success = false;

		Optional<Telement> sourceData = controller.getElementByHash(dragBoard.getString());
		
		if (sourceData.isEmpty()) return;
		
		EventTarget dragAndDropTarget = event.getTarget();

		// ----- update model -----//
		if (dragBoard.hasImage() && dragBoard.hasString() &&
				dragAndDropTarget instanceof ImageView targetImage &&
				!sourceData.equals(targetImage.getUserData())) {

			onDragDropped.accept(sourceData.get(), (Telement) targetImage.getUserData());

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
				&& event.getTarget() instanceof TelementsFlowPane targetElementPane) {

			Optional<Telement> sourceData = controller.getElementByHash(dragBoard.getString());

			if (sourceData.isEmpty()) return;
			
			updateModel(sourceData.get().status(), sourceData.get(), targetElementPane);
			updateImages();
			success = true;
		}
		event.setDropCompleted(success);

		event.consume();
	}
	
	private void updateModel(TelementStatus status, Telement sourceElement, TelementsFlowPane targetPane) {
		Optional<Tier> targetTier = targetPane.getTier();
		switch (status) {
			case TelementStatus.RANKED: {
				if (targetTier.isPresent())
					controller.moveTo(sourceElement, targetTier.get());
				else
					controller.unrank(sourceElement);
			}
				break;
			case TelementStatus.UNRANKED: {
				if (targetTier.isPresent())
					controller.rank(sourceElement, targetPane.getTier().get());
				else
					controller.moveUnranked(sourceElement, controller.getUnranked().getLast());
			}
				break;
			default: {
				System.err.println("--- Unexpected error, exiting ---");
				System.exit(-1);
			}
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

}
