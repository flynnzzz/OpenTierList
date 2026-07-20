package net.flynn.opentierlist;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.flynn.opentierlist.controller.*;
import net.flynn.opentierlist.model.enums.DefaultTier;
import net.flynn.opentierlist.model.models.Telement;
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
		
		var unranked = new ArrayList<Telement>();

		@SuppressWarnings("unused")
		Telement m, p, s, g, o, a;
		unranked.add(m = new Telement("Mookka", "cow.jpg")); unranked.add(p = new Telement("Pehkura", "sheep.jpg")); unranked.add(s = new Telement("Suynoh"));
		unranked.add(g = new Telement("Galeena")); unranked.add(o = new Telement("Okha", "duck.jpg")); unranked.add(a = new Telement("Aseeno", "usagi.jpg"));
		
		var defaultTierList = new TierList("Test", unranked);
		for (var defaultTier : DefaultTier.values()) {
			defaultTierList.addTier(defaultTier.value());
		}
		
		TierListController controller = new StandardTierListController(defaultTierList);
		
		controller.rank(m, DefaultTier.S.value());
		controller.rank(p, DefaultTier.S.value());
		controller.rank(s, DefaultTier.A.value());
		
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
