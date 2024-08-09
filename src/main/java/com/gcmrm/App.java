package com.gcmrm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gcmrm.controllers.AlgoLoader;
import com.gcmrm.controllers.AlgoSettings;
import com.gcmrm.controllers.Controller;
import com.gcmrm.models.Algo;
import com.gcmrm.models.Campaign;
import com.gcmrm.models.Image;
import com.gcmrm.models.Statistique;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



/**
 * Singleton. Central app class.
 *
 */
public class App extends Application implements EventHandler<WindowEvent> {
	/**
	 * int numbers of view inside the list.
	 */
	private static final int viewsNumbers = 7;

	/**
	 * String views fxml.
	 */
	public static final String[] VIEWS_PATHS = { "views/Home.fxml", "views/FastMode.fxml", "views/AlgoBrowser.fxml",
			"views/AlgoDetails.fxml", "views/ImagesBrowser.fxml", "views/ImageCorrection.fxml",  "views/Pdf.fxml"};

	/**
	 * String path to algo jar plugin files folder.
	 */

	public static final String PATH_TO_JARS = "./plugins/";

	private List<File> fileList;

	private ArrayList<Image> images;
	private ArrayList<Campaign> campaigns;
	private ArrayList<Algo> algos;
	private ArrayList<Statistique> stats;

	private AlgoSettings settings;
	private Controller[] controllers;
	private StackPane[] views;
	private Stage window;

	/**
	 * Constructor
	 */
	public App() {

		// Create the array objects.
		images = new ArrayList<Image>();
		campaigns = new ArrayList<Campaign>();
		algos = new ArrayList<Algo>();
		stats = new ArrayList<Statistique>();
		fileList = new ArrayList<File>();
		controllers = new Controller[viewsNumbers];
		views = new StackPane[viewsNumbers];

		// Create the algo setting object.
		settings = new AlgoSettings();
	}

	/**
	 * Checks if file URL is in file list.
	 * 
	 * @param url path to the image
	 * @return returns true if file exists, else false.
	 */
	public Image getImageFromUrl(String url) {
		if (images != null && !images.isEmpty()) {
			for (Image currentImage : images) {
				if (currentImage.getUrl().equals(url)) {
					return currentImage;
				}
			}
		}
		return null;
	}

	/**
	 * Main method (launch the app).
	 * 
	 * @param args app arguments.
	 */
	public static void main(String[] args) {

		launch(args);

	}

	/**
	 * @return image of the app
	 */
	public ArrayList<Image> getImages() {
		return images;
	}

	/**
	 * set current images of the app
	 * @param newImages list of images to set
	 */
	public void setImages(ArrayList<Image> newImages) {
		images = newImages;
	}

	/**
	 * not useded yet
	 * @return campaign of app
	 */
	public ArrayList<Campaign> getCampaigns() {
		return campaigns;
	}

	/**
	 * not used yet
	 * @param newCampaigns of app
	 */
	public void setCampaigns(ArrayList<Campaign> newCampaigns) {
		campaigns = newCampaigns;
	}

	/**
	 * get all the algorithms
	 * @return all algo available
	 */
	public ArrayList<Algo> getAlgos() {
		return algos;
	}

	/**
	 * set algorithms of the app
	 * @param newAlgos  of app
	 */
	public void setAlgos(ArrayList<Algo> newAlgos) {
		algos = newAlgos;
	}

	/**
	 * @return current algo settings
	 */
	public AlgoSettings getSettings() {
		return settings;
	}
	
	/**
	 * Change the algosettings attribute
	 * @param settings AlgoSettings
	 */
	public void setSettings(AlgoSettings settings) {
		this.settings=settings;
	}

	/**
	 * @return the window (of type stage)
	 */
	public Stage getWindow() {
		return window;
	}

	/**
	 * @param index index of the controller wanted
	 * @return the controller of the index-th view.
	 */
	public Controller getController(int index) {
		return controllers[index];
	}

	/**
	 * @param view viewIndexNumber
	 */
	public void setView(int view) {
		if (view >= 0 && view < viewsNumbers) {
			window.getScene().setRoot(views[view]);

		}
	}

	/**
	 * Quit app and calls the method saveAlgo 
	 */
	public void quit() {
		for (Algo currentAlgo : algos) {
			getAlgoStats(currentAlgo.getName()).saveStats(currentAlgo.getName());
		}
		try {
			settings.saveAlgo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Platform.exit();
		System.exit(0);

	}

	/**
	 * Start method of the application.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Load algos.
		algos = AlgoLoader.getAlgoList(PATH_TO_JARS);

		if (algos.size() > 0) {
			if(settings.saveNameExists() && settings.saveParamsExists())
			{
				for(int i = 0; i < algos.size();i++)
					if(algos.get(i).getName().equals(settings.getAlgoName())) {
						settings.setupSelectedAlgo(algos.get(i));
						break;
					}
				
				//in case the json file dosen't corresponds to any algo (after a munal suppression of the jar)
				if (settings.getSelectedAlgo()==null)
					settings.setupSelectedAlgo(algos.get(0));
			}
			else
				settings.setupSelectedAlgo(algos.get(0));
		}

		for (int i = 0; i < VIEWS_PATHS.length; i++) {
			// Creating a fxml loader for loading the view.
			FXMLLoader loader = new FXMLLoader();

			// Set the location of the fxml file.
			loader.setLocation(App.class.getResource(VIEWS_PATHS[i]));

			// Load the view.
			try {
				views[i] = (StackPane) loader.load();

				// Fetch the controller.
				controllers[i] = loader.getController();
				controllers[i].setApp(this);
				controllers[i].start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (Algo currentAlgo : algos) {
			stats.add(new Statistique(currentAlgo.getName()));
		}

		// Set the view home as default view.
		Scene scene = new Scene(views[0]);

		// Setup the window.
		window = primaryStage;
		window.setTitle("GCMRM");
		window.getIcons().add(new javafx.scene.image.Image(App.class.getResource("images/cell11.png").toString()));
		window.setScene(scene);
		window.setResizable(true);
		window.toFront();
		window.setOnCloseRequest(this);

		// Show the window.
		window.show();
	}

	/**
	 * get Statistics corresponding to Algo Name
	 * 
	 * @param name String
	 * @return Statistique algo stats
	 */
	public Statistique getAlgoStats(String name) {
		for (int i = 0; i < stats.size(); i++) {
			if (algos.get(i).getName().equals(name)) {
				return stats.get(i);
			}
		}

		return null;
	}

	/**
	 * Method launch on close window event (quit the app).
	 * 
	 * @param t event
	 */
	@Override
	public void handle(WindowEvent t) {
		quit();
	}

	/**
	 * @return the fileList
	 */
	public List<File> getFileList() {

		return fileList;
	}

	/**
	 * @param fileList the fileList to set
	 */
	public void setFileList(List<File> fileList) {
		this.fileList = fileList;

	}

	/**
	 * @return the stats
	 */
	public ArrayList<Statistique> getStats() {
		return stats;
	}

	/**
	 * @param stats the stats to set
	 */
	public void setStats(ArrayList<Statistique> stats) {
		this.stats = stats;
	}

}
