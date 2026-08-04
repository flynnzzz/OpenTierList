package net.flynn.opentierlist.ui.manual;

import java.util.function.BiConsumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.ui.ConfigHolder;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class SPUnTiered extends ScrollPane {
  private final FPElements unTieredPane;

  public SPUnTiered(MainPane parent, TierListController controller) {

    this.unTieredPane = new FPElements(parent.getFirstChild(), controller);
    setupPane();
  }

  private void setupPane() {
    this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

    this.setPrefWidth(ConfigHolder.DEFAULT_BAR_WIDTH);
    this.setMaxHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MAX_HEIGHT);
    this.setMinHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MAX_HEIGHT / 2);
    this.setFitToWidth(true);
    this.unTieredPane.setAlignment(Pos.CENTER_LEFT);

    this.setPadding(new Insets(
        ConfigHolder.DEFAULT_UNRANKED_PADDING_TOP,
        ConfigHolder.DEFAULT_UNRANKED_PADDING_RIGHT,
        ConfigHolder.DEFAULT_UNRANKED_PADDING_BOTTOM,
        ConfigHolder.DEFAULT_UNRANKED_PADDING_LEFT));

    this.setContent(unTieredPane);
  }

  public void updatePane() {
    unTieredPane.updateImages();
  }
}
