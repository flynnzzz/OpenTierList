package net.flynn.opentierlist.ui.manual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.ui.ConfigHolder;

/**
 *
 * @version 3.80
 * @since v1.2.5
 */
public class FPElements extends FlowPane {

  private final SPTiered grandparent;
  private ObservableList<IMGElement> images;

  private static Map<Long, Image> imageCache;

  private final TierListController controller;
  private final Tier tier;
  private List<TierElement> elements;

  public FPElements(
          SPTiered grandparent, TierListController controller, Tier tier
  ) {
    this.controller = controller;
    this.grandparent = grandparent;
    this.tier = tier;
    this.elements = tier.equalsTier(Tier.UNTIERED) ? controller.getUnTiered() : tier.getTiered();

    this.images = loadImages();
    if (imageCache == null) {
      System.err.println("--- Instantiating new image cache ---");
      imageCache = new HashMap<>();
    }
    this.setupPane();
  }

  public FPElements( SPTiered grandparent, TierListController controller ) {
    this(grandparent, controller, Tier.UNTIERED);
  }

  public static void reloadImageCache() {
    System.err.println("--- Reloading image cache ---");
    imageCache.clear();
  }

  public void updateImages() {
    elements = !tier.equalsTier(Tier.UNTIERED) ? tier.getTiered() : controller.getUnTiered();

    imageCache.keySet()
        .removeIf(id -> !controller.elementExists(id));

    images = loadImages();

    this.getChildren().clear();
    this.getChildren().addAll(images);
  }

  private ObservableList<IMGElement> loadImages() {

    if (images != null)
      images.clear();

    images = FXCollections.observableArrayList();

    elements.forEach(element -> {

      element.updateImagePath();
      Image img = imageCache.get(element.getId());

      if (img == null) {
        img = new Image(element.getImageUri(),
            ConfigHolder.DEFAULT_CELL_SIZE,
            ConfigHolder.DEFAULT_CELL_SIZE,
            false,
            false);
        imageCache.put(element.getId(), img);
      }

      var imageViewer = new IMGElement(
              img, controller, this, grandparent
      );
      imageViewer.setUserData(element);
      images.add(imageViewer);

    });

    return images;
  }

  private void setupPane() {
    this.getChildren().addAll(images);

    setFlowPaneBorder(ConfigHolder.DEFAULT_BAR_BORDER_COLOR);
    this.setPrefWidth(ConfigHolder.DEFAULT_BAR_WIDTH);
    this.setMaxHeight(ConfigHolder.DEFAULT_BAR_MAX_HEIGHT);
    this.setMinHeight(ConfigHolder.DEFAULT_BAR_MIN_HEIGHT);

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
      if (sourceData instanceof ImageView && event.getDragboard().hasImage()) {
        this.setFlowPaneBorder(ConfigHolder.DEFAULT_BAR_HIGHLIGHT_COLOR);
        this.setPadding(new Insets(ConfigHolder.DEFAULT_DRAG_ENTERED_PADDING));
      }
      event.consume();
    });

    this.setOnDragExited(event -> {
      var sourceData = event.getGestureSource();
      if (sourceData instanceof ImageView && event.getDragboard().hasImage()) {
        this.setFlowPaneBorder(ConfigHolder.DEFAULT_BAR_BORDER_COLOR);
        this.setPadding(Insets.EMPTY);
      }
      event.consume();
    });

    this.setOnDragDropped(this::handleDragDropped);

    this.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE)
        updateImages();
      event.consume();
    });
  }

  private void handleDragDropped(DragEvent event) {
    boolean success = false;

    Dragboard dragBoard = event.getDragboard();
    if (dragBoard.hasImage() && dragBoard.hasString()
        && event.getTarget() instanceof FPElements) {

      String elementHash = dragBoard.getString();
      Optional<TierElement> potentialSource = controller.getElementByHash(elementHash);
      if (potentialSource.isEmpty()) {
        System.err.println("--- Element to be dropped was not found ---");
        return;
      }

      var source = potentialSource.get();

      controller.moveElement(source, this.tier);

      updateImages();

      success = true;
    }
    event.setDropCompleted(success);
    event.consume();
  }

  private void setFlowPaneBorder(String color) {
    final var border = new Border(
        new BorderStroke(
            Paint.valueOf(color),
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            BorderWidths.DEFAULT));
    this.setBorder(border);
  }

}
