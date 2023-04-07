package com.example.ftpclient;

import Controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {
    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("LoginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("FTP-клиент!");
        stage.setScene(scene);
        stage.show();
        LoginController.loginStage = stage;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
