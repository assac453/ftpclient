package com.example.ftpclient.Global;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class CallAlert {
    public static void Alert(Alert.AlertType alertType, String title, String content, ActionEvent event){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.WINDOW_MODAL);
        if (event!=null){
            alert.initOwner(((Node)event.getSource()).getScene().getWindow());
        }
        alert.show();
    }
}
