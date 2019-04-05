package com.jbackup.application.view;

import java.io.File;
import java.util.Optional;

import com.jbackup.application.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Marco Jakob
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Open dialog to enter settings.
     */
    @FXML
    private void handleSettings() {    	
    	// show dialog
    	ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
    	Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.getDialogPane().getButtonTypes().add(loginButtonType);
    	boolean disabled = false; // computed based on content of text fields, for example
    	dialog.getDialogPane().lookupButton(loginButtonType).setDisable(disabled);
    	
    	Optional<ButtonType> result = dialog.showAndWait();
    	if (result.isPresent() && result.get() == ButtonType.OK) {
    		// todo
    	}
    }
    
    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("JBackup");
        alert.setHeaderText("About");
        alert.setContentText("Author: Andreas Novak andreasnovak999@gmail.com");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}