module openTierList {
	requires org.junit.jupiter.api;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	
    exports opentierlist.ui.gui.manual;
    opens opentierlist.ui.gui.manual to javafx.graphics;
}