package ui.gui.manual.panels;

import java.util.Optional;
import java.util.function.BiConsumer;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
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
import javafx.scene.paint.Paint;
import model.enums.TierElementStatus;
import model.models.Tier;
import model.models.TierElement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 2.90
 * @since v1.2.5
 */
public class TierPane extends HBox {
	
	//----- panels -----//
	private TextField tierNameLabel;
	private TierElementsPane elementsPane;
	private Button editTierButton;

	private TierListController controller;
	private Tier tier;
	private BiConsumer<TierElement, TierElement> onDragDropped;
	private String oldTextValue;
	
	public TierPane(TierListController controller, Tier tier) {	
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.controller = controller;
		this.tier = tier;
		this.editTierButton = new Button("Edit");
		// TODO: add image?

		//----- TierElementsPane's on 'drag dropped' behaviour -----//
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
	
	private void setupPane() {
		this.getChildren().addAll(tierNameLabel, elementsPane, editTierButton);
		
		//----- settings -----//
		{
			this.tierNameLabel.setEditable(true);

			tierNameLabel.setFocusTraversable(false);
			tierNameLabel.setPrefSize(UISettings.DEFAULT_CELL_SIZE, UISettings.DEFAULT_CELL_SIZE);
			
			this.editTierButton.setAlignment(Pos.CENTER_RIGHT);	
			editTierButton.setFocusTraversable(false);
			
			this.setTierNameLabelBackground();
			this.setTierNameLabelBorder(UISettings.DEFAULT_BAR_BORDER_COLOR);
			
			this.setSpacing(UISettings.DEFAULT_TIER_SPACING);
			this.setPadding(new Insets(
					UISettings.DEFAULT_TIER_PADDING_TOP,
					UISettings.DEFAULT_TIER_PADDING_RIGHT,
					UISettings.DEFAULT_TIER_PADDING_BOTTOM,
					UISettings.DEFAULT_TIER_PADDING_LEFT)
			);
		}
		setupDragAndDrop();
	}

	private void setupDragAndDrop() { 
		this.tierNameLabel.setOnDragDetected(this::handleDragDetected); 
		this.tierNameLabel.setOnDragOver(event -> {
			if (event.getDragboard().hasString() && !event.getDragboard().hasImage())
				event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});

		this.tierNameLabel.setOnDragEntered(event -> {
			if (event.getTarget() instanceof TextField target && event.getGestureSource() != target
					&& event.getDragboard().hasString() && !event.getDragboard().hasImage())
				this.setTierNameLabelBorder(UISettings.DEFAULT_BAR_HIGHLIGHT_COLOR);
			event.consume();
		});

		this.tierNameLabel.setOnDragExited(event -> {
			if (event.getTarget() instanceof TextField target && event.getGestureSource() != target
					&& event.getDragboard().hasString() && !event.getDragboard().hasImage())
				this.setTierNameLabelBorder(UISettings.DEFAULT_BAR_BORDER_COLOR);
			event.consume();
		});

		this.tierNameLabel.setOnDragDropped(this::handleDragDropped);

		this.tierNameLabel.focusedProperty().addListener( (observed, was, now) -> {
			if (now)
				oldTextValue = tierNameLabel.getText();
			else
				tierNameLabel.setText(oldTextValue);
		});

		this.tierNameLabel.setOnAction( event -> {
			controller.setTierName(tier, tierNameLabel.getText());
			oldTextValue = tierNameLabel.getText();
			tierNameLabel.getScene().getRoot().requestFocus();
		});
	}
	
	private void handleDragDetected(MouseEvent event) {

		// ----- define transfer mode -----//
		Dragboard dragBoard = ((TextField) event.getSource()).startDragAndDrop(TransferMode.MOVE);

		// ----- put Text on dragboard -----//
		var content = new ClipboardContent();
		String sourceId = Integer.valueOf(tier.hashCode()).toString();

		content.putString(sourceId.toString());
		dragBoard.setContent(content);
		event.consume();
	}

	private void handleDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasString() && !event.getDragboard().hasImage()) {
			Tier source = controller.getTierByHash(db.getString());
			Node potentialTarget = (Node) event.getTarget();
		
			while (potentialTarget != null && !(potentialTarget instanceof TierPane)) {
				potentialTarget = potentialTarget.getParent();
			}

			if (potentialTarget instanceof TierPane targetPane) {
				Tier target = (Tier) targetPane.getTier();
				controller.moveTierTo(source, target);
			}
		}
		event.setDropCompleted(true);
		event.consume();
	}

	private void setTierNameLabelBorder(String color) {
		var tierNameLabelBorder = new Border(
				new BorderStroke(
						Paint.valueOf(color),
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY,
						BorderWidths.DEFAULT));
		tierNameLabel.setBorder(tierNameLabelBorder);
	}

	private void setTierNameLabelBackground() {
		var backgroundColor = Paint.valueOf(this.tier.getHeader().color().toString());
		var nameLabelBackground = Background.fill(backgroundColor);
		tierNameLabel.setBackground(nameLabelBackground);
	}

	public Tier getTier() {	return this.tier; }

	public TextField getTextField() {
		return this.tierNameLabel;
	}
}
