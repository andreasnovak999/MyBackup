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
    //private Preferences prefs;

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
    	//this.prefs = Preferences.userRoot();
        // Fill source file list
    	File folder = new File("D:\\");
        search(folder, sourceFileData);
        System.out.println("total source files: " + sourceFileData.size());

        // Fill backup file list
        // folder = new File(prefs.get("backupPath", "F:\\Backup_21_09_2020\\"));//prefs.get("backupPath", ""));
        folder = new File("F:\\Backup_21_09_2020\\");//prefs.get("backupPath", ""));
        search(folder, backupFileData);
        System.out.println("total backup files: " + backupFileData.size());
    }

	private void search(final File folder, ObservableList<FileData> fileData) {
		try {
			for (final File f : folder.listFiles(f -> !f.getName().toLowerCase().startsWith("$"))) {
				if (f != null) {
					if (f.isDirectory())
						search(f, fileData);
					if (f.isFile())
							fileData.add(new FileData(f.getPath(), String.valueOf(f.length()), sdf.format(f.lastModified()), f.lastModified()));
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

        showFileOverview();
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
     * Shows the file overview inside the root layout.
     */
    public void showFileOverview() {
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
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) {
		launch(args);
	}
}