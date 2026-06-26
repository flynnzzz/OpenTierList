package ui.gui.manual.panels;

import java.util.function.BiConsumer;

import controller.controllers.TierListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import model.enums.TelementStatus;
import model.models.Telement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class UnrankedScrollPane extends ScrollPane {

	//----- panels -----//
	private TelementsFlowPane unrankedPane;
	
	@SuppressWarnings("unused")
	private MainPane parent;
	private TierListController controller;
	private BiConsumer<Telement, Telement> onDragDropped;
	
	public UnrankedScrollPane(MainPane parent, TierListController controller) {
		this.parent = parent;
		this.controller = controller;

		//----- TierElementsPane's on 'drag dropped' behaviour -----//
		this.onDragDropped = (Telement s, Telement t) -> {
			switch (s.status()) {
			case TelementStatus.UNRANKED: {
				this.controller.moveUnranked(s, t);
				break;
			}
			case TelementStatus.RANKED: {
				this.controller.unrank(controller.getUnranked().indexOf(t), s); 
				break;
			}
			default: break;
			}
		};
		
		this.unrankedPane = new TelementsFlowPane(controller, controller.getUnranked(), onDragDropped);
		
		this.setupPane();
	}	

	private void setupPane() {
		this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);	
		
		this.setPrefWidth(UISettings.DEFAULT_BAR_WIDTH);
		this.setMaxHeight(UISettings.DEFAULT_UNRANKED_PANE_MAX_HEIGHT);
		this.setMinHeight(UISettings.DEFAULT_UNRANKED_PANE_MAX_WIDHT);
		this.setFitToWidth(true);
		this.unrankedPane.setAlignment(Pos.CENTER_LEFT);

		this.setOnDragEntered(this::handleDragEntered);
		this.setOnDragExited(_ -> this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_BORDER_COLOR));
		
		this.setPadding(new Insets(
				UISettings.DEFAULT_UNRANKED_PADDING_TOP,
				UISettings.DEFAULT_UNRANKED_PADDING_RIGHT,
				UISettings.DEFAULT_UNRANKED_PADDING_BOTTOM,
				UISettings.DEFAULT_UNRANKED_PADDING_LEFT)
			);
		this.setUnrankedBorder();
		
		this.setContent(unrankedPane);
	}
	
	private void setUnrankedBorder() {
		var unrankedBorder = new Border(  
				new BorderStroke(
						Paint.valueOf(UISettings.DEFAULT_BAR_BORDER_COLOR), 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
					)
			);
		unrankedPane.setBorder(unrankedBorder);
	}
	
	private void handleDragEntered(DragEvent event) {
		if (event.getGestureSource()instanceof ImageView && event.getTarget() instanceof UnrankedScrollPane
				&& event.getDragboard().hasImage())
			this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_HIGHLIGHT_COLOR);
               
		event.consume();
	}
	
	private void setTierElementsPaneBorder(String color) {
		var tierElementsPaneBorder = new Border(
				new BorderStroke(
						Paint.valueOf(color), 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, 
						BorderWidths.DEFAULT
						)
				);
		this.unrankedPane.setBorder(tierElementsPaneBorder);
	}
	
	public void updatePane() {
		unrankedPane.updateImages();
	}
}