package com.gcmrm.controllers.views;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.gcmrm.App;
import com.gcmrm.controllers.Controller;
import com.gcmrm.models.Algo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 * Modal window "Fast mode configuration" view controller
 */
public class FastModeConfigController {
    /**
     * Value of param for int type.
     */
    public static final String paramInt = "int";

    /**
     * Value of param for float/double type.
     */
    public static final String paramFloat = "float";

    /**
     * Value of param for bool type.
     */
    public static final String paramBool = "bool";

    /**
     * Value of param for string type.
     */
    public static final String paramString = "string";

    /**
     * Value of param for color type.
     */
    public static final String paramColor = "color";
    
    private App app;

    @FXML
    private ComboBox<Algo> selectAlgo;
    
    @FXML
    private GridPane parametersPanel;
    
    /**
     * Set app field, and fill the algo combobox.
     * @param newApp the app.
     * @throws IOException Java exception
     */
    public void setApp(App newApp) throws IOException {
        app = newApp;
        ObservableList<Algo> algos = FXCollections.observableArrayList(app.getAlgos());
        selectAlgo.setItems(algos);
        if(app.getSettings().getSelectedAlgo() != null) {
            selectAlgo.getSelectionModel().select(app.getSettings().getSelectedAlgo());
        } else {
            selectAlgo.getSelectionModel().selectFirst();
        }
        reloadForm();
    }
    
    /**
     * Change the algo in the combobox.
     */
    public void changeAlgo() {
        try {
            app.getSettings().setupSelectedAlgo(selectAlgo.getSelectionModel().getSelectedItem());
            reloadForm();
        } catch (Exception ex) {
            Controller.exceptionModal(app, ex, "L'algo ne fonctionne pas correctement");
            parametersPanel.getChildren().clear();
                    parametersPanel.getRowConstraints().clear();
        }
    }
    
    /**
     * Change the parameter i to the value j.
     * @param index the index of the parameter.
     * @param value the value of the parameter.
     */
    public void paramChanged(int index, String value) {
        app.getSettings().getSelectedAlgoParams().get(index).setValue(value);
    }
    
    /**
     * Color to string
     * @param color color to stringify.
     */
    private static String toHexString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("#%08X", (r + g + b + a));
    }
    
    /**
     * Reload the parameter form after algo changed.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    private void reloadForm() throws FileNotFoundException, IOException {

        // Clean the panel.
        parametersPanel.getChildren().clear();
        parametersPanel.getRowConstraints().clear();
        
        for(int i = 0; i < app.getSettings().getSelectedAlgoParams().size(); i++) {
            // Add a row in the panel.
            parametersPanel.getRowConstraints().add(new RowConstraints(50));

            // Add the label in the panel
            Label label = new Label(app.getSettings().getSelectedAlgoParams().get(i).getName() + " :");
            parametersPanel.setRowIndex(label, i);
            parametersPanel.setColumnIndex(label, 0);
            parametersPanel.getChildren().add(label);

            // Create the input using the parameter type.
            Node input = null;
            switch(app.getSettings().getSelectedAlgoParams().get(i).getType()) {
            	
                case paramInt:
                    // Create a spinner of integer.
                    input = new Spinner<Integer>();

                    // UserData used to save the parameter id.
                    input.setUserData(i);

                    // Pointer to use in the anonymous class.
                    Node that1 = input;
                    
                    // Set value and step.
                    SpinnerValueFactory<Integer> valueFactory = 
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.parseInt(app.getSettings().getSelectedAlgoParams().get(i).getValue()), 1);
                    ((Spinner<Integer>)input).setValueFactory(valueFactory);
                    
                    // When spinner change value.
                    ((Spinner<Integer>)input).valueProperty().addListener(new ChangeListener<Integer>() {
                        @Override
                        public void changed(ObservableValue<? extends Integer> observable,
                            Integer oldValue, Integer newValue) {
                            paramChanged((Integer)that1.getUserData(), "" + newValue);
                        }
                    });
                    break;
                case paramFloat:
                    // Create a spinner of float.
                    input = new Spinner<Double>();

                    // UserData used to save the parameter id.
                    input.setUserData(i);

                    // Pointer to use in the anonymous class.
                    Node that2 = input;
                    
                    // Set value and step.
                    SpinnerValueFactory<Double> doubleValueFactory = 
                    new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE, Double.parseDouble(app.getSettings().getSelectedAlgoParams().get(i).getValue()), 0.01);
 
                    ((Spinner<Double>)input).setValueFactory(doubleValueFactory);
                    
                    // When spinner change value.
                    ((Spinner<Double>)input).valueProperty().addListener(new ChangeListener<Double>() {
                        @Override
                        public void changed(ObservableValue<? extends Double> observable,//
                            Double oldValue, Double newValue) {
                            paramChanged((Integer)that2.getUserData(), "" + newValue);
                        }
                    });
                    break;
                case paramBool:
                    // Create a checkbox
                    input = new CheckBox();

                    // UserData used to save the parameter id.
                    input.setUserData(i);

                    // Pointer to use in the anonymous class.
                    Node that3 = input;
                    
                    // Set value.
                    ((CheckBox)input).setSelected(Boolean.parseBoolean(app.getSettings().getSelectedAlgoParams().get(i).getValue()));
                    
                    // When checkbox change value.
                    ((CheckBox)input).setOnAction((event) -> {
                        paramChanged((Integer)that3.getUserData(), "" + ((CheckBox)that3).isSelected());
                    });
                    break;
                case paramString:
                    // Create a textfield.
                    input = new TextField();

                    // UserData used to save the parameter id.
                    input.setUserData(i);

                    // Pointer to use in the anonymous class.
                    Node that4 = input;
                    
                    // Set value.
                    ((TextField)input).setText(app.getSettings().getSelectedAlgoParams().get(i).getValue());
                    
                    // When text input change value.
                    ((TextField)input).textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable,//
                            String oldValue, String newValue) {
                            paramChanged((Integer)that4.getUserData(), "" + newValue);
                        }
                    });
                    break;
                case paramColor:
                    // Create a color input.
                    input = new ColorPicker();

                    // UserData used to save the parameter id.
                    input.setUserData(i);

                    // Pointer to use in the anonymous class.
                    Node that5 = input;
                    
                    // Set value.
                    ((ColorPicker)input).setValue(Color.web(app.getSettings().getSelectedAlgoParams().get(i).getValue()));
                    
                    // When color picker change value.
                    ((ColorPicker)input).setOnAction((event) -> {
                        paramChanged((Integer)that5.getUserData(), toHexString(((ColorPicker)that5).getValue()));
                    });
                    break;
                default:
                    // Create a label.
                    input = new Label();

                    // UserData used to save the parameter id.
                    input.setUserData(i);
                    
                    // Set value.
                    ((Label)input).setText(app.getSettings().getSelectedAlgoParams().get(i).getValue() + "/(invalid type)");
            }

            // Add the input.
            parametersPanel.setRowIndex(input, i);
            parametersPanel.setColumnIndex(input, 1);
            parametersPanel.getChildren().add(input);
        }
    }
}
