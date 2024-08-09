package com.gcmrm.controllers.views;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.junit.Test;
import static org.junit.Assert.*;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.gcmrm.App;

/**
 * Test for the controller for fast mode config page interface.
 */
public class FastModeConfigControllerTest extends ApplicationTest {
    /**
     * String fast mode config view fxml.
     */
    public static final String mainView = "views/FastModeConfig.fxml";

    private Object controller;

    private Node rootPanel;

    /**
     * Start the pseudo-app to test the view components.
     */
    @Override
    public void start(Stage stage) {
        // Creating a fxml loader for loading the view.
        FXMLLoader loader = new FXMLLoader();

        // Set the location of the fxml file.
        loader.setLocation(App.class.getResource(mainView));

        // Load the view.
        try {
            rootPanel = (Node) loader.load();
        } catch (IOException e) {
            
            rootPanel = null;
        }

        // Fetch the controller.
        controller = loader.getController();
    }

    /**
     * Test of the fast mode config view loading (panel).
     */
    @Test public void testLoadViewRootPanel() {
        // Test if the root container of the gui have been loaded.
        assertTrue("root panel must be a StackPane", rootPanel instanceof AnchorPane);
    }

    /**
     * Test of the fast mode config view loading (controller).
     */
    @Test public void testLoadViewController() {
        // Test if the controller of the gui have been loaded.
        assertTrue("controller must be a HomeController", controller instanceof FastModeConfigController);
    }
    
    /**
     * test if it finds picture in a folder
     */
    @Test public void testGetAllPhotoFile() {
    	FastModeController fmc = new FastModeController();
    	File f = new File("./src/test/resources/com/gcmrm/pictures");
    	ArrayList<File> af = new ArrayList<File>();
    	af.add(f);
    	assertSame("should find 2 picture",2,fmc.getAllPhotoFile(af).size()); 
    }
    
}