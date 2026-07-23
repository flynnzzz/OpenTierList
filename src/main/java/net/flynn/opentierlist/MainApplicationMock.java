package net.flynn.opentierlist;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.flynn.opentierlist.controller.*;
import net.flynn.opentierlist.model.enums.DefaultTier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.TierList;
import net.flynn.opentierlist.persistence.ResourceHolder;
import net.flynn.opentierlist.ui.manual.MainPane;

/**
 * 
 * @version 1.01
 * @since v1.2.5
 */
public class MainApplicationMock extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    var unranked = new ArrayList<TierElement>();

    @SuppressWarnings("unused")
    TierElement m, p, s, g, o, a;
    unranked.add(m = new TierElement("Mookka", "cow.jpg"));
    unranked.add(p = new TierElement("Pehkura", "sheep.jpg"));
    unranked.add(s = new TierElement("Suynoh"));
    unranked.add(g = new TierElement("Galeena"));
    unranked.add(o = new TierElement("Okha", "duck.jpg"));
    unranked.add(a = new TierElement("Aseeno", "usagi.jpg"));

    var defaultTierList = new TierList("Test", unranked);
    for (var defaultTier : DefaultTier.values()) {
      defaultTierList.addTier(defaultTier.value());
    }

    TierListController controller = new StandardTierListController(defaultTierList);

    controller.tier(m, DefaultTier.S.value());
    controller.tier(p, DefaultTier.S.value());
    controller.tier(s, DefaultTier.A.value());

    stage.setTitle("Open Tier Lists");
    BorderPane root = new MainPane(controller, stage);
    Scene scene = new Scene(root);

    stage.setHeight(900);
    stage.setWidth(1100);
    stage.getIcons().add(new Image(ResourceHolder.getEditButtonIcon()));
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
