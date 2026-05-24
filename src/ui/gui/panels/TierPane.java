package ui.gui.panels;

import controller.controllers.TierListController;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.models.Tier;
import ui.gui.settings.UISettings;

public class TierPane extends HBox {
	private TextField tierNameLabel;
	private TierElementsPane elementsPane;
	
	public TierPane(TierListController controller, Tier tier) {
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.elementsPane = new TierElementsPane(controller, tier);
		
		this.initPane();
	}
	
	private void initPane() {
		this.getChildren().addAll(tierNameLabel, elementsPane);
		
		//----- settings -----//
		this.tierNameLabel.setEditable(true);
		tierNameLabel.setPrefSize(UISettings.DEFAULT_CELL_SIZE, UISettings.DEFAULT_CELL_SIZE);
	}
}
