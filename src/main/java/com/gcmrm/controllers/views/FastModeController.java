package com.gcmrm.controllers.views;

import com.gcmrm.App;
import com.gcmrm.controllers.Controller;
import com.gcmrm.controllers.ImageLoader;
import com.gcmrm.models.Algo;
import com.gcmrm.models.Image;
import com.gcmrm.models.Measurement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.event.ActionEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

/**
 * Fast mode view controller
 */
public class FastModeController extends Controller {

	private Algo algo;

	private String paramAlgo;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private AnchorPane readFilePane;

	@FXML
	private AnchorPane progressPane;

	@FXML
	private Label progressLabel;
	
	@FXML 
	private Label label_current_algo;

	// so the algo run in background
	private Service<Void> calculateService;

	/**
	 * String config view fxml.
	 */
	public static final String CONFIG_VIEW_PATH = "views/FastModeConfig.fxml";

	/**
	 * set the algo to be used.
	 * 
	 * @param algo algo to be used
	 */
	public void setAlgo(Algo algo) {
		this.algo = algo;
	}

	/**
	 * To know which algo is used
	 * 
	 * @return position of the algo in the Algo ArrayList given by AlgoLoader
	 */
	public Algo getAlgo() {
		return algo;

	}

	/**
	 * Set the param that will be used with the algo
	 * 
	 * @param param in Json syntax
	 */
	public void setParamAlgo(String param) {
		paramAlgo = param;
	}

	/**
	 * 
	 * @return The param the algo will use
	 */
	public String getParamAlgo() {
		return paramAlgo;
	}

	/**
	 * Start methode of controller class, not used here.
	 */
	@Override
	public void start() {

		final Gson gson = new GsonBuilder().create();
		algo = getApp().getSettings().getSelectedAlgo();
		paramAlgo = gson.toJson(getApp().getSettings().getSelectedAlgoParams());
		
		if(algo!=null)
			label_current_algo.setText("Algo courant : "+algo.getName());
		else
			label_current_algo.setText("Algo courant : aucun");

	}

	/**
	 * Drag over interface action Handler
	 * 
	 * @param event triggered when files are dragged over
	 */
	public void dragOverHandler(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	/**
	 * Return a list of photo path, given a list of file/directory
	 * 
	 * @param fileList list of file or directory
	 * @return path to all the photo from files, directory and sub directory
	 */
	public ArrayList<String> getAllPhotoFile(List<File> fileList) {

		ImageLoader imLoad = new ImageLoader();
		ArrayList<String> photoPathList = new ArrayList<String>();
		if (fileList != null) {
			for (int loopFileNumber = 0; loopFileNumber < fileList.size(); loopFileNumber++) {
				if (fileList.get(loopFileNumber).isDirectory() == true) {
					try {
						imLoad.loadImages(fileList.get(loopFileNumber).toString());
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					photoPathList.add(fileList.get(loopFileNumber).getAbsolutePath());
				}
			}
			ArrayList<String> urls = imLoad.getPathsList();
			for (String s : urls)
				photoPathList.add(s);
		}

		return photoPathList;
	}

	/**
	 * Drop files over interface action handler
	 * 
	 * @param event when files are dropped
	 */
	public void DropHandler(DragEvent event) {

		List<File> fileList = event.getDragboard().getFiles();

		ArrayList<String> photoPathList = getAllPhotoFile(fileList);
		getApp().setFileList(fileList);

		launchMeasures(photoPathList);

	}

	/**
	 * On click windows action handler
	 * 
	 * @param event click event
	 */
	public void onClick(ActionEvent event) {

		FileChooser imagesFileChooser = new FileChooser();
		imagesFileChooser.setTitle("Deposer les Fichier");
		imagesFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		imagesFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg", "*.jpg"),
				new FileChooser.ExtensionFilter("jpeg", "*.jpeg"), new FileChooser.ExtensionFilter("tiff", "*.tiff"),
				new FileChooser.ExtensionFilter("tif", "*.tif"),new FileChooser.ExtensionFilter("png", "*.png")

		);
		List<File> fileList = imagesFileChooser.showOpenMultipleDialog(getApp().getWindow());

		if (fileList != null && fileList.size() != 0) {

			ArrayList<String> photoPathList = getAllPhotoFile(fileList);
			getApp().setFileList(fileList);

			launchMeasures(photoPathList);
		}

	}

	/**
	 * Launch measure on the given photos
	 * 
	 * @param photoPathList each element is a path to a photo
	 */
	public void launchMeasures(ArrayList<String> photoPathList) {

		if (algo == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Erreur");
			alert.setHeaderText(null);
			alert.setContentText("Aucun algorithme n'est sélectionné.");
			alert.showAndWait();
			return;
		}
		readFilePane.setVisible(false);
		progressPane.setVisible(true);
		ArrayList<Image> imageList = new ArrayList<Image>();

		// to launch the algo on each photo in background
		calculateService = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						updateMessage("Traitement en cours... ");

						final Gson gson = new GsonBuilder().create();

						int nbPhoto = photoPathList.size();
						int progress = 0;

						for (String url : photoPathList) {
							if (isCancelled()) {
								break;
							}
							Image image = new Image();
							image.setUrl(url);
							image.setId(imageList.size());

							Measurement measure = gson.fromJson(algo.getMeasures(url, paramAlgo), Measurement.class);

							image.addMeasure(measure);

							imageList.add(image);

							progress++;

							updateMessage("Traitement en cours... " + progress + "/" + nbPhoto);
						}
						return null;
					}
				};
			}

		};
		progressLabel.textProperty().bind(calculateService.messageProperty());
		calculateService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue,
				Worker.State oldValue, Worker.State newValue) -> {
			switch (newValue) {
			case FAILED:
				readFilePane.setVisible(true);
				progressPane.setVisible(false);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Erreur");
				alert.setHeaderText(null);
				alert.setContentText("Le traitement des images par l'algorithme a échoué.");
				alert.showAndWait();
				break;
			case CANCELLED:
				readFilePane.setVisible(true);
				progressPane.setVisible(false);
				break;
			case SUCCEEDED:
				getApp().setImages(imageList);
				readFilePane.setVisible(true);
				progressPane.setVisible(false);
				//the measure is done we can update the stats of this algo
				getApp().getAlgoStats(algo.getName()).updateTotalCount(imageList);
				//we can now chnage the view
				getApp().getController(4).start();
				getApp().setView(4);
				break;
			default:
				break;
			}
		});

		calculateService.start();

	}

	/**
	 * Event linked with the cancel button
	 * 
	 * @param event cancel button triggered
	 */
	public void cancelProcessing(ActionEvent event) {
		calculateService.cancel();
	}

	/**
	 * Open parameters Window
	 * 
	 */
	public void openParams() {

		AnchorPane rootPanel = null;
		FastModeConfigController controller;

		// Creating a fxml loader for loading the view.
		FXMLLoader loader = new FXMLLoader();

		// Set the location of the fxml file.
		loader.setLocation(App.class.getResource(CONFIG_VIEW_PATH));

		// Load the view.
		try {
			rootPanel = (AnchorPane) loader.load();

			// Fetch the controller.
			controller = loader.getController();

			controller.setApp(getApp());

			Scene scene = new Scene(rootPanel);

			Stage dialog = new Stage();
			dialog.setTitle("Parametre de comptage");
			dialog.getIcons().add(new javafx.scene.image.Image(App.class.getResource("images/cell11.png").toString()));
			dialog.initOwner(getApp().getWindow());
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.showAndWait();

			final Gson gson = new GsonBuilder().create();

			algo = getApp().getSettings().getSelectedAlgo();
			paramAlgo = gson.toJson(getApp().getSettings().getSelectedAlgoParams());
			
			label_current_algo.setText("Algo courant : "+algo.getName());

		} catch (IOException e) {

		}

	}
}
