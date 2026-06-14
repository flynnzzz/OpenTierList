package ui.gui.manual.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import controller.controllers.TierListController;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import model.models.Tier;

/**
 * 
 * @version 1.10
 * @since v1.2.5
 */
public class TierPanels extends ScrollPane {
	//----- panels -----//
	private VBox tierPanels;
	private List<TierPane> tierPaneList;

	private TierListController controller;
	
	public TierPanels(TierListController controller) {
		this.controller = controller;
		this.tierPaneList = new ArrayList<>();
		this.tierPanels = new VBox();
		this.initAllTiers();
		
		this.setupPane();
	}
	
	private void initAllTiers() {
		List<Tier> tiers = controller.getTiers();
		
		tiers.forEach( tier -> {
			var tierPane = new TierPane(controller, tier);
			tierPane.setUserData(tier); /* 
			tierPane.setOnDragDetected(this::handleDragDetected);
			tierPane.setOnDragEntered(this::handleDragEntered);
			tierPane.setOnDragOver(this::handleDragOver);
			tierPane.setOnDragExited(this::handleDragExited);
			tierPane.setOnDragDropped(this::handleDragDropped);
			tierPane.setOnDragDone(this::handleDragDone); */
			tierPaneList.add(tierPane); 
		});
	}
	
	private void setupPane() {
		//----- init vbox -----//
		tierPanels.getChildren().addAll(tierPaneList);
		
		this.setContent(tierPanels);
	}

	private void handleDragDetected(MouseEvent event) {
		
		//----- define transfer mode -----//
		Dragboard dragBoard = ( (TierPane)event.getSource() ).startDragAndDrop(TransferMode.MOVE);
		 
		//----- put Tier on dragboard -----//
		var content = new ClipboardContent();
		var viewer = (TierPane)event.getSource();
		var sourceId = Integer.valueOf( ((Tier)viewer.getUserData()).hashCode() ).toString();
		
		content.putString(sourceId.toString());
		
		dragBoard.setContent(content);
		
		event.consume();
	}

	private void handleDragOver(DragEvent event) {
		if (event.getGestureSource() != event.getSource() && event.getDragboard().hasString()) {
			this.getStyleClass().add("drag-over");
			event.acceptTransferModes(TransferMode.MOVE);
		}
		event.consume();
	}
	
	private void handleDragEntered(DragEvent event) {
		TierPane target = (TierPane) event.getTarget();
        if (event.getGestureSource() != target &&
                event.getDragboard().hasString()) {
        }
		event.consume();
	}
	
	private void handleDragExited(DragEvent event) {
		TierPane target = (TierPane) event.getTarget();
		this.getStyleClass().remove("drag-over");
		
		event.consume();
	}
	
	private void handleDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasString()) {
			//Node draggedNode = findNodeById(db.getString());
			ObservableList<Node> children = this.getChildren();
			//children.remove(draggedNode);
			//children.add(targetIndex, draggedNode);
		}
		event.setDropCompleted(true);
		event.consume();
	}
	
	private void handleDragDone(DragEvent event) {
		if (event.getTransferMode() == TransferMode.MOVE) {
		}
		event.consume();
	}
}
