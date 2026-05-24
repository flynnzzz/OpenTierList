package ui.gui.panels;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.models.TierElement;
import ui.gui.settings.UISettings;

public class UnrankedPane extends ScrollPane {

	private FlowPane flowPane;
	
	private TierListController controller;
	private List<ImageView> images;
	
	public UnrankedPane(TierListController controller) {
		this.controller = controller;
		this.flowPane = new FlowPane();
		this.images = loadImages();

		this.initPane();
	}	

	private List<ImageView> loadImages() {
		
		List<TierElement> elements = controller.getUnranked();
		this.images = new ArrayList<>();
		
		for (var element : elements) {
			String imageUrl = element.getImageUrl();
			var imageViewer = new ImageView(new Image(imageUrl));
			imageViewer.setUserData(element);
			
			//----- settings -----//

			imageViewer.setPreserveRatio(false);
			imageViewer.setSmooth(true);
			imageViewer.setCache(true);
			
			imageViewer.setFitHeight(UISettings.DEFAULT_CELL_SIZE);
			imageViewer.setFitWidth(UISettings.DEFAULT_CELL_SIZE);
			
			//imageViewer.setOnDragEntered(this::handleDragEntered);
			
			images.add(imageViewer);
		}
		return images;
	}
	
	private void handleDragEntered(DragEvent event) {
		
		//----- define transfer mode -----//
		Dragboard dragBoard = ( (ImageView)event.getAcceptingObject() ).startDragAndDrop(TransferMode.MOVE);
		
	
		//----- put image on dragboard -----//
		var content = new ClipboardContent();
		content.putImage(( (ImageView)event.getAcceptingObject() ).getImage());
		dragBoard.setContent(content);
		
		//----- update tier list -----//
		TierElement acceptingElement = (TierElement)( (ImageView)event.getAcceptingObject()).getUserData();
		controller.removeFromUnranked(acceptingElement);
		
		event.consume();
		
	}

	private void initPane() {
		this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

		this.flowPane.getChildren().addAll(images);
		{
			var borderColor = Paint.valueOf(Color.DIMGRAY.toString());
			var flowPaneBorder = new Border(
					new BorderStroke(
							borderColor, 
							BorderStrokeStyle.SOLID,
							CornerRadii.EMPTY, 
							BorderWidths.DEFAULT
						)
				);

			flowPane.setBorder(flowPaneBorder);
			
			//var backgroundColor = Paint.valueOf(Color.LIGHTGRAY.toString());
			//var flowPaneBackground = Background.fill(backgroundColor);
			//flowPane.setBackground(flowPaneBackground);
			
			flowPane.setPrefWidth(8*UISettings.DEFAULT_CELL_SIZE);
			flowPane.setMinWidth(2*UISettings.DEFAULT_CELL_SIZE);

			flowPane.setMaxHeight(3*UISettings.DEFAULT_CELL_SIZE);
			flowPane.setMinHeight(1*UISettings.DEFAULT_CELL_SIZE);
			
			flowPane.setAlignment(Pos.BASELINE_LEFT);
		}		
		
		this.setContent(flowPane);
		
		this.setPrefWidth(8*UISettings.DEFAULT_CELL_SIZE);
		this.setMaxHeight(3*UISettings.DEFAULT_CELL_SIZE);
		this.setMinHeight(2*UISettings.DEFAULT_CELL_SIZE);
		
		this.setPadding(new Insets(40, 20, 20, 30 + UISettings.DEFAULT_CELL_SIZE));
	}
}
