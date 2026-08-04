package net.flynn.opentierlist.ui.manual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

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

/**
 *
 * @version 3.40
 * @since v1.2.5
 */
public class FPElements extends FlowPane {
  private final SPTiers grandparent;
  private ObservableList<IMGElement> images;

  private static Map<Long, Image> imageCache;

  private final TierListController controller;
  private final Tier tier;
  private final BiConsumer<TierElement, TierElement> onDragDropped;
  private List<TierElement> elements;

  public FPElements(SPTiers grandparent, TierListController controller, List<TierElement> elements,
                    Tier tier, BiConsumer<TierElement, TierElement> onDragDropped) {
    this.controller = controller;
    this.elements = new ArrayList<>(elements);

    this.grandparent = grandparent;
    this.onDragDropped = onDragDropped;

    this.tier = tier;

    this.images = loadImages();
    if (imageCache == null) {
      System.err.println("--- Instantiating new image cache ---");
      imageCache = new HashMap<>();
    }
    this.setupPane();
  }

  public FPElements(SPTiers grandparent, TierListController controller, List<TierElement> elements,
                    BiConsumer<TierElement, TierElement> onDragDropped) {
    this(grandparent, controller, elements, Tier.UNTIERED, onDragDropped);
  }

  public static void reloadImageCache() {
    System.err.println("--- Reloading image cache ---");
    imageCache.clear();
  }

  public void updateImages() {
    elements = !tier.equalsTier(Tier.UNTIERED) ? new ArrayList<>(tier.getTiered())
        : new ArrayList<>(controller.getUnTiered());

    images.clear();

    imageCache.keySet()
        .removeIf(id -> !controller.tierElementExistsById(id));

    images = loadImages();

    this.getChildren().clear();
    this.getChildren().addAll(images);
  }

  private ObservableList<IMGElement> loadImages() {
    images = FXCollections.observableArrayList();

    elements.forEach(element -> {

      element.updateImagePath();
      Image img = imageCache.get(element.getId());

      if (img == null) {
        img = new Image(element.getImageUri(),
            UISettings.DEFAULT_CELL_SIZE,
            UISettings.DEFAULT_CELL_SIZE,
            false,
            false);
        imageCache.put(element.getId(), img);
      }

      var imageViewer = new IMGElement(
              img, controller, this, grandparent, onDragDropped
      );
      imageViewer.setUserData(element);

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
        updateImages();
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
