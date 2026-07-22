package net.flynn.opentierlist.ui.manual;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.TelementStatus;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.Tier;

/**
 * 
 * @version 3.00
 * @since v1.2.5
 */
public class FlowPaneTelements extends FlowPane {
  // ----- panels -----//
  private TierListController controller;
  private ScrollPaneTiers greatparent;
  private ContextMenu imageContextMenu;
  private MenuItem deleteImageMenu, unrankImageMenu;

  // ---- cache -----//
  private static Map<Long, Image> imageCache;

  private Optional<Tier> tier;
  private List<TierElement> elements;
  private ObservableList<ImageView> images;
  private BiConsumer<TierElement, TierElement> onDragDropped;

  public FlowPaneTelements(ScrollPaneTiers greatparent, TierListController controller, List<TierElement> elements,
      Optional<Tier> tier, BiConsumer<TierElement, TierElement> onDragDropped) {
    this.controller = controller;
    this.tier = tier;
    this.elements = new ArrayList<>(elements);
    this.greatparent = greatparent;
    this.onDragDropped = onDragDropped;
    this.images = loadImages();
    if (imageCache == null) {
      System.err.println("--- Instantiating new imagecache ---");
      imageCache = new HashMap<>();
    }
    this.setupPane();
  }

  public FlowPaneTelements(ScrollPaneTiers greatparent, TierListController controller, List<TierElement> elements,
      BiConsumer<TierElement, TierElement> onDragDropped) {
    this(greatparent, controller, elements, Optional.empty(), onDragDropped);
  }

  public Optional<Tier> getTier() {
    return this.tier;
  }

  private void setupImage(ImageView imageViewer) {
    // ----- settings -----//
    imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
    imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);

    imageContextMenu = new ContextMenu();
    deleteImageMenu = new MenuItem("delete");
    unrankImageMenu = new MenuItem("unrank");

    imageViewer.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getButton() == MouseButton.SECONDARY) {

        deleteImageMenu.setOnAction(_ -> {
          if (imageViewer.getUserData() instanceof TierElement telement) {
            controller.removeTelement(telement);
            this.getChildren().remove(imageViewer);
            updateImages();
          }
        });

        if (imageViewer.getUserData() instanceof TierElement telement && telement.isRanked()) {
          imageContextMenu.getItems().add(unrankImageMenu);
          unrankImageMenu.setOnAction(_ -> {
            controller.unrank(telement);
            greatparent.updateTierList();
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
        target.setStyle(UISettings.IMAGE_SOURCE_EFFECT);
        source.setStyle(UISettings.IMAGE_TARGET_EFFECT);

        imageViewer.setFitHeight(UISettings.DEFAULT_EXPANDED_IMAGE_SIZE);
        this.setPadding(
            new Insets(0, UISettings.DEFAULT_DRAG_ENTERED_PADDING, 0, UISettings.DEFAULT_DRAG_ENTERED_PADDING));
      }
      event.consume();
    });

    imageViewer.setOnDragExited(event -> {
      if (event.getTarget() instanceof ImageView target && event.getGestureSource() instanceof ImageView source) {
        source.setStyle("-fx-effect: null;");
        target.setStyle("-fx-effect: null;");

        imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
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
        ? new ArrayList<>(tier.get().getElements())
        : new ArrayList<>(controller.getUnranked());

    images.clear();

    imageCache.keySet()
        .removeIf(id -> !controller.telementExistsById(id));

    images = loadImages();

    this.getChildren().clear();
    this.getChildren().addAll(images);
  }

  private ObservableList<ImageView> loadImages() {
    images = FXCollections.observableArrayList();

    elements.forEach(element -> {

      try {
        element.updateImagePath();
      } catch (FileNotFoundException e) {
        System.err.println("--- Default resource not found, aborting ---");
        System.exit(-1);
      }
      Image img = imageCache.get(element.getId());
      if (img == null) {
        img = new Image(element.getImageUri(),
            UISettings.DEFAULT_CELL_SIZE,
            UISettings.DEFAULT_CELL_SIZE,
            false,
            false);
        imageCache.put(element.getId(), img);
      }

      var imageViewer = new ImageView(img);
      imageViewer.setUserData(element);
      setupImage(imageViewer);
      images.add(imageViewer);
    });

    return images;
  }

  private void setupPane() {
    this.getChildren().addAll(images);
    this.initFlowPaneBorder();

    // ----- settings -----//
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

    // TODO: visuals could be improved
    this.setOnDragEntered(event -> {
      var sourceData = event.getGestureSource();
      if (sourceData instanceof ImageView && event.getDragboard().hasImage()) {
        this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_HIGHLIGHT_COLOR);
        this.setPadding(new Insets(UISettings.DEFAULT_DRAG_ENTERED_PADDING));
      }
      event.consume();
    });

    this.setOnDragExited(event -> {
      var sourceData = event.getGestureSource();
      if (sourceData instanceof ImageView && event.getDragboard().hasImage()) {
        this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_BORDER_COLOR);
        this.setPadding(Insets.EMPTY);
      }
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
            BorderWidths.DEFAULT));
    this.setBorder(flowPaneBorder);
  }

  private void handleDragDetectedImage(MouseEvent event) {

    // ----- define transfer mode -----//
    Dragboard dragBoard = ((ImageView) event.getSource()).startDragAndDrop(TransferMode.MOVE);

    // ----- save image and TierElement hashcode on dragboard -----//
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

    Optional<TierElement> sourceData = controller.getElementByHash(dragBoard.getString());

    if (sourceData.isEmpty())
      return;

    EventTarget dragAndDropTarget = event.getTarget();

    // ----- update model -----//
    if (dragBoard.hasImage() && dragBoard.hasString() &&
        dragAndDropTarget instanceof ImageView targetImage &&
        !sourceData.equals(targetImage.getUserData())) {

      onDragDropped.accept(sourceData.get(), (TierElement) targetImage.getUserData());

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
        && event.getTarget() instanceof FlowPaneTelements targetElementPane) {

      Optional<TierElement> sourceData = controller.getElementByHash(dragBoard.getString());

      if (sourceData.isEmpty())
        return;

      updateModel(sourceData.get().status(), sourceData.get(), targetElementPane);
      updateImages();
      success = true;
    }
    event.setDropCompleted(success);

    event.consume();
  }

  private void updateModel(TelementStatus status, TierElement sourceElement, FlowPaneTelements targetPane) {
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
