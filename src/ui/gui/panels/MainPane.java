package ui.gui.panels;

import controller.controllers.TierListController;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {
	private Label titleLabel;
	private UnrankedPane unrankedPane;
	private TierPanels tiersPane;
	private TierListController controller;
	
	public MainPane(TierListController controller) {
		this.controller = controller;
		this.initPane();
	}
	
	public void initPane() {
		this.titleLabel = new Label("Temp");
		this.unrankedPane = new UnrankedPane(controller);
		this.tiersPane = new TierPanels(controller);
		
		setTop(titleLabel);
		setBottom(unrankedPane);
		setCenter(tiersPane);
	}
}
