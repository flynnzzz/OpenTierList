package net.flynn.opentierlist;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.flynn.opentierlist.controller.*;
import net.flynn.opentierlist.model.models.TierList;
import net.flynn.opentierlist.persistence.ResourceHolder;
import net.flynn.opentierlist.ui.manual.MainPane;

public class MainApplication extends Application {
  @Override
  public void start(Stage stage) {

    final var controller = TierListController.ofDefaultTiers();
    final BorderPane root = new MainPane(controller, stage);
    final Scene scene = new Scene(root);

    stage.setTitle(TierList.DEFAULT_TIER_LIST_NAME);
    stage.setHeight(900);
    stage.setWidth(1100);

    final var imageResource = ResourceHolder.getEditButtonIcon();

    stage.getIcons().add(new Image(imageResource));
    stage.setScene(scene);

    stage.show();
    stage.setX((1980 - stage.getWidth()) / 2);
    stage.setY(1080 - stage.getHeight() / 2);
  }

}
