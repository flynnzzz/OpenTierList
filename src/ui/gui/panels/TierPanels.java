package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.models.Tier;

public class TierPanels extends ScrollPane {
	private List<TierPane> tierPaneList;
	private VBox tierPanels;
	
	public TierPanels(List<Tier> tiers) {
		this.tierPaneList = new ArrayList<>();
		this.tierPanels = new VBox();
		this.initAllTiers(tiers);
		
		this.initPane();
	}
	
	private void initAllTiers(List<Tier> tiers) {
		for (int i = 0; i < tiers.size(); i++) {
			var tierPane = new TierPane(tiers.get(i));
			tierPaneList.add(tierPane);
		}
	}
	
	private void initPane() {
		//----- init vbox -----//
		tierPanels.getChildren().addAll(tierPaneList);
		
		this.setContent(tierPanels);
	}
}
