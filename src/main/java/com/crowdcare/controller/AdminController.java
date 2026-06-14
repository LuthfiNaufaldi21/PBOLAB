package com.crowdcare.controller;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.UserEntity;
import com.crowdcare.model.User;
import com.crowdcare.repository.CampaignRepository;
import com.crowdcare.repository.DonationRepository;
import com.crowdcare.repository.UserRepository;
import com.crowdcare.service.CampaignService;
import com.crowdcare.session.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class AdminController {

    // ========================
    // fx:id — admin-home.fxml (Dashboard)
    // ========================
    @FXML private Label statTotalUsers;
    @FXML private Label statTotalCampaigns;
    @FXML private Label statPendingCampaigns;
    @FXML private Label statTotalDonations;
    @FXML private Label adminWelcomeTitle;
    @FXML private VBox pendingCampaignListHome;

    // ========================
    // fx:id — admin-dashboard.fxml (Approval)
    // ========================
    @FXML private Label pendingBadgeLabel;
    @FXML private VBox pendingCampaignList;

    // ========================
    // fx:id — admin-users.fxml
    // ========================
    @FXML private VBox userListContainer;
    @FXML private Label statAdminCount;
    @FXML private Label statDonorCount;
    @FXML private Label statFundraiserCount;

    // ========================
    // fx:id — admin-reports.fxml
    // ========================
    @FXML private Label reportTotalUsers;
    @FXML private Label reportTotalCampaigns;
    @FXML private Label reportTotalDonation;
    @FXML private Label reportTotalTransaksi;
    @FXML private ProgressBar barApproved;
    @FXML private ProgressBar barPending;
    @FXML private ProgressBar barRejected;
    @FXML private ProgressBar barCompleted;
    @FXML private Label lblApproved;
    @FXML private Label lblPending;
    @FXML private Label lblRejected;
    @FXML private Label lblCompleted;
    @FXML private ProgressBar barAdmin;
    @FXML private ProgressBar barDonor;
    @FXML private ProgressBar barFundraiser;
    @FXML private Label lblAdminCount;
    @FXML private Label lblDonorCount;
    @FXML private Label lblFundraiserCount;
    @FXML private VBox campaignDanaList;

    // Spring context
    private static ApplicationContext springContext;

    public static void setSpringContext(ApplicationContext ctx) {
        springContext = ctx;
    }

    @FXML
    private void initialize() {
        // Set welcome label jika ada
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (adminWelcomeTitle != null && currentUser != null) {
            adminWelcomeTitle.setText("Selamat datang kembali, " + currentUser.getUsername() + "!");
        }

        // Load sesuai halaman yang aktif
        loadDashboardStats();
        loadPendingCampaigns();
        loadUserPage();
        loadReportPage();
    }

    // ========================
    // Dashboard Stats
    // ========================
    private void loadDashboardStats() {
        if (springContext == null) return;
        // Cek apakah ini halaman yang butuh stats (home/approval/users)
        if (statTotalUsers == null && statAdminCount == null && reportTotalUsers == null) return;

        try {
            UserRepository userRepo = springContext.getBean(UserRepository.class);
            CampaignRepository campaignRepo = springContext.getBean(CampaignRepository.class);
            DonationRepository donationRepo = springContext.getBean(DonationRepository.class);

            long totalUsers = userRepo.count();
            long approvedCount = campaignRepo.findByStatus("APPROVED").size();
            long pendingCount = campaignRepo.findByStatus("PENDING").size();
            long adminCount = userRepo.findByRole("ADMIN").size();
            long donorCount = userRepo.findByRole("DONOR").size();
            long fundraiserCount = userRepo.findByRole("FUNDRAISER").size();
            long totalDonation = donationRepo.findAll().stream()
                    .mapToLong(d -> d.getAmount() != null ? d.getAmount() : 0L).sum();

            Platform.runLater(() -> {
                // Home / approval stats
                if (statTotalUsers != null)
                    statTotalUsers.setText(String.valueOf(totalUsers));
                if (statTotalCampaigns != null)
                    statTotalCampaigns.setText(String.valueOf(approvedCount));
                if (statPendingCampaigns != null)
                    statPendingCampaigns.setText(pendingCount + " menunggu persetujuan");
                if (statTotalDonations != null)
                    statTotalDonations.setText(formatRupiah(totalDonation));
                if (pendingBadgeLabel != null)
                    pendingBadgeLabel.setText(pendingCount + " PENDING");

                // Users page stats
                if (statAdminCount != null)
                    statAdminCount.setText(String.valueOf(adminCount));
                if (statDonorCount != null)
                    statDonorCount.setText(String.valueOf(donorCount));
                if (statFundraiserCount != null)
                    statFundraiserCount.setText(String.valueOf(fundraiserCount));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================
    // Pending Campaign List (home + approval)
    // ========================
    private void loadPendingCampaigns() {
        if (springContext == null) return;
        VBox targetContainer = (pendingCampaignList != null) ? pendingCampaignList : pendingCampaignListHome;
        if (targetContainer == null) return;

        try {
            CampaignRepository campaignRepo = springContext.getBean(CampaignRepository.class);
            List<CampaignEntity> pendingList = campaignRepo.findByStatus("PENDING");

            Platform.runLater(() -> {
                targetContainer.getChildren().clear();
                if (pendingList.isEmpty()) {
                    Label emptyLabel = new Label("Tidak ada campaign yang menunggu persetujuan.");
                    emptyLabel.getStyleClass().add("admin-campaign-meta");
                    emptyLabel.setPadding(new javafx.geometry.Insets(12, 0, 0, 0));
                    targetContainer.getChildren().add(emptyLabel);
                    return;
                }
                for (CampaignEntity campaign : pendingList) {
                    targetContainer.getChildren().add(buildCampaignRow(campaign));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox buildCampaignRow(CampaignEntity campaign) {
        HBox row = new HBox(16);
        row.getStyleClass().add("admin-campaign-row");
        row.setAlignment(Pos.CENTER_LEFT);

        String firstLetter = campaign.getTitle().isEmpty() ? "?" :
                String.valueOf(campaign.getTitle().charAt(0)).toUpperCase();
        String[] colors = {"admin-cover-blue", "admin-cover-green", "admin-cover-orange", "admin-cover-purple"};
        String colorClass = colors[(int)(campaign.getId() % colors.length)];

        StackPane cover = new StackPane();
        cover.setMinWidth(78); cover.setPrefWidth(78); cover.setMaxWidth(78); cover.setPrefHeight(72);
        cover.getStyleClass().add(colorClass);
        Label coverText = new Label(firstLetter);
        coverText.getStyleClass().add("admin-cover-text");
        cover.getChildren().add(coverText);

        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);
        String creatorName = campaign.getCreator() != null ? campaign.getCreator().getFullName() : "Unknown";
        Label titleLabel = new Label(campaign.getTitle());
        titleLabel.getStyleClass().add("admin-campaign-title");
        Label metaLabel = new Label("Kategori " + campaign.getCategory() + " • Diajukan oleh " + creatorName);
        metaLabel.getStyleClass().add("admin-campaign-meta");
        Label targetLabel = new Label("Target " + formatRupiah(campaign.getTargetAmount()) + " • Batas waktu " + campaign.getEndDate());
        targetLabel.getStyleClass().add("admin-campaign-target");
        info.getChildren().addAll(titleLabel, metaLabel, targetLabel);

        Label statusLabel = new Label("PENDING");
        statusLabel.getStyleClass().add("admin-status-pending");

        Button rejectBtn = new Button("Tolak");
        rejectBtn.setPrefHeight(36);
        rejectBtn.getStyleClass().add("admin-reject-button");
        rejectBtn.setOnAction(e -> handleCampaignAction(campaign.getId(), false, row, statusLabel));

        Button approveBtn = new Button("Setujui");
        approveBtn.setPrefHeight(36);
        approveBtn.getStyleClass().add("admin-approve-button");
        approveBtn.setOnAction(e -> handleCampaignAction(campaign.getId(), true, row, statusLabel));

        row.getChildren().addAll(cover, info, statusLabel, rejectBtn, approveBtn);
        return row;
    }

    private void handleCampaignAction(Long campaignId, boolean approved, HBox row, Label statusLabel) {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null || !currentUser.canApproveCampaign()) {
            showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Hanya Admin yang dapat memproses campaign.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(approved ? "Setujui Campaign" : "Tolak Campaign");
        confirmation.setHeaderText("Apakah Anda yakin ingin " + (approved ? "menyetujui" : "menolak") + " campaign ini?");
        confirmation.setContentText(approved
                ? "Campaign akan dipublikasikan dan dapat menerima donasi."
                : "Campaign akan dikembalikan kepada penggalang dana.");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        try {
            CampaignService campaignService = springContext.getBean(CampaignService.class);
            if (approved) campaignService.approveCampaign(campaignId);
            else campaignService.rejectCampaign(campaignId);

            statusLabel.setText(approved ? "DISETUJUI" : "DITOLAK");
            statusLabel.getStyleClass().remove("admin-status-pending");
            statusLabel.getStyleClass().add(approved ? "admin-status-approved" : "admin-status-rejected");
            row.getChildren().removeIf(n -> n instanceof Button);
            row.getStyleClass().add(approved ? "admin-campaign-row-approved" : "admin-campaign-row-rejected");

            loadDashboardStats();
            showAlert(Alert.AlertType.INFORMATION,
                    approved ? "Campaign Disetujui" : "Campaign Ditolak",
                    approved ? "Campaign berhasil disetujui dan dipublikasikan."
                            : "Campaign telah ditolak dan dikembalikan.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Gagal memproses campaign: " + e.getMessage());
        }
    }

    // ========================
    // Users Page
    // ========================
    private void loadUserPage() {
        if (userListContainer == null || springContext == null) return;

        try {
            UserRepository userRepo = springContext.getBean(UserRepository.class);
            List<UserEntity> allUsers = userRepo.findAll();

            Platform.runLater(() -> {
                userListContainer.getChildren().clear();
                if (allUsers.isEmpty()) {
                    Label empty = new Label("Tidak ada pengguna terdaftar.");
                    empty.getStyleClass().add("admin-campaign-meta");
                    empty.setPadding(new javafx.geometry.Insets(16));
                    userListContainer.getChildren().add(empty);
                    return;
                }
                for (UserEntity u : allUsers) {
                    userListContainer.getChildren().add(buildUserRow(u));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox buildUserRow(UserEntity user) {
        HBox row = new HBox();
        row.getStyleClass().add("user-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new javafx.geometry.Insets(12, 16, 12, 16));

        // Avatar
        StackPane avatar = new StackPane();
        avatar.setMinWidth(38); avatar.setPrefWidth(38); avatar.setMaxWidth(38);
        avatar.setPrefHeight(38);
        avatar.getStyleClass().add("admin-cover-blue");
        avatar.setStyle("-fx-background-radius: 50%;");
        String initials = user.getFullName().length() >= 2
                ? user.getFullName().substring(0, 2).toUpperCase()
                : user.getFullName().toUpperCase();
        Label avatarLabel = new Label(initials);
        avatarLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white;");
        avatar.getChildren().add(avatarLabel);

        // Nama + username
        VBox nameBox = new VBox(2);
        nameBox.setMinWidth(220); nameBox.setPrefWidth(220); nameBox.setMaxWidth(220);
        Label nameLabel = new Label(user.getFullName());
        nameLabel.getStyleClass().add("user-name-label");
        Label usernameLabel = new Label("@" + user.getUsername());
        usernameLabel.getStyleClass().add("user-username-label");
        nameBox.getChildren().addAll(nameLabel, usernameLabel);

        HBox.setMargin(nameBox, new javafx.geometry.Insets(0, 0, 0, 12));

        // Username kolom
        Label usernameCol = new Label("@" + user.getUsername());
        usernameCol.getStyleClass().add("user-username-label");
        usernameCol.setMinWidth(160); usernameCol.setPrefWidth(160);

        // Role badge
        Label roleBadge = new Label(user.getRole());
        String badgeClass = switch (user.getRole()) {
            case "ADMIN" -> "user-role-badge-admin";
            case "DONOR" -> "user-role-badge-donor";
            case "FUNDRAISER" -> "user-role-badge-fundraiser";
            default -> "user-role-badge-donor";
        };
        roleBadge.getStyleClass().add(badgeClass);
        roleBadge.setMinWidth(130); roleBadge.setPrefWidth(130);

        // ID
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label idLabel = new Label(user.getId());
        idLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #94a3b8;");

        row.getChildren().addAll(avatar, nameBox, usernameCol, roleBadge, spacer, idLabel);
        return row;
    }

    // ========================
    // Reports Page
    // ========================
    private void loadReportPage() {
        if (reportTotalUsers == null || springContext == null) return;

        try {
            UserRepository userRepo = springContext.getBean(UserRepository.class);
            CampaignRepository campaignRepo = springContext.getBean(CampaignRepository.class);
            DonationRepository donationRepo = springContext.getBean(DonationRepository.class);

            long totalUsers = userRepo.count();
            long totalCampaigns = campaignRepo.count();
            long approved = campaignRepo.findByStatus("APPROVED").size();
            long pending = campaignRepo.findByStatus("PENDING").size();
            long rejected = campaignRepo.findByStatus("REJECTED").size();
            long completed = campaignRepo.findByStatus("COMPLETED").size();
            long adminCount = userRepo.findByRole("ADMIN").size();
            long donorCount = userRepo.findByRole("DONOR").size();
            long fundraiserCount = userRepo.findByRole("FUNDRAISER").size();
            List<?> allDonations = donationRepo.findAll();
            long totalDonation = allDonations.stream()
                    .mapToLong(d -> {
                        try {
                            return (long) d.getClass().getMethod("getAmount").invoke(d);
                        } catch (Exception ex) { return 0L; }
                    }).sum();
            long totalTransaksi = allDonations.size();
            List<CampaignEntity> allCampaigns = campaignRepo.findAll();

            Platform.runLater(() -> {
                // Top stats
                setText(reportTotalUsers, String.valueOf(totalUsers));
                setText(reportTotalCampaigns, String.valueOf(totalCampaigns));
                setText(reportTotalDonation, formatRupiah(totalDonation));
                setText(reportTotalTransaksi, String.valueOf(totalTransaksi));

                // Campaign bars
                double maxC = totalCampaigns > 0 ? totalCampaigns : 1;
                setBar(barApproved, lblApproved, approved, maxC);
                setBar(barPending, lblPending, pending, maxC);
                setBar(barRejected, lblRejected, rejected, maxC);
                setBar(barCompleted, lblCompleted, completed, maxC);

                // User bars
                double maxU = totalUsers > 0 ? totalUsers : 1;
                setBar(barAdmin, lblAdminCount, adminCount, maxU);
                setBar(barDonor, lblDonorCount, donorCount, maxU);
                setBar(barFundraiser, lblFundraiserCount, fundraiserCount, maxU);

                // Campaign dana list
                if (campaignDanaList != null) {
                    campaignDanaList.getChildren().clear();
                    if (allCampaigns.isEmpty()) {
                        campaignDanaList.getChildren().add(new Label("Belum ada campaign."));
                    } else {
                        for (CampaignEntity c : allCampaigns) {
                            HBox danaRow = new HBox(12);
                            danaRow.setAlignment(Pos.CENTER_LEFT);
                            danaRow.setPadding(new javafx.geometry.Insets(8, 0, 8, 0));

                            // Status badge
                            Label badge = new Label(c.getStatus());
                            String bc = switch (c.getStatus()) {
                                case "APPROVED" -> "admin-status-approved";
                                case "REJECTED" -> "admin-status-rejected";
                                case "COMPLETED" -> "admin-status-approved";
                                default -> "admin-status-pending";
                            };
                            badge.getStyleClass().add(bc);
                            badge.setMinWidth(80);

                            Label titleLbl = new Label(c.getTitle());
                            titleLbl.getStyleClass().add("admin-campaign-title");
                            HBox.setHgrow(titleLbl, Priority.ALWAYS);

                            long collected = c.getCollectedAmount() != null ? c.getCollectedAmount() : 0L;
                            long target = c.getTargetAmount() != null ? c.getTargetAmount() : 1L;
                            double prog = Math.min(1.0, (double) collected / target);

                            ProgressBar pb = new ProgressBar(prog);
                            pb.setPrefWidth(150); pb.setPrefHeight(10);
                            pb.getStyleClass().add("admin-system-progress");

                            Label dana = new Label(formatRupiah(collected) + " / " + formatRupiah(target));
                            dana.setStyle("-fx-font-size: 11px; -fx-text-fill: #475569;");
                            dana.setMinWidth(160);

                            danaRow.getChildren().addAll(badge, titleLbl, pb, dana);
                            campaignDanaList.getChildren().add(danaRow);

                            Separator sep = new Separator();
                            campaignDanaList.getChildren().add(sep);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setText(Label label, String text) {
        if (label != null) label.setText(text);
    }

    private void setBar(ProgressBar bar, Label label, long value, double max) {
        if (bar != null) bar.setProgress(value / max);
        if (label != null) label.setText(String.valueOf(value));
    }

    // ========================
    // Navigasi sidebar
    // ========================
    @FXML
    private void openAdminDashboard(ActionEvent event) {
        openAdminPage(event, "/view/admin-home.fxml", "CrowdCare - Dashboard Admin");
    }

    @FXML
    private void openAdminApproval(ActionEvent event) {
        openAdminPage(event, "/view/admin-dashboard.fxml", "CrowdCare - Approval Campaign");
    }

    @FXML
    private void handleViewUsers(ActionEvent event) {
        openAdminPage(event, "/view/admin-users.fxml", "CrowdCare - Kelola Pengguna");
    }

    @FXML
    private void handleViewReports(ActionEvent event) {
        openAdminPage(event, "/view/admin-reports.fxml", "CrowdCare - Laporan Sistem");
    }

    @FXML
    private void handleRefresh() {
        loadDashboardStats();
        loadPendingCampaigns();
        loadUserPage();
        loadReportPage();
        showAlert(Alert.AlertType.INFORMATION, "Data Diperbarui", "Data berhasil dimuat ulang dari database.");
    }

    @FXML
    private void handleAdminSettings() {
        showAlert(Alert.AlertType.INFORMATION, "Pengaturan Admin", "Fitur pengaturan administrator sedang dalam pengembangan.");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.getInstance().logout();
        try {
            URL loginUrl = getClass().getResource("/view/login.fxml");
            if (loginUrl == null) throw new IOException("File login.fxml tidak ditemukan.");
            FXMLLoader loader = new FXMLLoader(loginUrl);
            loader.setController(new LoginController());
            Parent root = loader.load();
            Stage stage = getStage(event);
            stage.setTitle("CrowdCare");
            stage.setScene(new Scene(root, 1000, 650));
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Keluar", e.getMessage());
        }
    }

    // ========================
    // Helper
    // ========================
    private void openAdminPage(ActionEvent event, String fxmlPath, String title) {
        try {
            URL pageUrl = getClass().getResource(fxmlPath);
            if (pageUrl == null) throw new IOException("File tidak ditemukan: " + fxmlPath);
            if (springContext != null) AdminController.setSpringContext(springContext);
            FXMLLoader loader = new FXMLLoader(pageUrl);
            AdminController controller = new AdminController();
            loader.setController(controller);
            Parent root = loader.load();
            Stage stage = getStage(event);
            stage.setTitle(title);
            stage.setScene(new Scene(root, 1200, 720));
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Halaman", e.getMessage());
        }
    }

    private String formatRupiah(long amount) {
        if (amount >= 1_000_000_000) return String.format("Rp%.1f M", amount / 1_000_000_000.0);
        if (amount >= 1_000_000) return String.format("Rp%.1f Jt", amount / 1_000_000.0);
        if (amount >= 1_000) return String.format("Rp%.0f Rb", amount / 1_000.0);
        return "Rp" + amount;
    }

    private Stage getStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}