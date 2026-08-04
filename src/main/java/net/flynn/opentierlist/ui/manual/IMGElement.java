package net.flynn.opentierlist.ui.manual;

import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.models.TierElement;

import java.util.Optional;
import java.util.function.BiConsumer;

public class IMGElement extends ImageView {

    private final FPElements parent;
    private final SPTiers grandparent;
    private final TierListController controller;
    private final BiConsumer<TierElement, TierElement> onDragDropped;

    public IMGElement(
            Image image, TierListController controller, FPElements parent, SPTiers grandparent, BiConsumer<TierElement, TierElement> onDragDropped
    ) {
        super(image);
        this.controller = controller;
        this.parent = parent;
        this.grandparent = grandparent;
        this.onDragDropped = onDragDropped;

        this.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
        this.setFitWidth(UISettings.DEFAULT_CELL_SIZE);


        setupEventHandlers();
    }

    private void setupEventHandlers() {

        final var imageContextMenu = new ContextMenu();
        final var deleteImageMenu = new MenuItem("Delete");
        final var unTierImageMenu = new MenuItem("UnTier");

        this.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {

                deleteImageMenu.setOnAction(_ -> {
                    if (this.getUserData() instanceof TierElement element) {
                        controller.removeElement(element);
                        parent.getChildren().remove(this);
                        parent.updateImages();
                    }
                });

                if (this.getUserData() instanceof TierElement element && element.isTiered()) {
                    imageContextMenu.getItems().add(unTierImageMenu);
                    unTierImageMenu.setOnAction(_ -> {
                        controller.unTier(element);
                        grandparent.updateTierList();
                    });
                }
                imageContextMenu.getItems().add(deleteImageMenu);
                imageContextMenu.show(this, Side.RIGHT, 0, 0);
            }
        });

        this.setOnDragDetected(this::handleDragDetectedImage);

        this.setOnDragOver(event -> {
            if (event.getDragboard().hasImage())
                event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
        this.setOnDragEntered(event -> {
            if (event.getDragboard().hasImage()) {

                this.setFitHeight(UISettings.DEFAULT_EXPANDED_IMAGE_SIZE);
                parent.setPadding(
                        new Insets(
                                0,
                                UISettings.DEFAULT_DRAG_ENTERED_PADDING,
                                0,
                                UISettings.DEFAULT_DRAG_ENTERED_PADDING
                        )
                );
            }
            event.consume();
        });

        this.setOnDragExited(event -> {
            if (event.getTarget() instanceof ImageView && event.getSource() instanceof ImageView) {

                this.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
                parent.setPadding(Insets.EMPTY);

            }
            event.consume();
        });

        this.setOnDragDropped(this::handleDragDroppedImage);

        this.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE)
                parent.updateImages();
            event.consume();
        });
    }

    private void handleDragDetectedImage(MouseEvent event) {

        if (!(event.getSource() instanceof ImageView sourceImage))
            return;

        Dragboard dragBoard = sourceImage.startDragAndDrop(TransferMode.MOVE);
        var content = new ClipboardContent();

        if (sourceImage.getUserData() instanceof TierElement sourceElement) {

            content.putImage(sourceImage.getImage());
            content.putString(String.valueOf(sourceElement.hashCode()));

            dragBoard.setContent(content);
        }
        event.consume();
    }

    private void handleDragDroppedImage(DragEvent event) {
        boolean success = false;

        Dragboard dragBoard = event.getDragboard();
        String elementHash = dragBoard.getString();

        Optional<TierElement> potentialSource = controller.getElementByHash(elementHash);
        if (potentialSource.isEmpty()) {
            System.err.println("--- Element to be dropped was not found ---");
            return;
        }

        EventTarget eventTarget = event.getTarget();
        if (dragBoard.hasImage() && dragBoard.hasString()
                && eventTarget instanceof ImageView targetImage
                && targetImage.getUserData() instanceof TierElement targetElement) {

            TierElement source = potentialSource.get();
            if (!source.equals(targetElement)) {

                onDragDropped.accept(potentialSource.get(), targetElement);
                parent.updateImages();

                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
}
