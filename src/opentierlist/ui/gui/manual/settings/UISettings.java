package opentierlist.ui.gui.manual.settings;

import javafx.scene.paint.Color;

public class UISettings {

	// ----- general -----//

	public final static int DEFAULT_CELL_SIZE = 80;
	public final static int DEFAULT_BAR_WIDTH = 9 * DEFAULT_CELL_SIZE;
	public final static int DEFAULT_BAR_MAX_HEIGHT = 4 * DEFAULT_CELL_SIZE;
	public final static int DEFAULT_BAR_MIN_HEIGHT = DEFAULT_CELL_SIZE + 2;
	public final static int DEFAULT_MAINPANE_LEFT_PADDING = DEFAULT_CELL_SIZE;
	
	// ----- colors -----//

	public final static String DEFAULT_BAR_BORDER_COLOR = Color.DIMGRAY.toString();
	public final static String DEFAULT_BAR_HIGHLIGHT_COLOR = Color.DEEPSKYBLUE.toString();

	// ----- unranked -----//

	public final static int DEFAULT_UNRANKED_PANE_MAX_HEIGHT = 3 * DEFAULT_CELL_SIZE;
	public final static int DEFAULT_UNRANKED_PANE_MAX_WIDHT = 2 * DEFAULT_CELL_SIZE;
	public final static int DEFAULT_UNRANKED_PADDING_TOP = 20;
	public final static int DEFAULT_UNRANKED_PADDING_RIGHT = 20;
	public final static int DEFAULT_UNRANKED_PADDING_BOTTOM = 20;
	public final static int DEFAULT_UNRANKED_PADDING_LEFT = 20;

	// ----- tier -----//

	public final static int DEFAULT_TIER_SPACING = 10;
	public final static int DEFAULT_TIER_PADDING_TOP = 10;
	public final static int DEFAULT_TIER_PADDING_RIGHT = 10;
	public final static int DEFAULT_TIER_PADDING_BOTTOM = 10;
	public final static int DEFAULT_TIER_PADDING_LEFT = 10;

	// ----- title -----//

	public final static int DEFAULT_TITLE_PADDING_TOP = 20;
	public final static int DEFAULT_TITLE_PADDING_RIGHT = 20;
	public final static int DEFAULT_TITLE_PADDING_BOTTOM = 20;
	public final static int DEFAULT_TITLE_PADDING_LEFT = 20;

	// ----- right buttons -----//

	public final static int DEFAULT_DBUTTON_PADDING = 10;
}
