package ui.gui.manual.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import model.models.Tier;

/**
 * 
 * @version 2.10
 * @since v1.2.5
 */
public class TierPanels extends ScrollPane {
	//----- panels -----//
	private VBox tierPanels;
	private List<TierPane> tierPaneList;

	private TierListController controller;
	
	public TierPanels(TierListController controller) {
		this.controller = controller;
		this.tierPaneList = new ArrayList<>();
		this.tierPanels = new VBox();
		this.initAllTiers();
		
		this.setupPane();
	}
	
	private void initAllTiers() {
		List<Tier> tiers = controller.getTiers();
		
		tiers.forEach( tier -> {
			var tierPane = new TierPane(controller, tier);

			tierPane.getTextField().setOnDragDone(event -> {
				if (event.getTransferMode() == TransferMode.MOVE)
					updatePane();
				event.consume();
			});

			tierPaneList.add(tierPane); 
		});
	}
	
	private void setupPane() {
		//----- init vbox -----//
		tierPanels.getChildren().addAll(tierPaneList);
		this.setContent(tierPanels);
	}

	public void updatePane() {
		tierPaneList = new ArrayList<>();
		tierPanels = new VBox();
		this.initAllTiers();
		this.setupPane();
	}
}


