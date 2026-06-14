package com.crowdcare.controller;

import com.crowdcare.session.UserSession;
import com.crowdcare.MainApplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class NavigationController {

    private static String selectedCampaignTitle = "Bantu Pendidikan Anak Desa";

    /* ==================================================
       DEKLARASI FXID ADAPTIF UTAMA (SIDEBAR & HEADER GLOBAL)
       ================================================== */
    @FXML
    private Label headerNameLabel;

    @FXML
    private Label headerRoleLabel;

    @FXML
    private Label avatarText;

    @FXML
    private Label welcomeTitleLabel;

    @FXML
    private Button btnCreateCampaign;

    @FXML
    private Button btnDonationHistory;

    /* ==================================================
       DEKLARASI FXID ELEMEN HALAMAN SPESIFIK (UNTUK HAK AKSES)
       ================================================== */
    @FXML
    private Button btnHeaderCreateCampaign;

    @FXML
    private VBox donationActionCard;

    @FXML
    private Label profileRoleBadgeLabel;

    @FXML
    private Label profileInfoRoleLabel;

    @FXML
    private Label settingsAccountNameLabel;

    @FXML
    private Label settingsAccountEmailLabel;

    /* ==================================================
       DAFTAR CAMPAIGN
       ================================================== */
    @FXML
    private TextField campaignSearchField;

    @FXML
    private ComboBox<String> campaignCategoryFilter;

    @FXML
    private Label campaignResultLabel;

    @FXML
    private VBox campaignCard1;

    @FXML
    private VBox campaignCard2;

    @FXML
    private VBox campaignCard3;

    @FXML
    private VBox campaignCard4;

    @FXML
    private VBox campaignCard5;

    @FXML
    private VBox campaignCard6;

    @FXML
    private VBox campaignEmptyState;

    private VBox[] campaignCards;

    private final String[] campaignTitles = {
            "Bantu Pendidikan Anak Desa",
            "Bantuan Operasi untuk Raka",
            "Renovasi Rumah Ibadah",
            "Bantuan Korban Banjir",
            "Pengobatan Ibu Sari",
            "Gerakan Tanam Seribu Pohon"
    };

    private final String[] campaignCategories = {
            "Pendidikan", "Kesehatan", "Sosial", "Bencana", "Kesehatan", "Lingkungan"
    };

    /* ==================================================
       BUAT CAMPAIGN
       ================================================== */
    @FXML
    private TextField campaignTitleField;

    @FXML
    private TextArea campaignDescriptionArea;

    @FXML
    private ComboBox<String> campaignCategoryBox;

    @FXML
    private TextField campaignTargetField;

    @FXML
    private DatePicker campaignDeadlinePicker;

    @FXML
    private Label previewCategoryLabel;

    @FXML
    private Label previewTitleLabel;

    @FXML
    private Label previewDescriptionLabel;

    @FXML
    private Label previewAmountLabel;

    @FXML
    private Label previewDeadlineLabel;

    @FXML
    private ImageView previewImageView;

    @FXML
    private Label previewImagePlaceholder;

    @FXML
    private Label uploadDescriptionLabel;

    private File selectedCampaignImage;

    /* ==================================================
       DONASI
       ================================================== */
    @FXML
    private Label detailTitleLabel;

    @FXML
    private TextField customDonationField;

    private long selectedDonationAmount = 0;
    private Button selectedDonationButton;

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

        public String getLabel() { return label; }
        public String getDetail() { return detail; }
    }

    private PaymentMethod selectedPaymentMethod = null;

    /* ==================================================
       FILTER RIWAYAT DONASI
       ================================================== */
    @FXML
    private TextField historySearchField;

    @FXML
    private ComboBox<String> historyStatusFilter;

    @FXML
    private DatePicker historyDateFilter;

    @FXML
    private Label historyResultLabel;

    @FXML
    private VBox historyRow1;

    @FXML
    private VBox historyRow2;

    @FXML
    private VBox historyRow3;

    @FXML
    private VBox historyRow4;

    @FXML
    private VBox historyRow5;

    @FXML
    private VBox historyEmptyState;

    private VBox[] historyRows;

    private final String[] historyCampaignNames = {
            "Bantu Pendidikan Anak Desa",
            "Bantuan Operasi untuk Raka",
            "Renovasi Rumah Ibadah",
            "Bantuan Korban Banjir",
            "Pengobatan Ibu Sari"
    };

    private final String[] historyStatuses = {
            "Berhasil", "Berhasil", "Berhasil", "Berhasil", "Diproses"
    };

    private final LocalDate[] historyDates = {
            LocalDate.of(2026, 6, 11),
            LocalDate.of(2026, 6, 8),
            LocalDate.of(2026, 6, 2),
            LocalDate.of(2026, 5, 27),
            LocalDate.of(2026, 5, 19)
    };

    /* ==================================================
       INITIALIZE
       ================================================== */
    @FXML
    private void initialize() {
        initializeCampaignListPage();
        initializeCreateCampaignPage();
        initializeDonationHistoryPage();

        if (detailTitleLabel != null) {
            detailTitleLabel.setText(selectedCampaignTitle);
        }

        applyRoleAccess();
    }

    // Menggunakan akses PUBLIC agar bisa dipicu saat transisi login selesai
    public void applyRoleAccess() {
        com.crowdcare.model.User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null) {
            // 1. Sinkronisasi Teks Nama & Role Global
            if (headerNameLabel != null) headerNameLabel.setText(currentUser.getFullName());
            if (headerRoleLabel != null) headerRoleLabel.setText(currentUser.getRoleName());
            if (welcomeTitleLabel != null) welcomeTitleLabel.setText("Selamat datang kembali, " + currentUser.getFullName() + "!");

            // 2. Pembuatan Inisial Avatar Otomatis
            if (avatarText != null && !currentUser.getFullName().isEmpty()) {
                String[] words = currentUser.getFullName().split(" ");
                String initials = words[0].substring(0, 1).toUpperCase();
                if (words.length > 1 && !words[1].isEmpty()) {
                    initials += words[1].substring(0, 1).toUpperCase();
                }
                avatarText.setText(initials);
            }

            // 3. Cadangan Injeksi Statis FXML ID Bawaan
            if (btnCreateCampaign != null) {
                btnCreateCampaign.setVisible(currentUser.canCreateCampaign());
                btnCreateCampaign.setManaged(currentUser.canCreateCampaign());
            }
            if (btnDonationHistory != null) {
                btnDonationHistory.setVisible(currentUser.canDonate());
                btnDonationHistory.setManaged(currentUser.canDonate());
            }
            if (btnHeaderCreateCampaign != null) {
                btnHeaderCreateCampaign.setVisible(currentUser.canCreateCampaign());
                btnHeaderCreateCampaign.setManaged(currentUser.canCreateCampaign());
            }
            if (donationActionCard != null) {
                donationActionCard.setVisible(currentUser.canDonate());
                donationActionCard.setManaged(currentUser.canDonate());
            }

            // =========================================================================
            // FIX TOTAL UNIVERSAL SCANNER:
            // Menyisir ALL NODES (Button, Label, Text, dll.) Secara Agresif Berdasarkan Isi Teks
            // =========================================================================
            if (headerNameLabel != null && headerNameLabel.getScene() != null) {
                Parent root = headerNameLabel.getScene().getRoot();

                java.util.List<Node> targetNodes = new java.util.ArrayList<>();
                searchAllNodesRecursively(root, targetNodes);

                // KONDISI A: PENGGUNA ADALAH DONATUR (Lenyapkan Semua Akses Buat Campaign)
                if (!currentUser.canCreateCampaign()) {
                    for (Node node : targetNodes) {
                        String cleanText = getNodeText(node);
                        if (cleanText.equalsIgnoreCase("Buat Campaign") || cleanText.equalsIgnoreCase("+ Buat Campaign")) {
                            node.setVisible(false);
                            node.setManaged(false);
                        }
                    }
                }

                // KONDISI B: PENGGUNA ADALAH FUNDRAISER (Lenyapkan Semua Akses Donasi)
                if (!currentUser.canDonate()) {
                    for (Node node : targetNodes) {
                        String cleanText = getNodeText(node);
                        if (cleanText.equalsIgnoreCase("Riwayat Donasi") || cleanText.equalsIgnoreCase("Donasi Sekarang")) {
                            node.setVisible(false);
                            node.setManaged(false);
                        }
                    }

                    // Sembunyikan kontainer kartu donasi kanan lewat CSS Selector jika ada
                    for (Node node : root.lookupAll(".detail-donation-card")) {
                        node.setVisible(false);
                        node.setManaged(false);
                    }
                }
            }
            // =========================================================================

            // 4. Sinkronisasi Data Halaman Profil
            if (profileRoleBadgeLabel != null) {
                profileRoleBadgeLabel.setText(currentUser.getRoleName().toUpperCase(Locale.ROOT));
            }
            if (profileInfoRoleLabel != null) {
                profileInfoRoleLabel.setText(currentUser.getRoleName());
            }

            // 5. Sinkronisasi Data Halaman Pengaturan
            if (settingsAccountNameLabel != null) {
                settingsAccountNameLabel.setText(currentUser.getFullName());
            }
            if (settingsAccountEmailLabel != null) {
                settingsAccountEmailLabel.setText(currentUser.getUsername());
            }
        }
    }

    // METHOD BANTUAN 1: Mengambil seluruh objek Node layout apa pun tipenya tanpa terkecuali
    private void searchAllNodesRecursively(Node node, java.util.List<Node> nodes) {
        if (node != null) {
            nodes.add(node);
            if (node instanceof javafx.scene.Parent) {
                for (Node child : ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
                    searchAllNodesRecursively(child, nodes);
                }
            }
        }
    }

    // METHOD BANTUAN 2: Ekstraksi konten teks multi-komponen (Mendukung Button, Label, dan Text)
    private String getNodeText(Node node) {
        if (node instanceof Button) {
            return ((Button) node).getText() != null ? ((Button) node).getText().trim() : "";
        } else if (node instanceof Label) {
            return ((Label) node).getText() != null ? ((Label) node).getText().trim() : "";
        } else if (node instanceof javafx.scene.text.Text) {
            return ((javafx.scene.text.Text) node).getText() != null ? ((javafx.scene.text.Text) node).getText().trim() : "";
        }
        return "";
    }

    private void initializeCreateCampaignPage() {
        if (campaignCategoryBox == null) return;

        campaignCategoryBox.getItems().setAll("Pendidikan", "Kesehatan", "Sosial", "Bencana", "Lingkungan");
        campaignTitleField.textProperty().addListener((obs, old, newVal) -> updateCampaignPreview());
        campaignDescriptionArea.textProperty().addListener((obs, old, newVal) -> updateCampaignPreview());
        campaignCategoryBox.valueProperty().addListener((obs, old, newVal) -> updateCampaignPreview());
        campaignDeadlinePicker.valueProperty().addListener((obs, old, newVal) -> updateCampaignPreview());

        campaignTargetField.textProperty().addListener((obs, old, newVal) -> {
            String sanitizedValue = newVal.replaceAll("\\D", "");
            if (!newVal.equals(sanitizedValue)) {
                campaignTargetField.setText(sanitizedValue);
                return;
            }
            updateCampaignPreview();
        });

        updateCampaignPreview();
    }

    private void initializeCampaignListPage() {
        if (campaignSearchField == null || campaignCategoryFilter == null) return;

        campaignCards = new VBox[]{campaignCard1, campaignCard2, campaignCard3, campaignCard4, campaignCard5, campaignCard6};
        campaignCategoryFilter.getItems().setAll("Semua Kategori", "Pendidikan", "Kesehatan", "Sosial", "Bencana", "Lingkungan");
        campaignCategoryFilter.setValue("Semua Kategori");

        campaignSearchField.textProperty().addListener((obs, old, newVal) -> applyCampaignFilter());
        campaignCategoryFilter.valueProperty().addListener((obs, old, newVal) -> applyCampaignFilter());

        applyCampaignFilter();
    }

    private void initializeDonationHistoryPage() {
        if (historySearchField == null || historyStatusFilter == null || historyDateFilter == null) return;

        historyRows = new VBox[]{historyRow1, historyRow2, historyRow3, historyRow4, historyRow5};
        historyStatusFilter.getItems().setAll("Semua Status", "Berhasil", "Diproses");
        historyStatusFilter.setValue("Semua Status");

        updateHistoryResultLabel(5);
        applyDonationHistoryFilter();
    }

    private void applyCampaignFilter() {
        if (campaignCards == null || campaignSearchField == null || campaignCategoryFilter == null) return;

        String query = campaignSearchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedCategory = campaignCategoryFilter.getValue();
        int visibleCount = 0;

        for (int i = 0; i < campaignCards.length; i++) {
            VBox card = campaignCards[i];
            if (card == null) continue;

            String title = campaignTitles[i].toLowerCase(Locale.ROOT);
            String category = campaignCategories[i].toLowerCase(Locale.ROOT);

            boolean matchesSearch = query.isEmpty() || title.contains(query) || category.contains(query);
            boolean matchesCategory = selectedCategory == null || selectedCategory.equals("Semua Kategori") || campaignCategories[i].equalsIgnoreCase(selectedCategory);
            boolean shouldShow = matchesSearch && matchesCategory;

            card.setVisible(shouldShow);
            card.setManaged(shouldShow);

            if (shouldShow) visibleCount++;
        }

        if (campaignResultLabel != null) {
            campaignResultLabel.setText(visibleCount == 1 ? "1 campaign ditemukan" : visibleCount + " campaign ditemukan");
        }
        if (campaignEmptyState != null) {
            campaignEmptyState.setVisible(visibleCount == 0);
            campaignEmptyState.setManaged(visibleCount == 0);
        }
    }

    @FXML
    private void handleResetCampaignFilter() {
        if (campaignSearchField != null) campaignSearchField.clear();
        if (campaignCategoryFilter != null) campaignCategoryFilter.setValue("Semua Kategori");
        applyCampaignFilter();
    }

    @FXML
    private void handleFilter() { applyDonationHistoryFilter(); }

    private void applyDonationHistoryFilter() {
        if (historyRows == null || historySearchField == null || historyStatusFilter == null || historyDateFilter == null) return;

        String searchQuery = historySearchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedStatus = historyStatusFilter.getValue();
        LocalDate selectedDate = historyDateFilter.getValue();
        int visibleCount = 0;

        for (int i = 0; i < historyRows.length; i++) {
            VBox row = historyRows[i];
            if (row == null) continue;

            boolean matchesCampaign = searchQuery.isEmpty() || historyCampaignNames[i].toLowerCase(Locale.ROOT).contains(searchQuery);
            boolean matchesStatus = selectedStatus == null || selectedStatus.equals("Semua Status") || historyStatuses[i].equalsIgnoreCase(selectedStatus);
            boolean matchesDate = selectedDate == null || historyDates[i].equals(selectedDate);
            boolean shouldShow = matchesCampaign && matchesStatus && matchesDate;

            row.setVisible(shouldShow);
            row.setManaged(shouldShow);

            if (shouldShow) visibleCount++;
        }

        updateHistoryResultLabel(visibleCount);
        updateHistoryEmptyState(visibleCount);
    }

    private void updateHistoryResultLabel(int visibleCount) {
        if (historyResultLabel != null) {
            historyResultLabel.setText(visibleCount == 1 ? "1 transaksi ditemukan" : visibleCount + " transaksi ditemukan");
        }
    }

    private void updateHistoryEmptyState(int visibleCount) {
        if (historyEmptyState != null) {
            historyEmptyState.setVisible(visibleCount == 0);
            historyEmptyState.setManaged(visibleCount == 0);
        }
    }

    @FXML
    private void handleReset() {
        if (historySearchField != null) historySearchField.clear();
        if (historyStatusFilter != null) historyStatusFilter.setValue("Semua Status");
        if (historyDateFilter != null) historyDateFilter.setValue(null);
        applyDonationHistoryFilter();
    }

    private void updateCampaignPreview() {
        if (campaignTitleField == null || campaignDescriptionArea == null || campaignCategoryBox == null ||
                campaignTargetField == null || campaignDeadlinePicker == null || previewCategoryLabel == null ||
                previewTitleLabel == null || previewDescriptionLabel == null || previewAmountLabel == null || previewDeadlineLabel == null) return;

        String title = campaignTitleField.getText().trim();
        String description = campaignDescriptionArea.getText().trim();
        String category = campaignCategoryBox.getValue();
        String targetText = campaignTargetField.getText().trim();

        previewTitleLabel.setText(title.isEmpty() ? "Judul campaign akan tampil di sini" : title);
        previewDescriptionLabel.setText(description.isEmpty() ? "Deskripsi singkat campaign akan tampil." : description);
        previewCategoryLabel.setText(category == null ? "KATEGORI" : category.toUpperCase(Locale.ROOT));

        if (targetText.isEmpty()) {
            previewAmountLabel.setText("Rp0 terkumpul");
        } else {
            try {
                long targetAmount = Long.parseLong(targetText);
                previewAmountLabel.setText("Rp0 terkumpul dari " + formatRupiah(targetAmount));
            } catch (NumberFormatException e) {
                previewAmountLabel.setText("Target dana terlalu besar");
            }
        }

        if (campaignDeadlinePicker.getValue() == null) {
            previewDeadlineLabel.setText("Belum ditentukan");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("id", "ID"));
            previewDeadlineLabel.setText("sampai " + campaignDeadlinePicker.getValue().format(formatter));
        }
    }

    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Campaign");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("File Gambar", "*.jpg", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter("Semua File", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(getStage(event));
        if (selectedFile == null) return;

        if (selectedFile.length() > 5L * 1024L * 1024L) {
            showAlert(Alert.AlertType.WARNING, "Ukuran Gambar Terlalu Besar", "Ukuran gambar maksimal adalah 5 MB.");
            return;
        }

        String fileName = selectedFile.getName().toLowerCase(Locale.ROOT);
        if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"))) {
            showAlert(Alert.AlertType.WARNING, "Format Tidak Didukung", "Gunakan gambar dengan format JPG, JPEG, atau PNG.");
            return;
        }

        try {
            Image image = new Image(selectedFile.toURI().toString());
            if (image.isError()) throw new IllegalArgumentException();

            selectedCampaignImage = selectedFile;
            previewImageView.setImage(image);
            previewImageView.setVisible(true);
            previewImageView.setManaged(true);
            previewImagePlaceholder.setVisible(false);
            previewImagePlaceholder.setManaged(false);
            uploadDescriptionLabel.setText(selectedFile.getName());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Gambar", "File gambar tidak dapat ditampilkan.");
        }
    }

    @FXML
    private void handleSaveDraft() {
        boolean formEmpty = campaignTitleField.getText().trim().isEmpty() && campaignDescriptionArea.getText().trim().isEmpty() &&
                campaignCategoryBox.getValue() == null && campaignTargetField.getText().trim().isEmpty() &&
                campaignDeadlinePicker.getValue() == null && selectedCampaignImage == null;

        if (formEmpty) {
            showAlert(Alert.AlertType.WARNING, "Draft Kosong", "Isi setidaknya satu bagian form sebelum menyimpan draft.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Draft Disimpan", "Data campaign berhasil disimpan sebagai draft sementara.");
    }

    @FXML
    private void handleSubmitCampaign() {
        String title = campaignTitleField.getText().trim();
        String description = campaignDescriptionArea.getText().trim();
        String category = campaignCategoryBox.getValue();
        String targetText = campaignTargetField.getText().trim();

        if (title.length() < 5) { showAlert(Alert.AlertType.WARNING, "Judul Terlalu Pendek", "Judul campaign minimal terdiri dari 5 karakter."); campaignTitleField.requestFocus(); return; }
        if (description.length() < 20) { showAlert(Alert.AlertType.WARNING, "Deskripsi Terlalu Pendek", "Deskripsi campaign minimal terdiri dari 20 karakter."); campaignDescriptionArea.requestFocus(); return; }
        if (category == null) { showAlert(Alert.AlertType.WARNING, "Kategori Belum Dipilih", "Pilih kategori campaign terlebih dahulu."); campaignCategoryBox.requestFocus(); return; }

        long targetAmount;
        try {
            targetAmount = Long.parseLong(targetText);
            if (targetAmount < 100_000) { showAlert(Alert.AlertType.WARNING, "Target Dana Terlalu Kecil", "Target dana minimal Rp100.000."); campaignTargetField.requestFocus(); return; }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Target Dana Tidak Valid", "Masukkan target dana menggunakan angka."); campaignTargetField.requestFocus(); return;
        }

        if (campaignDeadlinePicker.getValue() == null || !campaignDeadlinePicker.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Tanggal Tidak Valid", "Batas waktu harus setelah tanggal hari ini."); campaignDeadlinePicker.requestFocus(); return;
        }
        if (selectedCampaignImage == null) { showAlert(Alert.AlertType.WARNING, "Gambar Belum Dipilih", "Pilih gambar campaign terlebih dahulu."); return; }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Ajukan Campaign");
        confirmation.setHeaderText("Ajukan campaign \"" + title + "\"?");
        confirmation.setContentText("Kategori: " + category + "\nTarget dana: " + formatRupiah(targetAmount) + "\n\nCampaign akan dikirim kepada admin.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showAlert(Alert.AlertType.INFORMATION, "Campaign Berhasil Diajukan", "Campaign berhasil dikirim dan menunggu persetujuan admin.");
            resetCreateCampaignForm();
        }
    }

    private void resetCreateCampaignForm() {
        campaignTitleField.clear();
        campaignDescriptionArea.clear();
        campaignCategoryBox.getSelectionModel().clearSelection();
        campaignTargetField.clear();
        campaignDeadlinePicker.setValue(null);
        selectedCampaignImage = null;

        if (previewImageView != null) { previewImageView.setImage(null); previewImageView.setVisible(false); previewImageView.setManaged(false); }
        if (previewImagePlaceholder != null) { previewImagePlaceholder.setVisible(true); previewImagePlaceholder.setManaged(true); }
        if (uploadDescriptionLabel != null) uploadDescriptionLabel.setText("Format JPG atau PNG, maksimal 5 MB");
        updateCampaignPreview();
    }

    @FXML
    private void handleSelectDonationAmount(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (clickedButton.getUserData() == null) return;

        try {
            selectedDonationAmount = Long.parseLong(clickedButton.getUserData().toString());
            if (customDonationField != null) { customDonationField.clear(); customDonationField.setVisible(false); customDonationField.setManaged(false); }
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
        if (selectedDonationButton != null) selectedDonationButton.getStyleClass().remove("donation-option-selected");
        selectedDonationButton = button;
        if (!button.getStyleClass().contains("donation-option-selected")) button.getStyleClass().add("donation-option-selected");
    }

    @FXML
    private void handleDonation() {
        long donationAmount = selectedDonationAmount;

        if (customDonationField != null && customDonationField.isVisible()) {
            String customText = customDonationField.getText().replaceAll("\\D", "");
            if (customText.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Nominal Belum Diisi", "Masukkan nominal donasi terlebih dahulu."); return; }
            try { donationAmount = Long.parseLong(customText); } catch (NumberFormatException e) { return; }
        }

        if (donationAmount < 10_000) { showAlert(Alert.AlertType.WARNING, "Nominal Terlalu Kecil", "Nominal donasi minimal Rp10.000."); return; }

        PaymentMethod chosenMethod = showPaymentMethodDialog();
        if (chosenMethod == null) return;

        selectedPaymentMethod = chosenMethod;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Konfirmasi Donasi");
        confirmation.setHeaderText("Donasi untuk " + selectedCampaignTitle);
        confirmation.setContentText("Nominal : " + formatRupiah(donationAmount) + "\nMetode: " + chosenMethod.getLabel() + "\n\n" + chosenMethod.getDetail());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showAlert(Alert.AlertType.INFORMATION, "Donasi Berhasil", "Terima kasih! Donasi sebesar " + formatRupiah(donationAmount) + " berhasil diproses.");
            resetDonationState();
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

        Label bankGroupLabel = new Label("🏦  Transfer Bank");
        bankGroupLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        javafx.scene.layout.HBox bankRow = new javafx.scene.layout.HBox(10);
        for (PaymentMethod m : new PaymentMethod[]{PaymentMethod.BCA, PaymentMethod.MANDIRI, PaymentMethod.BNI}) {
            bankRow.getChildren().add(buildPaymentCard(m, result, dialog));
        }

        Label walletGroupLabel = new Label("💳  E-Wallet");
        walletGroupLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151; -fx-padding: 16 0 8 0;");
        javafx.scene.layout.HBox walletRow = new javafx.scene.layout.HBox(10);
        for (PaymentMethod m : new PaymentMethod[]{PaymentMethod.GOPAY, PaymentMethod.OVO, PaymentMethod.DANA}) {
            walletRow.getChildren().add(buildPaymentCard(m, result, dialog));
        }

        VBox body = new VBox(bankGroupLabel, bankRow, walletGroupLabel, walletRow);
        body.setStyle("-fx-padding: 20 24 4 24;");

        Button cancelButton = new Button("Batal");
        cancelButton.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 8px; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> dialog.close());

        javafx.scene.layout.HBox footer = new javafx.scene.layout.HBox(cancelButton);
        footer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        footer.setStyle("-fx-padding: 16 24 20 24; -fx-border-color: #e5e7eb; -fx-border-width: 1 0 0 0;");

        VBox root = new VBox(header, body, footer);
        dialog.setScene(new Scene(root, 520, 420));
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.centerOnScreen();
        dialog.showAndWait();

        return result[0];
    }

    private VBox buildPaymentCard(PaymentMethod method, PaymentMethod[] result, Stage dialog) {
        String[] iconAndSub = getPaymentIconAndSub(method);
        Label iconLabel = new Label(iconAndSub[0]); iconLabel.setStyle("-fx-font-size: 26px;");
        Label nameLabel = new Label(method.name()); nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        Label subLabel = new Label(iconAndSub[1]); subLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #9ca3af;");

        VBox card = new VBox(6, iconLabel, nameLabel, subLabel);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setPrefWidth(130); card.setPrefHeight(90);
        card.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 12px; -fx-border-color: #e5e7eb; -fx-border-width: 1.5px; -fx-cursor: hand; -fx-padding: 12 8;");

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #eff6ff; -fx-background-radius: 12px; -fx-border-color: #3b82f6; -fx-border-width: 2px; -fx-cursor: hand; -fx-padding: 12 8;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 12px; -fx-border-color: #e5e7eb; -fx-border-width: 1.5px; -fx-cursor: hand; -fx-padding: 12 8;"));
        card.setOnMouseClicked(e -> { result[0] = method; dialog.close(); });
        return card;
    }

    private String[] getPaymentIconAndSub(PaymentMethod method) {
        return switch (method) {
            case BCA -> new String[]{"🔵", "Transfer Bank"};
            case MANDIRI -> new String[]{"🟡", "Transfer Bank"};
            case BNI -> new String[]{"🟠", "Transfer Bank"};
            case GOPAY -> new String[]{"🟢", "E-Wallet"};
            case OVO -> new String[]{"🟣", "E-Wallet"};
            case DANA -> new String[]{"🔷", "E-Wallet"};
            case QRIS -> new String[]{"⬛", "Scan Barcode"};
        };
    }

    private void resetDonationState() {
        selectedDonationAmount = 0;
        selectedPaymentMethod = null;
        if (selectedDonationButton != null) { selectedDonationButton.getStyleClass().remove("donation-option-selected"); selectedDonationButton = null; }
        if (customDonationField != null) { customDonationField.clear(); customDonationField.setVisible(false); customDonationField.setManaged(false); }
    }

    /* ==================================================
       NAVIGASI DAN KONTROL SCREEN
       ================================================== */
    @FXML private void openDashboard(ActionEvent e) { openPage(e, "/view/dashboard.fxml", "CrowdCare - Dashboard"); }
    @FXML private void openCampaigns(ActionEvent e) { openPage(e, "/view/campaigns.fxml", "CrowdCare - Campaign"); }
    @FXML private void openCreateCampaign(ActionEvent e) { openPage(e, "/view/create-campaign.fxml", "CrowdCare - Buat Campaign"); }
    @FXML private void openDonationHistory(ActionEvent e) { openPage(e, "/view/donation-history.fxml", "CrowdCare - Riwayat Donasi"); }
    @FXML private void showProfile(ActionEvent e) { openPage(e, "/view/profile.fxml", "CrowdCare - Profil Saya"); }
    @FXML private void showSettings(ActionEvent e) { openPage(e, "/view/settings.fxml", "CrowdCare - Pengaturan"); }

    @FXML
    private void openCampaignDetail(ActionEvent event) {
        try {
            Button clickedButton = (Button) event.getSource();
            VBox currentCard = (VBox) clickedButton.getParent();
            for (Node child : currentCard.getChildren()) {
                if (child instanceof Label) {
                    String txt = ((Label) child).getText();
                    if (!txt.contains("terkumpul") && !txt.contains("hari lagi") && !txt.toUpperCase().matches("PENDIDIKAN|KESEHATAN|SOSIAL|BENCANA|LINGKUNGAN")) {
                        selectedCampaignTitle = txt;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal mendeteksi teks judul secara otomatis.");
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

    @FXML private void handleSaveProfile() { showAlert(Alert.AlertType.INFORMATION, "Profil Disimpan", "Perubahan profil berhasil disimpan."); }
    @FXML private void handleChangePhoto() { showAlert(Alert.AlertType.INFORMATION, "Ubah Foto", "Fitur pemilihan foto profil terhubung."); }
    @FXML private void handleSaveSettings() { showAlert(Alert.AlertType.INFORMATION, "Pengaturan Disimpan", "Perubahan pengaturan berhasil disimpan."); }

    @FXML
    private void handleChangePassword() {
        com.crowdcare.model.User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) return;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ubah Kata Sandi");
        dialog.setHeaderText("Masukkan kata sandi lama dan baru.");
        ButtonType confirmButtonType = new ButtonType("Simpan", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setStyle("-fx-padding: 20;");
        PasswordField oldP = new PasswordField(); PasswordField newP = new PasswordField(); PasswordField confP = new PasswordField();
        grid.add(new Label("Kata sandi lama:"), 0, 0); grid.add(oldP, 1, 0);
        grid.add(new Label("Kata sandi baru:"), 0, 1); grid.add(newP, 1, 1);
        grid.add(new Label("Konfirmasi baru:"), 0, 2); grid.add(confP, 1, 2);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == confirmButtonType) {
            if (!newP.getText().equals(confP.getText())) { showAlert(Alert.AlertType.WARNING, "Gagal", "Konfirmasi kata sandi tidak sesuai."); return; }
            try {
                currentUser.changePassword(oldP.getText(), newP.getText());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kata sandi berhasil diperbarui.");
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.WARNING, "Gagal", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Tindakan ini tidak dapat dibatalkan.", ButtonType.OK, ButtonType.CANCEL);
        confirmation.setTitle("Hapus Akun");
        confirmation.setHeaderText("Apakah Anda yakin ingin menghapus akun?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showAlert(Alert.AlertType.INFORMATION, "Akun Dihapus", "Akun Anda telah berhasil dihapus.");
            logout(event);
        }
    }

    @FXML private void handleShare() { showAlert(Alert.AlertType.INFORMATION, "Bagikan Campaign", "Tautan campaign disalin."); }
    @FXML private void handleDownloadReport() { showAlert(Alert.AlertType.INFORMATION, "Unduh Laporan", "Laporan riwayat donasi dibuat."); }

    /* ==================================================
       FUNGSI KUNCI RE-INITIALIZE STATE JAVAFX
       ================================================== */
    private void openPage(ActionEvent event, String fxmlPath, String title) {
        try {
            URL pageUrl = MainApplication.class.getResource(fxmlPath);

            if (pageUrl == null) {
                throw new IOException("File tidak ditemukan: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(pageUrl);
            loader.setController(this);

            Parent root = loader.load();

            // Eksekusi penataan akses di antrean render akhir agar scene terdeteksi penuh
            Platform.runLater(this::applyRoleAccess);

            Stage stage = getStage(event);
            stage.setTitle(title);
            stage.setScene(new Scene(root, 1200, 720));
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Halaman", exception.getMessage());
        }
    }

    private String formatRupiah(long amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        return "Rp" + formatter.format(amount);
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