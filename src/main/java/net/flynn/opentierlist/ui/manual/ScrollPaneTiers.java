package net.flynn.opentierlist.ui.manual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import net.flynn.opentierlist.controller.TierListController;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class ScrollPaneTiers extends ScrollPane {
  // ----- panels -----//
  private VBox tiersVBox;
  private MainPane parent;

  private TierListController controller;
  private ObservableList<HBoxTier> tierBoxList;

  public ScrollPaneTiers(MainPane parent, TierListController controller) {
    this.parent = parent;
    this.controller = controller;
    this.tierBoxList = FXCollections.observableArrayList();
    this.tiersVBox = new VBox();
    this.setupPane();
  }

  private void loadTiers() {
    controller.getTiers().forEach(tier -> {
      var tierPane = new HBoxTier(this, parent.getStage(), controller, tier);

      tierPane.getTextField().setOnDragDone(event -> {
        if (event.getTransferMode() == TransferMode.MOVE)
          updateAllTiers();
        event.consume();
      });

      tierBoxList.add(tierPane);
    });
  }

  private void setupPane() {
    // ----- init vbox -----//
    loadTiers();
    tiersVBox.getChildren().addAll(tierBoxList);
    this.setContent(tiersVBox);
    this.setFitToWidth(true);
    tiersVBox.setAlignment(Pos.CENTER);
  }

  public void updateAllTiers() {
    tiersVBox.getChildren().clear();
    tierBoxList.clear();

    controller.getTiers().forEach(tier -> {

      HBoxTier tierBox;
      tierBox = new HBoxTier(this, parent.getStage(), controller, tier);
      tierBox.getTextField().setOnDragDone(event -> {
        if (event.getTransferMode() == TransferMode.MOVE)
          updateAllTiers();
        event.consume();
      });
      tierBoxList.addAll(tierBox);
    });

    tiersVBox.getChildren().addAll(tierBoxList);
  }

  public void updateTierList() {
    parent.updateTierList();
  }
}
