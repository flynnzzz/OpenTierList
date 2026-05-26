module openTierLists {
	requires org.junit.jupiter.api;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	
    exports ui.gui.manual;
    opens ui.gui.manual to javafx.graphics;
}