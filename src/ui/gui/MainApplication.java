package ui.gui;

import controller.controllers.StandardTierListController;
import controller.controllers.TierListController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.gui.panels.MainPane;

public class MainApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		TierListController controller = new StandardTierListController();
		stage.setTitle("Open Tier Lists");
		BorderPane root = new MainPane(controller);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
