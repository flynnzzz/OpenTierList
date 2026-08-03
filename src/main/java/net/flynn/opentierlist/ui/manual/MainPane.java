package net.flynn.opentierlist.ui.manual;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.TierList;
import net.flynn.opentierlist.persistence.ResourceHolder;

import javax.imageio.ImageIO;

/**
 * 
 * @version 2.95
 * @since v1.2.5
 */
public class MainPane extends BorderPane {

  private final TextField titleLabel;
  private final ScrollPaneTiers tieredPane;
  private final ScrollPaneUnTiered unTieredPane;
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
    this.unTieredPane = new ScrollPaneUnTiered(this, controller);
    this.tieredPane = new ScrollPaneTiers(this, controller);
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

      tierListFileChooser.setTitle("Load tier list");
      tierListFileChooser.getExtensionFilters().add(new ExtensionFilter("Tier List json files", "*.tson"));
    }

    {
      // TODO: save as .tson to path
      final MenuItem menuNewTierList = new MenuItem("New...\t\t"), menuSaveItem = new MenuItem("Save as .tson file...\t\t"), menuLoadItem = new MenuItem("Load\t\t");

      menuNewTierList.setOnAction(_ -> {
        controller.setTierList(TierList.ofDefaultTiers());

        FlowPaneElements.reloadImageCache();
        updateTierList();
      });

      menuSaveItem.setOnAction(_ -> controller.saveTierList());

      menuLoadItem.setOnAction(_ -> parseAndLoadTierList());

      final MenuItem menuExport = setupMenuExport();
      final MenuItem menuExportAs = setupMenuExportAs();

      final var lightTheme = new NordLight().getUserAgentStylesheet();
      final var darkTheme = new NordDark().getUserAgentStylesheet();

      Application.setUserAgentStylesheet(lightTheme);

      final MenuItem menuLightTheme = new MenuItem("Light Theme\t\t"), menuDarkTheme = new MenuItem("Dark Theme\t\t");

      menuLightTheme.setOnAction(_ -> applyTheme(UISettings.Theme.LIGHT, lightTheme));
      menuDarkTheme.setOnAction(_ -> applyTheme(UISettings.Theme.DARK, darkTheme));

      fileMenu.getItems().addAll(menuNewTierList, menuSaveItem, menuLoadItem, menuExport, menuExportAs);
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

  // TODO: move to persistence
  private MenuItem setupMenuExport() {
    final MenuItem menuExport = new MenuItem("Export as PNG...\t\t");

    menuExport.setOnAction(_ -> {

      final Path defaultPath = Path.of(System.getProperty("user.home"), "Pictures", "OpenTierList");

      if (!Files.exists(defaultPath)) {
        if (!defaultPath.toFile().mkdir())
          System.err.println("--- Could not create folder 'OpenTierList' in " + System.getProperty("user.home") + "/Pictures ---");
      }

      final var file = defaultPath.resolve(controller.getTierListName() + ".png").toFile();

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(takeScreenshot(), null), "png", file);
        } catch (IOException _) {
          System.err.println(
                  "--- IO exception: could not export Tier List" + controller.getTierListName() + " ---"
          );
        }
    });
    return menuExport;
  }

  private MenuItem setupMenuExportAs() {

    final MenuItem menuExportAs = new MenuItem("Export as PNG to...\t\t");

    menuExportAs.setOnAction(_ -> {

      final var saveChooser = new FileChooser();
      saveChooser.setTitle("Save Tier List");
      saveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
      saveChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Pictures"));

      final var file = saveChooser.showSaveDialog(mainStage);
      if (file == null) {
        System.err.println("--- No file selected ---");
        return;
      }
      try {
        ImageIO.write(SwingFXUtils.fromFXImage(takeScreenshot(), null), "png", file);
      } catch (IOException _) {
        System.err.println(
                "--- IO exception: could not export Tier List" + controller.getTierListName() + " ---"
        );
      }
    });
    return menuExportAs;
  }

  private WritableImage takeScreenshot() {

    final double inboundWidth = tieredPane.getContent().getBoundsInLocal().getWidth(),
                 inboundHeight = tieredPane.getContent().getBoundsInLocal().getHeight();

    final WritableImage image = new WritableImage((int) inboundWidth, (int) inboundHeight);

    final var params = new SnapshotParameters();
    params.setTransform(new Scale(1, 1));

    // TODO: temporarily disable tier hbox buttons
    tieredPane.getContent().snapshot(params, image);

    return image;
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

      FlowPaneElements.reloadImageCache();
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

  public ScrollPaneTiers getFirstChild() {
    return this.tieredPane;
  }
}
