package net.flynn.opentierlist.ui.manual;

import java.util.function.BiConsumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.models.TierElement;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class ScrollPaneUnTiered extends ScrollPane {
  // ----- panels -----//
  private final FlowPaneElements unTieredPane;

  private final TierListController controller;

    public ScrollPaneUnTiered(MainPane parent, TierListController controller) {
      this.controller = controller;

    // ----- TierElementsPane's on 'drag dropped' behaviour -----//
        BiConsumer<TierElement, TierElement> onDragDropped = (TierElement s, TierElement t) -> {
            switch (s.status()) {
                case TieredStatus.UNTIERED: {
                    this.controller.moveUnTiered(s, t);
                    break;
                }
                case TieredStatus.TIERED: {
                    this.controller.unTier(s, controller.getUnTiered().indexOf(t));
                    break;
                }
                default: {
                    System.err.println("--- Unexpected error, exiting ---");
                    System.exit(-1);
                }
            }
        };

    this.unTieredPane = new FlowPaneElements(parent.getFirstChild(), controller, controller.getUnTiered(),
            onDragDropped);

    this.setupPane();
  }

  private void setupPane() {
    this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

    this.setPrefWidth(UISettings.DEFAULT_BAR_WIDTH);
    this.setMaxHeight(UISettings.DEFAULT_UNRANKED_PANE_MAX_HEIGHT);
    this.setMinHeight(UISettings.DEFAULT_UNRANKED_PANE_MAX_WIDTH);
    this.setFitToWidth(true);
    this.unTieredPane.setAlignment(Pos.CENTER_LEFT);

    this.setOnDragEntered(this::handleDragEntered);
    this.setOnDragExited(_ -> this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_BORDER_COLOR));

    this.setPadding(new Insets(
        UISettings.DEFAULT_UNRANKED_PADDING_TOP,
        UISettings.DEFAULT_UNRANKED_PADDING_RIGHT,
        UISettings.DEFAULT_UNRANKED_PADDING_BOTTOM,
        UISettings.DEFAULT_UNRANKED_PADDING_LEFT));
    this.setUnTieredBorder();

    this.setContent(unTieredPane);
  }

  private void setUnTieredBorder() {
    var unrankedBorder = new Border(
        new BorderStroke(
            Paint.valueOf(UISettings.DEFAULT_BAR_BORDER_COLOR),
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            BorderWidths.DEFAULT));
    unTieredPane.setBorder(unrankedBorder);
  }

  private void handleDragEntered(DragEvent event) {
    if (event.getGestureSource() instanceof ImageView
        && event.getTarget() instanceof ScrollPaneUnTiered
        && event.getDragboard().hasImage())
      setTierElementsPaneBorder(UISettings.DEFAULT_BAR_HIGHLIGHT_COLOR);

    event.consume();
  }

  private void setTierElementsPaneBorder(String color) {
    var tierElementsPaneBorder = new Border(
        new BorderStroke(
            Paint.valueOf(color),
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            BorderWidths.DEFAULT));
    this.unTieredPane.setBorder(tierElementsPaneBorder);
  }

  public void updatePane() {
    unTieredPane.updateImages();
  }
}
