package net.flynn.opentierlist.ui.manual;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.ui.ConfigHolder;
import net.flynn.opentierlist.controller.GraphicsController;

/**
 *
 * @version 3.80
 * @since v1.2.5
 */
public class FPElements extends FlowPane {

  private final TierListController tierListController;
  private final GraphicsController graphicsController;

  private final TieredStatus status;
  private final Tier tier;

  public FPElements(TierListController tierListController, GraphicsController graphicsController, Tier tier) {

    this.tierListController = tierListController;
    this.graphicsController = graphicsController;
    this.tier = tier;
    this.status = tier.equalsTier(Tier.UNTIERED) ? TieredStatus.UNTIERED : TieredStatus.TIERED;

    final List<TierElement> elements = tier.equalsTier(Tier.UNTIERED) ? tierListController.getUnTiered() : tier.getTiered();
    setupPane(graphicsController.loadImages(this, elements));
  }

  public FPElements( TierListController tierListController, GraphicsController graphicsController) {
    this(tierListController, graphicsController, Tier.UNTIERED);
  }

  private void setupPane(List<IMGElement> images) {
    this.getChildren().addAll(images);

    graphicsController.setFlowPaneBorder(this, ConfigHolder.DEFAULT_BAR_BORDER_COLOR);
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
        graphicsController.setFlowPaneBorder(this, ConfigHolder.DEFAULT_BAR_HIGHLIGHT_COLOR);
        this.setPadding(new Insets(ConfigHolder.DEFAULT_DRAG_ENTERED_PADDING));
      }
      event.consume();
    });

    this.setOnDragExited(event -> {
      var sourceData = event.getGestureSource();
      if (sourceData instanceof ImageView && event.getDragboard().hasImage()) {
        graphicsController.setFlowPaneBorder(this, ConfigHolder.DEFAULT_BAR_BORDER_COLOR);
        this.setPadding(Insets.EMPTY);
      }
      event.consume();
    });

    this.setOnDragDropped(this::handleDragDropped);

    this.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE)
        graphicsController.updateImages(this);
      event.consume();
    });
  }

  private void handleDragDropped(DragEvent event) {
    boolean success = false;

    Dragboard dragBoard = event.getDragboard();
    if (dragBoard.hasImage() && dragBoard.hasString()
        && event.getTarget() instanceof FPElements) {

      String elementHash = dragBoard.getString();
      Optional<TierElement> potentialSource = tierListController.getElementByHash(elementHash);
      if (potentialSource.isEmpty()) {
        System.err.println("[ERROR] --- Element to be dropped was not found ---");
        return;
      }

      var source = potentialSource.get();

      tierListController.moveElement(source, this.tier);

      graphicsController.updateImages(this);

      success = true;
    }
    event.setDropCompleted(success);
    event.consume();
  }

  public TieredStatus status() {
    return this.status;
  }

  public Tier getTier() {
    return tier.copy();
  }
}
