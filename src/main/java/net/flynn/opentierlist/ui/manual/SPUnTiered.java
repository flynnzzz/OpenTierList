package net.flynn.opentierlist.ui.manual;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import net.flynn.opentierlist.controller.TierListController;
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
    this.setVbarPolicy(ScrollBarPolicy.NEVER);

    this.setFitToWidth(true);

    this.setPrefHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MIN_HEIGHT);

    updateBorder(ConfigHolder.DEFAULT_ACCENT_COLOR_LIGHT);

    unTieredPane.setPrefWidth(ConfigHolder.DEFAULT_BAR_WIDTH);

    unTieredPane.setMaxHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MAX_HEIGHT);
    unTieredPane.setMinHeight(ConfigHolder.DEFAULT_UNRANKED_PANE_MIN_HEIGHT);
    unTieredPane.setAlignment(Pos.CENTER);


    this.setContent(unTieredPane);
  }

  public void updateBorder(String color) {
    final var border = new Border(
            new BorderStroke(
                    Paint.valueOf(color),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
            )
    );
    this.setBorder(border);
  }

  public void updatePane() {
    unTieredPane.updateImages();
  }
}
