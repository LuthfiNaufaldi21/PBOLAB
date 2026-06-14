package com.crowdcare.controller;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform; // Diperlukan untuk perbaikan transisi scene
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.crowdcare.model.User;
import com.crowdcare.services.UserService;
import com.crowdcare.session.UserSession;

import java.util.Optional;
import java.io.IOException;
import java.net.URL;

public class LoginController {

    private final UserService userService = UserService.getInstance();

    @FXML private VBox slide1;
    @FXML private VBox slide2;
    @FXML private VBox slide3;
    @FXML private Button dot1;
    @FXML private Button dot2;
    @FXML private Button dot3;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private VBox[] slides;
    private Button[] indicators;
    private int currentSlide = 0;
    private boolean isAnimating = false;
    private Timeline timeline;

    @FXML
    private void initialize() {
        slides = new VBox[]{slide1, slide2, slide3};
        indicators = new Button[]{dot1, dot2, dot3};
        showSlide(0, false);
        startAutoSlide();
    }

    private void startAutoSlide() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(7), event -> showNextSlide()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void restartTimeline() {
        if (timeline != null) { timeline.stop(); timeline.playFromStart(); }
    }

    private void showNextSlide() {
        if (isAnimating) return;
        int nextSlide = (currentSlide + 1) % slides.length;
        showSlide(nextSlide, true);
    }

    @FXML private void handlePreviousSlide() { if (isAnimating) return; int previousSlide = (currentSlide - 1 + slides.length) % slides.length; showSlide(previousSlide, true); restartTimeline(); }
    @FXML private void handleNextSlide() { if (isAnimating) return; int nextSlide = (currentSlide + 1) % slides.length; showSlide(nextSlide, true); restartTimeline(); }
    @FXML private void handleSlide1() { selectSlide(0); }
    @FXML private void handleSlide2() { selectSlide(1); }
    @FXML private void handleSlide3() { selectSlide(2); }

    private void selectSlide(int index) {
        if (isAnimating || index == currentSlide) { restartTimeline(); return; }
        showSlide(index, true);
        restartTimeline();
    }

    private void showSlide(int index, boolean animated) {
        if (index < 0 || index >= slides.length) return;
        if (!animated || currentSlide == index) {
            for (int i = 0; i < slides.length; i++) {
                boolean active = i == index;
                slides[i].setVisible(active);
                slides[i].setManaged(active);
                slides[i].setOpacity(active ? 1.0 : 0.0);
            }
            currentSlide = index; updateIndicators(); isAnimating = false; return;
        }

        if (isAnimating) return;
        isAnimating = true;

        VBox oldSlide = slides[currentSlide];
        VBox newSlide = slides[index];

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), oldSlide);
        fadeOut.setFromValue(1.0); fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            oldSlide.setVisible(false); oldSlide.setManaged(false);
            newSlide.setVisible(true); newSlide.setManaged(true); newSlide.setOpacity(0.0);
            currentSlide = index; updateIndicators();
            FadeTransition fadeIn = new FadeTransition(Duration.millis(450), newSlide);
            fadeIn.setFromValue(0.0); fadeIn.setToValue(1.0);
            fadeIn.setOnFinished(finishedEvent -> isAnimating = false);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void updateIndicators() {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].getStyleClass().removeAll("story-dot", "story-dot-active");
            if (i == currentSlide) { indicators[i].getStyleClass().add("story-dot-active"); }
            else { indicators[i].getStyleClass().add("story-dot"); }
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = emailField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data Belum Lengkap", "Username dan kata sandi harus diisi.");
            return;
        }

        Optional<User> authenticationResult = userService.authenticate(username, password);

        if (authenticationResult.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Gagal", "Username atau kata sandi tidak sesuai.");
            return;
        }

        User authenticatedUser = authenticationResult.get();
        UserSession.getInstance().login(authenticatedUser);

        try {
            String fxmlPath = authenticatedUser.getDashboardFxml();
            String windowTitle = authenticatedUser.getWindowTitle();

            Object controller;
            if (authenticatedUser.canApproveCampaign()) {
                controller = new AdminController();
            } else {
                controller = new NavigationController();
            }

            URL pageUrl = getClass().getResource(fxmlPath);
            if (pageUrl == null) {
                throw new IOException("File tidak ditemukan: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(pageUrl);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (timeline != null) { timeline.stop(); }

            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root, 1200, 720));
            stage.setResizable(false);
            stage.centerOnScreen();

            // =========================================================================
            // FIX SAKTI: Pemicu refresh hak akses sesaat setelah window sukses berganti panggung scene
            // =========================================================================
            if (controller instanceof NavigationController) {
                NavigationController navCtrl = (NavigationController) controller;
                Platform.runLater(navCtrl::applyRoleAccess);
            }
            // =========================================================================

        } catch (IOException exception) {
            exception.printStackTrace();
            UserSession.getInstance().logout();
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Halaman", exception.getMessage());
        }
    }

    @FXML private void handleForgotPassword() { showAlert(Alert.AlertType.INFORMATION, "Lupa Kata Sandi", "Fitur pemulihan kata sandi dalam pengembangan."); }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            URL registerUrl = getClass().getResource("/view/register.fxml");
            if (registerUrl == null) throw new IOException("File register.fxml tidak ditemukan.");

            FXMLLoader loader = new FXMLLoader(registerUrl);
            loader.setController(new RegisterController());
            Parent registerRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (timeline != null) timeline.stop();

            stage.setTitle("CrowdCare - Daftar Akun");
            stage.setScene(new Scene(registerRoot, 1000, 650));
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (IOException exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Pendaftaran", exception.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(message);
        alert.showAndWait();
    }
}