package com.crowdcare;

import com.crowdcare.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApplication.class.getResource("/view/login.fxml")
        );

        // Memasang controller tanpa fx:controller di file FXML
        loader.setController(new LoginController());

        Scene scene = new Scene(loader.load(), 1000, 650);

        stage.setTitle("CrowdCare");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}