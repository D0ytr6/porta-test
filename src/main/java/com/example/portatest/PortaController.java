package com.example.portatest;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PortaController {
    @FXML
    private Label fileStatus;

    @FXML
    protected void onHelloButtonClick() {
        fileStatus.setText("Welcome to JavaFX Application!");
    }

//    protected void onChooseFileButtonClick(){
//        FileChooser fil_chooser = new FileChooser();
//
//        File file = fil_chooser.showOpenDialog(stage);
//
//        if (file != null) {
//
//            label.setText(file.getAbsolutePath()
//                    + "  selected");
//        }
//    }
}