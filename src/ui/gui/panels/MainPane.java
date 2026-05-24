package ui.gui.panels;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * 
 * @version 1.11
 * @since v1.2.5
 */
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
		{
			titleLabel.setPadding(new Insets(30,0,10,40));
		}
		
		setBottom(unrankedPane);
		setCenter(tiersPane);
	}
}
