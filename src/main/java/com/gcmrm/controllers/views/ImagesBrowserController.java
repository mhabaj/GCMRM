package com.gcmrm.controllers.views;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.gcmrm.App;
import com.gcmrm.controllers.CampaignOdsExport;
import com.gcmrm.controllers.Controller;
import com.gcmrm.controllers.ImageHTMLExport;
import com.gcmrm.controllers.ImageSVGExport;
import com.gcmrm.models.Image;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 * Image browser view controller
 */
public class ImagesBrowserController extends Controller implements Initializable {
	/**
	 * Path to css
	 */
	public static final String HTML_STYLE = "styles/htmlImageBrowser.css";

	/**
	 * Path to js
	 */
	public static final String HTML_SCRIPT = "scripts/htmlImageBrowser.js";

	/**
	 * String icon folder.
	 */
	public static final String folderIcon = "images/folder.png";

	// to export images in background
	private Service<Void> calculateService;

	/**
	 * FXML TreeTableView File object
	 */
	@FXML
	private TreeTableView<File> currentTreeTableView;

	/**
	 * Image View
	 */
	@FXML
	private WebView view;

	/**
	 * Button to hide cross
	 */
	@FXML
	private ToggleButton hideButton;

	@FXML
	private Button button_export_ods;

	@FXML
	private Button button_export_jpg;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private Label progressLabel;

	/**
	 * Overriding initialize from Initializable.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		currentTreeTableView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TreeItem<File>>() {
					@Override
					public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue,
							TreeItem<File> newValue) {
						try {
							WebEngine we = view.getEngine();

							view.setContextMenuEnabled(false);

							Image i = getApp().getImageFromUrl(newValue.getValue().getAbsolutePath());

							if (i == null) {
								we.load(App.class.getResource(folderIcon).toString());
								hideButton.setDisable(true);
							} else {

								hideButton.setDisable(false);
								ImageHTMLExport ihtmle = new ImageHTMLExport();

								try {
									i.accept(ihtmle);
								} catch (Exception e) {
									// e.printStackTrace();
								}

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

								hideButton.setSelected(false);
							}
						} catch (Exception ex) {
							// ex.printStackTrace();
						}
					}
				});
	}

	/**
	 * Update TreeTableView with new files and folders which have been dragged into
	 * the drag and drop window.
	 */
	public void createFileBrowserTreeTableView() {

		ArrayList<File> fileRoots = getRoots(); // Get Roots from dragged files and folders
		TreeItem<File> root = new TreeItem<>(); // fake empty root TreeItem

		// reset the default visual tree columns.
		currentTreeTableView.getColumns().clear();
		currentTreeTableView.setShowRoot(false);
		currentTreeTableView.setRoot(root);

		// add real roots to the fake root
		for (File currentFile : fileRoots) {
			currentTreeTableView.getRoot().getChildren().add(new FileTreeItem(currentFile));

		}

		root.setExpanded(true);
		currentTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

		// Column 1:
		TreeTableColumn<File, FileTreeItem> nameColumn = new TreeTableColumn<>("Images");

		nameColumn.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<FileTreeItem>((FileTreeItem) cellData.getValue()));

		nameColumn.setCellFactory(column -> {
			TreeTableCell<File, FileTreeItem> cell = new TreeTableCell<File, FileTreeItem>() {

				@Override
				protected void updateItem(FileTreeItem item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty || item.getValue() == null) {
						setText(null);
						setGraphic(null);
						setStyle("");
					} else {
						File f = item.getValue();
						String text = f.getParentFile() == null ? File.separator : f.getName();
						setText(text);
						String style = item.isHidden() && f.getParentFile() != null ? "-fx-accent"
								: "-fx-text-base-color";
						setStyle("-fx-text-fill: " + style);
						if (item.isLeaf()) {
							setText(f.getName());
						} else {
							setText(f.getName());

						}
					}
				}
			};
			return cell;
		});

		nameColumn.setPrefWidth(300);
		nameColumn.setSortable(false);
		currentTreeTableView.getColumns().add(nameColumn);

		// Column 2:
		TreeTableColumn<File, String> sizeColumn = new TreeTableColumn<>("Objets comptés");

		sizeColumn.setCellValueFactory(cellData -> {
			FileTreeItem item = ((FileTreeItem) cellData.getValue());

			String stringCellsCount = item.isLeaf() ? item.getValue().getAbsolutePath() : null;
			if (stringCellsCount != null) {
				Image imgTmp = getApp().getImageFromUrl(stringCellsCount);

				if (imgTmp != null) {
					stringCellsCount = String.valueOf(imgTmp.getMeasurements().get(0).getPointsCount());
				}
			}
			return new ReadOnlyObjectWrapper<String>(stringCellsCount);
		});

		Callback<TreeTableColumn<File, String>, TreeTableCell<File, String>> sizeCellFactory = sizeColumn
				.getCellFactory();
		sizeColumn.setCellFactory(column -> {
			TreeTableCell<File, String> cell = sizeCellFactory.call(column);
			cell.setAlignment(Pos.CENTER_RIGHT);
			cell.setPadding(new Insets(0, 8, 0, 0));
			return cell;
		});

		sizeColumn.setPrefWidth(100);
		sizeColumn.setSortable(false);
		currentTreeTableView.getColumns().add(sizeColumn);

		currentTreeTableView.getSelectionModel().selectFirst();

	}

	/**
	 * @return File ArrayList Roots of the dropped/selected data fromApp.FileList.
	 */
	public ArrayList<File> getRoots() {
		List<File> files = getApp().getFileList();
		ArrayList<File> rootFiles = new ArrayList<File>();
		if (files != null && files.size() > 0) {
			rootFiles.add(files.get(0));
			for (File currentFile : files) {

				boolean contains = false;
				for (int rootFilesLoop = 0; rootFilesLoop < rootFiles.size() && contains == false; rootFilesLoop++) {
					if (currentFile.getAbsolutePath()
							.contains(rootFiles.get(rootFilesLoop).getAbsolutePath() + File.separator)) {
						contains = true;

					} else if (currentFile.getAbsolutePath()
							.contains(rootFiles.get(rootFilesLoop).getAbsolutePath() + File.separator)
							&& currentFile.getAbsolutePath().length() < rootFiles.get(rootFilesLoop).getAbsolutePath()
									.length()) {
						rootFiles.set(rootFilesLoop, currentFile);
					}

				}
				if (contains == false && !rootFiles.contains(currentFile)) {
					rootFiles.add(currentFile);
				}

			}
		}
		return rootFiles;
	}

	/**
	 * hide the crosses
	 */
	public void hide() {
		WebEngine we = view.getEngine();
		we.executeScript("hide(" + hideButton.isSelected() + ")");
	}

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Custom TreeItem class extends from TreeItem File to manage tree population
	 * and expansion. Inspired from:
	 * https://yumberc.github.io/FileBrowser/FileBrowser.html
	 * 
	 *
	 *
	 */
	private class FileTreeItem extends TreeItem<File> {
		private boolean expanded = false;
		private boolean directory;
		private boolean hidden;

		/**
		 * Class constructor
		 * 
		 * @param file
		 */
		FileTreeItem(File file) {
			super(file);
			EventHandler<TreeModificationEvent<File>> eventHandler = event -> changeExpand();
			addEventHandler(TreeItem.branchExpandedEvent(), eventHandler);
			addEventHandler(TreeItem.branchCollapsedEvent(), eventHandler);

			directory = ((File) getValue()).isDirectory();
			hidden = ((File) getValue()).isHidden();
		}

		/**
		 * Expand nodes and load children.
		 */
		private void changeExpand() {
			if (expanded != isExpanded()) {
				expanded = isExpanded();
				if (expanded) {
					createChildren();
				} else {
					getChildren().clear();
				}
				if (getChildren().size() == 0)
					Event.fireEvent(this,
							new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), this, getValue()));
			}
		}

		/**
		 * returns true if File is leaf in tree (=not directory)
		 */
		@Override
		public boolean isLeaf() {
			return !isDirectory();
		}

		/**
		 * @return true if File is a directory
		 */
		public boolean isDirectory() {
			return directory;
		}

		/**
		 * @return true if item is hidden in tree
		 */
		public boolean isHidden() {
			return hidden;
		}

		/**
		 * Scans Files to load as objects in tree.
		 */
		private void createChildren() {

			if (isDirectory() && getValue() != null) { //
				File[] files = ((File) getValue()).listFiles();

				if (files != null && files.length > 0) {
					getChildren().clear();

					for (File childFile : files) {
						// if entry is folder or a file which is inside the General Image List
						// (App.images list), then push into the tree
						if ((childFile.isDirectory())
								|| getApp().getImageFromUrl(childFile.getAbsolutePath()) != null) {
							getChildren().add(new FileTreeItem(childFile));
						}
					}
					// sort files by names
					getChildren().sort((ti1, ti2) -> {
						return ((FileTreeItem) ti1).isDirectory() == ((FileTreeItem) ti2).isDirectory()
								? ((File) ti1.getValue()).getName()
										.compareToIgnoreCase(((File) ti2.getValue()).getName())
								: ((FileTreeItem) ti1).isDirectory() ? -1 : 1;
					});
				}
			}
		}

	}
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Start methode of controller class, not used here.
	 */
	@Override
	public void start() {
		createFileBrowserTreeTableView();
	}

	/**
	 * reload the tableView after a correction (just count column)
	 */
	@Override
	public void reload() {

		// Column 2:
		currentTreeTableView.getColumns().remove(1); //remove the old count
		
		TreeTableColumn<File, String> sizeColumn = new TreeTableColumn<>("Objets comptés");

		sizeColumn.setCellValueFactory(cellData -> {
			FileTreeItem item = ((FileTreeItem) cellData.getValue());

			String stringCellsCount = item.isLeaf() ? item.getValue().getAbsolutePath() : null;
			if (stringCellsCount != null) {
				Image imgTmp = getApp().getImageFromUrl(stringCellsCount);

				if (imgTmp != null) {
					stringCellsCount = String.valueOf(imgTmp.getMeasurements().get(0).getPointsCount());
				}
			}
			return new ReadOnlyObjectWrapper<String>(stringCellsCount);
		});

		Callback<TreeTableColumn<File, String>, TreeTableCell<File, String>> sizeCellFactory = sizeColumn
				.getCellFactory();
		sizeColumn.setCellFactory(column -> {
			TreeTableCell<File, String> cell = sizeCellFactory.call(column);
			cell.setAlignment(Pos.CENTER_RIGHT);
			cell.setPadding(new Insets(0, 8, 0, 0));
			return cell;
		});

		sizeColumn.setPrefWidth(100);
		sizeColumn.setSortable(false);
		currentTreeTableView.getColumns().add(sizeColumn);
		
		//reload the image (like initialize)
		int row =currentTreeTableView.getSelectionModel().getSelectedIndex();
		try {
			WebEngine we = view.getEngine();

			view.setContextMenuEnabled(false);

			Image i = getApp().getImageFromUrl(currentTreeTableView.getTreeItem(row).getValue().getAbsolutePath());

			if (i == null) {
				we.load(App.class.getResource(folderIcon).toString());
				hideButton.setDisable(true);
			} else {

				hideButton.setDisable(false);
				ImageHTMLExport ihtmle = new ImageHTMLExport();

				try {
					i.accept(ihtmle);
				} catch (Exception e) {
					// e.printStackTrace();
				}

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

				hideButton.setSelected(false);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	/**
	 * Change the view to the image correction view.
	 * 
	 * @param event mouse event
	 */
	public void imageDetails(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() >= 2) {
				try {
					Image i = getApp().getImageFromUrl(
							currentTreeTableView.getSelectionModel().getSelectedItem().getValue().getAbsolutePath());
					if (i == null) {
					} else {
						((ImageCorrectionController) getApp().getController(5)).setImage(i);
						getApp().setView(5);
					}
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * Managed the export ods button (launch a filechooser.saveDialog)
	 * 
	 * @param event the button export ods is clicked
	 */
	public void onClick_export_ods(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Deposer les Fichier");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ods", "*.ods"));
		File file = fileChooser.showSaveDialog(getApp().getWindow());

		if (file != null) {
			progressIndicator.setVisible(true);
			CampaignOdsExport visitor = new CampaignOdsExport();
			try {
				visitor.visit(getApp().getImages(), file.getAbsolutePath());

				progressIndicator.setVisible(false);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Export ods");
				alert.setHeaderText("L'export est terminé !");
				alert.setContentText("Voulez vous ouvrir le fichier ?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					Desktop desktop = Desktop.getDesktop();
					desktop.open(file);
				} else {
					// ... user chose CANCEL or closed the dialog
				}
			} catch (IOException e) {
				progressIndicator.setVisible(false);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Export all the corrected images in svg format (asking in witch directory) not
	 * used in the view yet, replaced by jpg export
	 * 
	 * @param event the button export svg is clicked
	 */
	public void onClick_export_svg(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		File selectedDirectory = directoryChooser.showDialog(getApp().getWindow());

		if (selectedDirectory != null) {
			progressIndicator.setVisible(true);
			ImageSVGExport svgExport = new ImageSVGExport();

			for (Image image : getApp().getImages()) {

				String photoName = image.getUrl().substring(image.getUrl().lastIndexOf(File.separator),
						image.getUrl().lastIndexOf('.'));

				try {

					svgExport.visit(image);

					String svg = svgExport.getResult();

					FileWriter lu = new FileWriter(selectedDirectory.getAbsolutePath() + photoName + "_X.svg");
					BufferedWriter out = new BufferedWriter(lu);

					out.write(svg);
					out.close();
				} catch (Exception e) {
					progressIndicator.setVisible(false);
					e.printStackTrace();
				}

			}

			progressIndicator.setVisible(false);

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Export Images");
			alert.setHeaderText("L'export des images est terminé !");
			alert.setContentText("Voulez vous ouvrir le dossier ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				try {
					Desktop.getDesktop().open(selectedDirectory);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// ... user chose CANCEL or closed the dialog
			}
		}
	}

	/**
	 * Managed the export Images button (to download the pictures as jpg) Ask in
	 * witch folder the user wants to save, and if he wants to open this folder
	 * (after export)
	 * 
	 * The export is done in a parallel thread
	 * 
	 * @param event the button export Images is clicked
	 */
	public void onClick_export_jpg(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		File selectedDirectory = directoryChooser.showDialog(getApp().getWindow());

		if (selectedDirectory != null) {
			progressIndicator.setVisible(true);
			// to launch the algo on each photo in background
			calculateService = new Service<Void>() {

				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {

						@Override
						protected Void call() throws Exception {

							int nbPhoto = getApp().getImages().size();
							int progress = 0;
							updateMessage(progress + "/" + nbPhoto);

							for (Image image : getApp().getImages()) {

								String photoName = image.getUrl().substring(image.getUrl().lastIndexOf(File.separator),
										image.getUrl().lastIndexOf('.'));

								try {
									image.saveJPG(selectedDirectory.getAbsolutePath() + photoName + "_X.jpg");
								} catch (Exception e) {
									progressIndicator.setVisible(false);
									updateMessage("");
									e.printStackTrace();
								}

								progress++;

								updateMessage(progress + "/" + nbPhoto);

							}
							updateMessage("");
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
				case CANCELLED:
					progressIndicator.setVisible(false);
					// progressLabel.set("");
					break;
				case SUCCEEDED:
					progressIndicator.setVisible(false);

					// progressLabel.setText(null);;
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Export Images");
					alert.setHeaderText("L'export des images est terminé !");
					alert.setContentText("Voulez vous ouvrir le dossier ?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						try {
							Desktop.getDesktop().open(selectedDirectory);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						// ... user chose CANCEL or closed the dialog
					}
					break;
				default:
					break;
				}
			});

			calculateService.start();

		}
	}

}
