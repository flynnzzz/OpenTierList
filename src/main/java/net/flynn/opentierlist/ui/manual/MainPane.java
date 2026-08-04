package net.flynn.opentierlist.ui.manual;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import javafx.application.Application;
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
import net.flynn.opentierlist.model.models.TierList;
import net.flynn.opentierlist.persistence.ResourceHolder;

/**
 * 
 * @version 2.95
 * @since v1.2.5
 */
public class MainPane extends BorderPane {

  private final TextField titleLabel;
  private final SPTiers tieredPane;
  private final SPUnTiered unTieredPane;
  private final Button addTierButton, addElementButton;
  private final FileChooser imageFileChooser, tierListFileChooser;
  private final MenuBar menuBar;
  private final Menu fileMenu, viewMenu;
  private final Stage mainStage;

  private final TierListController controller;
  private String oldTitle;

  public MainPane(TierListController controller, Stage mainStage) {
    this.controller = controller;
    this.mainStage = mainStage;
    this.titleLabel = new TextField(controller.getTierListName());
    this.unTieredPane = new SPUnTiered(this, controller);
    this.tieredPane = new SPTiers(this, controller);
    this.addTierButton = new Button();
    this.addElementButton = new Button();
    this.menuBar = new MenuBar();
    this.fileMenu = new Menu("File");
    this.viewMenu = new Menu("View");
    this.imageFileChooser = new FileChooser();
    this.tierListFileChooser = new FileChooser();

    initPane();
  }

  public void initPane() {

    {
      imageFileChooser.setTitle("Select file");
      imageFileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
      imageFileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Pictures"));

      tierListFileChooser.setTitle("Load tier list");
      tierListFileChooser.getExtensionFilters().add(new ExtensionFilter("Tier List json files", "*.tson"));
      imageFileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
    }

    {
      final MenuItem menuNewTierList = new MenuItem("New...\t\t"),
              menuSaveItem = new MenuItem("Save file...\t\t"),
              menuSaveItemAs = new MenuItem("Save file to...\t\t"),
              menuLoadItem = new MenuItem("Load\t\t");

      menuNewTierList.setOnAction(_ -> {
        controller.setTierList(TierList.ofDefaultTiers());

        FPElements.reloadImageCache();
        updateTierList();
      });

      menuSaveItem.setOnAction(_ -> controller.saveTierList());

      menuSaveItemAs.setOnAction(_ -> {

        final var saveChooser = new FileChooser();
        saveChooser.setTitle("Save Tier List");
        saveChooser.getExtensionFilters().addAll(tierListFileChooser.getExtensionFilters());
        saveChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));

        saveChooser.setInitialFileName(controller.getTierListName() + ".tson");

        final var file = saveChooser.showSaveDialog(mainStage);
        if (file == null) {
          System.err.println("--- No file selected ---");
          return;
        }
        controller.saveTierList(file.toPath());

      });

      menuLoadItem.setOnAction(_ -> parseAndLoadTierList());

      final MenuItem menuExport = new MenuItem("Export as PNG...\t\t");
      menuExport.setOnAction(_ -> controller.exportTierList(tieredPane));

      final MenuItem menuExportAs = setupMenuExportAs();

      final var lightTheme = new NordLight().getUserAgentStylesheet();
      final var darkTheme = new NordDark().getUserAgentStylesheet();

      Application.setUserAgentStylesheet(lightTheme);

      final MenuItem menuLightTheme = new MenuItem("Light Theme\t\t"), menuDarkTheme = new MenuItem("Dark Theme\t\t");

      menuLightTheme.setOnAction(_ -> applyTheme(UISettings.Theme.LIGHT, lightTheme));
      menuDarkTheme.setOnAction(_ -> applyTheme(UISettings.Theme.DARK, darkTheme));

      fileMenu.getItems().addAll(menuNewTierList, menuSaveItem, menuSaveItemAs, menuLoadItem, menuExport, menuExportAs);
      viewMenu.getItems().addAll(menuLightTheme, menuDarkTheme);
      menuBar.getMenus().addAll(fileMenu, viewMenu);
      setTop(menuBar);
    }

    {
      addTierButton.setTooltip(new Tooltip("Add new Tier"));
      addElementButton.setTooltip(new Tooltip("Add new Element"));

      addTierButton.setFocusTraversable(false);
      addElementButton.setFocusTraversable(false);

      try {
        var imageURI = getClass().getResource(ResourceHolder.getAddTierButtonIcon());
        if (imageURI == null)
          throw new URISyntaxException("imageURI", "--- add tier button resource not found, exiting ---");
        addTierButton.setGraphic(new ImageView(new Image(imageURI.toURI().toString())));

        imageURI = getClass().getResource(ResourceHolder.getAddElementButtonIcon());
        if (imageURI == null)
          throw new URISyntaxException("imageURI", "--- add element button resource not found, exiting ---");
        addElementButton.setGraphic(new ImageView(new Image(imageURI.toURI().toString())));
      } catch (URISyntaxException e) {
        System.err.println(e.getReason());
        System.exit(-1);
      }

    }

    final HBox buttonsHBox = new HBox();
    {
      buttonsHBox.getChildren().addAll(addTierButton, addElementButton);

      buttonsHBox.setPadding(new Insets(UISettings.DEFAULT_DBUTTON_PADDING,
              UISettings.DEFAULT_DBUTTON_PADDING,
              UISettings.DEFAULT_DBUTTON_PADDING,
              UISettings.DEFAULT_DBUTTON_PADDING));

      buttonsHBox.setSpacing((double) UISettings.DEFAULT_DBUTTON_PADDING / 3);
      buttonsHBox.setAlignment(Pos.BOTTOM_CENTER);
    }
    {
      titleLabel.setFocusTraversable(false);
      HBox titleBox = new HBox(titleLabel);
      titleBox.setPadding(new Insets(
              UISettings.DEFAULT_TITLE_PADDING_TOP,
              UISettings.DEFAULT_TITLE_PADDING_RIGHT,
              UISettings.DEFAULT_TITLE_PADDING_BOTTOM,
              UISettings.DEFAULT_TITLE_PADDING_LEFT));

      titleBox.setAlignment(Pos.BASELINE_CENTER);
      var centerBox = new VBox(titleBox, tieredPane, buttonsHBox);
      centerBox.setAlignment(Pos.CENTER);
      setCenter(centerBox);

      HBox unrankedBox = new HBox(unTieredPane);
      unrankedBox.setAlignment(Pos.BASELINE_CENTER);
      setBottom(unrankedBox);
    }

    setupEventHandlers();
  }

  private MenuItem setupMenuExportAs() {

    final MenuItem menuExportAs = new MenuItem("Export as PNG to...\t\t");

    menuExportAs.setOnAction(_ -> {

      final var saveChooser = new FileChooser();
      saveChooser.setTitle("Save Tier List");
      saveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
      saveChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Pictures"));

      saveChooser.setInitialFileName(controller.getTierListName() + ".png");

      final var file = saveChooser.showSaveDialog(mainStage);
      if (file == null) {
        System.err.println("--- No file selected ---");
        return;
      }
      controller.exportTierList(tieredPane, file.toPath());
    });
    return menuExportAs;
  }

  private void parseAndLoadTierList() {

    final File toParse = tierListFileChooser.showOpenDialog(mainStage);

    if (toParse == null) {
      System.err.println("--- No file selected ---");
      return;
    }

    final var parsedTier = controller.loadTierList(toParse);

    if (parsedTier.isPresent()) {
      controller.setTierList(parsedTier.get());

      titleLabel.getScene().getRoot().requestFocus();

      FPElements.reloadImageCache();
      updateTierList();
    }

  }

  private void applyTheme(UISettings.Theme mode, String styleSheet) {

    Objects.requireNonNull(styleSheet, "--- Style sheet cannot be null ---");
    if (styleSheet.isBlank())
      throw new IllegalArgumentException("--- Style sheet cannot be blank ---");

    Application.setUserAgentStylesheet(styleSheet);

    switch (mode) {
      case UISettings.Theme.LIGHT -> tieredPane.updateBorder(UISettings.DEFAULT_ACCENT_COLOR_LIGHT);
      case UISettings.Theme.DARK -> tieredPane.updateBorder(UISettings.DEFAULT_ACCENT_COLOR_DARK);
    }
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
        this.mainStage.setTitle(titleLabel.getText());
        this.oldTitle = titleLabel.getText();
      }
      titleLabel.getScene().getRoot().requestFocus();
    });

    addTierButton.setOnAction(_ -> {
      controller.addDefaultTier();
      tieredPane.updateAllTiers();
    });

    addElementButton.setOnAction(_ -> {
      List<File> files = imageFileChooser.showOpenMultipleDialog(mainStage);
      if (files == null || files.isEmpty()) {
        System.err.println("--- No file selected ---");
        return;
      }

      files.forEach(selectedFile -> {
        if (selectedFile != null && selectedFile.exists()) {
          try {
            controller.addUnTiered(new TierElement(selectedFile.getName(), selectedFile.toURI().toString()));
          } catch (IllegalArgumentException _) {
            System.err.println("--- Resource not found, aborting ---");
            System.exit(-1);
          }
        }
      });

      updateTierList();

    });
  }

  public void updateTierList() {
    titleLabel.setText(controller.getTierListName());
    mainStage.setTitle(titleLabel.getText());
    oldTitle = titleLabel.getText();
    tieredPane.updateAllTiers();
    unTieredPane.updatePane();
  }

  public Stage getMainStage() {
    return this.mainStage;
  }

  public SPTiers getFirstChild() {
    return this.tieredPane;
  }
}
