package net.flynn.opentierlist.ui.manual;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.models.Tier;

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
  private static Map<Tier, HBoxTier> tiersCache;

  public ScrollPaneTiers(MainPane parent, TierListController controller) {
    this.parent = parent;
    this.controller = controller;
    this.tierBoxList = FXCollections.observableArrayList();
    this.tiersVBox = new VBox();
    if (tiersCache == null)
      tiersCache = new HashMap<>();
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

      tiersCache.put(tier, tierPane);
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

    tiersCache.keySet()
        .removeIf(t -> !controller.getTiers().contains(t));

    System.err.println("--- Tier cache: ---");
    System.err.println(tiersCache.keySet().stream()
        .map(t -> t.toString() + ": " + tiersCache.get(t))
        .sorted()
        .collect(Collectors.joining(System.lineSeparator())));

    controller.getTiers().forEach(tier -> {
      Optional<HBoxTier> potentialCachedHBox = Optional.ofNullable(tiersCache.get(tier));

      HBoxTier tierBox;
      if (potentialCachedHBox.isPresent()) {
        tierBox = potentialCachedHBox.get();
      } else {
        tierBox = new HBoxTier(this, parent.getStage(), controller, tier);
        tierBox.getTextField().setOnDragDone(event -> {
          if (event.getTransferMode() == TransferMode.MOVE)
            updateAllTiers();
          event.consume();
        });
        tiersCache.put(tier, tierBox);
      }
      tierBoxList.addAll(tierBox);
    });

    tiersVBox.getChildren().addAll(tierBoxList);
  }

  public void updateTierList() {
    parent.updateTierList();
  }
}
