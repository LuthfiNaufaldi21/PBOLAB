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
    private TextField customDonationField;

    private long selectedDonationAmount = 0;
    private Button selectedDonationButton;

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

        String formattedAmount = formatRupiah(donationAmount);

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmation.setTitle("Konfirmasi Donasi");

        confirmation.setHeaderText(
                "Donasi untuk Bantu Pendidikan Anak Desa"
        );

        confirmation.setContentText(
                "Nominal donasi: "
                        + formattedAmount
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
                        + " berhasil diproses."
        );

        selectedDonationAmount = 0;

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

    @FXML
    private void openCampaignDetail(
            ActionEvent event
    ) {
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

        confirmation.showAndWait();
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