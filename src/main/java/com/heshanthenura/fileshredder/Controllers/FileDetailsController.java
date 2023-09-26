package com.heshanthenura.fileshredder.Controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class FileDetailsController {
    @FXML
    private Text fileName;

    @FXML
    private Text filePath;

    @FXML
    private Text fileSize;

    public void setFileDetails(String name, String path, String size) {
        fileName.setText("Name: " + name);
        filePath.setText("Path: " + path);
        fileSize.setText("Size: " + size);
    }

}
