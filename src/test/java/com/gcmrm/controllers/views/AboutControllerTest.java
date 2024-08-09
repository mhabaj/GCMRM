package com.gcmrm.controllers.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.junit.Test;
import static org.junit.Assert.*;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import com.gcmrm.App;

/**
 * Test for the controller for about page interface.
 */
public class AboutControllerTest extends ApplicationTest {
    /**
     * String about view fxml.
     */
    public static final String mainView = "views/About.fxml";

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
     * Test of the about view loading (panel).
     */
    @Test public void testLoadViewRootPanel() {
        // Test if the root container of the gui have been loaded.
        assertTrue("root panel must be a StackPane", rootPanel instanceof AnchorPane);
    }

    /**
     * Test of the about view loading (controller).
     */
    @Test public void testLoadViewController() {
        // Test if the controller of the gui have been loaded.
        assertTrue("controller must be a HomeController", controller instanceof AboutController);
    }
}