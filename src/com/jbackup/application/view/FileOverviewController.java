package com.jbackup.application.view;

import java.util.ArrayList;
import java.util.Optional;

import com.jbackup.application.MainApp;
import com.jbackup.application.model.FileData;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class FileOverviewController {
    @FXML
    private Label sourceLabel;
	@FXML
    private TableView<FileData> sourceFileTable;
    @FXML
    private TableColumn<FileData, String> sourcePathColumn;
    @FXML
    private TableColumn<FileData, String> sourceSizeColumn;
    @FXML
    private TableColumn<FileData, String> sourceChangeDateColumn;
    @FXML
    private Label backupLabel;
    @FXML
    private TableView<FileData> backupFileTable;
    @FXML
    private TableColumn<FileData, String> backupPathColumn;
    @FXML
    private TableColumn<FileData, String> backupSizeColumn;
    @FXML
    private TableColumn<FileData, String> backupChangeDateColumn;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public FileOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the file table and columns
        sourcePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathProperty());
        sourceSizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        sourceChangeDateColumn.setCellValueFactory(cellData -> cellData.getValue().changeDateProperty());
        backupPathColumn.setCellValueFactory(cellData -> cellData.getValue().pathProperty());
        backupSizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        backupChangeDateColumn.setCellValueFactory(cellData -> cellData.getValue().changeDateProperty());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        sourceFileTable.setItems(mainApp.getSourceFileData());
        backupFileTable.setItems(mainApp.getBackupFileData());

        sourceLabel.setText("SOURCE files: " + mainApp.getSourceFileData().size());
        backupLabel.setText("BACKUP files: " + mainApp.getBackupFileData().size());
    }

    /**
     * Called when the user clicks on the compare button.
     */
    @FXML
    private void handleCompare() {
    	// compare source and backup files and add to list of files to backup
    	ArrayList<FileData> filesToBackup = new ArrayList<FileData>();
    	ObservableList<FileData> backupFileList = mainApp.getBackupFileData();
    	for (FileData sourceFileData : mainApp.getSourceFileData()) {
    		if (!backupFileList.contains(sourceFileData)) {
    			filesToBackup.add(sourceFileData);
    			System.out.println("backup file: " + sourceFileData.getPath());
    		}
    		else if (sourceFileData.getLastModified() > backupFileList.get(backupFileList.indexOf(sourceFileData)).getLastModified()) {
    			filesToBackup.add(sourceFileData);
    			System.out.println("backup file: " + sourceFileData.getPath());
    		}
    	}

    	// show compare result
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Compare Result");
		alert.setHeaderText("Files to backup: " + filesToBackup.size());
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Start Backup");
		((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancel");

		// show files to backup
//		StringBuilder sb = new StringBuilder();
//		for (FileData fileToBackup : filesToBackup) {
//			sb.append(fileToBackup.getPath() + " " + fileToBackup.getChangeDate() + System.lineSeparator());
//		}
//		alert.setContentText(sb.toString());

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent()) {
			if (result.get() == ButtonType.OK)
				System.out.println("Ok pressed");
			else if (result.get() == ButtonType.CANCEL)
				System.out.println("Cancel pressed");
		}
    }
}