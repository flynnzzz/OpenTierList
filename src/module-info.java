module openTierLists {
	requires org.junit.jupiter.api;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.controls;
	
    exports ui.gui;
    opens ui.gui to javafx.graphics;
}