package ui.gui.manual.panels;

import java.util.Optional;
import java.util.function.BiConsumer;

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
import model.models.TierElement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 1.40
 * @since v1.2.5
 */
public class TierPane extends HBox {
	
	//----- panels -----//
	private TextField tierNameLabel;
	private TierElementsPane elementsPane;

	private Color tierNameColor;
	private BiConsumer<TierElement, TierElement> onDragDropped;
	
	public TierPane(TierListController controller, Tier tier) {
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.tierNameColor = tier.getHeader().color();

		this.onDragDropped = (s, t) -> {
			controller.moveTo(s, controller.getTierByElement(t), t);
		};
		
		this.elementsPane = new TierElementsPane(controller, tier.getElements(), Optional.of(tier), onDragDropped);
		
		this.setupPane();
	}
	
	private void setupPane() {
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
		
		this.setSpacing(UISettings.DEFAULT_TIER_SPACING);
		this.setPadding(new Insets(
				UISettings.DEFAULT_TIER_PADDING_TOP,
				UISettings.DEFAULT_TIER_PADDING_RIGHT,
				UISettings.DEFAULT_TIER_PADDING_BOTTOM,
				UISettings.DEFAULT_TIER_PADDING_LEFT)
			);
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
		var tierNameLabelBorder = new Border(
				new BorderStroke(
						Paint.valueOf(UISettings.DEFAULT_BAR_BORDER_COLOR), 
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
