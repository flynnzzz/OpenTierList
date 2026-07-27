package net.flynn.opentierlist.ui.manual;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.persistence.ResourceHolder;

/**
 * 
 * @version 2.00
 * @since v1.2.5
 */
public class MainPane extends BorderPane {

  // ----- panels ----- //
  private final TextField titleLabel;

  private final ScrollPaneTiers tieredPane;
  private final ScrollPaneUnTiered unTieredPane;

  private final Button addTierButton, addElementButton;

  private final FileChooser imageFileChooser, tierListFileChooser;
  private final Stage stage;

  // ----- menu bar ----- //
  private final MenuBar menuBar;
  private final Menu fileMenu;

  private final TierListController controller;

  private String oldTitle;

  public MainPane(TierListController controller, Stage stage) {
    this.controller = controller;
    this.stage = stage;
    this.titleLabel = new TextField(controller.getTierListName());
    this.unTieredPane = new ScrollPaneUnTiered(this, controller);
    this.tieredPane = new ScrollPaneTiers(this, controller);
    this.addTierButton = new Button();
    this.addElementButton = new Button();
    this.menuBar = new MenuBar();
    this.fileMenu = new Menu("File");
    this.imageFileChooser = new FileChooser();
    this.tierListFileChooser = new FileChooser();

    initPane();
  }

  public void initPane() {
    // ----- title ----- //
    titleLabel.setFocusTraversable(false);

    // ----- file chooser ----- //
    imageFileChooser.setTitle("Select file");
    imageFileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

    tierListFileChooser.setTitle("Load tier list");
    tierListFileChooser.getExtensionFilters().add(new ExtensionFilter("Tier List json files", "*.tson"));

    // ----- menu bar ----- //
    MenuItem menuSaveItem = new MenuItem("Save"), menuLoadItem = new MenuItem("Load");

    menuSaveItem.setOnAction(_ -> controller.saveTierList());

    menuLoadItem.setOnAction(_ -> {

      File toParse = tierListFileChooser.showOpenDialog(stage);

      if (toParse == null) {
        System.err.println("--- No file selected ---");
        return;
      }

      var parsedTier = controller.loadTierList(toParse);

      if (parsedTier.isPresent()) {
        controller.setTierList(parsedTier.get());
        titleLabel.setText(controller.getTierListName());
        stage.setTitle(titleLabel.getText());
        oldTitle = titleLabel.getText();

        titleLabel.getScene().getRoot().requestFocus();

        FlowPaneElements.reloadImageCache();
        updateTierList();
      }
    });

    fileMenu.getItems().addAll(menuSaveItem, menuLoadItem);
    menuBar.getMenus().add(fileMenu);
    setTop(menuBar);

    HBox titleBox = new HBox(titleLabel);
    titleBox.setPadding(new Insets(
        UISettings.DEFAULT_TITLE_PADDING_TOP,
        UISettings.DEFAULT_TITLE_PADDING_RIGHT,
        UISettings.DEFAULT_TITLE_PADDING_BOTTOM,
        UISettings.DEFAULT_TITLE_PADDING_LEFT));
    titleBox.setAlignment(Pos.BASELINE_CENTER);

    // ----- buttons -----//
    addTierButton.setTooltip(new Tooltip("Add new Tier"));
    addElementButton.setTooltip(new Tooltip("Add new Element"));

    addTierButton.setFocusTraversable(false);
    addElementButton.setFocusTraversable(false);

    try {
      var imageURI = getClass().getResource(ResourceHolder.getAddtierbuttonicon());
      if (imageURI == null)
        throw new URISyntaxException("imageURI", "--- addtier button resource not found, exiting ---");
      addTierButton.setGraphic(new ImageView(new Image(imageURI.toURI().toString())));

      imageURI = getClass().getResource(ResourceHolder.getAddElementButtonIcon());
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

    buttonsHBox.setSpacing((double) UISettings.DEFAULT_DBUTTON_PADDING / 3);
    buttonsHBox.setAlignment(Pos.BOTTOM_CENTER);

    // ----- tiers -----//
    var centerBox = new VBox(titleBox, tieredPane, buttonsHBox);
    centerBox.setAlignment(Pos.CENTER);
    setCenter(centerBox);

    // ----- unranked -----//
    HBox unrankedBox = new HBox(unTieredPane);
    unrankedBox.setAlignment(Pos.BASELINE_CENTER);
    setBottom(unrankedBox);

    setupEventHandlers();
  }

  private void setupEventHandlers() {
    titleLabel.focusedProperty().addListener((_, _, now) -> {
      if (now)
        this.oldTitle = titleLabel.getText();
      else
        titleLabel.setText(oldTitle);
    });

    titleLabel.setOnAction(_ -> {
      if (!titleLabel.getText().isBlank()) {
        controller.setTierListName(titleLabel.getText());
        this.stage.setTitle(titleLabel.getText());
        this.oldTitle = titleLabel.getText();
      }
      titleLabel.getScene().getRoot().requestFocus();
    });

    addTierButton.setOnAction(_ -> {
      controller.addDefaultTier();
      tieredPane.updateAllTiers();
    });

    addElementButton.setOnAction(_ -> {
      List<File> files = imageFileChooser.showOpenMultipleDialog(stage);
      if (files == null || files.isEmpty()) {
        System.err.println("--- No file selected ---");
        return;
      }

      files.forEach(selectedFile -> {
        if (selectedFile != null && selectedFile.exists()) {
          try {
            controller.addUnTiered(new TierElement(selectedFile.getName(), selectedFile.toURI().toString()));
          } catch (IllegalArgumentException | FileNotFoundException _) {
            System.err.println("--- Resource not found, aborting ---");
            System.exit(-1);
          }
        }
      });
      updateTierList();
    });
  }

  public void updateTierList() {
    tieredPane.updateAllTiers();
    unTieredPane.updatePane();
  }

  public Stage getStage() {
    return this.stage;
  }

  public ScrollPaneTiers getFirstChild() {
    return this.tieredPane;
  }
}
