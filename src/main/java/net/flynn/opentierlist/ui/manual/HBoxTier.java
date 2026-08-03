package net.flynn.opentierlist.ui.manual;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.BiConsumer;

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
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.persistence.ResourceHolder;

/**
 * 
 * @version 3.00
 * @since v1.2.5
 */
public class HBoxTier extends HBox {

  private final TextField tierNameLabel;
  private final FlowPaneElements tieredPane;
  private final Button editTierButton;
  private final ScrollPaneTiers parent;

  private ContextMenu contextMenu;
  private MenuItem delete;
  private MenuItem duplicate;
  private MenuItem color;

  private Stage colorStage;

  private ColorPicker colorPicker;
  private Button confirmColor;

  private final TierListController controller;
  private final Stage mainStage;
  private final Tier tier;

  private String oldTextValue;

  public HBoxTier(ScrollPaneTiers parent, Stage mainStage, TierListController controller, Tier tier) {
    this.parent = parent;
    this.tierNameLabel = new TextField(tier.getName());
    this.controller = controller;
    this.mainStage = mainStage;
    this.tier = tier;
    this.editTierButton = new Button();

    // ----- TierElementsPane's on 'drag dropped' behaviour -----//
    BiConsumer<TierElement, TierElement> onDragDropped = (src, dest) -> {
      switch (src.getStatus()) {
        case TieredStatus.TIERED: {
          Optional<Tier> potentialTargetTier = controller.getTierByElement(dest);

          potentialTargetTier.ifPresent(targetTier -> controller.moveElement(src, targetTier, dest));
          break;
        }
        case TieredStatus.UNTIERED: {
          Optional<Tier> potentialTargetTier = controller.getTierByElement(dest);

          potentialTargetTier.ifPresent(targetTier -> controller.tier(src, targetTier, dest));
          break;
        }
        default: {
          System.err.println("--- Unexpected error, aborting ---");
          System.exit(-1);
        }
      }
    };

    this.tieredPane = new FlowPaneElements(parent, controller, tier.getTiered(), tier, onDragDropped);
    this.setupPane();
  }

  private void setupPane() {
    this.getChildren().addAll(tierNameLabel, tieredPane, editTierButton);

    // ----- settings -----//
    {
      this.tierNameLabel.setEditable(true);
      tierNameLabel.setFocusTraversable(false);
      tierNameLabel.setAlignment(Pos.CENTER);
      tierNameLabel.setPrefSize(UISettings.DEFAULT_CELL_SIZE, UISettings.DEFAULT_CELL_SIZE);

      this.editTierButton.setAlignment(Pos.CENTER);
      editTierButton.setFocusTraversable(false);

      this.setTierNameLabelBackground();
      this.setTierNameLabelBorder(UISettings.DEFAULT_BAR_BORDER_COLOR);

      this.setAlignment(Pos.CENTER);

      this.setSpacing(UISettings.DEFAULT_TIER_SPACING);
      this.setPadding(new Insets(
          UISettings.DEFAULT_TIER_PADDING_TOP,
          UISettings.DEFAULT_TIER_PADDING_RIGHT,
          UISettings.DEFAULT_TIER_PADDING_BOTTOM,
          UISettings.DEFAULT_TIER_PADDING_LEFT));
    }
    setupEditButton();
    setupEventHandlers();
  }

  private void setupEventHandlers() {

    setupDragAndDrop();

    setupColorPicker();

    setupEditMenu();

    this.tierNameLabel.focusedProperty().addListener((_, _, now) -> {
      if (now)
        oldTextValue = tierNameLabel.getText();
      else
        tierNameLabel.setText(oldTextValue);
    });

    this.tierNameLabel.setOnAction(_ -> {
      if (!tierNameLabel.getText().isBlank()) {
        controller.setTierName(tier, tierNameLabel.getText());
        oldTextValue = tierNameLabel.getText();
      }
      tierNameLabel.getScene().getRoot().requestFocus();
    });

    Tooltip tooltip = new Tooltip("Click to drag and move");
    tierNameLabel.setTooltip(tooltip);

    // ----- edit button -----//
    editTierButton.setOnAction(_ -> contextMenu.show(editTierButton, Side.BOTTOM, 0, 0));

    delete.setOnAction(_ -> {
      tier.getTiered().forEach(controller::unTier);

      controller.removeTier(tier);
      parent.updateTierList();
    });

    duplicate.setOnAction(_ -> {
      var clone = tier.copy();
      controller.addTier(clone);
      controller.moveTier(clone, controller.getTiers().indexOf(tier) + 1);

      parent.updateTierList();
    });

    color.setOnAction(_ -> {
      colorStage.show();
      colorStage.setX(mainStage.getX() + (mainStage.getWidth() - colorStage.getWidth()) / 2.0);
      colorStage.setY(mainStage.getY() + (mainStage.getHeight() - colorStage.getHeight()) / 2.0);

      mainStage.setOnCloseRequest(_ -> {
        if (colorStage.isShowing()) {
          colorStage.close();
        }
      });
    });

    confirmColor.setOnAction(_ -> {
      var chosenColor = Optional.ofNullable(colorPicker.getValue());

      if (chosenColor.isEmpty())
        return;

      tier.setColor(chosenColor.get().toString());
      parent.updateTierList();
      colorStage.close();
    });
  }

  private void setupColorPicker() {
    colorStage = new Stage();

    BorderPane colorPane = new BorderPane();

    Scene colorMenu = new Scene(colorPane, 200, 150);
    colorPicker = new ColorPicker();
    colorPicker.setPadding(new Insets(5, 20, 5, 20));
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

  private void setupEditButton() {
    try {
      var imageURI = getClass().getResource(ResourceHolder.getEditButtonIcon());
      if (imageURI == null)
        throw new URISyntaxException("imageURI", "--- Default edit tier resource not found, exiting ---");
      editTierButton.setGraphic(new ImageView(new Image(imageURI.toURI().toString())));
    } catch (URISyntaxException e) {
      System.err.println(e.getReason());
      System.exit(-1);
    }
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

    this.tierNameLabel.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE)
        parent.updateAllTiers();
      event.consume();
    });

    this.tierNameLabel.setOnDragDropped(this::handleDragDropped);
  }

  private void handleDragDetected(MouseEvent event) {

    // ----- define transfer mode -----//
    Dragboard dragBoard = ((TextField) event.getSource()).startDragAndDrop(TransferMode.MOVE);

    // ----- put Text on drag board -----//
    var content = new ClipboardContent();
    String sourceId = Integer.valueOf(tier.hashCode()).toString();

    content.putString(sourceId);
    dragBoard.setContent(content);
    event.consume();
  }

  private void handleDragDropped(DragEvent event) {
    Dragboard db = event.getDragboard();
    if (db.hasString() && !event.getDragboard().hasImage()) {

      Optional<Tier> source = controller.getTierByHash(db.getString());

      if (source.isEmpty())
        return;

      Node potentialTarget = (Node) event.getTarget();

      while (potentialTarget != null && !(potentialTarget instanceof HBoxTier)) {
        potentialTarget = potentialTarget.getParent();
      }

      if (potentialTarget instanceof HBoxTier targetPane) {
        Tier target = targetPane.getTier();
        controller.moveTier(source.get(), target);
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
    var backgroundColor = Paint.valueOf(tier.getColor());
    var nameLabelBackground = Background.fill(backgroundColor);
    tierNameLabel.setBackground(nameLabelBackground);
  }

  private Tier getTier() {
    return this.tier;
  }
}
