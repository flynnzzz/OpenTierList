package ui.gui.manual;

import controller.controllers.StandardTierListController;
import controller.controllers.TierListController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import persistence.ResourceHolder;
import ui.gui.manual.panels.MainPane;

public class MainApplication extends Application {

	@Override
	public void start(@SuppressWarnings("exports") Stage stage) throws Exception {
		
		TierListController controller = new StandardTierListController();
		
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
