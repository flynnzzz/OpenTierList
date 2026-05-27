package ui.gui.manual.panels;

import java.util.Optional;
import java.util.function.BiConsumer;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
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
import model.enums.TierElementStatus;
import model.models.Tier;
import model.models.TierElement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 2.00
 * @since v1.2.5
 */
public class TierPane extends HBox {
	
	//----- panels -----//
	private TextField tierNameLabel;
	private TierElementsPane elementsPane;

	@SuppressWarnings("unused")
	private TierListController controller;
	private Tier tier;
	private Color tierNameColor;
	private BiConsumer<TierElement, TierElement> onDragDropped;
	
	public TierPane(TierListController controller, Tier tier) {
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.tierNameColor = tier.getHeader().color();
		this.controller = controller;
		this.tier = tier;

		//----- TierElementsPane on 'drag dropped' behaviour -----//
		this.onDragDropped = (s, t) -> {
			switch (s.status()) {
			case TierElementStatus.RANKED:
				controller.moveTo(s, controller.getTierByElement(t), t); break;
			case TierElementStatus.UNRANKED: {
				Tier targetTier = controller.getTierByElement(t); 
				controller.rank(targetTier.getElements().indexOf(t), s, targetTier); break;
			}
			default: break;
			}
		};
		
		this.elementsPane = new TierElementsPane(controller, tier.getElements(), Optional.of(tier), onDragDropped);
		
		this.setupPane();
	}
	
	public Tier getTier() {	return this.tier; }
	
	public boolean contains(ImageView node) {
		for (Node n : elementsPane.getChildren()) {
			if (node.equals(n)) return true;
		}
		return false;
	}
 	
	private void setupPane() {
		this.getChildren().addAll(tierNameLabel, elementsPane);
		
		//----- settings -----//
		{
			this.tierNameLabel.setEditable(true);
			tierNameLabel.setPrefSize(UISettings.DEFAULT_CELL_SIZE, UISettings.DEFAULT_CELL_SIZE);
			
			this.setTierNameLabelBackground();
			this.setTierNameLabelBorder(UISettings.DEFAULT_BAR_BORDER_COLOR);
			
			//----- drag and drop -----//
			this.setOnDragDetected(this::handleDragDetected);
			this.setOnDragOver(this::handleDragOver);
			this.setOnDragEntered(this::handleDragEntered);
			this.setOnDragExited(this::handleDragExited);
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
		
		//Dragboard dragBoard = ((TierPane) event.getSource()).startDragAndDrop(TransferMode.MOVE);
		// TODO: ...
		
		event.consume();
	}
	
	private void handleDragOver(DragEvent event) {
		
		if (event.getGestureSource() != event.getSource() && event.getDragboard().hasImage())
			event.acceptTransferModes(TransferMode.MOVE);
		
		event.consume();
	}
	
	private void handleDragEntered(DragEvent event) {
		
		var sourceData = event.getGestureSource();
		//TierPane target = (TierPane) event.getTarget();
		
		if (sourceData instanceof ImageView)
			if (event.getDragboard().hasImage())
				this.setTierElementsPaneBorder(Color.DEEPSKYBLUE.toString());
               
		event.consume();
	}
	
	private void handleDragExited(DragEvent event) {
		
		this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_BORDER_COLOR);
		
		event.consume();
	}
	
	private void handleDragDropped(DragEvent event) {
		
		Dragboard dragBoard = event.getDragboard();
		
		boolean success = false;
		
		if (dragBoard.hasImage() && dragBoard.hasString()) {
			// TODO: ...
		}
		
		event.setDropCompleted(success);
		event.consume();
	}
	private void handleDragDone(DragEvent event) {
		
		if (event.getTransferMode() == TransferMode.MOVE) {
			// TODO: ...
		}
		event.consume();
	}
	
	private void setTierNameLabelBorder(String color) {
		var tierNameLabelBorder = new Border(
				new BorderStroke(
						Paint.valueOf(color), 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
					)
				);
		tierNameLabel.setBorder(tierNameLabelBorder);
	}
	
	private void setTierElementsPaneBorder(String color) {
		var tierElementsPaneBorder = new Border(
				new BorderStroke(
						Paint.valueOf(color), 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
						)
				);
		this.elementsPane.setBorder(tierElementsPaneBorder);
	}
	
	private void setTierNameLabelBackground() {
		var backgroundColor = Paint.valueOf(this.tierNameColor.toString());
		var nameLabelBackground = Background.fill(backgroundColor);
		tierNameLabel.setBackground(nameLabelBackground);
	}
}
