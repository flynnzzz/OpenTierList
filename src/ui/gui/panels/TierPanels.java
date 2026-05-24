package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class TierPanels extends ScrollPane {
	private VBox tierPanels;

	private TierListController controller;
	private List<TierPane> tierPaneList;
	
	public TierPanels(TierListController controller) {
		this.controller = controller;
		this.tierPaneList = new ArrayList<>();
		this.tierPanels = new VBox();
		this.initAllTiers();
		
		this.initPane();
	}
	
	private void initAllTiers() {
		var tiers = controller.getTiers();
		for (int i = 0; i < tiers.size(); i++) {
			var tierPane = new TierPane(controller, tiers.get(i));
			tierPaneList.add(tierPane);
		}
	}
	
	private void initPane() {
		//----- init vbox -----//
		tierPanels.getChildren().addAll(tierPaneList);
		
		this.setContent(tierPanels);
	}
}
