package opentierlist.ui.gui.manual;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import opentierlist.controller.controllers.StandardTierListController;
import opentierlist.controller.controllers.TierListController;
import opentierlist.model.models.TierList;
import opentierlist.persistence.ResourceHolder;
import opentierlist.ui.gui.manual.panels.MainPane;

public class MainApplication extends Application {
		private final static String DEFAULT_TIERLIST_NAME = TierList.DEFAULT_TIERLIST_NAME;
	
	@Override
	public void start(@SuppressWarnings("exports") Stage stage) throws Exception {
		
		TierListController controller = new StandardTierListController();
		stage.setTitle(DEFAULT_TIERLIST_NAME);
		
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
