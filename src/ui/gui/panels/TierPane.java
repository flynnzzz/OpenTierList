package ui.gui.panels;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.models.Tier;

public class TierPane extends HBox {
	private TextField tierNameLabel;
	private TierElementsPane elementsPane;
	
	public TierPane(Tier tier) {
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.elementsPane = new TierElementsPane(tier);
		
		this.initPane();
	}
	
	private void initPane() {
		this.getChildren().addAll(tierNameLabel, elementsPane);
		this.tierNameLabel.setEditable(isPressed());
	}
}
