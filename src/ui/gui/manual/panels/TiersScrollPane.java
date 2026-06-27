package ui.gui.manual.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import model.models.Tier;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class TiersScrollPane extends ScrollPane {
	//----- panels -----//
	private VBox tierPanels;
	private List<TierHBox> tierPaneList;
	
	private MainPane parent;

	private TierListController controller;
	
	public TiersScrollPane(MainPane parent, TierListController controller) {
		this.parent = parent;
		this.controller = controller;
		this.tierPaneList = new ArrayList<>();
		this.tierPanels = new VBox();
		this.initAllTiers();
		
		this.setupPane();
	}
	
	private void initAllTiers() {
		List<Tier> tiers = controller.getTiers();
		
		tiers.forEach( tier -> {
			var tierPane = new TierHBox(this, parent.getStage(), controller, tier);

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
		this.setFitToWidth(true);
		tierPanels.setAlignment(Pos.CENTER);
	}

	public void updatePane() {
		tierPaneList.clear();
		tierPanels = new VBox();
		this.initAllTiers();
		this.setupPane();
	}
	
	public void updateAll() {
		parent.updateAll();
	}
}


