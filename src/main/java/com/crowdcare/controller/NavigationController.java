package com.crowdcare.controller;
import com.crowdcare.session.UserSession;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    // Variabel statis untuk menyimpan judul campaign yang sedang dipilih/diklik
    private static String selectedCampaignTitle = "Bantu Pendidikan Anak Desa";

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
            "Pendidikan",
            "Kesehatan",
            "Sosial",
            "Bencana",
            "Kesehatan",
            "Lingkungan"
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
    private Label detailTitleLabel; // Label judul di halaman detail (jika ada)

    @FXML
    private TextField customDonationField;

    private long selectedDonationAmount = 0;
    private Button selectedDonationButton;

    /*
     * Enum metode pembayaran.
     * Mengelompokkan semua pilihan dalam satu tipe
     * agar tidak pakai String mentah (type-safe).
     */
    private enum PaymentMethod {
        BCA("Transfer Bank - BCA",
                "Rekening: 1234567890\na.n. CrowdCare Foundation"),
        MANDIRI("Transfer Bank - Mandiri",
                "Rekening: 0987654321\na.n. CrowdCare Foundation"),
        BNI("Transfer Bank - BNI",
                "Rekening: 1122334455\na.n. CrowdCare Foundation"),
        GOPAY("E-Wallet - GoPay",
                "Nomor GoPay: 0812-3456-7890"),
        OVO("E-Wallet - OVO",
                "Nomor OVO: 0821-9876-5432"),
        DANA("E-Wallet - DANA",
                "Nomor DANA: 0831-1111-2222"),
        QRIS("QRIS",
                "Scan kode QRIS di aplikasi pembayaran Anda.");

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
            "Berhasil",
            "Berhasil",
            "Berhasil",
            "Berhasil",
            "Diproses"
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
        initializeCreateCampaignPage();
        initializeCampaignListPage();
        initializeDonationHistoryPage();

        // Set judul halaman detail secara dinamis jika labelnya terdeteksi aktif
        if (detailTitleLabel != null) {
            detailTitleLabel.setText(selectedCampaignTitle);
        }
    }

    private void initializeCreateCampaignPage() {
        if (campaignCategoryBox == null) {
            return;
        }

        campaignCategoryBox.getItems().setAll(
                "Pendidikan",
                "Kesehatan",
                "Sosial",
                "Bencana",
                "Lingkungan"
        );

        campaignTitleField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateCampaignPreview()
        );

        campaignDescriptionArea.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateCampaignPreview()
        );

        campaignCategoryBox.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateCampaignPreview()
        );

        campaignDeadlinePicker.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateCampaignPreview()
        );

        campaignTargetField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    String sanitizedValue =
                            newValue.replaceAll("\\D", "");

                    if (!newValue.equals(sanitizedValue)) {
                        campaignTargetField.setText(
                                sanitizedValue
                        );
                        return;
                    }

                    updateCampaignPreview();
                }
        );

        updateCampaignPreview();
    }

    private void initializeCampaignListPage() {
        if (campaignSearchField == null
                || campaignCategoryFilter == null) {
            return;
        }

        campaignCards = new VBox[]{
                campaignCard1,
                campaignCard2,
                campaignCard3,
                campaignCard4,
                campaignCard5,
                campaignCard6
        };

        campaignCategoryFilter.getItems().setAll(
                "Semua Kategori",
                "Pendidikan",
                "Kesehatan",
                "Sosial",
                "Bencana",
                "Lingkungan"
        );

        campaignCategoryFilter.setValue(
                "Semua Kategori"
        );

        campaignSearchField
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) ->
                                applyCampaignFilter()
                );

        campaignCategoryFilter
                .valueProperty()
                .addListener(
                        (observable, oldValue, newValue) ->
                                applyCampaignFilter()
                );

        applyCampaignFilter();
    }

    private void initializeDonationHistoryPage() {
        if (historySearchField == null
                || historyStatusFilter == null
                || historyDateFilter == null) {
            return;
        }

        historyRows = new VBox[]{
                historyRow1,
                historyRow2,
                historyRow3,
                historyRow4,
                historyRow5
        };

        historyStatusFilter.getItems().setAll(
                "Semua Status",
                "Berhasil",
                "Diproses"
        );

        historyStatusFilter.setValue(
                "Semua Status"
        );

        updateHistoryResultLabel(5);
        applyDonationHistoryFilter();
    }

    /* ==================================================
       FILTER CAMPAIGN
       ================================================== */

    private void applyCampaignFilter() {
        if (campaignCards == null
                || campaignSearchField == null
                || campaignCategoryFilter == null) {
            return;
        }

        String query = campaignSearchField
                .getText()
                .trim()
                .toLowerCase(Locale.ROOT);

        String selectedCategory =
                campaignCategoryFilter.getValue();

        int visibleCount = 0;

        for (int i = 0; i < campaignCards.length; i++) {
            VBox card = campaignCards[i];

            if (card == null) {
                continue;
            }

            String title = campaignTitles[i]
                    .toLowerCase(Locale.ROOT);

            String category = campaignCategories[i]
                    .toLowerCase(Locale.ROOT);

            boolean matchesSearch =
                    query.isEmpty()
                            || title.contains(query)
                            || category.contains(query);

            boolean matchesCategory =
                    selectedCategory == null
                            || selectedCategory.equals(
                            "Semua Kategori"
                    )
                            || campaignCategories[i]
                            .equalsIgnoreCase(
                                    selectedCategory
                            );

            boolean shouldShow =
                    matchesSearch && matchesCategory;

            card.setVisible(shouldShow);
            card.setManaged(shouldShow);

            if (shouldShow) {
                visibleCount++;
            }
        }

        if (campaignResultLabel != null) {
            campaignResultLabel.setText(
                    visibleCount == 1
                            ? "1 campaign ditemukan"
                            : visibleCount
                            + " campaign ditemukan"
            );
        }

        if (campaignEmptyState != null) {
            boolean empty = visibleCount == 0;

            campaignEmptyState.setVisible(empty);
            campaignEmptyState.setManaged(empty);
        }
    }

    @FXML
    private void handleResetCampaignFilter() {
        if (campaignSearchField != null) {
            campaignSearchField.clear();
        }

        if (campaignCategoryFilter != null) {
            campaignCategoryFilter.setValue(
                    "Semua Kategori"
            );
        }

        applyCampaignFilter();
    }

    /* ==================================================
       FILTER RIWAYAT DONASI
       ================================================== */

    @FXML
    private void handleFilter() {
        applyDonationHistoryFilter();
    }

    private void applyDonationHistoryFilter() {
        if (historyRows == null
                || historySearchField == null
                || historyStatusFilter == null
                || historyDateFilter == null) {
            return;
        }

        String searchQuery = historySearchField
                .getText()
                .trim()
                .toLowerCase(Locale.ROOT);

        String selectedStatus =
                historyStatusFilter.getValue();

        LocalDate selectedDate =
                historyDateFilter.getValue();

        int visibleCount = 0;

        for (int i = 0; i < historyRows.length; i++) {
            VBox row = historyRows[i];

            if (row == null) {
                continue;
            }

            boolean matchesCampaign =
                    searchQuery.isEmpty()
                            || historyCampaignNames[i]
                            .toLowerCase(Locale.ROOT)
                            .contains(searchQuery);

            boolean matchesStatus =
                    selectedStatus == null
                            || selectedStatus.equals(
                            "Semua Status"
                    )
                            || historyStatuses[i]
                            .equalsIgnoreCase(
                                    selectedStatus
                            );

            boolean matchesDate =
                    selectedDate == null
                            || historyDates[i]
                            .equals(selectedDate);

            boolean shouldShow =
                    matchesCampaign
                            && matchesStatus
                            && matchesDate;

            row.setVisible(shouldShow);
            row.setManaged(shouldShow);

            if (shouldShow) {
                visibleCount++;
            }
        }

        updateHistoryResultLabel(visibleCount);
        updateHistoryEmptyState(visibleCount);
    }

    private void updateHistoryResultLabel(
            int visibleCount
    ) {
        if (historyResultLabel == null) {
            return;
        }

        historyResultLabel.setText(
                visibleCount == 1
                        ? "1 transaksi ditemukan"
                        : visibleCount
                        + " transaksi ditemukan"
        );
    }

    private void updateHistoryEmptyState(
            int visibleCount
    ) {
        if (historyEmptyState == null) {
            return;
        }

        boolean empty = visibleCount == 0;

        historyEmptyState.setVisible(empty);
        historyEmptyState.setManaged(empty);
    }

    @FXML
    private void handleReset() {
        if (historySearchField != null) {
            historySearchField.clear();
        }

        if (historyStatusFilter != null) {
            historyStatusFilter.setValue(
                    "Semua Status"
            );
        }

        if (historyDateFilter != null) {
            historyDateFilter.setValue(null);
        }

        applyDonationHistoryFilter();
    }

    /* ==================================================
       PREVIEW CAMPAIGN
       ================================================== */

    private void updateCampaignPreview() {
        if (campaignTitleField == null
                || campaignDescriptionArea == null
                || campaignCategoryBox == null
                || campaignTargetField == null
                || campaignDeadlinePicker == null
                || previewCategoryLabel == null
                || previewTitleLabel == null
                || previewDescriptionLabel == null
                || previewAmountLabel == null
                || previewDeadlineLabel == null) {
            return;
        }

        String title = campaignTitleField
                .getText()
                .trim();

        String description =
                campaignDescriptionArea
                        .getText()
                        .trim();

        String category =
                campaignCategoryBox.getValue();

        String targetText =
                campaignTargetField
                        .getText()
                        .trim();

        previewTitleLabel.setText(
                title.isEmpty()
                        ? "Judul campaign akan tampil di sini"
                        : title
        );

        previewDescriptionLabel.setText(
                description.isEmpty()
                        ? "Deskripsi singkat campaign akan tampil di bagian ini."
                        : description
        );

        previewCategoryLabel.setText(
                category == null
                        ? "KATEGORI"
                        : category.toUpperCase(
                        Locale.ROOT
                )
        );

        if (targetText.isEmpty()) {
            previewAmountLabel.setText(
                    "Rp0 terkumpul"
            );
        } else {
            try {
                long targetAmount =
                        Long.parseLong(targetText);

                previewAmountLabel.setText(
                        "Rp0 terkumpul dari "
                                + formatRupiah(
                                targetAmount
                        )
                );

            } catch (NumberFormatException exception) {
                previewAmountLabel.setText(
                        "Target dana terlalu besar"
                );
            }
        }

        if (campaignDeadlinePicker
                .getValue() == null) {

            previewDeadlineLabel.setText(
                    "Belum ditentukan"
            );

        } else {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd MMM yyyy",
                            new Locale("id", "ID")
                    );

            previewDeadlineLabel.setText(
                    "sampai "
                            + campaignDeadlinePicker
                            .getValue()
                            .format(formatter)
            );
        }
    }

    /* ==================================================
       PILIH GAMBAR
       ================================================== */

    @FXML
    private void handleChooseImage(
            ActionEvent event
    ) {
        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Pilih Gambar Campaign"
        );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "File Gambar",
                        "*.jpg",
                        "*.jpeg",
                        "*.png"
                ),
                new FileChooser.ExtensionFilter(
                        "Semua File",
                        "*.*"
                )
        );

        Stage stage = getStage(event);

        File selectedFile =
                fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            return;
        }

        long maximumSize =
                5L * 1024L * 1024L;

        if (selectedFile.length() > maximumSize) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Ukuran Gambar Terlalu Besar",
                    "Ukuran gambar maksimal adalah 5 MB."
            );
            return;
        }

        String fileName = selectedFile
                .getName()
                .toLowerCase(Locale.ROOT);

        boolean validFormat =
                fileName.endsWith(".jpg")
                        || fileName.endsWith(".jpeg")
                        || fileName.endsWith(".png");

        if (!validFormat) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Format Tidak Didukung",
                    "Gunakan gambar dengan format JPG, JPEG, atau PNG."
            );
            return;
        }

        try {
            Image image = new Image(
                    selectedFile
                            .toURI()
                            .toString()
            );

            if (image.isError()) {
                throw new IllegalArgumentException(
                        "Gambar tidak dapat dibaca."
                );
            }

            selectedCampaignImage = selectedFile;

            previewImageView.setImage(image);
            previewImageView.setVisible(true);
            previewImageView.setManaged(true);

            previewImagePlaceholder.setVisible(false);
            previewImagePlaceholder.setManaged(false);

            uploadDescriptionLabel.setText(
                    selectedFile.getName()
            );

        } catch (Exception exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Gagal Membuka Gambar",
                    "File gambar tidak dapat ditampilkan."
            );
        }
    }

    /* ==================================================
       SIMPAN DAN AJUKAN CAMPAIGN
       ================================================== */

    @FXML
    private void handleSaveDraft() {
        String title =
                campaignTitleField
                        .getText()
                        .trim();

        String description =
                campaignDescriptionArea
                        .getText()
                        .trim();

        String target =
                campaignTargetField
                        .getText()
                        .trim();

        boolean formEmpty =
                title.isEmpty()
                        && description.isEmpty()
                        && campaignCategoryBox
                        .getValue() == null
                        && target.isEmpty()
                        && campaignDeadlinePicker
                        .getValue() == null
                        && selectedCampaignImage == null;

        if (formEmpty) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Draft Kosong",
                    "Isi setidaknya satu bagian form sebelum menyimpan draft."
            );
            return;
        }

        showAlert(
                Alert.AlertType.INFORMATION,
                "Draft Disimpan",
                "Data campaign berhasil disimpan sebagai draft sementara."
        );
    }

    @FXML
    private void handleSubmitCampaign() {
        String title =
                campaignTitleField
                        .getText()
                        .trim();

        String description =
                campaignDescriptionArea
                        .getText()
                        .trim();

        String category =
                campaignCategoryBox.getValue();

        String targetText =
                campaignTargetField
                        .getText()
                        .trim();

        if (title.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Judul Belum Diisi",
                    "Masukkan judul campaign terlebih dahulu."
            );

            campaignTitleField.requestFocus();
            return;
        }

        if (title.length() < 5) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Judul Terlalu Pendek",
                    "Judul campaign minimal terdiri dari 5 karakter."
            );

            campaignTitleField.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Deskripsi Belum Diisi",
                    "Masukkan deskripsi campaign terlebih dahulu."
            );

            campaignDescriptionArea.requestFocus();
            return;
        }

        if (description.length() < 20) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Deskripsi Terlalu Pendek",
                    "Deskripsi campaign minimal terdiri dari 20 karakter."
            );

            campaignDescriptionArea.requestFocus();
            return;
        }

        if (category == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Kategori Belum Dipilih",
                    "Pilih kategori campaign terlebih dahulu."
            );

            campaignCategoryBox.requestFocus();
            return;
        }

        if (targetText.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Target Dana Belum Diisi",
                    "Masukkan target dana campaign."
            );

            campaignTargetField.requestFocus();
            return;
        }

        long targetAmount;

        try {
            targetAmount =
                    Long.parseLong(targetText);

        } catch (NumberFormatException exception) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Target Dana Tidak Valid",
                    "Masukkan target dana menggunakan angka."
            );

            campaignTargetField.requestFocus();
            return;
        }

        if (targetAmount < 100_000) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Target Dana Terlalu Kecil",
                    "Target dana minimal Rp100.000."
            );

            campaignTargetField.requestFocus();
            return;
        }

        if (campaignDeadlinePicker
                .getValue() == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Batas Waktu Belum Dipilih",
                    "Pilih batas waktu campaign."
            );

            campaignDeadlinePicker.requestFocus();
            return;
        }

        if (!campaignDeadlinePicker
                .getValue()
                .isAfter(LocalDate.now())) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Tanggal Tidak Valid",
                    "Batas waktu harus setelah tanggal hari ini."
            );

            campaignDeadlinePicker.requestFocus();
            return;
        }

        if (selectedCampaignImage == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Gambar Belum Dipilih",
                    "Pilih gambar campaign terlebih dahulu."
            );
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmation.setTitle(
                "Ajukan Campaign"
        );

        confirmation.setHeaderText(
                "Ajukan campaign \""
                        + title
                        + "\"?"
        );

        confirmation.setContentText(
                "Kategori: "
                        + category
                        + "\nTarget dana: "
                        + formatRupiah(targetAmount)
                        + "\nBatas waktu: "
                        + campaignDeadlinePicker
                        .getValue()
                        + "\n\nCampaign akan dikirim kepada admin untuk diperiksa."
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isEmpty()
                || result.get()
                != ButtonType.OK) {
            return;
        }

        showAlert(
                Alert.AlertType.INFORMATION,
                "Campaign Berhasil Diajukan",
                "Campaign berhasil dikirim dan sekarang menunggu persetujuan admin."
        );

        resetCreateCampaignForm();
    }

    private void resetCreateCampaignForm() {
        campaignTitleField.clear();
        campaignDescriptionArea.clear();

        campaignCategoryBox
                .getSelectionModel()
                .clearSelection();

        campaignTargetField.clear();

        campaignDeadlinePicker
                .setValue(null);

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
            uploadDescriptionLabel.setText(
                    "Format JPG atau PNG, maksimal 5 MB"
            );
        }

        updateCampaignPreview();
    }

    /* ==================================================
       PILIHAN NOMINAL DONASI
       ================================================== */

    @FXML
    private void handleSelectDonationAmount(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        Object userData = clickedButton.getUserData();

        if (userData == null) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Nominal Tidak Valid",
                    "Nominal donasi tidak ditemukan."
            );
            return;
        }

        try {
            selectedDonationAmount = Long.parseLong(
                    userData.toString()
            );

            if (customDonationField != null) {
                customDonationField.clear();
                customDonationField.setVisible(false);
                customDonationField.setManaged(false);
            }

            updateSelectedDonationButton(clickedButton);

        } catch (NumberFormatException exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Nominal Tidak Valid",
                    "Nominal donasi tidak dapat dibaca."
            );
        }
    }

    @FXML
    private void handleCustomDonation(ActionEvent event) {
        selectedDonationAmount = 0;

        Button clickedButton = (Button) event.getSource();

        updateSelectedDonationButton(clickedButton);

        if (customDonationField != null) {
            customDonationField.setVisible(true);
            customDonationField.setManaged(true);
            customDonationField.clear();
            customDonationField.requestFocus();
        }
    }

    private void updateSelectedDonationButton(Button button) {
        if (selectedDonationButton != null) {
            selectedDonationButton
                    .getStyleClass()
                    .remove("donation-option-selected");
        }

        selectedDonationButton = button;

        if (!button.getStyleClass()
                .contains("donation-option-selected")) {

            button.getStyleClass()
                    .add("donation-option-selected");
        }
    }

    @FXML
    private void handleDonation() {
        long donationAmount = selectedDonationAmount;

        if (customDonationField != null
                && customDonationField.isVisible()) {

            String customText = customDonationField
                    .getText()
                    .replaceAll("\\D", "");

            if (customText.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Nominal Belum Diisi",
                        "Masukkan nominal donasi terlebih dahulu."
                );
                return;
            }

            try {
                donationAmount = Long.parseLong(customText);

            } catch (NumberFormatException exception) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Nominal Tidak Valid",
                        "Masukkan nominal donasi yang benar."
                );
                return;
            }
        }

        if (donationAmount <= 0) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Pilih Nominal",
                    "Pilih salah satu nominal donasi terlebih dahulu."
            );
            return;
        }

        if (donationAmount < 10_000) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Nominal Terlalu Kecil",
                    "Nominal donasi minimal Rp10.000."
            );
            return;
        }

        /*
         * LANGKAH 1: Dialog pilih metode pembayaran.
         * Pengguna memilih salah satu dari tombol
         * Transfer Bank, E-Wallet, atau QRIS.
         * Jika dibatalkan, proses donasi dihentikan.
         */
        PaymentMethod chosenMethod =
                showPaymentMethodDialog();

        if (chosenMethod == null) {
            return;
        }

        selectedPaymentMethod = chosenMethod;

        /*
         * LANGKAH 2: Dialog konfirmasi akhir.
         * Menampilkan ringkasan nominal + metode
         * sebelum donasi benar-benar diproses.
         */
        String formattedAmount =
                formatRupiah(donationAmount);

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmation.setTitle("Konfirmasi Donasi");

        // Diubah menjadi dinamis sesuai campaign yang dipilih oleh pengguna
        confirmation.setHeaderText(
                "Donasi untuk " + selectedCampaignTitle
        );

        confirmation.setContentText(
                "Nominal donasi : "
                        + formattedAmount
                        + "\nMetode pembayaran: "
                        + chosenMethod.getLabel()
                        + "\n\n"
                        + chosenMethod.getDetail()
                        + "\n\nLanjutkan donasi?"
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {
            return;
        }

        showAlert(
                Alert.AlertType.INFORMATION,
                "Donasi Berhasil",
                "Terima kasih! Donasi sebesar "
                        + formattedAmount
                        + " via "
                        + chosenMethod.getLabel()
                        + " berhasil diproses."
        );

        resetDonationState();
    }

    /*
     * Menampilkan dialog pilihan metode pembayaran
     * dengan tampilan custom menggunakan Stage sendiri.
     * Jauh lebih menarik dibanding Alert bawaan JavaFX.
     * Mengembalikan null jika pengguna membatalkan.
     */
    private PaymentMethod showPaymentMethodDialog() {

        /*
         * Gunakan array 1 elemen sebagai wadah hasil
         * agar bisa diisi dari dalam lambda
         * (lambda hanya boleh akses variabel effectively final).
         */
        PaymentMethod[] result = {null};

        Stage dialog = new Stage();
        dialog.setTitle("Pilih Metode Pembayaran");
        dialog.setResizable(false);

        // ── Judul dialog ──────────────────────────────
        Label titleLabel = new Label(
                "Pilih Metode Pembayaran"
        );
        titleLabel.setStyle(
                "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #1a1a2e;"
        );

        Label subtitleLabel = new Label(
                "Pilih salah satu metode pembayaran di bawah ini"
        );
        subtitleLabel.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: #6b7280;"
        );

        javafx.scene.layout.VBox header =
                new javafx.scene.layout.VBox(4,
                        titleLabel, subtitleLabel
                );
        header.setStyle(
                "-fx-padding: 24 24 16 24;"
                        + "-fx-border-color: #e5e7eb;"
                        + "-fx-border-width: 0 0 1 0;"
        );

        // ── Helper: buat kartu metode pembayaran ──────
        // Setiap kartu berisi ikon teks + nama + deskripsi singkat.
        // Klik kartu → simpan hasil → tutup dialog.

        // ── Grup: Transfer Bank ───────────────────────
        Label bankGroupLabel = new Label(
                "🏦  Transfer Bank"
        );
        bankGroupLabel.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #374151;"
                        + "-fx-padding: 0 0 8 0;"
        );

        javafx.scene.layout.HBox bankRow =
                new javafx.scene.layout.HBox(10);

        for (PaymentMethod method : new PaymentMethod[]{
                PaymentMethod.BCA,
                PaymentMethod.MANDIRI,
                PaymentMethod.BNI
        }) {
            bankRow.getChildren().add(
                    buildPaymentCard(
                            method, result, dialog
                    )
            );
        }

        // ── Grup: E-Wallet ────────────────────────────
        Label walletGroupLabel = new Label(
                "💳  E-Wallet"
        );
        walletGroupLabel.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #374151;"
                        + "-fx-padding: 16 0 8 0;"
        );

        javafx.scene.layout.HBox walletRow =
                new javafx.scene.layout.HBox(10);

        for (PaymentMethod method : new PaymentMethod[]{
                PaymentMethod.GOPAY,
                PaymentMethod.OVO,
                PaymentMethod.DANA
        }) {
            walletRow.getChildren().add(
                    buildPaymentCard(
                            method, result, dialog
                    )
            );
        }

        // ── Grup: QRIS ────────────────────────────────
        Label qrisGroupLabel = new Label(
                "📷  Scan Barcode"
        );
        qrisGroupLabel.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #374151;"
                        + "-fx-padding: 16 0 8 0;"
        );

        javafx.scene.layout.HBox qrisRow =
                new javafx.scene.layout.HBox(10);

        qrisRow.getChildren().add(
                buildPaymentCard(
                        PaymentMethod.QRIS,
                        result,
                        dialog
                )
        );

        // ── Tombol Batal ──────────────────────────────
        Button cancelButton = new Button("Batal");
        cancelButton.setStyle(
                "-fx-background-color: #f3f4f6;"
                        + "-fx-text-fill: #374151;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 10 24 10 24;"
                        + "-fx-background-radius: 8px;"
                        + "-fx-cursor: hand;"
        );
        cancelButton.setOnAction(e -> dialog.close());

        cancelButton.setOnMouseEntered(e ->
                cancelButton.setStyle(
                        "-fx-background-color: #e5e7eb;"
                                + "-fx-text-fill: #111827;"
                                + "-fx-font-size: 13px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-padding: 10 24 10 24;"
                                + "-fx-background-radius: 8px;"
                                + "-fx-cursor: hand;"
                )
        );
        cancelButton.setOnMouseExited(e ->
                cancelButton.setStyle(
                        "-fx-background-color: #f3f4f6;"
                                + "-fx-text-fill: #374151;"
                                + "-fx-font-size: 13px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-padding: 10 24 10 24;"
                                + "-fx-background-radius: 8px;"
                                + "-fx-cursor: hand;"
                )
        );

        javafx.scene.layout.HBox footer =
                new javafx.scene.layout.HBox();
        footer.setAlignment(
                javafx.geometry.Pos.CENTER_RIGHT
        );
        footer.setStyle(
                "-fx-padding: 16 24 20 24;"
                        + "-fx-border-color: #e5e7eb;"
                        + "-fx-border-width: 1 0 0 0;"
        );
        footer.getChildren().add(cancelButton);

        // ── Body: semua grup ──────────────────────────
        javafx.scene.layout.VBox body =
                new javafx.scene.layout.VBox(
                        bankGroupLabel,
                        bankRow,
                        walletGroupLabel,
                        walletRow,
                        qrisGroupLabel,
                        qrisRow
                );
        body.setStyle("-fx-padding: 20 24 4 24;");

        // ── Root layout ───────────────────────────────
        javafx.scene.layout.VBox root =
                new javafx.scene.layout.VBox(
                        header,
                        body,
                        footer
                );
        root.setStyle(
                "-fx-background-color: #ffffff;"
                        + "-fx-effect: dropshadow("
                        + "gaussian, rgba(0,0,0,0.12), 20, 0, 0, 4);"
        );

        javafx.scene.Scene scene =
                new javafx.scene.Scene(root, 520, 420);

        dialog.setScene(scene);
        dialog.initModality(
                javafx.stage.Modality.APPLICATION_MODAL
        );
        dialog.centerOnScreen();
        dialog.showAndWait();

        return result[0];
    }

    /*
     * Membangun satu kartu metode pembayaran.
     * Kartu berisi ikon, nama bank/wallet, dan
     * deskripsi singkat. Hover dan klik diberi efek visual.
     */
    private javafx.scene.layout.VBox buildPaymentCard(
            PaymentMethod method,
            PaymentMethod[] result,
            Stage dialog
    ) {
        String[] iconAndSub =
                getPaymentIconAndSub(method);

        Label iconLabel = new Label(iconAndSub[0]);
        iconLabel.setStyle(
                "-fx-font-size: 26px;"
        );

        Label nameLabel = new Label(
                getPaymentShortName(method)
        );
        nameLabel.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #1a1a2e;"
        );

        Label subLabel = new Label(iconAndSub[1]);
        subLabel.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #9ca3af;"
        );

        javafx.scene.layout.VBox card =
                new javafx.scene.layout.VBox(
                        6,
                        iconLabel,
                        nameLabel,
                        subLabel
                );
        card.setAlignment(
                javafx.geometry.Pos.CENTER
        );
        card.setPrefWidth(130);
        card.setPrefHeight(90);
        card.setStyle(
                "-fx-background-color: #f9fafb;"
                        + "-fx-background-radius: 12px;"
                        + "-fx-border-color: #e5e7eb;"
                        + "-fx-border-radius: 12px;"
                        + "-fx-border-width: 1.5px;"
                        + "-fx-cursor: hand;"
                        + "-fx-padding: 12 8 12 8;"
        );

        card.setOnMouseEntered(e ->
                card.setStyle(
                        "-fx-background-color: #eff6ff;"
                                + "-fx-background-radius: 12px;"
                                + "-fx-border-color: #3b82f6;"
                                + "-fx-border-radius: 12px;"
                                + "-fx-border-width: 2px;"
                                + "-fx-cursor: hand;"
                                + "-fx-padding: 12 8 12 8;"
                )
        );

        card.setOnMouseExited(e ->
                card.setStyle(
                        "-fx-background-color: #f9fafb;"
                                + "-fx-background-radius: 12px;"
                                + "-fx-border-color: #e5e7eb;"
                                + "-fx-border-radius: 12px;"
                                + "-fx-border-width: 1.5px;"
                                + "-fx-cursor: hand;"
                                + "-fx-padding: 12 8 12 8;"
                )
        );

        card.setOnMouseClicked(e -> {
            result[0] = method;
            dialog.close();
        });

        return card;
    }

    /*
     * Mengembalikan [ikon emoji, teks sub-label]
     * untuk setiap metode pembayaran.
     */
    private String[] getPaymentIconAndSub(
            PaymentMethod method
    ) {
        return switch (method) {
            case BCA      -> new String[]{"🔵", "Transfer Bank"};
            case MANDIRI  -> new String[]{"🟡", "Transfer Bank"};
            case BNI      -> new String[]{"🟠", "Transfer Bank"};
            case GOPAY    -> new String[]{"🟢", "E-Wallet"};
            case OVO      -> new String[]{"🟣", "E-Wallet"};
            case DANA     -> new String[]{"🔷", "E-Wallet"};
            case QRIS     -> new String[]{"⬛", "Scan Barcode"};
        };
    }

    /*
     * Mengembalikan nama pendek untuk ditampilkan
     * di kartu metode pembayaran.
     */
    private String getPaymentShortName(
            PaymentMethod method
    ) {
        return switch (method) {
            case BCA     -> "BCA";
            case MANDIRI -> "Mandiri";
            case BNI     -> "BNI";
            case GOPAY   -> "GoPay";
            case OVO     -> "OVO";
            case DANA    -> "DANA";
            case QRIS    -> "QRIS";
        };
    }

    /*
     * Mereset semua state donasi setelah
     * transaksi berhasil diproses.
     */
    private void resetDonationState() {
        selectedDonationAmount = 0;
        selectedPaymentMethod = null;

        if (selectedDonationButton != null) {
            selectedDonationButton
                    .getStyleClass()
                    .remove("donation-option-selected");

            selectedDonationButton = null;
        }

        if (customDonationField != null) {
            customDonationField.clear();
            customDonationField.setVisible(false);
            customDonationField.setManaged(false);
        }
    }

        /* ==================================================
           NAVIGASI
           ================================================== */

    @FXML
    private void openDashboard(
            ActionEvent event
    ) {
        openPage(
                event,
                "/view/dashboard.fxml",
                "CrowdCare - Dashboard"
        );
    }

    @FXML
    private void openCampaigns(
            ActionEvent event
    ) {
        openPage(
                event,
                "/view/campaigns.fxml",
                "CrowdCare - Campaign"
        );
    }

    @FXML
    private void openCreateCampaign(
            ActionEvent event
    ) {
        openPage(
                event,
                "/view/create-campaign.fxml",
                "CrowdCare - Buat Campaign"
        );
    }

    @FXML
    private void openDonationHistory(
            ActionEvent event
    ) {
        openPage(
                event,
                "/view/donation-history.fxml",
                "CrowdCare - Riwayat Donasi"
        );
    }

    // Membuka halaman detail secara dinamis dengan mengintip label VBox asal tombol
    @FXML
    private void openCampaignDetail(
            ActionEvent event
    ) {
        try {
            // Ambil referensi tombol yang diklik
            Button clickedButton = (Button) event.getSource();
            // Ambil VBox tempat tombol "Lihat Detail" berada
            VBox currentCard = (VBox) clickedButton.getParent();

            // Cari teks Judul Campaign di dalam VBox (Mengabaikan label status/kategori)
            for (Node child : currentCard.getChildren()) {
                if (child instanceof Label) {
                    String txt = ((Label) child).getText();
                    // Filter agar tidak sengaja mengambil label "terkumpul", "hari lagi", atau nama Kategori Kapital
                    if (!txt.contains("terkumpul") && !txt.contains("hari lagi") &&
                            !txt.equals("PENDIDIKAN") && !txt.equals("KESEHATAN") &&
                            !txt.equals("SOSIAL") && !txt.equals("BENCANA") && !txt.equals("LINGKUNGAN")) {

                        selectedCampaignTitle = txt; // Ambil judul aslinya (Contoh: "Bantuan Operasi untuk Raka")
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal mendeteksi teks judul secara otomatis, menggunakan default.");
        }

        // Muat Halaman Detail
        openPage(
                event,
                "/view/campaign-detail.fxml",
                "CrowdCare - Detail Campaign"
        );
    }

    @FXML
    private void showProfile(
            ActionEvent event
    ) {
        openPage(
                event,
                "/view/profile.fxml",
                "CrowdCare - Profil Saya"
        );
    }

    @FXML
    private void showSettings(
            ActionEvent event
    ) {
        openPage(
                event,
                "/view/settings.fxml",
                "CrowdCare - Pengaturan"
        );
    }

    @FXML
    private void logout(
            ActionEvent event
    ) {
        UserSession.getInstance().logout();

        try {
            URL loginUrl =
                    getClass().getResource(
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

        /* ==================================================
           PROFIL DAN PENGATURAN
           ================================================== */

    @FXML
    private void handleSaveProfile() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Profil Disimpan",
                "Perubahan profil berhasil disimpan."
        );
    }

    @FXML
    private void handleChangePhoto() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Ubah Foto",
                "Pemilihan foto profil akan dihubungkan pada tahap berikutnya."
        );
    }

    @FXML
    private void handleSaveSettings() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Pengaturan Disimpan",
                "Perubahan pengaturan berhasil disimpan."
        );
    }

    @FXML
    private void handleChangePassword() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Kata Sandi Diperbarui",
                "Kata sandi berhasil diperbarui."
        );
    }

    @FXML
    private void handleDeleteAccount() {
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmation.setTitle(
                "Hapus Akun"
        );

        confirmation.setHeaderText(
                "Apakah Anda yakin ingin menghapus akun?"
        );

        confirmation.setContentText(
                "Tindakan ini tidak dapat dibatalkan."
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {
            return;
        }

        UserSession.getInstance().logout();

        showAlert(
                Alert.AlertType.INFORMATION,
                "Akun Dihapus",
                "Akun Anda telah berhasil dihapus."
        );
    }

        /* ==================================================
           AKSI LAIN
           ================================================== */

    @FXML
    private void handleShare() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Bagikan Campaign",
                "Tautan campaign berhasil disalin."
        );
    }

    @FXML
    private void handleDownloadReport() {
        showAlert(
                Alert.AlertType.INFORMATION,
                "Unduh Laporan",
                "Laporan riwayat donasi berhasil dibuat."
        );
    }

        /* ==================================================
           FUNGSI BANTUAN
           ================================================== */

    private String formatRupiah(
            long amount
    ) {
        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        new Locale("id", "ID")
                );

        return "Rp"
                + formatter.format(amount);
    }

    private void openPage(
            ActionEvent event,
            String fxmlPath,
            String title
    ) {
        try {
            URL pageUrl =
                    getClass().getResource(
                            fxmlPath
                    );

            if (pageUrl == null) {
                throw new IOException(
                        "File tidak ditemukan: "
                                + fxmlPath
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(pageUrl);

            loader.setController(
                    new NavigationController()
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

    private Stage getStage(
            ActionEvent event
    ) {
        Node source =
                (Node) event.getSource();

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