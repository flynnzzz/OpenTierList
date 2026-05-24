package ui.gui;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.StandardTierListController;
import controller.controllers.TierListController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.enums.DefaultTier;
import model.models.ListTierElement;
import model.models.Tier;
import model.models.TierElement;
import model.models.TierList;
import ui.gui.panels.MainPane;

public class MainApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		var unranked = new ListTierElement();

		TierElement m, p, s, g, o, a;
		unranked.add(m = new TierElement("Mookka")); unranked.add(p = new TierElement("Pehkura")); unranked.add(s = new TierElement("Suynoh"));
		unranked.add(g = new TierElement("Galeena")); unranked.add(o = new TierElement("Okha")); unranked.add(a = new TierElement("Aseeno"));

		var defaultTierList = new TierList("Test", unranked);
		for (var defaultTier : DefaultTier.values()) {
			defaultTierList.addTier(defaultTier.value());
		}
		
		TierListController controller = new StandardTierListController(defaultTierList);
		
		controller.rank(m, DefaultTier.S.value());
		controller.rank(p, DefaultTier.S.value());
		
		stage.setTitle("Open Tier Lists");
		BorderPane root = new MainPane(controller);
		Scene scene = new Scene(root);
		stage.setHeight(900);
		stage.setWidth(1100);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
