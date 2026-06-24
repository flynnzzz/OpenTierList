package ui.gui.manual;

import java.util.ArrayList;

import controller.controllers.StandardTierListController;
import controller.controllers.TierListController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.enums.DefaultTier;
import model.models.TierElement;
import model.models.TierList;
import ui.gui.manual.panels.MainPane;

/**
 * 
 * @version 1.01
 * @since v1.2.5
 */
public class MainApplicationMock extends Application {

	@Override
	@SuppressWarnings("exports")
	public void start(Stage stage) throws Exception {
		
		var unranked = new ArrayList<TierElement>();

		@SuppressWarnings("unused")
		TierElement m, p, s, g, o, a;
		unranked.add(m = new TierElement("Mookka", "cow.jpg")); unranked.add(p = new TierElement("Pehkura", "sheep.jpg")); unranked.add(s = new TierElement("Suynoh"));
		unranked.add(g = new TierElement("Galeena")); unranked.add(o = new TierElement("Okha", "duck.jpg")); unranked.add(a = new TierElement("Aseeno", "usagi.jpg"));
		
		var defaultTierList = new TierList("Test", unranked);
		for (var defaultTier : DefaultTier.values()) {
			defaultTierList.addTier(defaultTier.value());
		}
		
		TierListController controller = new StandardTierListController(defaultTierList);
		
		controller.rank(m, DefaultTier.S.value());
		controller.rank(p, DefaultTier.S.value());
		controller.rank(s, DefaultTier.A.value());
		
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
