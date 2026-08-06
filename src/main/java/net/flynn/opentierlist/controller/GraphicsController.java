package net.flynn.opentierlist.controller;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.ui.ConfigHolder;
import net.flynn.opentierlist.ui.manual.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphicsController {

  private final TierListController tierListController;

  private Stage mainStage;
  private MainPane mainPane;
  private SPTiered tieredPane;
  private SPUnTiered unTieredPane;

  private final FileChooser tierListFileChooser;

  private final Map<Integer, Image> imageCache;

  public GraphicsController(TierListController tierListController) {
    this.tierListController = tierListController;
    this.tierListFileChooser = new FileChooser();

    tierListFileChooser.setTitle("Load tier list");
    tierListFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tier List json files", "*.tson"));

    this.imageCache = new HashMap<>();
    System.err.println("[INFO] --- Instantiating new image cache ---");

  }

  public static void alert(Alert.AlertType type, String title, String message) {
    final var alert = new Alert(type);

    alert.setTitle(title);
    alert.setHeaderText(message);
    alert.setContentText("");

    alert.show();
  }

  public ObservableList<HBTier> loadTiers() {
    return FXCollections.observableArrayList(
        tierListController
            .getTiers().stream()
            .map(t -> new HBTier(tierListController, this, t))
            .toList());
  }

  public ObservableList<IMGElement> loadImages(FPElements parent, List<TierElement> elements) {

    final ObservableList<IMGElement> images = FXCollections.observableArrayList();

    elements.forEach(element -> {

      element.updateImagePath();
      Image img = imageCache.get(element.hashCode());

      if (img == null) {
        img = new Image(element.getImageUri(),
            ConfigHolder.DEFAULT_CELL_SIZE,
            ConfigHolder.DEFAULT_CELL_SIZE,
            false,
            false);
        imageCache.put(element.hashCode(), img);
      }

      var imageViewer = new IMGElement(
          img, tierListController, this, parent);
      imageViewer.setUserData(element);
      images.add(imageViewer);

    });

    return images;
  }

  public void updateImages(FPElements pane) {

    List<TierElement> elements = pane.status() == TieredStatus.TIERED ? pane.getTier().getTiered()
        : tierListController.getUnTiered();

    imageCache.keySet()
        .removeIf(hash -> !tierListController.elementExists(hash));

    final List<IMGElement> images = loadImages(pane, elements);

    pane.getChildren().clear();
    pane.getChildren().addAll(images);
  }

  public void updateTiered() {
    tieredPane.update();
  }

  public void updateUnTiered() {
    unTieredPane.update();
  }

  public void updateTierList() {

    final String newTitle = tierListController.getTierListName();
    mainPane.updateTitleLabel(newTitle);
    mainStage.setTitle("OpenTL - " + newTitle);

    updateTiered();
    updateUnTiered();
  }

  private void updateBorders(String color) {
    setBorder(tieredPane, color);
    setBorder(unTieredPane, color);
  }

  public void addElement(ActionEvent ignoredEvent) {
    final var imageFileChooser = new FileChooser();

    imageFileChooser.setTitle("Select file");
    imageFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
        "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
    imageFileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Pictures"));
    imageFileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));

    List<File> files = imageFileChooser.showOpenMultipleDialog(mainStage);
    if (files == null || files.isEmpty()) {
      System.err.println("[INFO] --- No file selected ---");
      return;
    }

    files.forEach(selectedFile -> {
      if (selectedFile != null && selectedFile.exists()) {
        try {
          final var el = new TierElement(selectedFile.getName(), selectedFile.toURI().toString());
          tierListController.addUnTiered(el);
        } catch (IllegalArgumentException _) {
          System.err.println("[ERROR] --- Resource not found, aborting ---");
          System.exit(-1);
        }
      }
    });

    updateTierList();

  }

  public void addTier(ActionEvent ignoredEvent) {
    tierListController.addDefaultTier();
    updateTiered();
  }

  public void parseAndLoadTierList(ActionEvent ignoredEvent) {

    final File toParse = tierListFileChooser.showOpenDialog(mainStage);

    if (toParse == null) {
      System.err.println("[INFO] --- No file selected ---");
      return;
    }

    final var parsedTier = tierListController.loadTierList(toParse);

    if (parsedTier.isPresent()) {
      tierListController.setTierList(parsedTier.get());

      mainStage.requestFocus();

      reloadImageCache();
      updateTierList();
    }

  }

  private FileChooser createSaveChooser(
      String title, FileChooser.ExtensionFilter filter, File initialDir) {

    final var saveChooser = new FileChooser();
    saveChooser.setTitle(title);
    saveChooser.getExtensionFilters().addAll(filter);
    saveChooser.setInitialDirectory(initialDir);

    saveChooser.setInitialFileName(tierListController.getTierListName() + ".tson");

    return saveChooser;
  }

  public void saveTierList(ActionEvent ignoredEvent) {

    if (tierListController.saveTierList()) {
      GraphicsController.alert(
          Alert.AlertType.INFORMATION, "Save successful",
          tierListController.getTierListName() + " was saved successfully");
    } else {
      GraphicsController.alert(
          Alert.AlertType.ERROR, "Save unsuccessful",
          tierListController.getTierListName() + " could not be saved successfully");
    }
  }

  public void saveTierListAs(ActionEvent ignoredEvent) {

    final var saveChooser = createSaveChooser(
        "Save Tier List as...",
        new FileChooser.ExtensionFilter("Tier List json files", "*.tson"),
        new File(System.getProperty("user.home") + "/Documents"));

    final var file = saveChooser.showSaveDialog(mainStage);
    if (file == null) {
      System.err.println("[INFO] --- No file selected ---");
      return;
    }

    if (tierListController.saveTierList(file.toPath())) {
      GraphicsController.alert(
          Alert.AlertType.INFORMATION, "Save successful",
          tierListController.getTierListName() + " was saved successfully");
    } else {
      GraphicsController.alert(
          Alert.AlertType.ERROR, "Save unsuccessful",
          tierListController.getTierListName() + " could not be saved successfully");
    }

  }

  public void exportTierList(ActionEvent ignoredEvent) {
    if (tierListController.exportTierList(tieredPane)) {
      GraphicsController.alert(
          Alert.AlertType.INFORMATION, "Export successful",
          tierListController.getTierListName() + " was exported successfully");
    } else {
      GraphicsController.alert(
          Alert.AlertType.ERROR, "Export unsuccessful",
          tierListController.getTierListName() + " could not be exported successfully");
    }
  }

  public void exportTierListAs(ActionEvent ignoredEvent) {

    final var saveChooser = createSaveChooser(
        "Export Tier List as...",
        new FileChooser.ExtensionFilter("PNG images", "*.png"),
        new File(System.getProperty("user.home") + "/Pictures"));

    final var file = saveChooser.showSaveDialog(mainStage);
    if (file == null) {
      System.err.println("[INFO] --- No file selected ---");
      return;
    }
    if (tierListController.exportTierList(tieredPane, file.toPath())) {
      GraphicsController.alert(
          Alert.AlertType.INFORMATION, "Export successful",
          tierListController.getTierListName() + " was exported successfully");
    } else {
      GraphicsController.alert(
          Alert.AlertType.ERROR, "Export unsuccessful",
          tierListController.getTierListName() + " could not be exported successfully");
    }

  }

  public void reloadImageCache() {
    System.err.println("[INFO] --- Reloading image cache ---");
    imageCache.clear();
  }

  public void setStageTitle(String string) {
    mainStage.setTitle(string);
  }

  public void setScrollPaneWidth(TieredStatus status, double width) {
    switch (status) {
      case TIERED -> tieredPane.setPrefWidth(width);
      case UNTIERED -> unTieredPane.setPrefWidth(width);
    }
  }

  public void setBorder(ScrollPane pane, String color) {

    final var border = new Border(
            new BorderStroke(
                    Paint.valueOf(color),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT));
    pane.setBorder(border);

  }

  public void setGraphic(Button button, String resource) {
    try {
      var imageURI = getClass().getResource(resource);
      if (imageURI == null)
        throw new URISyntaxException("imageURI", "[ERROR] --- add tier button resource not found, exiting ---");
      button.setGraphic(new ImageView(new Image(imageURI.toURI().toString())));

    } catch (URISyntaxException e) {
      System.err.println(e.getReason());
      System.exit(-1);
    }
  }

  public void setTheme(ConfigHolder.Theme mode) {

    final var lightTheme = new NordLight().getUserAgentStylesheet();
    final var darkTheme = new NordDark().getUserAgentStylesheet();


    switch (mode) {
      case LIGHT -> {
        Application.setUserAgentStylesheet(lightTheme);
        mainPane.setButtonGraphics(ConfigHolder.Theme.LIGHT);
        tieredPane.setButtonThemes(ConfigHolder.Theme.LIGHT);
        updateBorders(ConfigHolder.DEFAULT_ACCENT_COLOR_LIGHT);
      }
      case DARK -> {
        Application.setUserAgentStylesheet(darkTheme);
        mainPane.setButtonGraphics(ConfigHolder.Theme.DARK);
        tieredPane.setButtonThemes(ConfigHolder.Theme.DARK);
        updateBorders(ConfigHolder.DEFAULT_ACCENT_COLOR_DARK);
      }
    }
  }

  public void setFlowPaneBorder(FlowPane flowPane, String color) {
    final var border = new Border(
        new BorderStroke(
            Paint.valueOf(color),
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            BorderWidths.DEFAULT));
    flowPane.setBorder(border);
  }

  public void setMainStage(Stage mainStage) {
    this.mainStage = mainStage;
  }

  public void setMainPane(MainPane mainPane) {
    this.mainPane = mainPane;
  }

  public void setTieredPane(SPTiered tieredPane) {
    this.tieredPane = tieredPane;
  }

  public void setUnTieredPane(SPUnTiered unTieredPane) {
    this.unTieredPane = unTieredPane;
  }

  public Stage getMainStage() {
    return mainStage;
  }

}
