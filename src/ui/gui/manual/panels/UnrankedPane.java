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
import model.enums.TierElementStatus;
import model.models.TierElement;
import ui.gui.manual.settings.UISettings;

/**
 * 
 * @version 2.20
 * @since v1.2.5
 */
public class UnrankedPane extends ScrollPane {

	//----- panels -----//
	private TierElementsPane unrankedPane;
	
	private TierListController controller;
	private BiConsumer<TierElement, TierElement> onDragDropped;
	
	public UnrankedPane(TierListController controller) {
		this.controller = controller;

		//----- TierElementsPane's on 'drag dropped' behaviour -----//
		this.onDragDropped = (TierElement s, TierElement t) -> {
			switch (s.status()) {
			case TierElementStatus.UNRANKED: {
				this.controller.moveUnranked(s, t);
				break;
			}
			case TierElementStatus.RANKED: {
				this.controller.unrank(controller.getUnranked().indexOf(t), s); 
				break;
			}
			default: break;
			}
		};
		
		this.unrankedPane = new TierElementsPane(controller, controller.getUnranked(), onDragDropped);
		
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
		this.setOnDragExited(event -> this.setTierElementsPaneBorder(UISettings.DEFAULT_BAR_BORDER_COLOR));
		
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
		if (event.getGestureSource()instanceof ImageView && event.getTarget() instanceof UnrankedPane
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
}