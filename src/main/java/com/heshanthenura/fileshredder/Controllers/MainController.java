package com.heshanthenura.fileshredder.Controllers;

import com.heshanthenura.fileshredder.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Logger;

public class MainController {

    Logger logger = Logger.getLogger("info-logger");

    @FXML
    private AnchorPane fileDropPane;

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
                // List and process the dropped files
                dragboard.getFiles().forEach(file -> {
                    logger.info("Dropped file: " + file.getAbsolutePath());
                    // You can perform actions with the dropped files here

                    // Load filedetails.fxml and add it to fileDetailsHolder
                    try {
                        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("filedetails.fxml"));
                        AnchorPane fileDetails = loader.load();
                        FileDetailsController controller = loader.getController();
                        controller.setFileDetails(file.getName(), file.getAbsolutePath(), Long.toString(file.length()));
                        fileDetailsHolder.getChildren().add(fileDetails);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

}