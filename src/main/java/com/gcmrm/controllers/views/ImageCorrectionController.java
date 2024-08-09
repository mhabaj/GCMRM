package com.gcmrm.controllers.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.time.LocalDateTime;


import com.gcmrm.App;
import com.gcmrm.controllers.Controller;
import com.gcmrm.controllers.ImageHTMLExport;
import com.gcmrm.models.Image;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * Image correction view controller
 */
public class ImageCorrectionController extends Controller {
	/**
	 * Path to css
	 */
	public static final String HTML_STYLE = "styles/htmlImageCorrection.css";

	/**
	 * Path to js
	 */
	public static final String HTML_SCRIPT = "scripts/htmlImageCorrection.js";

	private Image image;

	/**
	 * URL Label
	 */
	@FXML
	private Label url;

	/**
	 * Count Label
	 */
	@FXML
	private Label count;

	/**
	 * View
	 */
	@FXML
	private WebView view;

	/**
	 * zoom slidebar
	 */
	@FXML
	private Slider zoom;
	
	private boolean hide_cross;

	/**
	 * Start methode of controller class, not used here.
	 */
	@Override
	public void start() {
		hide_cross=false;
	}

	void setImage(Image image) {
		try {
			this.image = image;

			url.setText(image.getUrl());
			count.setText("" + image.getMeasurements().get(0).getPointsCount());

			view.setContextMenuEnabled(false);

			WebEngine we = view.getEngine();

			ImageHTMLExport ihtmle = new ImageHTMLExport();

			try {
				image.accept(ihtmle);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			we.setOnAlert(event -> showAlert(event.getData()));

			we.setOnError(event -> showAlert(event.getMessage()));

			we.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
				// When the page is loaded.
				if (newValue == Worker.State.SUCCEEDED) {
					// Get the window js object.
					JSObject window = (JSObject) we.executeScript("window");

					// Add 'this' in the window object.
					window.setMember("controller", this);
				}
			});

			String html = ihtmle.getResult();
			// the stream holding the file content
			InputStream is1 = App.class.getResourceAsStream(HTML_STYLE);
			InputStream is2 = App.class.getResourceAsStream(HTML_SCRIPT);

			// creating an InputStreamReader object
			InputStreamReader is1Reader = new InputStreamReader(is1);
			// Creating a BufferedReader object
			BufferedReader reader1 = new BufferedReader(is1Reader);
			StringBuffer sb1 = new StringBuffer();
			String str1;
			while ((str1 = reader1.readLine()) != null) {
				sb1.append(str1);
			}

			// creating an InputStreamReader object
			InputStreamReader is2Reader = new InputStreamReader(is2);
			// Creating a BufferedReader object
			BufferedReader reader2 = new BufferedReader(is2Reader);
			StringBuffer sb2 = new StringBuffer();
			String str2;
			while ((str2 = reader2.readLine()) != null) {
				sb2.append(str2);
			}

			html = html.replace("{%CSS%}", sb1.toString());
			html = html.replace("{%JS%}", sb2.toString());

			we.loadContent(html);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Edit the point quantity, remove the point if quantity = 0.
	 * 
	 * @param id int representing the point
	 * @param newQuantity int corresponding to the knew quantity
	 */
	public void editPoint(int id, int newQuantity) {

		if (newQuantity == 0) {
			//update the view
			image.getMeasurements().get(0).removePoint(id);
			count.setText(String.valueOf(image.getMeasurements().get(0).getPointsCount()));
			

			//update the stats
			getApp().getAlgoStats(getApp().getSettings().getSelectedAlgo().getName()).retraitPoint(); //update stats of the algo
		} else {
			//to check if the edit add or delete a point
			if(image.getMeasurements().get(0).getPoint(id).getQuantity()>newQuantity)
				getApp().getAlgoStats(getApp().getSettings().getSelectedAlgo().getName()).retraitPoint();
			else
				getApp().getAlgoStats(getApp().getSettings().getSelectedAlgo().getName()).ajoutPoint();
			
			//update the view
			image.getMeasurements().get(0).editPoint(id, newQuantity);
			count.setText(String.valueOf(image.getMeasurements().get(0).getPointsCount()));
		}

	}

	/**
	 * Create a new point at x, y with the id id.
	 * 
	 * @param x x of the knew point
	 * @param y y of the knew point
	 * @param id new id of the new point
	 */
	public void createPoint(int id, int x, int y) {
		image.getMeasurements().get(0).addPoint(x, y);
		count.setText(String.valueOf(image.getMeasurements().get(0).getPointsCount()));

		getApp().getAlgoStats(getApp().getSettings().getSelectedAlgo().getName()).ajoutPoint();

	}

	/**
	 * Return to the imagebrowser view.
	 */
	public void goBack() {

		//set image status to corrected:
		image.getMeasurements().get(0).setCorrected(true);
		image.getMeasurements().get(0).setLastModificationDate(LocalDateTime.now().toString());
		
		getApp().getController(4).reload();
		getApp().setView(4);

	}

	/**
	 * Change zoom value depending on the sidebar attribute
	 */
	public void zoom() {
		WebEngine we = view.getEngine();
		we.executeScript("zoom(" + zoom.getValue() + ")");
	}

	/**
	 * Zoom with CTRL + mouse wheel
	 * 
	 * @param event ScrollEvent
	 */
	public void wheelZoom(ScrollEvent event) {
		if (event.isControlDown()) {
			zoom.setValue(zoom.getValue() + event.getDeltaY());
			zoom();
			event.consume();
		}
	}

	/**
	 * Show test messages
	 * 
	 * @param message content of the alert
	 */
	private void showAlert(String message) {
		Dialog<Void> alert = new Dialog<>();
		alert.getDialogPane().setContentText(message);
		alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		alert.showAndWait();
	}
	
	/**
	 * hide the crosses if key H is pressed
	 * @param event KeyEvent
	 */
	public void hide(KeyEvent event) {
		if(event.getCode() == KeyCode.H) {
			hide_cross=!hide_cross;
			WebEngine we = view.getEngine();
			we.executeScript("hide(" + hide_cross + ")");
		}
	}
}
