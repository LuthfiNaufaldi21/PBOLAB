package com.crowdcare.controller;

import com.crowdcare.CrowdCareApplication;
import com.crowdcare.MainApplication;
import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.DonationEntity;
import com.crowdcare.model.User;
import com.crowdcare.service.CampaignService;
import com.crowdcare.service.DonationService;
import com.crowdcare.session.UserSession;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class NavigationController {

    private static final Locale ID_LOCALE = new Locale("id", "ID");
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy", ID_LOCALE);
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", ID_LOCALE);
    private static final long MONTHLY_DONATION_TARGET = 3_500_000L;

    private static String selectedCampaignTitle = "Bantu Pendidikan Anak Desa";

    private Long selectedCampaignId;
    private CampaignEntity selectedCampaign;
    private List<CampaignEntity> campaignList = new ArrayList<>();
    private List<DonationEntity> donationHistory = new ArrayList<>();
    private CampaignDraft savedDraft;

    /* ==================================================
       HEADER, SIDEBAR, PROFIL, DAN PENGATURAN
       ================================================== */
    @FXML private Label headerNameLabel;
    @FXML private Label headerRoleLabel;
    @FXML private Label avatarText;
    @FXML private Label welcomeTitleLabel;
    @FXML private Button btnCreateCampaign;
    @FXML private Button btnDonationHistory;
    @FXML private Button btnHeaderCreateCampaign;
    @FXML private VBox donationActionCard;
    @FXML private Label profileRoleBadgeLabel;
    @FXML private Label profileInfoRoleLabel;
    @FXML private ImageView settingsAvatarImage;
    @FXML private Label settingsAccountNameLabel;
    @FXML private Label settingsAccountEmailLabel;
    @FXML private Label settingsAvatarText;

    /* Profile page */
    @FXML private ImageView profileAvatarImage;
    @FXML private Label profileAvatarText;
    @FXML private Label profileDisplayName;
    @FXML private Label profileDisplayEmail;
    @FXML private Label profileInfoIdLabel;
    @FXML private Label profileInfoJoinLabel;
    @FXML private Label profileInfoLoginLabel;
    @FXML private Label profileTotalDonasi;
    @FXML private Label profileTotalTransaksi;
    @FXML private Label profileCampaignSupported;
    @FXML private Label profileCampaignSelesai;
    @FXML private Label profileCampaignDibuat;
    @FXML private Label profileCampaignAktif;
    @FXML private Label profileDampakSosial;
    @FXML private Label profileOrangTerbantu;
    @FXML private TextField profileNameField;
    @FXML private TextField profilePhoneField;
    @FXML private TextField profileEmailField;
    @FXML private TextField profileAddressField;
    @FXML private TextArea profileBioArea;

    /* Settings page */
    @FXML private ComboBox<String> settingsLanguageCombo;
    @FXML private CheckBox settingsDarkModeCheck;
    @FXML private CheckBox settingsAnimationCheck;
    @FXML private CheckBox settingsNotifNewCampaign;
    @FXML private CheckBox settingsNotifProgress;
    @FXML private CheckBox settingsNotifDonasi;
    @FXML private CheckBox settingsNotifPromo;
    @FXML private CheckBox settingsNotifReminder;
    @FXML private CheckBox settingsPrivacyShowName;
    @FXML private CheckBox settingsPrivacyShowProfile;
    @FXML private CheckBox settingsPrivacyHideAmount;
    @FXML private PasswordField settingsOldPasswordField;
    @FXML private PasswordField settingsNewPasswordField;
    @FXML private PasswordField settingsConfirmPasswordField;

    /* ==================================================
       DASHBOARD DONATUR
       ================================================== */
    @FXML private Label donorTotalDonationLabel;
    @FXML private Label donorCampaignSupportedLabel;
    @FXML private Label donorActiveCampaignLabel;
    @FXML private Label donorImpactLabel;
    @FXML private HBox donorRecommendationBox;
    @FXML private Label donorMonthlyTotalLabel;
    @FXML private Label donorMonthlyTargetLabel;
    @FXML private ProgressBar donorMonthlyProgress;

    /* ==================================================
       DASHBOARD PENGGALANG DANA
       ================================================== */
    @FXML private Label fundraiserCampaignCountLabel;
    @FXML private Label fundraiserTotalCollectedLabel;
    @FXML private Label fundraiserStatusLabel;
    @FXML private Label fundraiserDonorCountLabel;
    @FXML private HBox fundraiserCampaignBox;
    @FXML private Label fundraiserQuickStatusLabel;

    /* ==================================================
       DAFTAR CAMPAIGN
       ================================================== */
    @FXML private TextField campaignSearchField;
    @FXML private ComboBox<String> campaignCategoryFilter;
    @FXML private Label campaignResultLabel;
    @FXML private TilePane campaignGrid;
    @FXML private VBox campaignEmptyState;

    /* ==================================================
       BUAT CAMPAIGN
       ================================================== */
    @FXML private TextField campaignTitleField;
    @FXML private TextArea campaignDescriptionArea;
    @FXML private ComboBox<String> campaignCategoryBox;
    @FXML private TextField campaignTargetField;
    @FXML private DatePicker campaignDeadlinePicker;
    @FXML private Label previewCategoryLabel;
    @FXML private Label previewTitleLabel;
    @FXML private Label previewDescriptionLabel;
    @FXML private Label previewAmountLabel;
    @FXML private Label previewDeadlineLabel;
    @FXML private ImageView previewImageView;
    @FXML private Label previewImagePlaceholder;
    @FXML private Label uploadDescriptionLabel;

    private File selectedCampaignImage;

    /* ==================================================
       DETAIL CAMPAIGN DAN DONASI
       ================================================== */
    @FXML private Label detailCoverCategoryLabel;
    @FXML private Label detailCategoryLabel;
    @FXML private Label detailTitleLabel;
    @FXML private Label detailDescriptionLabel;
    @FXML private Label detailOwnerInitialLabel;
    @FXML private Label detailOwnerNameLabel;
    @FXML private Label detailStatusLabel;
    @FXML private Label detailTotalAmountLabel;
    @FXML private Label detailTargetTextLabel;
    @FXML private ProgressBar detailProgressBar;
    @FXML private Label detailPercentLabel;
    @FXML private Label detailDonorCountLabel;
    @FXML private Label detailDaysLabel;
    @FXML private TextField customDonationField;
    @FXML private TextArea donationMessageArea;
    @FXML private ImageView detailCoverImage;
    @FXML private Label detailStoryParagraph1;
    @FXML private Label detailStoryParagraph2;
    @FXML private Label detailStoryParagraph3;

    private long selectedDonationAmount = 0;
    private Button selectedDonationButton;
    private PaymentMethod selectedPaymentMethod = null;

    /* ==================================================
       RIWAYAT DONASI
       ================================================== */
    @FXML private TextField historySearchField;
    @FXML private ComboBox<String> historyStatusFilter;
    @FXML private DatePicker historyDateFilter;
    @FXML private Label historyResultLabel;
    @FXML private VBox historyRowsContainer;
    @FXML private VBox historyEmptyState;

    private enum PaymentMethod {
        BCA("Transfer Bank - BCA", "Rekening: 1234567890\na.n. CrowdCare Foundation"),
        MANDIRI("Transfer Bank - Mandiri", "Rekening: 0987654321\na.n. CrowdCare Foundation"),
        BNI("Transfer Bank - BNI", "Rekening: 1122334455\na.n. CrowdCare Foundation"),
        GOPAY("E-Wallet - GoPay", "Nomor GoPay: 0812-3456-7890"),
        OVO("E-Wallet - OVO", "Nomor OVO: 0821-9876-5432"),
        DANA("E-Wallet - DANA", "Nomor DANA: 0831-1111-2222"),
        QRIS("QRIS", "Scan kode QRIS di aplikasi pembayaran Anda.");

        private final String label;
        private final String detail;

        PaymentMethod(String label, String detail) {
            this.label = label;
            this.detail = detail;
        }

        public String getLabel() {
            return label;
        }

        public String getDetail() {
            return detail;
        }
    }

    @FXML
    private void initialize() {
        initializeDashboardPage();
        initializeCampaignListPage();
        initializeCreateCampaignPage();
        initializeCampaignDetailPage();
        initializeDonationHistoryPage();
        applyRoleAccess();
    }

    public void applyRoleAccess() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        if (headerNameLabel != null) headerNameLabel.setText(currentUser.getFullName());
        if (headerRoleLabel != null) headerRoleLabel.setText(currentUser.getRoleName());
        if (welcomeTitleLabel != null) {
            welcomeTitleLabel.setText("Selamat datang kembali, " + currentUser.getFullName() + "!");
        }
        if (avatarText != null) {
            loadAvatar(currentUser.getId(), null, avatarText);
        }

        setNodeVisible(btnCreateCampaign, currentUser.canCreateCampaign());
        setNodeVisible(btnDonationHistory, currentUser.canDonate());
        setNodeVisible(btnHeaderCreateCampaign, currentUser.canCreateCampaign());

        boolean canDonateSelected = currentUser.canDonate()
                && selectedCampaign != null
                && "APPROVED".equals(selectedCampaign.getStatus());
        if (donationActionCard != null) {
            setNodeVisible(donationActionCard, canDonateSelected);
        }

        Node anchor = sceneAnchor();
        if (anchor != null && anchor.getScene() != null) {
            List<Node> nodes = new ArrayList<>();
            searchAllNodesRecursively(anchor.getScene().getRoot(), nodes);

            for (Node node : nodes) {
                String text = getNodeText(node);
                if (!currentUser.canCreateCampaign()
                        && (text.equalsIgnoreCase("Buat Campaign")
                        || text.equalsIgnoreCase("+ Buat Campaign")
                        || text.equalsIgnoreCase("Buat Penggalangan Baru"))) {
                    setNodeVisible(node, false);
                }

                if (!currentUser.canDonate()
                        && (text.equalsIgnoreCase("Riwayat Donasi")
                        || text.equalsIgnoreCase("Donasi Sekarang")
                        || text.equalsIgnoreCase("Salurkan Donasi Baru"))) {
                    setNodeVisible(node, false);
                }
            }

            if (!currentUser.canDonate()) {
                for (Node node : anchor.getScene().getRoot().lookupAll(".detail-donation-card")) {
                    setNodeVisible(node, false);
                }
            }
        }

        if (profileRoleBadgeLabel != null) {
            profileRoleBadgeLabel.setText(currentUser.getRoleName().toUpperCase(Locale.ROOT));
        }
        if (profileInfoRoleLabel != null) {
            profileInfoRoleLabel.setText(currentUser.getRoleName());
        }
        if (settingsAccountNameLabel != null) {
            settingsAccountNameLabel.setText(currentUser.getFullName());
        }
        if (settingsAccountEmailLabel != null) {
            settingsAccountEmailLabel.setText(currentUser.getUsername());
        }
    }

    private void initializeDashboardPage() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        if (donorTotalDonationLabel != null) {
            loadDonorDashboard(currentUser);
        }

        if (fundraiserCampaignCountLabel != null) {
            loadFundraiserDashboard(currentUser);
        }
    }

    private void loadDonorDashboard(User currentUser) {
        donationHistory = safeDonationsByDonor(currentUser.getId());
        List<CampaignEntity> approvedCampaigns = safeApprovedCampaigns();

        long totalDonation = donationHistory.stream().mapToLong(d -> amountOrZero(d.getAmount())).sum();
        Set<Long> supportedCampaigns = new LinkedHashSet<>();
        for (DonationEntity donation : donationHistory) {
            if (donation.getCampaign() != null && donation.getCampaign().getId() != null) {
                supportedCampaigns.add(donation.getCampaign().getId());
            }
        }

        long monthlyDonation = donationHistory.stream()
                .filter(d -> d.getDonatedAt() != null)
                .filter(d -> d.getDonatedAt().getMonth().equals(LocalDate.now().getMonth()))
                .filter(d -> d.getDonatedAt().getYear() == LocalDate.now().getYear())
                .mapToLong(d -> amountOrZero(d.getAmount()))
                .sum();

        donorTotalDonationLabel.setText(formatRupiah(totalDonation));
        donorCampaignSupportedLabel.setText(supportedCampaigns.size() + " Program");
        donorActiveCampaignLabel.setText(String.valueOf(approvedCampaigns.size()));
        donorImpactLabel.setText(Math.max(0, supportedCampaigns.size() * 35 + donationHistory.size() * 3) + " Jiwa");

        if (donorMonthlyTotalLabel != null) donorMonthlyTotalLabel.setText(formatRupiah(monthlyDonation));
        if (donorMonthlyTargetLabel != null) {
            donorMonthlyTargetLabel.setText("Target pribadi " + formatRupiah(MONTHLY_DONATION_TARGET));
        }
        if (donorMonthlyProgress != null) {
            donorMonthlyProgress.setProgress(clampProgress((double) monthlyDonation / MONTHLY_DONATION_TARGET));
        }

        if (donorRecommendationBox != null) {
            donorRecommendationBox.getChildren().clear();
            approvedCampaigns.stream()
                    .sorted(Comparator.comparingDouble(this::campaignProgress))
                    .limit(2)
                    .forEach(campaign -> donorRecommendationBox.getChildren().add(buildDashboardCampaignCard(campaign, "Terkumpul")));

            if (donorRecommendationBox.getChildren().isEmpty()) {
                donorRecommendationBox.getChildren().add(buildEmptyCard("Belum ada campaign aktif."));
            }
        }
    }

    private void loadFundraiserDashboard(User currentUser) {
        List<CampaignEntity> campaigns = safeCampaignsByCreator(currentUser.getId());

        long totalCollected = campaigns.stream().mapToLong(c -> amountOrZero(c.getCollectedAmount())).sum();
        long activeCount = campaigns.stream().filter(c -> "APPROVED".equals(c.getStatus())).count();
        long pendingCount = campaigns.stream().filter(c -> "PENDING".equals(c.getStatus())).count();
        long donationCount = campaigns.stream()
                .mapToLong(c -> safeDonationsByCampaign(c.getId()).size())
                .sum();

        fundraiserCampaignCountLabel.setText(campaigns.size() + " Program");
        fundraiserTotalCollectedLabel.setText(formatRupiah(totalCollected));
        fundraiserStatusLabel.setText(activeCount + " aktif / " + pendingCount + " menunggu");
        fundraiserDonorCountLabel.setText(donationCount + " Transaksi");

        if (fundraiserQuickStatusLabel != null) {
            fundraiserQuickStatusLabel.setText(pendingCount > 0
                    ? pendingCount + " campaign masih menunggu verifikasi admin."
                    : "Semua campaign terbaru sudah tersinkron.");
        }

        if (fundraiserCampaignBox != null) {
            fundraiserCampaignBox.getChildren().clear();
            campaigns.stream()
                    .sorted(Comparator.comparing(CampaignEntity::getEndDate,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .limit(2)
                    .forEach(campaign -> fundraiserCampaignBox.getChildren()
                            .add(buildDashboardCampaignCard(campaign, statusText(campaign.getStatus()))));

            if (fundraiserCampaignBox.getChildren().isEmpty()) {
                fundraiserCampaignBox.getChildren().add(buildEmptyCard("Belum ada campaign. Ajukan campaign pertama Anda."));
            }
        }
    }

    private VBox buildDashboardCampaignCard(CampaignEntity campaign, String caption) {
        VBox card = new VBox(11);
        card.setPrefWidth(285);
        card.setUserData(campaign.getId());
        card.getStyleClass().add("campaign-card");

        StackPane cover = new StackPane();
        cover.setPrefHeight(105);
        cover.getStyleClass().add(dashboardCoverStyle(campaign.getCategory()));

        Label category = new Label(campaign.getCategory().toUpperCase(Locale.ROOT));
        category.getStyleClass().add("campaign-category");
        cover.getChildren().add(category);

        Label title = new Label(campaign.getTitle());
        title.setWrapText(true);
        title.getStyleClass().add("campaign-title");

        Label smallLabel = new Label(caption);
        smallLabel.getStyleClass().add("campaign-small-label");

        Label amount = new Label(formatRupiah(amountOrZero(campaign.getCollectedAmount()))
                + " dari " + formatRupiah(amountOrZero(campaign.getTargetAmount())));
        amount.getStyleClass().add("campaign-amount");

        ProgressBar progress = new ProgressBar(campaignProgress(campaign));
        progress.setPrefWidth(250);
        progress.getStyleClass().add("campaign-progress");

        HBox meta = new HBox();
        meta.setAlignment(Pos.CENTER_LEFT);
        Label percent = new Label(percentText(campaign) + " tercapai");
        percent.getStyleClass().add("campaign-percentage");
        Label days = new Label(daysText(campaign));
        days.getStyleClass().add("campaign-days");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        meta.getChildren().addAll(percent, spacer, days);

        Button detailButton = new Button("Lihat Detail");
        detailButton.setPrefHeight(34);
        detailButton.setMaxWidth(Double.MAX_VALUE);
        detailButton.setUserData(campaign.getId());
        detailButton.getStyleClass().add("campaign-detail-button");
        detailButton.setOnAction(this::openCampaignDetail);

        card.getChildren().addAll(cover, title, smallLabel, amount, progress, meta, detailButton);
        return card;
    }

    private VBox buildEmptyCard(String message) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(285);
        box.setPrefHeight(190);
        box.getStyleClass().add("campaign-card");
        Label title = new Label(message);
        title.setWrapText(true);
        title.getStyleClass().add("campaign-description");
        box.getChildren().add(title);
        return box;
    }

    private void initializeCampaignListPage() {
        if (campaignSearchField == null || campaignCategoryFilter == null || campaignGrid == null) return;

        campaignCategoryFilter.getItems().setAll(
                "Semua Kategori", "Pendidikan", "Kesehatan", "Sosial", "Bencana", "Lingkungan"
        );
        campaignCategoryFilter.setValue("Semua Kategori");

        campaignList = loadCampaignsForCurrentUser();
        campaignSearchField.textProperty().addListener((obs, old, value) -> applyCampaignFilter());
        campaignCategoryFilter.valueProperty().addListener((obs, old, value) -> applyCampaignFilter());

        applyCampaignFilter();
    }

    private List<CampaignEntity> loadCampaignsForCurrentUser() {
        User currentUser = currentUser();
        List<CampaignEntity> result;

        if (currentUser != null && currentUser.canCreateCampaign() && !currentUser.canDonate()) {
            result = safeCampaignsByCreator(currentUser.getId());
        } else {
            result = safeApprovedCampaigns();
        }

        result.sort(Comparator.comparing(CampaignEntity::getEndDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return result;
    }

    private void applyCampaignFilter() {
        if (campaignGrid == null || campaignSearchField == null || campaignCategoryFilter == null) return;

        String query = campaignSearchField.getText() == null
                ? ""
                : campaignSearchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedCategory = campaignCategoryFilter.getValue();

        List<CampaignEntity> filtered = campaignList.stream()
                .filter(campaign -> matchesCampaignQuery(campaign, query))
                .filter(campaign -> selectedCategory == null
                        || selectedCategory.equals("Semua Kategori")
                        || selectedCategory.equalsIgnoreCase(campaign.getCategory()))
                .toList();

        campaignGrid.getChildren().clear();
        for (CampaignEntity campaign : filtered) {
            campaignGrid.getChildren().add(buildCampaignListCard(campaign));
        }

        if (campaignResultLabel != null) {
            campaignResultLabel.setText(filtered.size() == 1
                    ? "1 campaign ditemukan"
                    : filtered.size() + " campaign ditemukan");
        }
        if (campaignEmptyState != null) {
            boolean empty = filtered.isEmpty();
            campaignEmptyState.setVisible(empty);
            campaignEmptyState.setManaged(empty);
        }
    }

    private boolean matchesCampaignQuery(CampaignEntity campaign, String query) {
        if (query.isBlank()) return true;
        return containsIgnoreCase(campaign.getTitle(), query)
                || containsIgnoreCase(campaign.getCategory(), query)
                || containsIgnoreCase(campaign.getDescription(), query)
                || containsIgnoreCase(statusText(campaign.getStatus()), query);
    }

    private VBox buildCampaignListCard(CampaignEntity campaign) {
        VBox card = new VBox(11);
        card.setPrefWidth(275);
        card.setUserData(campaign.getId());
        card.getStyleClass().add("campaign-list-card");

        StackPane cover = new StackPane();
        cover.setPrefHeight(135);
        cover.getStyleClass().add(listCoverStyle(campaign.getCategory()));
        Label category = new Label(campaign.getCategory().toUpperCase(Locale.ROOT));
        category.getStyleClass().add("campaign-category");
        cover.getChildren().add(category);

        Label title = new Label(campaign.getTitle());
        title.setWrapText(true);
        title.getStyleClass().add("campaign-list-title");

        Label description = new Label(truncate(campaign.getDescription(), 92));
        description.setWrapText(true);
        description.getStyleClass().add("campaign-description");

        Label amount = new Label(formatRupiah(amountOrZero(campaign.getCollectedAmount())) + " terkumpul");
        amount.getStyleClass().add("campaign-list-amount");

        ProgressBar progress = new ProgressBar(campaignProgress(campaign));
        progress.setPrefWidth(245);
        progress.getStyleClass().add("campaign-progress");

        HBox meta = new HBox();
        meta.setAlignment(Pos.CENTER_LEFT);
        Label percent = new Label(percentText(campaign));
        percent.getStyleClass().add("campaign-percentage");
        Label right = new Label("APPROVED".equals(campaign.getStatus()) ? daysText(campaign) : statusText(campaign.getStatus()));
        right.getStyleClass().add("campaign-days");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        meta.getChildren().addAll(percent, spacer, right);

        Button detailButton = new Button("Lihat Detail");
        detailButton.setPrefWidth(245);
        detailButton.setPrefHeight(38);
        detailButton.setUserData(campaign.getId());
        detailButton.getStyleClass().add("campaign-detail-button");
        detailButton.setOnAction(this::openCampaignDetail);

        card.getChildren().addAll(cover, title, description, amount, progress, meta, detailButton);
        return card;
    }

    @FXML
    private void handleResetCampaignFilter() {
        if (campaignSearchField != null) campaignSearchField.clear();
        if (campaignCategoryFilter != null) campaignCategoryFilter.setValue("Semua Kategori");
        applyCampaignFilter();
    }

    private void initializeCreateCampaignPage() {
        if (campaignCategoryBox == null) return;

        campaignCategoryBox.getItems().setAll("Pendidikan", "Kesehatan", "Sosial", "Bencana", "Lingkungan");
        campaignTitleField.textProperty().addListener((obs, old, value) -> updateCampaignPreview());
        campaignDescriptionArea.textProperty().addListener((obs, old, value) -> updateCampaignPreview());
        campaignCategoryBox.valueProperty().addListener((obs, old, value) -> updateCampaignPreview());
        campaignDeadlinePicker.valueProperty().addListener((obs, old, value) -> updateCampaignPreview());
        campaignTargetField.textProperty().addListener((obs, old, value) -> {
            String sanitizedValue = value.replaceAll("\\D", "");
            if (!value.equals(sanitizedValue)) {
                campaignTargetField.setText(sanitizedValue);
                return;
            }
            updateCampaignPreview();
        });

        restoreDraftIfAvailable();
        updateCampaignPreview();
    }

    private void updateCampaignPreview() {
        if (campaignTitleField == null || campaignDescriptionArea == null || campaignCategoryBox == null
                || campaignTargetField == null || campaignDeadlinePicker == null || previewCategoryLabel == null
                || previewTitleLabel == null || previewDescriptionLabel == null || previewAmountLabel == null
                || previewDeadlineLabel == null) {
            return;
        }

        String title = campaignTitleField.getText().trim();
        String description = campaignDescriptionArea.getText().trim();
        String category = campaignCategoryBox.getValue();
        String targetText = campaignTargetField.getText().trim();
        LocalDate deadline = campaignDeadlinePicker.getValue();

        previewCategoryLabel.setText(category == null ? "KATEGORI" : category.toUpperCase(Locale.ROOT));
        previewTitleLabel.setText(title.isEmpty() ? "Judul campaign akan tampil di sini" : title);
        previewDescriptionLabel.setText(description.isEmpty()
                ? "Deskripsi singkat campaign akan tampil di bagian ini."
                : truncate(description, 130));
        previewAmountLabel.setText(targetText.isEmpty() ? "Target dana belum diisi" : "Target " + formatRupiah(parseLongOrZero(targetText)));
        previewDeadlineLabel.setText(deadline == null ? "Belum ditentukan" : daysText(deadline));
    }

    @FXML
    private void handleChooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar Campaign");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Gambar JPG/PNG", "*.jpg", "*.jpeg", "*.png")
        );

        File selectedFile = chooser.showOpenDialog(ownerWindow());
        if (selectedFile == null) return;

        if (selectedFile.length() > 5 * 1024 * 1024) {
            showAlert(Alert.AlertType.WARNING, "Gambar Terlalu Besar", "Ukuran gambar maksimal 5 MB.");
            return;
        }

        selectedCampaignImage = selectedFile;
        try {
            Image image = new Image(selectedFile.toURI().toString());
            if (previewImageView != null) {
                previewImageView.setImage(image);
                previewImageView.setVisible(true);
                previewImageView.setManaged(true);
            }
            if (previewImagePlaceholder != null) {
                previewImagePlaceholder.setVisible(false);
                previewImagePlaceholder.setManaged(false);
            }
            if (uploadDescriptionLabel != null) uploadDescriptionLabel.setText(selectedFile.getName());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Gambar", "File gambar tidak dapat ditampilkan.");
        }
    }

    @FXML
    private void handleSaveDraft() {
        if (campaignTitleField == null) return;

        boolean formEmpty = campaignTitleField.getText().trim().isEmpty()
                && campaignDescriptionArea.getText().trim().isEmpty()
                && campaignCategoryBox.getValue() == null
                && campaignTargetField.getText().trim().isEmpty()
                && campaignDeadlinePicker.getValue() == null
                && selectedCampaignImage == null;

        if (formEmpty) {
            showAlert(Alert.AlertType.WARNING, "Draft Kosong", "Isi setidaknya satu bagian form sebelum menyimpan draft.");
            return;
        }

        savedDraft = new CampaignDraft(
                campaignTitleField.getText(),
                campaignDescriptionArea.getText(),
                campaignCategoryBox.getValue(),
                campaignTargetField.getText(),
                campaignDeadlinePicker.getValue(),
                selectedCampaignImage
        );
        showAlert(Alert.AlertType.INFORMATION, "Draft Disimpan", "Draft tersimpan selama sesi aplikasi masih aktif.");
    }

    @FXML
    private void handleSubmitCampaign() {
        User currentUser = currentUser();
        if (currentUser == null || !currentUser.canCreateCampaign()) {
            showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Hanya Penggalang Dana yang bisa membuat campaign.");
            return;
        }

        String title = campaignTitleField.getText().trim();
        String description = campaignDescriptionArea.getText().trim();
        String category = campaignCategoryBox.getValue();
        String targetText = campaignTargetField.getText().trim();

        if (title.length() < 5) {
            showAlert(Alert.AlertType.WARNING, "Judul Terlalu Pendek", "Judul campaign minimal terdiri dari 5 karakter.");
            campaignTitleField.requestFocus();
            return;
        }
        if (description.length() < 20) {
            showAlert(Alert.AlertType.WARNING, "Deskripsi Terlalu Pendek", "Deskripsi campaign minimal terdiri dari 20 karakter.");
            campaignDescriptionArea.requestFocus();
            return;
        }
        if (category == null) {
            showAlert(Alert.AlertType.WARNING, "Kategori Belum Dipilih", "Pilih kategori campaign terlebih dahulu.");
            campaignCategoryBox.requestFocus();
            return;
        }

        long targetAmount;
        try {
            targetAmount = Long.parseLong(targetText);
            if (targetAmount < 100_000) {
                showAlert(Alert.AlertType.WARNING, "Target Dana Terlalu Kecil", "Target dana minimal Rp100.000.");
                campaignTargetField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Target Dana Tidak Valid", "Masukkan target dana menggunakan angka.");
            campaignTargetField.requestFocus();
            return;
        }

        if (campaignDeadlinePicker.getValue() == null || !campaignDeadlinePicker.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Tanggal Tidak Valid", "Batas waktu harus setelah tanggal hari ini.");
            campaignDeadlinePicker.requestFocus();
            return;
        }
        if (selectedCampaignImage == null) {
            showAlert(Alert.AlertType.WARNING, "Gambar Belum Dipilih", "Pilih gambar campaign terlebih dahulu.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Ajukan Campaign");
        confirmation.setHeaderText("Ajukan campaign \"" + title + "\"?");
        confirmation.setContentText("Kategori: " + category
                + "\nTarget dana: " + formatRupiah(targetAmount)
                + "\n\nCampaign akan dikirim kepada admin untuk diverifikasi.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        try {
            byte[] imageBytes = null;
            String mimeType = null;
            if (selectedCampaignImage != null && selectedCampaignImage.exists()) {
                imageBytes = java.nio.file.Files.readAllBytes(selectedCampaignImage.toPath());
                String name = selectedCampaignImage.getName().toLowerCase(Locale.ROOT);
                mimeType = name.endsWith(".png") ? "image/png" : "image/jpeg";
            }

            CampaignEntity campaign = campaignService().createCampaign(
                    title,
                    description,
                    targetAmount,
                    LocalDate.now(),
                    campaignDeadlinePicker.getValue(),
                    category,
                    currentUser.getId(),
                    imageBytes,
                    mimeType
            );
            selectedCampaignId = campaign.getId();
            selectedCampaignTitle = campaign.getTitle();
            savedDraft = null;
            showAlert(Alert.AlertType.INFORMATION, "Campaign Berhasil Diajukan",
                    "Campaign berhasil disimpan dan menunggu persetujuan admin.");
            resetCreateCampaignForm();
        } catch (IllegalArgumentException exception) {
            showAlert(Alert.AlertType.WARNING, "Gagal Mengajukan Campaign", exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Mengajukan Campaign", "Terjadi kesalahan saat menyimpan campaign.");
        }
    }

    private void restoreDraftIfAvailable() {
        if (savedDraft == null || campaignTitleField == null) return;

        campaignTitleField.setText(savedDraft.title);
        campaignDescriptionArea.setText(savedDraft.description);
        campaignCategoryBox.setValue(savedDraft.category);
        campaignTargetField.setText(savedDraft.target);
        campaignDeadlinePicker.setValue(savedDraft.deadline);
        selectedCampaignImage = savedDraft.imageFile;

        if (selectedCampaignImage != null && selectedCampaignImage.exists() && uploadDescriptionLabel != null) {
            uploadDescriptionLabel.setText(selectedCampaignImage.getName());
            try {
                Image image = new Image(selectedCampaignImage.toURI().toString());
                if (previewImageView != null) {
                    previewImageView.setImage(image);
                    previewImageView.setVisible(true);
                    previewImageView.setManaged(true);
                }
                if (previewImagePlaceholder != null) {
                    previewImagePlaceholder.setVisible(false);
                    previewImagePlaceholder.setManaged(false);
                }
            } catch (Exception ignored) {
                selectedCampaignImage = null;
            }
        }
    }

    private void resetCreateCampaignForm() {
        campaignTitleField.clear();
        campaignDescriptionArea.clear();
        campaignCategoryBox.getSelectionModel().clearSelection();
        campaignTargetField.clear();
        campaignDeadlinePicker.setValue(null);
        selectedCampaignImage = null;

        if (previewImageView != null) {
            previewImageView.setImage(null);
            previewImageView.setVisible(false);
            previewImageView.setManaged(false);
        }
        if (previewImagePlaceholder != null) {
            previewImagePlaceholder.setVisible(true);
            previewImagePlaceholder.setManaged(true);
        }
        if (uploadDescriptionLabel != null) {
            uploadDescriptionLabel.setText("Format JPG atau PNG, maksimal 5 MB");
        }
        updateCampaignPreview();
    }

    private void initializeCampaignDetailPage() {
        if (detailTitleLabel == null) return;

        selectedCampaign = resolveSelectedCampaign();
        if (selectedCampaign == null) {
            detailTitleLabel.setText(selectedCampaignTitle);
            return;
        }

        selectedCampaignId = selectedCampaign.getId();
        selectedCampaignTitle = selectedCampaign.getTitle();

        if (detailCoverCategoryLabel != null) {
            detailCoverCategoryLabel.setText(selectedCampaign.getCategory().toUpperCase(Locale.ROOT));
        }
        if (detailCoverImage != null && selectedCampaign.getImage() != null) {
            try {
                Image image = new Image(new java.io.ByteArrayInputStream(selectedCampaign.getImage()));
                detailCoverImage.setImage(image);
                detailCoverImage.setVisible(true);
                detailCoverImage.setManaged(true);
            } catch (Exception e) {
                detailCoverImage.setImage(null);
                detailCoverImage.setVisible(false);
                detailCoverImage.setManaged(false);
            }
        } else if (detailCoverImage != null) {
            detailCoverImage.setImage(null);
            detailCoverImage.setVisible(false);
            detailCoverImage.setManaged(false);
        }
        if (detailCategoryLabel != null) {
            detailCategoryLabel.setText(selectedCampaign.getCategory().toUpperCase(Locale.ROOT));
        }
        detailTitleLabel.setText(selectedCampaign.getTitle());
        if (detailDescriptionLabel != null) {
            detailDescriptionLabel.setText(selectedCampaign.getDescription());
        }
        if (detailOwnerInitialLabel != null && selectedCampaign.getCreator() != null) {
            detailOwnerInitialLabel.setText(initials(selectedCampaign.getCreator().getFullName()));
        }
        if (detailOwnerNameLabel != null && selectedCampaign.getCreator() != null) {
            detailOwnerNameLabel.setText(selectedCampaign.getCreator().getFullName());
        }
        if (detailStatusLabel != null) {
            detailStatusLabel.setText(statusText(selectedCampaign.getStatus()));
            detailStatusLabel.getStyleClass().removeAll("campaign-status-approved", "campaign-status-pending", "campaign-status-rejected");
            detailStatusLabel.getStyleClass().add(statusStyle(selectedCampaign.getStatus()));
        }
        if (detailTotalAmountLabel != null) {
            detailTotalAmountLabel.setText(formatRupiah(amountOrZero(selectedCampaign.getCollectedAmount())));
        }
        if (detailTargetTextLabel != null) {
            detailTargetTextLabel.setText("dari target " + formatRupiah(amountOrZero(selectedCampaign.getTargetAmount())));
        }
        if (detailProgressBar != null) {
            detailProgressBar.setProgress(campaignProgress(selectedCampaign));
        }
        if (detailPercentLabel != null) {
            detailPercentLabel.setText(percentText(selectedCampaign));
        }
        if (detailDonorCountLabel != null) {
            detailDonorCountLabel.setText(String.valueOf(safeDonationsByCampaign(selectedCampaign.getId()).size()));
        }
        if (detailDaysLabel != null) {
            detailDaysLabel.setText(daysNumberText(selectedCampaign));
        }
        if (detailStoryParagraph1 != null) {
            detailStoryParagraph1.setText(selectedCampaign.getDescription());
        }
        if (detailStoryParagraph2 != null) {
            detailStoryParagraph2.setText("Dana yang terkumpul dicatat langsung di sistem CrowdCare sehingga progres campaign dapat dipantau oleh donatur dan penggalang dana.");
        }
        if (detailStoryParagraph3 != null) {
            detailStoryParagraph3.setText("Campaign berstatus " + statusText(selectedCampaign.getStatus()).toLowerCase(Locale.ROOT)
                    + " dengan batas waktu " + formatDate(selectedCampaign.getEndDate()) + ".");
        }

        if (customDonationField != null) {
            customDonationField.textProperty().addListener((obs, old, value) -> {
                String sanitizedValue = value.replaceAll("\\D", "");
                if (!value.equals(sanitizedValue)) {
                    customDonationField.setText(sanitizedValue);
                }
            });
        }
    }

    private CampaignEntity resolveSelectedCampaign() {
        if (selectedCampaignId != null) {
            Optional<CampaignEntity> campaign = campaignService().getCampaignById(selectedCampaignId);
            if (campaign.isPresent()) return campaign.get();
        }

        return safeApprovedCampaigns().stream()
                .filter(campaign -> campaign.getTitle().equalsIgnoreCase(selectedCampaignTitle))
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void handleSelectDonationAmount(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (clickedButton.getUserData() == null) return;

        try {
            selectedDonationAmount = Long.parseLong(clickedButton.getUserData().toString());
            if (customDonationField != null) {
                customDonationField.clear();
                customDonationField.setVisible(false);
                customDonationField.setManaged(false);
            }
            updateSelectedDonationButton(clickedButton);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Nominal Tidak Valid", "Nominal donasi tidak dapat dibaca.");
        }
    }

    @FXML
    private void handleCustomDonation(ActionEvent event) {
        selectedDonationAmount = 0;
        updateSelectedDonationButton((Button) event.getSource());
        if (customDonationField != null) {
            customDonationField.setVisible(true);
            customDonationField.setManaged(true);
            customDonationField.clear();
            customDonationField.requestFocus();
        }
    }

    private void updateSelectedDonationButton(Button button) {
        if (selectedDonationButton != null) {
            selectedDonationButton.getStyleClass().remove("donation-option-selected");
        }
        selectedDonationButton = button;
        if (!button.getStyleClass().contains("donation-option-selected")) {
            button.getStyleClass().add("donation-option-selected");
        }
    }

    @FXML
    private void handleDonation() {
        User currentUser = currentUser();
        if (currentUser == null || !currentUser.canDonate()) {
            showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Hanya Donatur yang bisa berdonasi.");
            return;
        }

        CampaignEntity campaign = selectedCampaign != null ? selectedCampaign : resolveSelectedCampaign();
        if (campaign == null) {
            showAlert(Alert.AlertType.WARNING, "Campaign Tidak Ditemukan", "Pilih campaign terlebih dahulu.");
            return;
        }
        if (!"APPROVED".equals(campaign.getStatus())) {
            showAlert(Alert.AlertType.WARNING, "Campaign Belum Aktif", "Campaign belum bisa menerima donasi.");
            return;
        }

        long donationAmount = selectedDonationAmount;
        if (customDonationField != null && customDonationField.isVisible()) {
            String customText = customDonationField.getText().replaceAll("\\D", "");
            if (customText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Nominal Belum Diisi", "Masukkan nominal donasi terlebih dahulu.");
                return;
            }
            donationAmount = parseLongOrZero(customText);
        }

        if (donationAmount < 10_000) {
            showAlert(Alert.AlertType.WARNING, "Nominal Terlalu Kecil", "Nominal donasi minimal Rp10.000.");
            return;
        }

        PaymentMethod chosenMethod = showPaymentMethodDialog();
        if (chosenMethod == null) return;

        selectedPaymentMethod = chosenMethod;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Konfirmasi Donasi");
        confirmation.setHeaderText("Donasi untuk " + campaign.getTitle());
        confirmation.setContentText("Nominal : " + formatRupiah(donationAmount)
                + "\nMetode: " + chosenMethod.getLabel()
                + "\n\n" + chosenMethod.getDetail());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        try {
            String message = donationMessageArea == null ? "" : donationMessageArea.getText().trim();
            String storedMessage = chosenMethod.getLabel() + (message.isBlank() ? "" : " - " + message);
            donationService().donate(currentUser.getId(), campaign.getId(), donationAmount, storedMessage);
            selectedCampaign = campaignService().getCampaignById(campaign.getId()).orElse(campaign);
            showAlert(Alert.AlertType.INFORMATION, "Donasi Berhasil",
                    "Terima kasih! Donasi sebesar " + formatRupiah(donationAmount) + " berhasil dicatat.");
            resetDonationState();
            initializeCampaignDetailPage();
            applyRoleAccess();
        } catch (IllegalArgumentException exception) {
            showAlert(Alert.AlertType.WARNING, "Donasi Gagal", exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Donasi Gagal", "Terjadi kesalahan saat menyimpan donasi.");
        }
    }

    private PaymentMethod showPaymentMethodDialog() {
        PaymentMethod[] result = {null};
        Stage dialog = new Stage();
        dialog.setTitle("Pilih Metode Pembayaran");
        dialog.setResizable(false);

        Label titleLabel = new Label("Pilih Metode Pembayaran");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
        Label subtitleLabel = new Label("Pilih salah satu metode pembayaran di bawah ini");
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #6b7280;");
        VBox header = new VBox(4, titleLabel, subtitleLabel);
        header.setStyle("-fx-padding: 24 24 16 24; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");

        Label bankGroupLabel = new Label("Transfer Bank");
        bankGroupLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        HBox bankRow = new HBox(10);
        for (PaymentMethod method : new PaymentMethod[]{PaymentMethod.BCA, PaymentMethod.MANDIRI, PaymentMethod.BNI}) {
            bankRow.getChildren().add(buildPaymentCard(method, result, dialog));
        }

        Label walletGroupLabel = new Label("E-Wallet dan QRIS");
        walletGroupLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151; -fx-padding: 16 0 8 0;");
        HBox walletRow = new HBox(10);
        for (PaymentMethod method : new PaymentMethod[]{PaymentMethod.GOPAY, PaymentMethod.OVO, PaymentMethod.DANA, PaymentMethod.QRIS}) {
            walletRow.getChildren().add(buildPaymentCard(method, result, dialog));
        }

        VBox body = new VBox(bankGroupLabel, bankRow, walletGroupLabel, walletRow);
        body.setStyle("-fx-padding: 20 24 4 24;");

        Button cancelButton = new Button("Batal");
        cancelButton.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 8px; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> dialog.close());

        HBox footer = new HBox(cancelButton);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setStyle("-fx-padding: 16 24 20 24; -fx-border-color: #e5e7eb; -fx-border-width: 1 0 0 0;");

        VBox root = new VBox(header, body, footer);
        dialog.setScene(new Scene(root, 650, 440));
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        Window owner = ownerWindow();
        if (owner != null) dialog.initOwner(owner);
        dialog.centerOnScreen();
        dialog.showAndWait();

        return result[0];
    }

    private VBox buildPaymentCard(PaymentMethod method, PaymentMethod[] result, Stage dialog) {
        Label codeLabel = new Label(method.name());
        codeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
        Label nameLabel = new Label(paymentShortName(method));
        nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        Label subLabel = new Label(paymentGroup(method));
        subLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #9ca3af;");

        VBox card = new VBox(6, codeLabel, nameLabel, subLabel);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(140);
        card.setPrefHeight(90);
        card.setStyle(paymentCardStyle(false));
        card.setOnMouseEntered(e -> card.setStyle(paymentCardStyle(true)));
        card.setOnMouseExited(e -> card.setStyle(paymentCardStyle(false)));
        card.setOnMouseClicked(e -> {
            result[0] = method;
            dialog.close();
        });
        return card;
    }

    private String paymentCardStyle(boolean hover) {
        return hover
                ? "-fx-background-color: #eff6ff; -fx-background-radius: 12px; -fx-border-color: #3b82f6; -fx-border-width: 2px; -fx-cursor: hand; -fx-padding: 12 8;"
                : "-fx-background-color: #f9fafb; -fx-background-radius: 12px; -fx-border-color: #e5e7eb; -fx-border-width: 1.5px; -fx-cursor: hand; -fx-padding: 12 8;";
    }

    private String paymentShortName(PaymentMethod method) {
        return switch (method) {
            case BCA, MANDIRI, BNI -> "Transfer Bank";
            case GOPAY, OVO, DANA -> "E-Wallet";
            case QRIS -> "Scan QRIS";
        };
    }

    private String paymentGroup(PaymentMethod method) {
        return switch (method) {
            case BCA, MANDIRI, BNI -> "Bank";
            case GOPAY, OVO, DANA -> "Dompet digital";
            case QRIS -> "Kode QR";
        };
    }

    private void resetDonationState() {
        selectedDonationAmount = 0;
        selectedPaymentMethod = null;
        if (selectedDonationButton != null) {
            selectedDonationButton.getStyleClass().remove("donation-option-selected");
            selectedDonationButton = null;
        }
        if (customDonationField != null) {
            customDonationField.clear();
            customDonationField.setVisible(false);
            customDonationField.setManaged(false);
        }
        if (donationMessageArea != null) {
            donationMessageArea.clear();
        }
    }

    private void initializeDonationHistoryPage() {
        if (historySearchField == null || historyStatusFilter == null || historyDateFilter == null || historyRowsContainer == null) {
            return;
        }

        User currentUser = currentUser();
        donationHistory = currentUser != null && currentUser.canDonate()
                ? safeDonationsByDonor(currentUser.getId())
                : new ArrayList<>();

        historyStatusFilter.getItems().setAll("Semua Status", "Berhasil");
        historyStatusFilter.setValue("Semua Status");

        historySearchField.textProperty().addListener((obs, old, value) -> applyDonationHistoryFilter());
        historyStatusFilter.valueProperty().addListener((obs, old, value) -> applyDonationHistoryFilter());
        historyDateFilter.valueProperty().addListener((obs, old, value) -> applyDonationHistoryFilter());

        applyDonationHistoryFilter();
    }

    @FXML
    private void handleFilter() {
        applyDonationHistoryFilter();
    }

    private void applyDonationHistoryFilter() {
        if (historyRowsContainer == null || historySearchField == null || historyStatusFilter == null || historyDateFilter == null) {
            return;
        }

        String searchQuery = historySearchField.getText() == null
                ? ""
                : historySearchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedStatus = historyStatusFilter.getValue();
        LocalDate selectedDate = historyDateFilter.getValue();

        List<DonationEntity> filtered = donationHistory.stream()
                .filter(donation -> matchesDonationQuery(donation, searchQuery))
                .filter(donation -> selectedStatus == null || selectedStatus.equals("Semua Status") || selectedStatus.equals("Berhasil"))
                .filter(donation -> selectedDate == null
                        || (donation.getDonatedAt() != null && donation.getDonatedAt().toLocalDate().equals(selectedDate)))
                .toList();

        historyRowsContainer.getChildren().clear();
        for (DonationEntity donation : filtered) {
            historyRowsContainer.getChildren().add(buildDonationHistoryRow(donation));
        }

        if (historyResultLabel != null) {
            historyResultLabel.setText(filtered.size() == 1
                    ? "1 transaksi ditemukan"
                    : filtered.size() + " transaksi ditemukan");
        }
        if (historyEmptyState != null) {
            boolean empty = filtered.isEmpty();
            historyEmptyState.setVisible(empty);
            historyEmptyState.setManaged(empty);
        }
    }

    private boolean matchesDonationQuery(DonationEntity donation, String query) {
        if (query.isBlank()) return true;
        return donation.getCampaign() != null && containsIgnoreCase(donation.getCampaign().getTitle(), query);
    }

    private HBox buildDonationHistoryRow(DonationEntity donation) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("history-transaction-row");

        Label campaign = new Label(donation.getCampaign() == null ? "-" : donation.getCampaign().getTitle());
        campaign.setPrefWidth(330);
        campaign.getStyleClass().add("history-title-label");

        Label date = new Label(donation.getDonatedAt() == null ? "-" : DATE_TIME_FORMAT.format(donation.getDonatedAt()));
        date.setPrefWidth(155);
        date.getStyleClass().add("history-muted-label");

        Label amount = new Label(formatRupiah(amountOrZero(donation.getAmount())));
        amount.setPrefWidth(135);
        amount.getStyleClass().add("history-amount-label");

        Label status = new Label("Berhasil");
        status.setPrefWidth(90);
        status.getStyleClass().add("history-status-success");

        Button detail = new Button("Detail");
        detail.setUserData(donation.getCampaign() == null ? null : donation.getCampaign().getId());
        detail.getStyleClass().add("history-detail-button");
        detail.setOnAction(this::openCampaignDetail);

        row.getChildren().addAll(campaign, date, amount, status, detail);
        return row;
    }

    @FXML
    private void handleReset() {
        if (historySearchField != null) historySearchField.clear();
        if (historyStatusFilter != null) historyStatusFilter.setValue("Semua Status");
        if (historyDateFilter != null) historyDateFilter.setValue(null);
        applyDonationHistoryFilter();
    }

    /* ==================================================
       NAVIGASI
       ================================================== */
    @FXML
    private void openDashboard(ActionEvent event) {
        User currentUser = currentUser();
        if (currentUser == null) {
            openPage(event, "/view/dashboard.fxml", "CrowdCare - Dashboard");
            return;
        }
        openPage(event, currentUser.getDashboardFxml(), currentUser.getWindowTitle());
    }

    @FXML
    private void openCampaigns(ActionEvent event) {
        openPage(event, "/view/campaigns.fxml", "CrowdCare - Campaign");
    }

    @FXML
    private void openCreateCampaign(ActionEvent event) {
        User currentUser = currentUser();
        if (currentUser == null || !currentUser.canCreateCampaign()) {
            showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Hanya Penggalang Dana yang bisa membuat campaign.");
            return;
        }
        openPage(event, "/view/create-campaign.fxml", "CrowdCare - Buat Campaign");
    }

    @FXML
    private void openDonationHistory(ActionEvent event) {
        User currentUser = currentUser();
        if (currentUser == null || !currentUser.canDonate()) {
            showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Hanya Donatur yang memiliki riwayat donasi.");
            return;
        }
        openPage(event, "/view/donation-history.fxml", "CrowdCare - Riwayat Donasi");
    }

    @FXML
    private void showProfile(ActionEvent event) {
        openPage(event, "/view/profile.fxml", "CrowdCare - Profil Saya");
        loadProfilePage();
    }

    private void loadProfilePage() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        com.crowdcare.entity.UserEntity entity = databaseUserService().findEntityById(currentUser.getId());
        if (entity == null) return;

        loadAvatar(currentUser.getId(), profileAvatarImage, profileAvatarText);
        if (profileDisplayName != null)
            profileDisplayName.setText(currentUser.getFullName());
        if (profileDisplayEmail != null)
            profileDisplayEmail.setText(entity.getEmail() != null ? entity.getEmail() : currentUser.getUsername());
        if (profileInfoRoleLabel != null)
            profileInfoRoleLabel.setText(currentUser.getRoleName());
        if (profileRoleBadgeLabel != null)
            profileRoleBadgeLabel.setText(currentUser.getRoleName().toUpperCase(Locale.ROOT));
        if (profileInfoIdLabel != null)
            profileInfoIdLabel.setText(entity.getId());
        if (profileInfoJoinLabel != null)
            profileInfoJoinLabel.setText(entity.getCreatedAt() != null
                    ? entity.getCreatedAt().format(DateTimeFormatter.ofPattern("d MMMM yyyy", ID_LOCALE))
                    : "-");
        if (profileInfoLoginLabel != null)
            profileInfoLoginLabel.setText(entity.getLastLogin() != null
                    ? entity.getLastLogin().format(DATE_TIME_FORMAT)
                    : "Baru pertama kali");

        if (profileNameField != null)
            profileNameField.setText(currentUser.getFullName());
        if (profilePhoneField != null)
            profilePhoneField.setText(entity.getPhone() != null ? entity.getPhone() : "");
        if (profileEmailField != null)
            profileEmailField.setText(entity.getEmail() != null ? entity.getEmail() : "");
        if (profileAddressField != null)
            profileAddressField.setText(entity.getAddress() != null ? entity.getAddress() : "");
        if (profileBioArea != null)
            profileBioArea.setText(entity.getBio() != null ? entity.getBio() : "");

        loadProfileStats(currentUser, entity);
    }

    private void loadProfileStats(User currentUser, com.crowdcare.entity.UserEntity entity) {
        List<DonationEntity> donations = safeDonationsByDonor(currentUser.getId());
        List<CampaignEntity> campaigns = safeCampaignsByCreator(currentUser.getId());
        List<CampaignEntity> approvedCampaigns = safeApprovedCampaigns();

        long totalDonasi = donations.stream().mapToLong(d -> amountOrZero(d.getAmount())).sum();
        Set<Long> supportedIds = new LinkedHashSet<>();
        for (DonationEntity d : donations) {
            if (d.getCampaign() != null && d.getCampaign().getId() != null)
                supportedIds.add(d.getCampaign().getId());
        }
        long activeCampaigns = campaigns.stream().filter(c -> "APPROVED".equals(c.getStatus())).count();
        long completedCampaigns = campaigns.stream()
                .filter(c -> c.getEndDate() != null && c.getEndDate().isBefore(LocalDate.now())).count();

        if (profileTotalDonasi != null)
            profileTotalDonasi.setText(formatRupiah(totalDonasi));
        if (profileTotalTransaksi != null)
            profileTotalTransaksi.setText(donations.size() + " transaksi");
        if (profileCampaignSupported != null)
            profileCampaignSupported.setText(String.valueOf(supportedIds.size()));
        if (profileCampaignSelesai != null)
            profileCampaignSelesai.setText(completedCampaigns + " campaign selesai");
        if (profileCampaignDibuat != null)
            profileCampaignDibuat.setText(String.valueOf(campaigns.size()));
        if (profileCampaignAktif != null)
            profileCampaignAktif.setText(activeCampaigns + " campaign aktif");
        if (profileDampakSosial != null)
            profileDampakSosial.setText(String.valueOf(Math.max(0, supportedIds.size() * 35 + donations.size() * 3)));
        if (profileOrangTerbantu != null)
            profileOrangTerbantu.setText("orang terbantu");
    }

    @FXML
    private void showSettings(ActionEvent event) {
        openPage(event, "/view/settings.fxml", "CrowdCare - Pengaturan");
        loadSettingsPage();
    }

    private void loadSettingsPage() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        com.crowdcare.entity.UserEntity entity = databaseUserService().findEntityById(currentUser.getId());
        if (entity == null) return;

        loadAvatar(currentUser.getId(), settingsAvatarImage, settingsAvatarText);
        if (settingsAccountNameLabel != null)
            settingsAccountNameLabel.setText(currentUser.getFullName());
        if (settingsAccountEmailLabel != null)
            settingsAccountEmailLabel.setText(entity.getEmail() != null ? entity.getEmail() : currentUser.getUsername());

        if (settingsLanguageCombo != null) {
            settingsLanguageCombo.getItems().setAll("Bahasa Indonesia", "English");
            boolean hasValue = entity.getLanguage() != null
                    && settingsLanguageCombo.getItems().contains(entity.getLanguage());
            settingsLanguageCombo.setValue(hasValue ? entity.getLanguage() : "Bahasa Indonesia");
        }

        if (settingsDarkModeCheck != null)
            settingsDarkModeCheck.setSelected(entity.getDarkMode() != null && entity.getDarkMode());
        if (settingsAnimationCheck != null)
            settingsAnimationCheck.setSelected(entity.getAnimationEnabled() == null || entity.getAnimationEnabled());
        if (settingsNotifNewCampaign != null)
            settingsNotifNewCampaign.setSelected(entity.getNotifNewCampaign() == null || entity.getNotifNewCampaign());
        if (settingsNotifProgress != null)
            settingsNotifProgress.setSelected(entity.getNotifCampaignProgress() == null || entity.getNotifCampaignProgress());
        if (settingsNotifDonasi != null)
            settingsNotifDonasi.setSelected(entity.getNotifDonationStatus() == null || entity.getNotifDonationStatus());
        if (settingsNotifPromo != null)
            settingsNotifPromo.setSelected(entity.getNotifPromoEmail() != null && entity.getNotifPromoEmail());
        if (settingsNotifReminder != null)
            settingsNotifReminder.setSelected(entity.getNotifCampaignReminder() == null || entity.getNotifCampaignReminder());
        if (settingsPrivacyShowName != null)
            settingsPrivacyShowName.setSelected(entity.getPrivacyShowName() == null || entity.getPrivacyShowName());
        if (settingsPrivacyShowProfile != null)
            settingsPrivacyShowProfile.setSelected(entity.getPrivacyShowProfile() == null || entity.getPrivacyShowProfile());
        if (settingsPrivacyHideAmount != null)
            settingsPrivacyHideAmount.setSelected(entity.getPrivacyHideAmount() != null && entity.getPrivacyHideAmount());
    }

    @FXML
    private void openCampaignDetail(ActionEvent event) {
        Long id = extractCampaignId(event.getSource());
        if (id != null) {
            selectedCampaignId = id;
            selectedCampaign = campaignService().getCampaignById(id).orElse(null);
            if (selectedCampaign != null) selectedCampaignTitle = selectedCampaign.getTitle();
        }

        openPage(event, "/view/campaign-detail.fxml", "CrowdCare - Detail Campaign");
    }

    @FXML
    private void logout(ActionEvent event) {
        UserSession.getInstance().logout();
        try {
            URL loginUrl = MainApplication.class.getResource("/view/login.fxml");
            if (loginUrl == null) throw new IOException("File login.fxml tidak ditemukan.");

            FXMLLoader loader = new FXMLLoader(loginUrl);
            loader.setController(new LoginController());
            Parent root = loader.load();

            Stage stage = getStage(event);
            stage.setTitle("CrowdCare");
            stage.setScene(new Scene(root, 1000, 650));
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (IOException exception) {
            showAlert(Alert.AlertType.ERROR, "Gagal Keluar", exception.getMessage());
        }
    }

    @FXML
    private void handleSaveProfile() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        String name = profileNameField != null ? profileNameField.getText() : currentUser.getFullName();
        String email = profileEmailField != null ? profileEmailField.getText() : "";
        String phone = profilePhoneField != null ? profilePhoneField.getText() : "";
        String address = profileAddressField != null ? profileAddressField.getText() : "";
        String bio = profileBioArea != null ? profileBioArea.getText() : "";

        try {
            com.crowdcare.entity.UserEntity updated = databaseUserService().updateUser(
                    currentUser.getId(), name, email, phone, address, bio);
            UserSession.getInstance().login(
                    databaseUserService().toModel(updated));
            showAlert(Alert.AlertType.INFORMATION, "Profil Disimpan",
                    "Perubahan profil berhasil disimpan ke database.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", e.getMessage());
        }
    }

    @FXML
    private void handleChangePhoto() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Foto Profil");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Gambar", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Node anchor = sceneAnchor();
        Window win = (anchor != null && anchor.getScene() != null) ? anchor.getScene().getWindow() : null;
        File file = (win != null) ? chooser.showOpenDialog(win) : chooser.showOpenDialog(null);
        if (file == null) return;

        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            databaseUserService().updateAvatar(currentUser.getId(), imageBytes);

            loadAvatar(currentUser.getId(), profileAvatarImage, profileAvatarText);
            loadAvatar(currentUser.getId(), settingsAvatarImage, settingsAvatarText);
            if (avatarText != null) loadAvatar(currentUser.getId(), null, avatarText);

            showAlert(Alert.AlertType.INFORMATION, "Foto Profil",
                    "Foto profil berhasil diperbarui.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal membaca file gambar: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveSettings() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        String lang = settingsLanguageCombo != null ? settingsLanguageCombo.getValue() : null;
        Boolean dark = settingsDarkModeCheck != null ? settingsDarkModeCheck.isSelected() : null;
        Boolean anim = settingsAnimationCheck != null ? settingsAnimationCheck.isSelected() : null;
        Boolean notif1 = settingsNotifNewCampaign != null ? settingsNotifNewCampaign.isSelected() : null;
        Boolean notif2 = settingsNotifProgress != null ? settingsNotifProgress.isSelected() : null;
        Boolean notif3 = settingsNotifDonasi != null ? settingsNotifDonasi.isSelected() : null;
        Boolean notif4 = settingsNotifPromo != null ? settingsNotifPromo.isSelected() : null;
        Boolean notif5 = settingsNotifReminder != null ? settingsNotifReminder.isSelected() : null;
        Boolean priv1 = settingsPrivacyShowName != null ? settingsPrivacyShowName.isSelected() : null;
        Boolean priv2 = settingsPrivacyShowProfile != null ? settingsPrivacyShowProfile.isSelected() : null;
        Boolean priv3 = settingsPrivacyHideAmount != null ? settingsPrivacyHideAmount.isSelected() : null;

        try {
            databaseUserService().updateSettings(currentUser.getId(), lang, dark, anim,
                    notif1, notif2, notif3, notif4, notif5, priv1, priv2, priv3);
            showAlert(Alert.AlertType.INFORMATION, "Pengaturan Disimpan",
                    "Perubahan pengaturan berhasil disimpan ke database.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", e.getMessage());
        }
    }

    @FXML
    private void handleChangePassword() {
        User currentUser = currentUser();
        if (currentUser == null) return;

        if (settingsOldPasswordField == null || settingsNewPasswordField == null || settingsConfirmPasswordField == null) {
            showAlert(Alert.AlertType.WARNING, "Gagal", "Halaman pengaturan tidak dimuat dengan benar.");
            return;
        }

        String oldPw = settingsOldPasswordField.getText();
        String newPw = settingsNewPasswordField.getText();
        String confirmPw = settingsConfirmPasswordField.getText();

        if (oldPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Gagal", "Semua kolom kata sandi harus diisi.");
            return;
        }

        if (!newPw.equals(confirmPw)) {
            showAlert(Alert.AlertType.WARNING, "Gagal", "Konfirmasi kata sandi baru tidak sesuai.");
            return;
        }

        if (newPw.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Gagal", "Kata sandi baru minimal 6 karakter.");
            return;
        }

        try {
            databaseUserService().updatePassword(currentUser.getId(), oldPw, newPw);
            currentUser.changePassword(oldPw, newPw);
            settingsOldPasswordField.clear();
            settingsNewPasswordField.clear();
            settingsConfirmPasswordField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Sukses",
                    "Kata sandi berhasil diperbarui.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Semua data terkait akun ini akan dihapus secara permanen.", ButtonType.OK, ButtonType.CANCEL);
        confirmation.setTitle("Hapus Akun");
        confirmation.setHeaderText("Apakah Anda yakin ingin menghapus akun?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                User currentUser = currentUser();
                if (currentUser != null) {
                    databaseUserService().deleteUser(currentUser.getId());
                }
                showAlert(Alert.AlertType.INFORMATION, "Akun Dihapus",
                        "Akun Anda telah berhasil dihapus dari database.");
                logout(event);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Gagal", e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        initializeDashboardPage();
        applyRoleAccess();
        showAlert(Alert.AlertType.INFORMATION, "Data Diperbarui", "Dashboard berhasil dimuat ulang dari database.");
    }

    @FXML
    private void handleShare() {
        String text = "CrowdCare - " + selectedCampaignTitle
                + (selectedCampaignId == null ? "" : " (Campaign ID: " + selectedCampaignId + ")");
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
        showAlert(Alert.AlertType.INFORMATION, "Bagikan Campaign", "Informasi campaign disalin ke clipboard.");
    }

    @FXML
    private void handleDownloadReport() {
        User currentUser = currentUser();
        if (currentUser == null || !currentUser.canDonate()) {
            showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Laporan hanya tersedia untuk Donatur.");
            return;
        }

        List<DonationEntity> donations = safeDonationsByDonor(currentUser.getId());
        if (donations.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Belum Ada Data", "Belum ada donasi yang bisa dibuat menjadi laporan.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Simpan Laporan Donasi");
        chooser.setInitialFileName("laporan-donasi-crowdcare.txt");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
        File file = chooser.showSaveDialog(ownerWindow());
        if (file == null) return;

        try {
            Files.writeString(file.toPath(), buildDonationReport(currentUser, donations), StandardCharsets.UTF_8);
            showAlert(Alert.AlertType.INFORMATION, "Laporan Disimpan", "Laporan donasi berhasil disimpan.");
        } catch (IOException exception) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", exception.getMessage());
        }
    }

    private String buildDonationReport(User user, List<DonationEntity> donations) {
        StringBuilder builder = new StringBuilder();
        long total = donations.stream().mapToLong(d -> amountOrZero(d.getAmount())).sum();

        builder.append("Laporan Donasi CrowdCare\n");
        builder.append("Nama: ").append(user.getFullName()).append('\n');
        builder.append("Username: ").append(user.getUsername()).append('\n');
        builder.append("Total Donasi: ").append(formatRupiah(total)).append("\n\n");

        int index = 1;
        for (DonationEntity donation : donations) {
            builder.append(index++).append(". ");
            builder.append(donation.getCampaign() == null ? "-" : donation.getCampaign().getTitle());
            builder.append(" | ");
            builder.append(donation.getDonatedAt() == null ? "-" : DATE_TIME_FORMAT.format(donation.getDonatedAt()));
            builder.append(" | ");
            builder.append(formatRupiah(amountOrZero(donation.getAmount())));
            if (donation.getMessage() != null && !donation.getMessage().isBlank()) {
                builder.append(" | ").append(donation.getMessage());
            }
            builder.append('\n');
        }

        return builder.toString();
    }

    private void openPage(ActionEvent event, String fxmlPath, String title) {
        try {
            URL pageUrl = MainApplication.class.getResource(fxmlPath);
            if (pageUrl == null) throw new IOException("File tidak ditemukan: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(pageUrl);
            loader.setController(this);
            Parent root = loader.load();

            Stage stage = getStage(event);
            stage.setTitle(title);
            stage.setScene(new Scene(root, 1200, 720));
            stage.setResizable(false);
            stage.centerOnScreen();

            Platform.runLater(this::applyRoleAccess);
        } catch (IOException exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Halaman", exception.getMessage());
        }
    }

    /* ==================================================
       HELPER DATA DAN UI
       ================================================== */
    private CampaignService campaignService() {
        return CrowdCareApplication.getContext().getBean(CampaignService.class);
    }

    private DonationService donationService() {
        return CrowdCareApplication.getContext().getBean(DonationService.class);
    }

    private com.crowdcare.service.DatabaseUserService databaseUserService() {
        return CrowdCareApplication.getContext().getBean(com.crowdcare.service.DatabaseUserService.class);
    }

    private User currentUser() {
        return UserSession.getInstance().getCurrentUser();
    }

    private List<CampaignEntity> safeApprovedCampaigns() {
        try {
            return new ArrayList<>(campaignService().getApprovedCampaigns());
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<CampaignEntity> safeCampaignsByCreator(String creatorId) {
        try {
            return new ArrayList<>(campaignService().getCampaignsByCreator(creatorId));
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<DonationEntity> safeDonationsByDonor(String donorId) {
        try {
            return new ArrayList<>(donationService().getDonationHistoryByDonor(donorId));
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<DonationEntity> safeDonationsByCampaign(Long campaignId) {
        if (campaignId == null) return new ArrayList<>();
        try {
            return new ArrayList<>(donationService().getDonationsByCampaign(campaignId));
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void searchAllNodesRecursively(Node node, List<Node> nodes) {
        if (node == null) return;

        nodes.add(node);
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                searchAllNodesRecursively(child, nodes);
            }
        }
    }

    private String getNodeText(Node node) {
        if (node instanceof Button button) {
            return button.getText() == null ? "" : button.getText().trim();
        }
        if (node instanceof Label label) {
            return label.getText() == null ? "" : label.getText().trim();
        }
        if (node instanceof javafx.scene.text.Text text) {
            return text.getText() == null ? "" : text.getText().trim();
        }
        return "";
    }

    private void loadAvatar(String userId, ImageView imageView, Label initialsLabel) {
        com.crowdcare.entity.UserEntity entity = databaseUserService().findEntityById(userId);
        if (entity == null) return;

        byte[] avatar = entity.getAvatar();
        if (avatar != null && avatar.length > 0 && imageView != null) {
            try {
                Image img = new Image(new java.io.ByteArrayInputStream(avatar));
                imageView.setImage(img);
                imageView.setVisible(true);
                imageView.setManaged(true);
                double r = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2.0;
                if (r <= 0) r = 31;
                Circle clip = new Circle(r);
                clip.centerXProperty().bind(imageView.fitWidthProperty().divide(2));
                clip.centerYProperty().bind(imageView.fitHeightProperty().divide(2));
                clip.radiusProperty().bind(
                        javafx.beans.binding.Bindings.min(
                                imageView.fitWidthProperty().divide(2),
                                imageView.fitHeightProperty().divide(2)));
                imageView.setClip(clip);
                if (initialsLabel != null) {
                    initialsLabel.setVisible(false);
                    initialsLabel.setManaged(false);
                }
                return;
            } catch (Exception ignored) {}
        }

        if (imageView != null) {
            imageView.setVisible(false);
            imageView.setManaged(false);
        }
        if (initialsLabel != null) {
            initialsLabel.setVisible(true);
            initialsLabel.setManaged(true);
            initialsLabel.setText(initials(UserSession.getInstance().getCurrentUser() != null
                    ? UserSession.getInstance().getCurrentUser().getFullName() : "?"));
        }
    }

    private Node sceneAnchor() {
        Node[] candidates = {
                headerNameLabel, campaignSearchField, campaignTitleField, detailTitleLabel,
                historySearchField, welcomeTitleLabel, settingsAccountNameLabel, profileRoleBadgeLabel
        };

        for (Node node : candidates) {
            if (node != null) return node;
        }
        return null;
    }

    private Long extractCampaignId(Object source) {
        if (!(source instanceof Node node)) return null;

        Node cursor = node;
        while (cursor != null) {
            Object userData = cursor.getUserData();
            Long id = parseCampaignId(userData);
            if (id != null) return id;
            cursor = cursor.getParent();
        }
        return null;
    }

    private Long parseCampaignId(Object userData) {
        if (userData instanceof Number number) return number.longValue();
        if (userData instanceof String text) {
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private void setNodeVisible(Node node, boolean visible) {
        if (node == null) return;
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private String initials(String fullName) {
        if (fullName == null || fullName.isBlank()) return "CC";
        String[] words = fullName.trim().split("\\s+");
        StringBuilder builder = new StringBuilder(words[0].substring(0, 1).toUpperCase(Locale.ROOT));
        if (words.length > 1) {
            builder.append(words[1].substring(0, 1).toUpperCase(Locale.ROOT));
        }
        return builder.toString();
    }

    private String formatRupiah(long amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(ID_LOCALE);
        return "Rp" + formatter.format(amount);
    }

    private String formatDate(LocalDate date) {
        return date == null ? "-" : DATE_FORMAT.format(date);
    }

    private long amountOrZero(Long amount) {
        return amount == null ? 0L : amount;
    }

    private long parseLongOrZero(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return 0L;
        }
    }

    private double campaignProgress(CampaignEntity campaign) {
        long target = amountOrZero(campaign.getTargetAmount());
        if (target <= 0) return 0;
        return clampProgress((double) amountOrZero(campaign.getCollectedAmount()) / target);
    }

    private double clampProgress(double progress) {
        return Math.max(0, Math.min(1, progress));
    }

    private String percentText(CampaignEntity campaign) {
        return Math.round(campaignProgress(campaign) * 100) + "%";
    }

    private String daysText(CampaignEntity campaign) {
        return daysText(campaign.getEndDate());
    }

    private String daysText(LocalDate endDate) {
        if (endDate == null) return "-";
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        if (days <= 0) return "Selesai";
        return days + " hari lagi";
    }

    private String daysNumberText(CampaignEntity campaign) {
        if (campaign.getEndDate() == null) return "-";
        long days = ChronoUnit.DAYS.between(LocalDate.now(), campaign.getEndDate());
        return String.valueOf(Math.max(days, 0));
    }

    private String statusText(String status) {
        if ("APPROVED".equals(status)) return "Disetujui";
        if ("REJECTED".equals(status)) return "Ditolak";
        return "Menunggu Review";
    }

    private String statusStyle(String status) {
        if ("APPROVED".equals(status)) return "campaign-status-approved";
        if ("REJECTED".equals(status)) return "campaign-status-rejected";
        return "campaign-status-pending";
    }

    private String dashboardCoverStyle(String category) {
        return switch (normalize(category)) {
            case "kesehatan" -> "campaign-image-green";
            case "sosial" -> "campaign-image-orange";
            case "bencana" -> "campaign-image-purple";
            case "lingkungan" -> "campaign-image-cyan";
            default -> "campaign-image-blue";
        };
    }

    private String listCoverStyle(String category) {
        return switch (normalize(category)) {
            case "kesehatan" -> "campaign-cover-green";
            case "sosial" -> "campaign-cover-orange";
            case "bencana" -> "campaign-cover-purple";
            case "lingkungan" -> "campaign-cover-cyan";
            default -> "campaign-cover-blue";
        };
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) return "";
        if (value.length() <= maxLength) return value;
        return value.substring(0, Math.max(0, maxLength - 3)).trim() + "...";
    }

    private Stage getStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private Window ownerWindow() {
        Node anchor = sceneAnchor();
        return anchor == null || anchor.getScene() == null ? null : anchor.getScene().getWindow();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Window owner = ownerWindow();
        if (owner != null) alert.initOwner(owner);
        alert.showAndWait();
    }

    private static final class CampaignDraft {
        private final String title;
        private final String description;
        private final String category;
        private final String target;
        private final LocalDate deadline;
        private final File imageFile;

        private CampaignDraft(String title, String description, String category,
                              String target, LocalDate deadline, File imageFile) {
            this.title = title;
            this.description = description;
            this.category = category;
            this.target = target;
            this.deadline = deadline;
            this.imageFile = imageFile;
        }
    }
}
