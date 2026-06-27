package ui.gui.manual.panels;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 1.90
 * @since v1.2.5
 */
public class MainPane extends BorderPane {

	// ----- panels -----//
	private TextField titleLabel;
	private UnrankedScrollPane unrankedPane;
	private TiersScrollPane tiersPane;
	private VBox buttons;
	private Button addTierButton, addElementButton;
	// TODO: file selector

	private TierListController controller;
	private Stage stage;
	private String oldTitle;
	
	public MainPane(TierListController controller, Stage stage) {
		this.controller = controller;
		this.stage = stage;
		this.initPane();
	}
	
	public void initPane() {

		// ----- title -----//
		titleLabel = new TextField(controller.getTierListName());
		titleLabel.setFocusTraversable(false);
		titleLabel.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
		titleLabel.setAlignment(Pos.CENTER);

		HBox titleBox = new HBox(titleLabel);
		setTop(titleBox);
		titleBox.setPadding(new Insets(
				UISettings.DEFAULT_TITLE_PADDING_TOP,
				UISettings.DEFAULT_TITLE_PADDING_RIGHT,
				UISettings.DEFAULT_TITLE_PADDING_BOTTOM,
				UISettings.DEFAULT_TITLE_PADDING_LEFT));
		titleBox.setAlignment(Pos.CENTER);

		// ----- unranked -----//
		unrankedPane = new UnrankedScrollPane(this, controller);
		HBox unrankedBox = new HBox(unrankedPane);
		unrankedBox.setAlignment(Pos.CENTER);

		setBottom(unrankedBox);

		// ----- buttons -----//
		addTierButton = new Button("Add Tier");
		addElementButton = new Button("Add Element");
		
		buttons = new VBox();
		buttons.getChildren().addAll(addTierButton, addElementButton);
		addTierButton.setPadding(new Insets(UISettings.DEFAULT_RBUTTON_PADDING / 3));
		addElementButton.setPadding(new Insets(UISettings.DEFAULT_RBUTTON_PADDING / 3));

		addTierButton.setAlignment(Pos.CENTER);	
		addElementButton.setAlignment(Pos.CENTER);	
		buttons.setPadding(new Insets(UISettings.DEFAULT_RBUTTON_PADDING));
		buttons.setSpacing(UISettings.DEFAULT_RBUTTON_PADDING);

		setRight(buttons);

		// ----- tiers -----//
		tiersPane = new TiersScrollPane(this, controller);
		HBox tiersBox = new HBox(tiersPane);
		tiersBox.setAlignment(Pos.CENTER); 
		tiersBox.setPadding(new Insets(0, 0, 0, 0));
		setCenter(tiersBox);

		HBox leftSpacer = new HBox();
		leftSpacer.setPrefWidth(buttons.getWidth() + UISettings.DEFAULT_RBUTTON_PADDING * 8); // I'm never doing UI manualy again
		HBox.setHgrow(leftSpacer, Priority.NEVER);
		setLeft(leftSpacer);

		setupEventHandlers();
	}

	private void setupEventHandlers() {
		titleLabel.focusedProperty().addListener((_, _, now) -> {
			if (now)
				this.oldTitle = titleLabel.getText();
			else
				titleLabel.setText(oldTitle);
		});

		titleLabel.setOnAction( _ -> {
			if (!titleLabel.getText().isBlank()) {
				controller.setTierListName(titleLabel.getText());
				this.oldTitle = titleLabel.getText();
			}
			titleLabel.getScene().getRoot().requestFocus();
		});
		
		addTierButton.setOnAction( _ -> {
			controller.addTier();
			tiersPane.updatePane();
		});
	}
	
	public void updateAll() {
		tiersPane.updatePane();
		unrankedPane.updatePane();
	}
	
	public Stage getStage() {
		return this.stage;
	}
}
