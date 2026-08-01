package net.flynn.opentierlist.ui.manual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import net.flynn.opentierlist.controller.TierListController;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class ScrollPaneTiers extends ScrollPane {
  // ----- panels -----//
  private final VBox tiersVBox;
  private final MainPane parent;

  private final TierListController controller;
  private final ObservableList<HBoxTier> tierBoxList;

  public ScrollPaneTiers(MainPane parent, TierListController controller) {
    this.parent = parent;
    this.controller = controller;
    this.tierBoxList = FXCollections.observableArrayList();
    this.tiersVBox = new VBox();
    this.setupPane();
  }

  private void loadTiers() {
    controller.getTiers().forEach(tier -> tierBoxList.add(new HBoxTier(this, parent.getStage(), controller, tier)));
  }

  private void setupPane() {
    // ----- init vbox -----//
    loadTiers();
    tiersVBox.getChildren().addAll(tierBoxList);
    this.setContent(tiersVBox);
    this.setFitToWidth(true);
    this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

    updateBorder(UISettings.DEFAULT_ACCENT_COLOR_LIGHT);

    tiersVBox.setAlignment(Pos.CENTER);
    tiersVBox.setPadding(new Insets(UISettings.DEFAULT_TIERS_VBOX_PADDING));
  }

  public void updateAllTiers() {
    tiersVBox.getChildren().clear();
    tierBoxList.clear();

    loadTiers();

    tiersVBox.getChildren().addAll(tierBoxList);
  }

  public void updateTierList() {
    parent.updateTierList();
  }

  public void updateBorder(String color) {
    final var border = new Border(
            new BorderStroke(
                    Paint.valueOf(color),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT));
    this.setBorder(border);
  }
}
