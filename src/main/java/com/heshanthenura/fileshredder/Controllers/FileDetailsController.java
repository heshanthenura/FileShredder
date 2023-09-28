package com.heshanthenura.fileshredder.Controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

import static com.heshanthenura.fileshredder.Controllers.MainController.addedFilePaths;

public class FileDetailsController {

    @FXML
    private VBox fileDetails;

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

    @FXML
    void removeFile(MouseEvent event) {
        if (fileDetails != null) {
            // Get the parent layout of fileDetailsHolder (assuming it's VBox)
            VBox parentLayout = (VBox) fileDetails.getParent();

            // Remove fileDetailsHolder from its parent layout
            parentLayout.getChildren().remove(fileDetails);

            // Remove the file path from the addedFilePaths list
            String path = filePath.getText().substring("Path: ".length());
            addedFilePaths.remove(path);
            System.out.println("Updated file paths: " + addedFilePaths);
        }
    }




    @FXML
    void openLocation(MouseEvent event) {
        // Get the file path from the filePath text field
        String path = filePath.getText().substring("Path: ".length());

        // Create a File object for the file
        File file = new File(path);

        // Check if the file exists and is not a directory
        if (file.exists() && !file.isDirectory()) {
            try {
                // Get the parent directory of the file
                File parentDir = file.getParentFile();

                // Check if the parent directory exists
                if (parentDir != null && parentDir.exists()) {
                    String os = System.getProperty("os.name").toLowerCase();

                    if (os.contains("mac")) {
                        // macOS: Use "open" command
                        Runtime.getRuntime().exec(new String[]{"open", "-R", parentDir.getAbsolutePath()});
                    } else if (os.contains("nix") || os.contains("nux") || os.contains("unix")) {
                        // Linux: Use "xdg-open" command
                        Runtime.getRuntime().exec(new String[]{"xdg-open", parentDir.getAbsolutePath()});
                    } else if (os.contains("win")) {
                        // Windows: Use "explorer.exe" command
                        Runtime.getRuntime().exec(new String[]{"explorer.exe", parentDir.getAbsolutePath()});
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
