package ui.gui.manual.panels;

import java.util.Optional;
import java.util.function.BiConsumer;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import model.enums.TelementStatus;
import model.models.Tier;
import model.models.Telement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 3.00
 * @since v1.2.5
 */
public class TierHBox extends HBox {
	
	//----- panels -----//
	private TextField tierNameLabel;
	private TelementsFlowPane elementsPane;
	private Button editTierButton;
	private TiersScrollPane parent;
	
	private TierListController controller;
	private Tier tier;
	private BiConsumer<Telement, Telement> onDragDropped;
	
	private String oldTextValue;
	
	public TierHBox(TiersScrollPane parent, TierListController controller, Tier tier) {	
		this.parent = parent;
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.controller = controller;
		this.tier = tier;
		this.editTierButton = new Button("Edit");
		// TODO: add image?

		//----- TierElementsPane's on 'drag dropped' behaviour -----//
		this.onDragDropped = (element, t) -> {
			switch (element.status()) {
			case TelementStatus.RANKED: {
				Optional<Tier> targetTier = controller.getTierByElement(t);
				if (targetTier.isPresent())
					controller.moveTo(element, targetTier.get(), t); break;
			}
			case TelementStatus.UNRANKED: {
				Optional<Tier> targetTier = controller.getTierByElement(t); 
				if (targetTier.isPresent())
					controller.rank(targetTier.get().getElements().indexOf(t), element, targetTier.get()); break;
			}
			default: break;
			}
		};
		
		this.elementsPane = new TelementsFlowPane(controller, tier.getElements(), Optional.of(tier), onDragDropped);
		
		this.setupPane();
	}
	
	private void setupPane() {
		this.getChildren().addAll(tierNameLabel, elementsPane, editTierButton);
		
		//----- settings -----//
		{
			this.tierNameLabel.setEditable(true);
			tierNameLabel.setFocusTraversable(false);
			tierNameLabel.setAlignment(Pos.CENTER);
			tierNameLabel.setPrefSize(UISettings.DEFAULT_CELL_SIZE, UISettings.DEFAULT_CELL_SIZE);
			
			this.editTierButton.setAlignment(Pos.CENTER);	
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

		setupEventHandlers();
	}

	private void setupEventHandlers() {

		setupDragAndDrop();
		
		this.tierNameLabel.focusedProperty().addListener( (_, _, now) -> {
			if (now)
				oldTextValue = tierNameLabel.getText();
			else
				tierNameLabel.setText(oldTextValue);
		});

		this.tierNameLabel.setOnAction( _ -> {
			if (!tierNameLabel.getText().isBlank()) {
				controller.setTierName(tier, tierNameLabel.getText());
				oldTextValue = tierNameLabel.getText();
			}
			tierNameLabel.getScene().getRoot().requestFocus();
		});
		
		tierNameLabel.setTooltip(new Tooltip("Click to drag and move"));
		
		//----- edit button -----//
		editTierButton.setOnAction(_ -> {
		    ContextMenu contextMenu = new ContextMenu();

		    MenuItem delete = new MenuItem("Delete");
		    MenuItem duplicate = new MenuItem("Duplicate");
		    
		    delete.setOnAction( _ -> {
		    	tier.getElements().forEach(controller::unrank);
		    	
		    	controller.removeTier(tier);
		    	parent.updateAll();
		    });
		    
		    duplicate.setOnAction(_ -> {
		    	Tier clone = tier.copy();
		    	controller.addTier(clone);
		    	controller.moveTierTo(clone, controller.getTiers().indexOf(tier) + 1);
		    	
		    	parent.updateAll();
		    });
		    
		    contextMenu.getItems().addAll(delete, duplicate);
		    contextMenu.show(editTierButton, Side.BOTTOM, 0, 0);
		});
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
	}
	
	private void handleDragDetected(MouseEvent event) {

		//----- define transfer mode -----//
		Dragboard dragBoard = ((TextField) event.getSource()).startDragAndDrop(TransferMode.MOVE);

		//----- put Text on dragboard -----//
		var content = new ClipboardContent();
		String sourceId = Integer.valueOf(tier.hashCode()).toString();

		content.putString(sourceId.toString());
		dragBoard.setContent(content);
		event.consume();
	}

	private void handleDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasString() && !event.getDragboard().hasImage()) {
			
			Optional<Tier> source = controller.getTierByHash(db.getString());
			
			if (source.isEmpty()) return;
			
			Node potentialTarget = (Node) event.getTarget();
		
			while (potentialTarget != null && !(potentialTarget instanceof TierHBox)) {
				potentialTarget = potentialTarget.getParent();
			}

			if (potentialTarget instanceof TierHBox targetPane) {
				Tier target = (Tier) targetPane.getTier();
				controller.moveTierTo(source.get(), target);
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

	private Tier getTier() { return this.tier; }

	public TextField getTextField() {
		return this.tierNameLabel;
	}
}
