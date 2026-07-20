package net.flynn.opentierlist.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class UIController {
	public static void errorAlert(String context) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(context);
		ButtonType okButton = new ButtonType("Ok");
		alert.getButtonTypes().add(okButton);
		
		alert.setOnCloseRequest(_ -> alert.close());
		alert.show();
	}
}
