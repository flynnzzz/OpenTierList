package ui.gui.panels;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.models.Tier;
import ui.gui.settings.UISettings;

/**
 * 
 * @version 1.40
 * @since v1.2.5
 */
public class TierPane extends HBox {
	private TextField tierNameLabel;
	private Color tierNameColor;
	private TierElementsPane elementsPane;
	
	public TierPane(TierListController controller, Tier tier) {
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.elementsPane = new TierElementsPane(controller, tier);
		this.tierNameColor = tier.getHeader().color();
		
		this.initPane();
	}
	
	private void initPane() {
		this.getChildren().addAll(tierNameLabel, elementsPane);
		
		//----- settings -----//
		{
			this.tierNameLabel.setEditable(true);
			tierNameLabel.setPrefSize(UISettings.DEFAULT_CELL_SIZE, UISettings.DEFAULT_CELL_SIZE);
			
			this.setTierNameLabelBackground();
			this.setTierNameLabelBorder();
			
			//----- drag and drop -----//
			this.setOnDragDetected(this::handleDragDetected);
			this.setOnDragDropped(this::handleDragDropped);
			this.setOnDragDone(this::handleDragDone);
		}
		
		//TODO: organize later
		this.setSpacing(10);
		this.setPadding(new Insets(10,10,10,30));
	}
	
	private void handleDragDetected(MouseEvent event) {
		this.startDragAndDrop(TransferMode.NONE);
		event.consume();
	}
	
	private void handleDragDropped(DragEvent event) {
		event.setDropCompleted(true);
		event.consume();
	}
	private void handleDragDone(DragEvent event) {
		event.consume();
	}
	
	private void setTierNameLabelBorder() {
		var borderColor = Paint.valueOf(Color.DIMGRAY.toString());
		var tierNameLabelBorder = new Border(
				new BorderStroke(
						borderColor, 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
					)
				);
		tierNameLabel.setBorder(tierNameLabelBorder);
	}
	
	private void setTierNameLabelBackground() {
		var backgroundColor = Paint.valueOf(this.tierNameColor.toString());
		var nameLabelBackground = Background.fill(backgroundColor);
		tierNameLabel.setBackground(nameLabelBackground);
	
	}
}
