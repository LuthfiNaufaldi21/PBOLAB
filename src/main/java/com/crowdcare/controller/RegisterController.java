package com.crowdcare.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.crowdcare.model.User;
import com.crowdcare.service.UserService;

import java.io.IOException;
import java.net.URL;

public class RegisterController {
    private final UserService userService =
            UserService.getInstance();

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private CheckBox termsCheckBox;

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll(
                "Donatur",
                "Penggalang Dana"
        );
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String role =
                roleComboBox.getValue();

        String password =
                passwordField.getText();

        String confirmPassword =
                confirmPasswordField.getText();

        if (name.isEmpty()
                || email.isEmpty()
                || role == null
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Data Belum Lengkap",
                    "Semua kolom harus diisi."
            );
            return;
        }

        if (!email.contains("@")) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Email Tidak Valid",
                    "Masukkan alamat email yang benar."
            );
            return;
        }

        if (password.length() < 6) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Kata Sandi Terlalu Pendek",
                    "Kata sandi minimal terdiri dari 6 karakter."
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Kata Sandi Tidak Sama",
                    "Konfirmasi kata sandi tidak sesuai."
            );
            return;
        }

        if (!termsCheckBox.isSelected()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Syarat dan Ketentuan",
                    "Anda harus menyetujui syarat dan ketentuan."
            );
            return;
        }

        try {
            /*
             * Hasil register bertipe User, tetapi object
             * sebenarnya dapat berupa Donor atau Fundraiser.
             *
             * Ini merupakan POLYMORPHISM.
             */
            User registeredUser =
                    userService.register(
                            name,
                            email,
                            password,
                            role
                    );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Pendaftaran Berhasil",
                    "Akun "
                            + registeredUser.getFullName()
                            + " berhasil dibuat sebagai "
                            + registeredUser.getRoleName()
                            + "."
            );

            openLogin(event);

        } catch (IllegalArgumentException exception) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Pendaftaran Gagal",
                    exception.getMessage()
            );
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        openLogin(event);
    }

    private void openLogin(ActionEvent event) {
        try {
            URL loginUrl = getClass().getResource("/view/login.fxml");

            if (loginUrl == null) {
                throw new IOException("File login.fxml tidak ditemukan.");
            }

            FXMLLoader loader = new FXMLLoader(loginUrl);
            loader.setController(new LoginController());

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("CrowdCare");
            stage.setScene(new Scene(root, 1000, 650));
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException exception) {
            exception.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Gagal Membuka Login",
                    exception.getMessage()
            );
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}