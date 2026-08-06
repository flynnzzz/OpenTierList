package net.flynn.opentierlist.ui.manual;

import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.ui.ConfigHolder;
import net.flynn.opentierlist.controller.GraphicsController;

import java.util.Optional;

public class ElementView extends ImageView {

  private final TierListController tierListController;
  private final GraphicsController graphicsController;

  private final ElementPane parent;

  public ElementView(
      Image image, TierListController tierListController, GraphicsController graphicsController, ElementPane parent) {
    super(image);
    this.tierListController = tierListController;
    this.graphicsController = graphicsController;
    this.parent = parent;

    this.setFitHeight(ConfigHolder.DEFAULT_CELL_SIZE);
    this.setFitWidth(ConfigHolder.DEFAULT_CELL_SIZE);

    setupEventHandlers();
  }

  private void setupEventHandlers() {

    this.setOnMouseClicked(mouseEvent -> {

      final var imageContextMenu = new ContextMenu();
      if (mouseEvent.getButton() == MouseButton.SECONDARY) {

        final var deleteImageMenu = new MenuItem("Delete");
        deleteImageMenu.setOnAction(_ -> {

          if (this.getUserData() instanceof TierElement element) {
            tierListController.removeElement(element);
            parent.getChildren().remove(this);
            graphicsController.updateImages(parent);
          }
        });

        if (this.getUserData() instanceof TierElement element && element.isTiered()) {

          final var unTierImageMenu = new MenuItem("UnTier");
          imageContextMenu.getItems().add(unTierImageMenu);
          unTierImageMenu.setOnAction(_ -> {

            tierListController.unTier(element);
            graphicsController.updateTierList();
          });
        }

        imageContextMenu.getItems().add(deleteImageMenu);
        imageContextMenu.show(this, Side.RIGHT, 0, 0);

      }
    });

    this.setOnDragDetected(this::handleDragDetectedImage);

    this.setOnDragOver(event -> {
      if (event.getDragboard().hasImage())
        event.acceptTransferModes(TransferMode.MOVE);
      event.consume();
    });
    this.setOnDragEntered(event -> {
      if (event.getDragboard().hasImage()) {

        this.setFitHeight(ConfigHolder.DEFAULT_EXPANDED_IMAGE_SIZE);
        graphicsController.setScrollPaneWidth(
            parent.status(), ConfigHolder.DEFAULT_BAR_WIDTH + 16);

        parent.setPadding(
            new Insets(
                0, ConfigHolder.DEFAULT_DRAG_ENTERED_PADDING,
                0, ConfigHolder.DEFAULT_DRAG_ENTERED_PADDING));
      }
      event.consume();
    });

    this.setOnDragExited(event -> {
      if (event.getTarget() instanceof ImageView && event.getSource() instanceof ImageView) {

        this.setFitHeight(ConfigHolder.DEFAULT_CELL_SIZE);
        graphicsController.setScrollPaneWidth(
            parent.status(), ConfigHolder.DEFAULT_BAR_WIDTH);

        parent.setPadding(Insets.EMPTY);

      }
      event.consume();
    });

    this.setOnDragDropped(this::handleDragDroppedImage);

    this.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE)
        graphicsController.updateImages(parent);
      event.consume();
    });
  }

  private void handleDragDetectedImage(MouseEvent event) {

    if (!(event.getSource() instanceof ImageView sourceImage))
      return;

    Dragboard dragBoard = sourceImage.startDragAndDrop(TransferMode.MOVE);
    var content = new ClipboardContent();

    if (sourceImage.getUserData() instanceof TierElement sourceElement) {

      content.putImage(sourceImage.getImage());
      content.putString(String.valueOf(sourceElement.hashCode()));

      dragBoard.setContent(content);
    }
    event.consume();
  }

  private void handleDragDroppedImage(DragEvent event) {

    boolean success = false;

    Dragboard dragBoard = event.getDragboard();
    String elementHash = dragBoard.getString();

    Optional<TierElement> potentialSource = tierListController.getElementByHash(elementHash);
    if (potentialSource.isEmpty()) {
      System.err.println("[ERROR] --- Element to be dropped was not found ---");
      return;
    }

    EventTarget eventTarget = event.getTarget();
    if (dragBoard.hasImage() && dragBoard.hasString()
        && eventTarget instanceof ImageView targetImage
        && targetImage.getUserData() instanceof TierElement targetElement) {

      TierElement sourceElement = potentialSource.get();

      if (!sourceElement.equals(targetElement)) {

        Optional<Tier> potentialTargetTier = tierListController.getTierByElement(targetElement);

        potentialTargetTier.ifPresent(
            targetTier -> tierListController.insertElement(sourceElement, targetTier, targetElement));
      }

      graphicsController.updateImages(parent);

      success = true;
    }

    event.setDropCompleted(success);
    event.consume();

  }
}
