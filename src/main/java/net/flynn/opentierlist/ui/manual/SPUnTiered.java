package net.flynn.opentierlist.ui.manual;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.ui.ConfigHolder;
import net.flynn.opentierlist.controller.GraphicsController;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class SPUnTiered extends ScrollPane {
  private final FPElements unTieredPane;
  private final GraphicsController graphicsController;

  public SPUnTiered(TierListController controller, GraphicsController graphicsController) {

    this.graphicsController = graphicsController;
    this.unTieredPane = new FPElements(controller, graphicsController);
    setupPane();
  }

  private void setupPane() {
    this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    this.setVbarPolicy(ScrollBarPolicy.NEVER);

    this.setFitToWidth(true);
    this.setPrefWidth(ConfigHolder.DEFAULT_BAR_WIDTH);

    this.setPrefHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MIN_HEIGHT);

    graphicsController.setBorder(this, ConfigHolder.DEFAULT_ACCENT_COLOR_LIGHT);

    unTieredPane.setPrefWidth(ConfigHolder.DEFAULT_BAR_WIDTH);

    unTieredPane.setMaxHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MAX_HEIGHT);
    unTieredPane.setMinHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MIN_HEIGHT);
    unTieredPane.setAlignment(Pos.CENTER_LEFT);

    this.setContent(unTieredPane);
  }

  public void update() {
    graphicsController.updateImages(unTieredPane);
  }

}
