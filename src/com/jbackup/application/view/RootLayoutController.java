package com.jbackup.application.view;

import java.io.IOException;
import java.util.prefs.Preferences;

import com.jbackup.application.MainApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
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
    	try {
    		// load the fxml file and create a new stage for the popup dialog
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("view/SettingsEditDialog.fxml"));
    		AnchorPane page = (AnchorPane) loader.load();

    		// Create the dialog Stage.
    		Stage dialogStage = new Stage();
    		dialogStage.setTitle("Edit Person");
    		dialogStage.initModality(Modality.WINDOW_MODAL);
    		dialogStage.initOwner(mainApp.getPrimaryStage());
    		Scene scene = new Scene(page);
    		dialogStage.setScene(scene);

    		// Set the person into the controller.
    		SettingsEditDialogController controller = loader.getController();
    		controller.setDialogStage(dialogStage);
    		controller.setPreferences(Preferences.userRoot());

    		// Show the dialog and wait until the user closes it
    		dialogStage.showAndWait();

    		controller.isOkClicked();
    	} catch (IOException e) {
    		e.printStackTrace();
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