package com.gcmrm.controllers.views;

import com.gcmrm.controllers.AlgoSettings;
import com.gcmrm.controllers.Controller;
import com.gcmrm.models.Algo;
import com.gcmrm.models.Parameter;
import com.gcmrm.models.Statistique;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Algorithms details view controller
 */
public class AlgoDetailsController extends Controller {
    private Algo algo;
    
    @FXML
    private Label algoDesc;
    
    @FXML
    private TableView paramList;
    
    @FXML
    private TableColumn<Parameter, String> paramListNames;
        
    @FXML
    private TableColumn<Parameter, String> paramListTypes;
            
    @FXML
    private TableColumn<Parameter, String> paramListDefaults;
                
    @FXML
    private TableColumn<Parameter, String> paramListDesc;
    
    @FXML
    private Label labelPrecondition;
    
    @FXML
    private Label labelPostcondition;
    
   
    /**
     * @param selectedAlgo Algo
     */
    public void setAlgo(Algo selectedAlgo) {
        ObservableList<Parameter> oList = FXCollections.observableArrayList(new ArrayList<Parameter>());
        try {
            algo = selectedAlgo;
            Statistique tempStats=  getApp().getAlgoStats(algo.getName());
            algoDesc.setText(algo.getDescAlgo() + "\n\n" + 
            		"Taux de rappel : "+getApp().getAlgoStats(algo.getName()).getRecall_score()+"\n"+
            		"Taux de précision : "+getApp().getAlgoStats(algo.getName()).getPrecision_score()
            );
            oList = FXCollections.observableArrayList(AlgoSettings.parseAlgoJsonToParams(algo.getDescParam()));
        } catch(Exception e) {
            Controller.exceptionModal(getApp(), e, "L'algo ne fonctionne pas correctement");
        }

        paramList.setItems(oList);

        paramListNames.setCellValueFactory(new PropertyValueFactory<>("name"));
        paramListTypes.setCellValueFactory(new PropertyValueFactory<>("type"));
        paramListDefaults.setCellValueFactory(new PropertyValueFactory<>("value"));
        paramListDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        labelPrecondition.setText(selectedAlgo.getPrecondition());
        labelPostcondition.setText(selectedAlgo.getPostcondition());
    }
    
    /**
     * Start methode of controller class, not used here.
     */
    @Override
    public void start() {
    }
    
    /**
     * Return to the algobrowser view.
     */
    public void goBack() {
        getApp().setView(2);
    }

}
