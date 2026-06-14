package com.crowdcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, message = "Username minimal 3 karakter")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    // Validasi panjang password dilakukan di DatabaseUserService, bukan di JPA level
    // agar akun default (admin/user) tetap bisa di-seed
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "language")
    private String language = "Bahasa Indonesia";

    @Column(name = "dark_mode")
    private Boolean darkMode = false;

    @Column(name = "animation_enabled")
    private Boolean animationEnabled = true;

    @Column(name = "notif_new_campaign")
    private Boolean notifNewCampaign = true;

    @Column(name = "notif_campaign_progress")
    private Boolean notifCampaignProgress = true;

    @Column(name = "notif_donation_status")
    private Boolean notifDonationStatus = true;

    @Column(name = "notif_promo_email")
    private Boolean notifPromoEmail = false;

    @Column(name = "notif_campaign_reminder")
    private Boolean notifCampaignReminder = true;

    @Column(name = "privacy_show_name")
    private Boolean privacyShowName = true;

    @Column(name = "privacy_show_profile")
    private Boolean privacyShowProfile = true;

    @Column(name = "privacy_hide_amount")
    private Boolean privacyHideAmount = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ========================
    // Konstruktor
    // ========================

    public UserEntity() {}

    public UserEntity(String id, String fullName, String username, String password, String role) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // ========================
    // Getter & Setter (ENCAPSULATION)
    // ========================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Boolean getDarkMode() { return darkMode; }
    public void setDarkMode(Boolean darkMode) { this.darkMode = darkMode; }

    public Boolean getAnimationEnabled() { return animationEnabled; }
    public void setAnimationEnabled(Boolean animationEnabled) { this.animationEnabled = animationEnabled; }

    public Boolean getNotifNewCampaign() { return notifNewCampaign; }
    public void setNotifNewCampaign(Boolean notifNewCampaign) { this.notifNewCampaign = notifNewCampaign; }

    public Boolean getNotifCampaignProgress() { return notifCampaignProgress; }
    public void setNotifCampaignProgress(Boolean notifCampaignProgress) { this.notifCampaignProgress = notifCampaignProgress; }

    public Boolean getNotifDonationStatus() { return notifDonationStatus; }
    public void setNotifDonationStatus(Boolean notifDonationStatus) { this.notifDonationStatus = notifDonationStatus; }

    public Boolean getNotifPromoEmail() { return notifPromoEmail; }
    public void setNotifPromoEmail(Boolean notifPromoEmail) { this.notifPromoEmail = notifPromoEmail; }

    public Boolean getNotifCampaignReminder() { return notifCampaignReminder; }
    public void setNotifCampaignReminder(Boolean notifCampaignReminder) { this.notifCampaignReminder = notifCampaignReminder; }

    public Boolean getPrivacyShowName() { return privacyShowName; }
    public void setPrivacyShowName(Boolean privacyShowName) { this.privacyShowName = privacyShowName; }

    public Boolean getPrivacyShowProfile() { return privacyShowProfile; }
    public void setPrivacyShowProfile(Boolean privacyShowProfile) { this.privacyShowProfile = privacyShowProfile; }

    public Boolean getPrivacyHideAmount() { return privacyHideAmount; }
    public void setPrivacyHideAmount(Boolean privacyHideAmount) { this.privacyHideAmount = privacyHideAmount; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public byte[] getAvatar() { return avatar; }
    public void setAvatar(byte[] avatar) { this.avatar = avatar; }
}