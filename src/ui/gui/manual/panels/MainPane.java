package ui.gui.manual.panels;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 
 * @version 1.90
 * @since v1.2.5
 */
public class MainPane extends BorderPane {

	// ----- panels -----//
	private TextField titleLabel;
	private UnrankedPane unrankedPane;
	private TierPanels tiersPane;
	private VBox buttons;
	private Button addTierButton, addElementButton;

	private TierListController controller;
	private String oldTitle;
	
	public MainPane(TierListController controller) {
		this.controller = controller;
		this.initPane();
	}
	
	public void initPane() {

		// ----- title -----//
		titleLabel = new TextField("Temp");
		titleLabel.setFocusTraversable(false);
		HBox titleBox = new HBox(titleLabel);
		setTop(titleBox);
		titleBox.setPadding(new Insets(30, 0, 10, 40));
		titleBox.setAlignment(Pos.CENTER);

		// ----- unranked -----//
		unrankedPane = new UnrankedPane(controller);
		HBox unrankedBox = new HBox(unrankedPane);
		unrankedBox.setAlignment(Pos.CENTER);

		setBottom(unrankedBox);

		// ----- buttons -----//
		addTierButton = new Button("Add Tier");
		addElementButton = new Button("Add Element");

		buttons = new VBox();
		buttons.getChildren().addAll(addTierButton, addElementButton);
		addTierButton.setPadding(new Insets(5));
		addElementButton.setPadding(new Insets(5));

		addTierButton.setAlignment(Pos.CENTER);	
		addElementButton.setAlignment(Pos.CENTER);	
		buttons.setPadding(new Insets(15));
		buttons.setSpacing(15);

		setRight(buttons);

		// ----- tiers -----//
		tiersPane = new TierPanels(controller);
		HBox tiersBox = new HBox(tiersPane);
		tiersBox.setAlignment(Pos.CENTER); 
		tiersBox.setPadding(new Insets(0, 0, 0, buttons.getWidth() + 50));
		setCenter(tiersBox);


		VBox emptyBox = new VBox();
		emptyBox.setPadding(new Insets(15));
		setLeft(emptyBox);

		setupEventHandlers();
	}

	private void setupEventHandlers() {
		titleLabel.focusedProperty().addListener((observed, was, now) -> {
			if (now)
				this.oldTitle = titleLabel.getText();
			else
				titleLabel.setText(oldTitle);
		});

		titleLabel.setOnAction( event -> {
			if (!titleLabel.getText().isBlank()) {
				controller.setTierListName(titleLabel.getText());
				this.oldTitle = titleLabel.getText();
			}
			titleLabel.getScene().getRoot().requestFocus();
		});
	}
}
