package ui.gui.panels;

import controller.controllers.TierListController;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {
	private UnrankedPane unrankedPane;
	private TierPanels tiersPane;
	private TierListController controller;
	
	public MainPane(TierListController controller) {
		this.controller = controller;
		this.initPane();
	}
	
	public void initPane() {
		
		this.unrankedPane = new UnrankedPane(controller);
		this.tiersPane = new TierPanels(controller.getTiers());
		
		setBottom(unrankedPane);
		setCenter(tiersPane);
	}
}
