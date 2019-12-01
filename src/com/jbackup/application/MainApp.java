package com.jbackup.application;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import com.jbackup.application.model.FileData;
import com.jbackup.application.view.FileOverviewController;
import com.jbackup.application.view.RootLayoutController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of source file data.
     */
    private ObservableList<FileData> sourceFileData = FXCollections.observableArrayList();
    
    /**
     * The data as an observable list of backup file data.
     */
    private ObservableList<FileData> backupFileData = FXCollections.observableArrayList();
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /**
     * Constructor
     */
    public MainApp() {
        // Fill source file list
    	File folder = new File("E:\\");
        List<String> sourceResult = new ArrayList<>();
        search(folder, sourceResult, 0);
        System.out.println("total source files: " + sourceFileData.size());
        
        // Fill backup file list
        folder = new File("D:\\");
        List<String> backupResult = new ArrayList<>();
        search(folder, backupResult, 1);
        System.out.println("total backup files: " + backupFileData.size());
    }
    
	private void search(final File folder, List<String> result, int type) {
		try {
			for (final File f : folder.listFiles(f -> !f.getName().toLowerCase().startsWith("$"))) {
				if (f != null) {
					if (f.isDirectory())
						search(f, result, type);
					if (f.isFile())
						if (type == 0)
							sourceFileData.add(new FileData(f.getPath(), String.valueOf(f.length()), sdf.format(f.lastModified()), f.lastModified()));
						else
							backupFileData.add(new FileData(f.getPath(), String.valueOf(f.length()), sdf.format(f.lastModified()), f.lastModified()));
				}
			}
		} catch (Exception ex) {
			; // ignore exceptions from system folders
		}
	}

    /**
     * Returns the data as an list of source files.
     * @return
     */
    public ObservableList<FileData> getSourceFileData() {
        return sourceFileData;
    }
    
    /**
     * Returns the data as an list of backup files.
     * @return
     */
    public ObservableList<FileData> getBackupFileData() {
        return backupFileData;
    }

	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JBackupApp");

        // Set the application icon
        this.primaryStage.getIcons().add(new Image("file:resources/images/data-backup-icon-28.jpg"));

        initRootLayout();

        showPersonOverview();
	}

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // load file overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/FileOverview.fxml"));
            AnchorPane fileOverview = (AnchorPane) loader.load();

            // set file overview into the center of root layout
            rootLayout.setCenter(fileOverview);

            // give the controller access to the main app
            FileOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
//    public boolean showPersonEditDialog(Person person) {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
//            AnchorPane page = (AnchorPane) loader.load();
//
//            // Create the dialog Stage.
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Edit Person");
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
//            Scene scene = new Scene(page);
//            dialogStage.setScene(scene);
//
//            // Set the person into the controller.
//            PersonEditDialogController controller = loader.getController();
//            controller.setDialogStage(dialogStage);
//            controller.setPerson(person);
//
//            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
//
//            return controller.isOkClicked();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
