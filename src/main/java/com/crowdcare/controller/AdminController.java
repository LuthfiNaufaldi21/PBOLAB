package com.crowdcare.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class AdminController {

    @FXML
    private Label pendingCountLabel;

    @FXML
    private Label pendingBadgeLabel;

    private int pendingCount = 4;

    @FXML
    private void initialize() {
        updatePendingLabels();
    }

    @FXML
    private void openAdminDashboard(ActionEvent event) {
        openAdminPage(
                event,
                "/view/admin-home.fxml",
                "CrowdCare - Dashboard Admin"
        );
    }

    @FXML
    private void openAdminApproval(ActionEvent event) {
        openAdminPage(
                event,
                "/view/admin-dashboard.fxml",
                "CrowdCare - Approval Campaign"
        );
    }

    @FXML
    private void handleApprove(ActionEvent event) {
        processCampaign(event, true);
    }

    @FXML
    private void handleReject(ActionEvent event) {
        processCampaign(event, false);
    }

    private void processCampaign(
            ActionEvent event,
            boolean approved
    ) {
        Button clickedButton = (Button) event.getSource();

        if (!(clickedButton.getParent() instanceof HBox row)) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Kesalahan Tampilan",
                    "Baris campaign tidak ditemukan."
            );
            return;
        }

        Label statusLabel = row.getChildren()
                .stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .filter(label ->
                        label.getStyleClass()
                                .contains("admin-status-pending")
                )
                .findFirst()
                .orElse(null);

        if (statusLabel == null) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Kesalahan Status",
                    "Status campaign tidak ditemukan."
            );
            return;
        }

        String action = approved ? "menyetujui" : "menolak";
        String resultStatus = approved
                ? "DISETUJUI"
                : "DITOLAK";

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmation.setTitle(
                approved
                        ? "Setujui Campaign"
                        : "Tolak Campaign"
        );

        confirmation.setHeaderText(
                "Apakah Anda yakin ingin "
                        + action
                        + " campaign ini?"
        );

        confirmation.setContentText(
                approved
                        ? "Campaign akan dipublikasikan dan dapat menerima donasi."
                        : "Campaign akan dikembalikan kepada penggalang dana."
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {
            return;
        }

        statusLabel.setText(resultStatus);

        statusLabel.getStyleClass().remove(
                "admin-status-pending"
        );

        if (approved) {
            statusLabel.getStyleClass().add(
                    "admin-status-approved"
            );

            row.getStyleClass().add(
                    "admin-campaign-row-approved"
            );
        } else {
            statusLabel.getStyleClass().add(
                    "admin-status-rejected"
            );

            row.getStyleClass().add(
                    "admin-campaign-row-rejected"
            );
        }

        // Menghapus tombol Setujui dan Tolak dari baris.
        row.getChildren().removeIf(
                node -> node instanceof Button
        );

        pendingCount = Math.max(
                0,
                pendingCount - 1
        );

        updatePendingLabels();

        showAlert(
                Alert.AlertType.INFORMATION,
                approved
                        ? "Campaign Disetujui"
                        : "Campaign Ditolak",
                approved
                        ? "Campaign berhasil disetujui dan dipublikasikan."
                        : "Campaign telah ditolak dan dikembalikan."
        );
    }

    private void updatePendingLabels() {
        if (pendingCountLabel != null) {
            pendingCountLabel.setText(
                    String.valueOf(pendingCount)
            );
        }

        if (pendingBadgeLabel != null) {
            pendingBadgeLabel.setText(
                    pendingCount + " PENDING"
            );
        }
    }

    @FXML
    private void handleRefresh() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Data Diperbarui",
                "Data sistem berhasil dimuat ulang."
        );
    }

    @FXML
    private void handleViewUsers() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Kelola Pengguna",
                "Halaman kelola pengguna masih dalam pengembangan."
        );
    }

    @FXML
    private void handleViewReports() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Laporan Sistem",
                "Halaman laporan sistem masih dalam pengembangan."
        );
    }

    @FXML
    private void handleAdminSettings() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Pengaturan Admin",
                "Pengaturan administrator masih berupa simulasi."
        );
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            URL loginUrl = getClass().getResource(
                    "/view/login.fxml"
            );

            if (loginUrl == null) {
                throw new IOException(
                        "File login.fxml tidak ditemukan."
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(loginUrl);

            loader.setController(
                    new LoginController()
            );

            Parent root = loader.load();
            Stage stage = getStage(event);

            stage.setTitle("CrowdCare");
            stage.setScene(
                    new Scene(root, 1000, 650)
            );
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException exception) {
            exception.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Gagal Keluar",
                    exception.getMessage()
            );
        }
    }

    private void openAdminPage(
            ActionEvent event,
            String fxmlPath,
            String title
    ) {
        try {
            URL pageUrl =
                    getClass().getResource(fxmlPath);

            if (pageUrl == null) {
                throw new IOException(
                        "File tidak ditemukan: "
                                + fxmlPath
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(pageUrl);

            loader.setController(
                    new AdminController()
            );

            Parent root = loader.load();
            Stage stage = getStage(event);

            stage.setTitle(title);
            stage.setScene(
                    new Scene(root, 1200, 720)
            );
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException exception) {
            exception.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Gagal Membuka Halaman",
                    exception.getMessage()
            );
        }
    }

    private Stage getStage(ActionEvent event) {
        Node source = (Node) event.getSource();

        return (Stage) source
                .getScene()
                .getWindow();
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