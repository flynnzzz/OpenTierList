package ui.gui.manual.panels;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @version 1.11
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
	
	public MainPane(TierListController controller) {
		this.controller = controller;
		this.initPane();
	}
	
	public void initPane() {
		this.titleLabel = new TextField("Temp");

		titleLabel.setFocusTraversable(false);
		titleLabel.focusedProperty().addListener((observed, was, now) -> {
			if (titleLabel.getText().isBlank()) // alert
			if (!was)
					controller.setTierListName(titleLabel.getText());
		});
		titleLabel.setOnAction( event -> {
			titleLabel.getScene().getRoot().requestFocus();
		});
		
		this.unrankedPane = new UnrankedPane(controller);
		this.tiersPane = new TierPanels(controller);

		this.addTierButton = new Button("Add Tier");
		this.addElementButton = new Button("Add Element");

		this.buttons = new VBox();
		buttons.getChildren().addAll(addTierButton, addElementButton);
		
		setTop(titleLabel);
		titleLabel.setPadding(new Insets(30, 0, 10, 40));
		
		setBottom(unrankedPane);
		setCenter(tiersPane);

		setRight(buttons);
		
		addTierButton.setPadding(new Insets(5));
		addElementButton.setPadding(new Insets(5));
		addTierButton.setAlignment(Pos.CENTER);	
		addElementButton.setAlignment(Pos.CENTER);	
		buttons.setPadding(new Insets(15));
		buttons.setSpacing(15);
	}
}
