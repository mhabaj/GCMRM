package com.gcmrm.controllers.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.gcmrm.App;
import com.gcmrm.controllers.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Home page view controller.
 */
public class HomeController extends Controller {
	/**
	 * String config view fxml.
	 */
	public static final String ABOUT_VIEW_PATH = "views/About.fxml";

	/**
	 * Path used to open user manual while using an Eclipse project
	 */
	public static final String DOC_PATH = "views/UserManual.pdf";

	/**
	 * Path To open manual from the generated Jar
	 */
	public static final String ALTERNATIVE_DOC_PATH_FOR_JAR = "./UserManual.pdf";

	/**
	 * Change the view to the algobrowser.
	 */
	public void goAlgoBrowser() {
		getApp().setView(2);
	}

	/**
	 * Change the view to the fastmode view.
	 */
	public void goFastMode() {
		getApp().setView(1);
	}

	/**
	 * Change the view to the fastmode view.
	 */
	public void goDoc() {
		// Following commented code dosen't work beacuse pdf is too large. Check
		// controllers.views.PdfController
		// Api note for futher info.

		// getApp().setView(6);
		// ((PdfController)getApp().getController(6)).setTitle("Guide Utilisateur");
		// ((PdfController)getApp().getController(6)).setPdf(DOC_PATH);

		try {
			Desktop.getDesktop().open(new File("src/main/resources/com/gcmrm/" + DOC_PATH));

		} catch (Exception e) {
			// When project if launched via jar, it will use the aleternative path (can't
			// open resource from inside jar in outside programs)
			try {
				Desktop.getDesktop().open(new File(ALTERNATIVE_DOC_PATH_FOR_JAR));
			} catch (Exception e1) {
				System.out.println("Tried to open UserManual from " + DOC_PATH + "and " + ALTERNATIVE_DOC_PATH_FOR_JAR
						+ "but failed. Check HomeController.goDoc() for more info. ");
			}
		}
	}

	/**
	 * Change the view to the fastmode view.
	 */
	public void goAbout() {
		AnchorPane rootPanel = null;
		AboutController controller;

		// Creating a fxml loader for loading the view.
		FXMLLoader loader = new FXMLLoader();

		// Set the location of the fxml file.
		loader.setLocation(App.class.getResource(ABOUT_VIEW_PATH));

		// Load the view.
		try {
			rootPanel = (AnchorPane) loader.load();

			// Fetch the controller.
			controller = loader.getController();

			Scene scene = new Scene(rootPanel);

			Stage dialog = new Stage();
			dialog.setTitle("A propos...");
			dialog.getIcons().add(new javafx.scene.image.Image(App.class.getResource("images/cell11.png").toString()));
			dialog.initOwner(getApp().getWindow());
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.showAndWait();

		} catch (IOException e) {

		}
	}

	/**
	 * Start methode of controller class, not used here.
	 */
	@Override
	public void start() {
	}
}