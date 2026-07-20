package net.flynn.opentierlist.ui.manual;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.models.ImagePath;
import net.flynn.opentierlist.model.models.Telement;
import net.flynn.opentierlist.persistence.ResourceHolder;

/**
 * 
 * @version 2.00
 * @since v1.2.5
 */
public class MainPane extends BorderPane {
	// ----- panels -----//
	private TextField titleLabel;
	private UnrankedScrollPane unrankedPane;
	private TiersScrollPane tiersPane;
	private Button addTierButton, addElementButton;
	private FileChooser fileChooser;

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
		
		// ----- file chooser -----//
		fileChooser = new FileChooser();
		fileChooser.setTitle("Select file");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
		
		HBox titleBox = new HBox(titleLabel);
		setTop(titleBox);
		titleBox.setPadding(new Insets(
				UISettings.DEFAULT_TITLE_PADDING_TOP,
				UISettings.DEFAULT_TITLE_PADDING_RIGHT,
				UISettings.DEFAULT_TITLE_PADDING_BOTTOM,
				UISettings.DEFAULT_TITLE_PADDING_LEFT));
		titleBox.setAlignment(Pos.BASELINE_CENTER);

		// ----- unranked -----//
		unrankedPane = new UnrankedScrollPane(this, controller);
		HBox unrankedBox = new HBox(unrankedPane);
		unrankedBox.setAlignment(Pos.BASELINE_CENTER);
		setBottom(unrankedBox);
		
		// ----- buttons -----//
		addTierButton = new Button();
		addElementButton = new Button();
		
		addTierButton.setTooltip(new Tooltip("Add new Tier"));
		addElementButton.setTooltip(new Tooltip("Add new Element"));
		
		addTierButton.setFocusTraversable(false);
		addElementButton.setFocusTraversable(false);

		try {
			var imageURI = getClass().getResource(ResourceHolder.getAddtierbuttonicon());
			if (imageURI == null) 
				throw new URISyntaxException("imageURI", "--- addtier button resource not found, exiting ---");
			addTierButton.setGraphic(new ImageView(new Image(imageURI.toURI().toString())));

			imageURI = getClass().getResource(ResourceHolder.getAddelementbuttonicon());
			if (imageURI == null) 
				throw new URISyntaxException("imageURI", "--- addelement button resource not found, exiting ---");
			addElementButton.setGraphic(new ImageView(new Image(imageURI.toURI().toString())));
		} catch (URISyntaxException e) {
			System.err.println(e.getReason());
			System.exit(-1);
		}
		
		HBox buttonsHBox = new HBox();
		buttonsHBox.getChildren().addAll(addTierButton, addElementButton);

		buttonsHBox.setPadding(new Insets(UISettings.DEFAULT_DBUTTON_PADDING,
				UISettings.DEFAULT_DBUTTON_PADDING,
				UISettings.DEFAULT_DBUTTON_PADDING,
				UISettings.DEFAULT_DBUTTON_PADDING));
		
		buttonsHBox.setSpacing(UISettings.DEFAULT_DBUTTON_PADDING / 3);
		buttonsHBox.setAlignment(Pos.BOTTOM_CENTER);

		// ----- tiers -----//
		tiersPane = new TiersScrollPane(this, controller);
		HBox tiersBox = new HBox(tiersPane);
		setCenter(tiersBox);

		var centerBox = new VBox(tiersPane, buttonsHBox);
		centerBox.setAlignment(Pos.CENTER);
		setCenter(centerBox);

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
				stage.setTitle(titleLabel.getText());
				this.oldTitle = titleLabel.getText();
			}
			titleLabel.getScene().getRoot().requestFocus();
		});
		
		addTierButton.setOnAction( _ -> {
			controller.addTier();
			tiersPane.updatePane();
		});
		
		addElementButton.setOnAction( _ -> {
			List<File> files = fileChooser.showOpenMultipleDialog(stage);
			files.forEach( selectedFile -> {
				if (selectedFile != null && selectedFile.exists()) {
					controller.addToUnranked(new Telement(selectedFile.getName(), ImagePath.of(selectedFile)));
				}
			});
			updateAll();
		});
	}
	
	public void updateAll() {
		tiersPane.updatePane();
		unrankedPane.updatePane();
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public TiersScrollPane getFirstChild() {
		return this.tiersPane;
	}
}
