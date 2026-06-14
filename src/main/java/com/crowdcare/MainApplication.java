package com.crowdcare;

import com.crowdcare.controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void init() {
        // Spring Boot dijalankan di background thread sebelum JavaFX muncul
        String[] args = getParameters().getRaw().toArray(new String[0]);
        CrowdCareApplication.startSpring(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApplication.class.getResource("/view/login.fxml")
        );

        loader.setController(new LoginController());

        Scene scene = new Scene(loader.load(), 1000, 650);

        stage.setTitle("CrowdCare");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void stop() {
        // Matikan Spring Boot saat JavaFX ditutup
        CrowdCareApplication.stopSpring();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}