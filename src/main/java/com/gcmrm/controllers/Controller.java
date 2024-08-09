package com.gcmrm.controllers;

import com.gcmrm.App;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;

/**
 * Abstract controller with commons methods for the view controllers, like return to home or app management.
 */
public abstract class Controller {
    /**
     * Error windows title.
     */
    public static final String ERROR_TITLE = "Erreur";
	
    /**
     * Error windows header.
     */
    public static final String ERROR_HEADER = "Le programme a atteint une exception.";

    /**
     * Critical error windows title.
     */
    public static final String CRITICAL_ERROR_HEADER = "Erreur Fatale";

    /**
     * Critical error windows warning.
     */
    public static final String ERROR_MESSAGE = ", L'application va se terminer.";

    /**
     * Error windows stacktrace.
     */
    public static final String ERROR_STACKTRACE = "Etat de la pile d'appel :";
    
    private App app;

    /**
     * Set the App.
     * @param newApp became the new app
     */
    public void setApp(App newApp) {
        app = newApp;
    }

    /**
     * Get the App.
     * @return current app
     */
    public App getApp() {
        return app;
    }
    
    /**
     * Method used after initialize and set up app to manage model inside the view.
     */
    public abstract void start();
    
    /**
     * Method if you want to reload view in a different way than the start method 
     */
    public void reload() {
    	start();
    }

    /**
     * Return to the home view.
     */
    public void goHome() {
        app.setView(0);
    }
    
    /**
     * A window for low importance exception.
     * @param a The application.
     * @param e The exception.
     * @param message The message.
     */
    public static void exceptionModal(App a, Exception e, String message) {
        // Create the window.
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(ERROR_TITLE);
        alert.setHeaderText(ERROR_HEADER);
        alert.setContentText(message + ".");
        alert.initOwner(a.getWindow());
        alert.initModality(Modality.APPLICATION_MODAL); 

        // Expend content.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label(ERROR_STACKTRACE);

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        // Show and wait.
        alert.showAndWait();
    }

    /**
     * A window for critical exception.
     * @param a The application to quit.
     * @param e The exception.
     * @param message The message.
     */
    public static void criticalExceptionModal(App a, Exception e, String message) {
        // Create the window.
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(CRITICAL_ERROR_HEADER);
        alert.setHeaderText(ERROR_HEADER);
        alert.setContentText(message + ERROR_MESSAGE);
        alert.initOwner(a.getWindow());
        alert.initModality(Modality.APPLICATION_MODAL); 

        // Expend content.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label(ERROR_STACKTRACE);

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        // Show and wait.
        alert.showAndWait();

        // Quit the app.
        a.quit();
    }
}
