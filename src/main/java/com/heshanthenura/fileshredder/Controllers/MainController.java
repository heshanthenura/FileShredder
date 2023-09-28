package com.heshanthenura.fileshredder.Controllers;

import com.heshanthenura.fileshredder.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class MainController {

    public static List<String> addedFilePaths = new ArrayList<>();
    private FileDetailsController fileDetailsController;


    Logger logger = Logger.getLogger("info-logger");

    @FXML
    private ScrollPane fileDropPane;

    @FXML
    private VBox fileDetailsHolder;

    public void initialize() {
        // Set up drag-and-drop behavior
        fileDropPane.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != fileDropPane
                    && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        fileDropPane.setOnDragDropped((DragEvent event) -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasFiles()) {
                // List and process the dropped files and folders
                List<File> filesAndFolders = dragboard.getFiles();
                processFilesAndFolders(filesAndFolders);

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void processFilesAndFolders(List<File> filesAndFolders) {
        for (File fileOrFolder : filesAndFolders) {
            if (fileOrFolder.isDirectory()) {
                // If it's a directory, recursively process its contents
                processFilesAndFolders(List.of(fileOrFolder.listFiles()));
            } else {
                // If it's a file, process it
                String filePath = fileOrFolder.getAbsolutePath();
                logger.info("Dropped file: " + filePath);

                // Check if the path is not already in the list
                if (!addedFilePaths.contains(filePath)) {
                    addedFilePaths.add(filePath);

                    // Load filedetails.fxml and add it to fileDetailsHolder
                    try {
                        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("filedetails.fxml"));
                        VBox fileDetails = loader.load();
                        fileDetailsController  = loader.getController();
                        fileDetailsController.setFileDetails(fileOrFolder.getName(), filePath, Long.toString(fileOrFolder.length()));
                        fileDetailsHolder.getChildren().add(fileDetails);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (String path :
                addedFilePaths) {
            logger.info(path);
        }

    }

    public void removeFilePath(String path) {
        addedFilePaths.remove(path);
    }

    @FXML
    void shredAndDeleteFiles(MouseEvent event) {
        int passes = 1;

        List<String> filesToDelete = new ArrayList<>(); // Store file paths to delete

        for (String filePath : addedFilePaths) {
            File file = new File(filePath);

            if (file.exists() && !file.isDirectory()) {
                try (FileOutputStream fos = new FileOutputStream(file, false)) {
                    for (int i = 0; i < passes; i++) {
                        // Overwrite the file's contents with pseudorandom data
                        byte[] buffer = new byte[1024];
                        new Random().nextBytes(buffer);
                        fos.write(buffer);
                    }
                    logger.info("Shredding process completed for " + filePath);

                    // Add the file path to the list of files to delete
                    filesToDelete.add(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Now, delete the files one by one
        for (String filePath : filesToDelete) {
            File fileToDelete = new File(filePath);
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                if (fileToDelete.delete()) {
                    logger.info("File shredded and deleted: " + filePath);
                } else {
                    logger.warning("Failed to delete file after shredding: " + filePath);
                }
            }
        }

        // Remove the associated child elements from fileDetailsHolder
        removeFileDetailsHolder();
    }


    @FXML
    void shredFiles(MouseEvent event) {
        int passes = 1;

        for (String filePath : addedFilePaths) {
            File file = new File(filePath);

            if (file.exists() && !file.isDirectory()) {
                try (FileOutputStream fos = new FileOutputStream(file, false)) {
                    for (int i = 0; i < passes; i++) {
                        // Overwrite the file's contents with pseudorandom data
                        byte[] buffer = new byte[1024];
                        new Random().nextBytes(buffer);
                        fos.write(buffer);
                    }
                    logger.info("Shredding process completed for " + filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        removeFileDetailsHolder();
    }

    private void removeFileDetailsHolder() {
        fileDetailsHolder.getChildren().clear();
    }


}