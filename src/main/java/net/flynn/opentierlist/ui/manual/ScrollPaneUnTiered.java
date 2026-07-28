package net.flynn.opentierlist.ui.manual;

import java.util.function.BiConsumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierElement;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class ScrollPaneUnTiered extends ScrollPane {
  // ----- panels -----//
  private final FlowPaneElements unTieredPane;

  public ScrollPaneUnTiered(MainPane parent, TierListController controller) {

    // ----- TierElementsPane's on 'drag dropped' behaviour -----//
    BiConsumer<TierElement, TierElement> onDragDropped = (src, dest) -> {
      switch (src.getStatus()) {
        case TieredStatus.UNTIERED: {
          controller.moveElement(src, Tier.UNTIERED, dest);
          break;
        }
        case TieredStatus.TIERED: {
          controller.unTier(src, dest);
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
    this.setMinHeight(UISettings.DEFAULT_UNRANKED_PANE_MAX_HEIGHT / 2);
    this.setFitToWidth(true);
    this.unTieredPane.setAlignment(Pos.CENTER_LEFT);

    this.setPadding(new Insets(
        UISettings.DEFAULT_UNRANKED_PADDING_TOP,
        UISettings.DEFAULT_UNRANKED_PADDING_RIGHT,
        UISettings.DEFAULT_UNRANKED_PADDING_BOTTOM,
        UISettings.DEFAULT_UNRANKED_PADDING_LEFT));

    this.setContent(unTieredPane);
  }

  public void updatePane() {
    unTieredPane.updateImages();
  }
}
