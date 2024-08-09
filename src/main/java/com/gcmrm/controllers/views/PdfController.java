package com.gcmrm.controllers.views;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import com.gcmrm.App;
import com.gcmrm.controllers.Controller;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Algorithms details view controller.
 * 
 * This class is currently not used due to problems encountered with
 *          the pdf opening / visualization in-program. <br> Check
 *          controllers.views.HomeController for further info about the
 *          alternative way in-use to open pdfs.
 * 
 */
public class PdfController extends Controller {

	/**
	 * Path to js
	 */
	public static final String HTML_DOC = "views/pdf.html";

	@FXML
	private Label title;

	@FXML
	private WebView view;

	/**
	 * Set the algo used with this view.
	 * 
	 * @param path indicate where the user guide is stored
	 */
	public void setPdf(String path) {
		try {
			String base64Pdf;

			Path pdfPath = Paths.get(App.class.getResource(path).toURI());

			WebEngine we = view.getEngine();

			we.setOnAlert(event -> showAlert(event.getData()));

			we.setOnError(event -> showAlert(event.getMessage()));

			// Change the path according to yours.
			String url = App.class.getResource("pdfjs/viewer.html").toExternalForm();
			// We add our stylesheet.
			we.setUserStyleSheetLocation(App.class.getResource("pdfjs/viewer.css").toExternalForm());
			we.setJavaScriptEnabled(true);
			we.load(url);

			// creating an BufferedInputStream object
			byte[] data = Files.readAllBytes(pdfPath);
			base64Pdf = Base64.getEncoder().encodeToString(data);

			we.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
				// When the page is loaded.
				if (newValue == Worker.State.SUCCEEDED) {
					// we.executeScript("alert('a')");
					we.executeScript("openFileFromBase64('" + base64Pdf + "')");
				}
			});
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * set the title of the pdf
	 * 
	 * @param titleString title of the pdf
	 */
	public void setTitle(String titleString) {
		title.setText(titleString);
	}

	/**
	 * Show test messages
	 * 
	 * @param message
	 */
	private void showAlert(String message) {
		Dialog<Void> alert = new Dialog<>();
		alert.getDialogPane().setContentText(message);
		alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		alert.showAndWait();
	}

	/**
	 * Start methode of controller class, not used here.
	 */
	@Override
	public void start() {

	}

}
