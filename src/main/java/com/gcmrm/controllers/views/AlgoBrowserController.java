package com.gcmrm.controllers.views;

import com.gcmrm.App;
import com.gcmrm.controllers.AlgoLoader;
import com.gcmrm.controllers.AlgoSettings;
import com.gcmrm.controllers.Controller;
import com.gcmrm.models.Algo;
import com.gcmrm.models.Statistique;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Algorithms browser view controller.
 */
public class AlgoBrowserController extends Controller{
    @FXML
    private Label nbAlgoLabel;
    
    @FXML
    private TableView algoList;
    
    @FXML
    private TableColumn<Algo, String> algoListNames;
    
    @FXML
    private TableColumn<Algo, String> algoListObjects;
    
    @FXML
    private TableColumn<Algo, String> algoListDesc;
    
    @FXML Button refresh_button;
    
    /**
     * Start methode of controller class, link model to view.
     */
    @Override
    public void start() {
        nbAlgoLabel.setText(getApp().getAlgos().size() + " algos");
        
        ObservableList<Algo> oList = FXCollections.observableArrayList(getApp().getAlgos());
        
        algoList.setItems(oList);
        
        algoListNames.setCellValueFactory(new PropertyValueFactory<>("name"));
        algoListObjects.setCellValueFactory(new PropertyValueFactory<>("countedObject"));
        algoListDesc.setCellValueFactory(new PropertyValueFactory<>("descAlgo"));
    }
    
    /**
     * Open the algo plugin folder.
     */
    public void openFolder() {
        Desktop d = Desktop.getDesktop();
        try {
            d.open(new File(App.PATH_TO_JARS));
        } catch (IOException ex) {
        }
    }

    /**
     * Change the view to the algo plugin view.
     * @param event mouse event
     */
    public void algoDetails(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() >= 2) {
                try {
                    // Set the algo of the algo details view, fetched from app.
                    ((AlgoDetailsController)getApp().getController(3)).setAlgo((Algo)algoList.getSelectionModel().getSelectedItem());
                } catch (Exception ex) {
                }
                getApp().setView(3);
            }
        }
    }
    
    /**
     * refreshes Algos from plugin folder
     * @param event ActionEvent
     */
    public void refreshAlgos(ActionEvent event) {
    	//we start by saving previous algos's stats
    	for (Algo currentAlgo : getApp().getAlgos()) {
			getApp().getAlgoStats(currentAlgo.getName()).saveStats(currentAlgo.getName());
		}
    	
    	//we do not save the settings because they may be linked to a deleted algo
    	
    	// Load algos and settings
		ArrayList<Algo> algos = AlgoLoader.getAlgoList(App.PATH_TO_JARS);
		AlgoSettings settings = new AlgoSettings();
		ArrayList<Statistique> stats= new ArrayList<Statistique>();
		
		try {
		if (algos.size() > 0) {
			if(settings.saveNameExists() && settings.saveParamsExists())
			{
				for(int i = 0; i < algos.size();i++)
					if(algos.get(i).getName().equals(settings.getAlgoName())) {
						settings.setupSelectedAlgo(algos.get(i));
						break;
					}
				
				//in case the json file dosen't corresponds to any algo (after a manual suppression of the jar)
				if (settings.getSelectedAlgo()==null)
					settings.setupSelectedAlgo(algos.get(0));			
			}
			else
				settings.setupSelectedAlgo(algos.get(0));
		}
		
		//reload all the stats stats
		for (Algo currentAlgo : algos) {
			stats.add(new Statistique(currentAlgo.getName()));
		}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		getApp().setAlgos(algos);
		getApp().setSettings(settings);
		getApp().setStats(stats);
		
		getApp().getController(1).start();//to reload current algo in fastmode view
		this.start();
    }
}
