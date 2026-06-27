package ui.gui.manual.panels;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.BiConsumer;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.enums.TelementStatus;
import model.models.Tier;
import persistence.ResourceHolder;
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
	
	//----- edit menu -----//
    ContextMenu contextMenu;
    MenuItem delete;
    MenuItem duplicate;
    MenuItem color;
	
	//----- color picker -----//
    Stage colorStage;
    BorderPane colorPane;
    Scene colorMenu;
    ColorPicker colorPicker;
    Button confirmColor;
    
	private TierListController controller;
	private Stage mainStage;
	private Tier tier;
	private BiConsumer<Telement, Telement> onDragDropped;
	
	private String oldTextValue;
	
	public TierHBox(TiersScrollPane parent, Stage mainStage, TierListController controller, Tier tier) {	
		this.parent = parent;
		this.tierNameLabel = new TextField(tier.getHeader().name());
		this.controller = controller;
		this.mainStage = mainStage;
		this.tier = tier;
		this.editTierButton = new Button();
		
		// TODO: sort later
		Image img;
		try {
			img = new Image(getClass().getResource(ResourceHolder.getEditButtonIcon()).toURI().toString()); 
			editTierButton.setGraphic(new ImageView(img));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

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
		
		setupColorPicker();

		setupEditMenu();
		
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

		Tooltip tooltip = new Tooltip("Click to drag and move");
		tierNameLabel.setTooltip(tooltip);
		
		//----- edit button -----//
		editTierButton.setOnAction(_ -> {
			contextMenu.show(editTierButton, Side.BOTTOM, 0, 0);
		});
		
		delete.setOnAction( _ -> {
	    	tier.getElements().forEach(controller::unrank);
	    	
	    	controller.removeTier(tier);
	    	parent.updateAll();
	    });
	    
	    duplicate.setOnAction(_ -> {
	    	var clone = tier.copy();
	    	controller.addTier(clone);
	    	controller.moveTierTo(clone, controller.getTiers().indexOf(tier) + 1);
	    	
	    	parent.updateAll();
	    });

	    color.setOnAction(_ -> {
	    	colorStage.show();
	        mainStage.setOnCloseRequest(_ -> {
		    	if (colorStage.isShowing()) {
		    		colorStage.close(); 
		    	}
		    });
	    });
	    
		confirmColor.setOnAction(_ -> {
			var chosenColor = Optional.of(colorPicker.getValue());

    		if (chosenColor.isEmpty()) return;
    		
			tier.setColor(chosenColor.get());
			parent.updateAll();
			colorStage.close();	
		});
	}
	
	private void setupColorPicker() {
	    colorStage = new Stage();

	    colorPane = new BorderPane();
	    
	    colorMenu = new Scene(colorPane, 200, 150);
	    colorPicker = new ColorPicker();
    	colorPicker.setPadding(new Insets(5,20,5,20));
	    VBox colorBox = new VBox();
	    Label colorLabel = new Label("Pick a color");
	    confirmColor = new Button("Ok");
	    confirmColor.setAlignment(Pos.BASELINE_RIGHT);
	    
	    colorBox.getChildren().addAll(colorLabel, colorPicker, confirmColor);
	    colorBox.setAlignment(Pos.CENTER);
	    colorBox.setSpacing(15);
    	
	    colorPane.setCenter(colorBox);
	    colorStage.setTitle("Color picker");
    	colorStage.setScene(colorMenu);
    	colorStage.initModality(Modality.WINDOW_MODAL);
    	colorStage.initStyle(StageStyle.UTILITY);
    	colorStage.setAlwaysOnTop(true);
	}
	
	private void setupEditMenu() {
	    contextMenu = new ContextMenu();
	    delete = new MenuItem("Delete");
	    duplicate = new MenuItem("Duplicate");
	    color = new MenuItem("Color");
	    contextMenu.getItems().addAll(delete, duplicate, color);
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
