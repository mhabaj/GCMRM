package com.gcmrm.controllers.views;

import java.awt.Desktop;
import java.net.URI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

/**
 * Modal window "about" view controller
 */
public class AboutController {
	@FXML
	Hyperlink linkToImageJWebSite;
	
	/**
	 * Launch ImageJ web site (link defined in view file about.fxml)
	 * @param event Action event, hyperlink is clicked
	 */
	public void LaunchImageJWebSite(ActionEvent event) {
		try {
			 
		     Desktop.getDesktop().browse(new URI("https://imagej.net/Welcome"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
