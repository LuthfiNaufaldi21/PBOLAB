package com.crowdcare.service;

import com.crowdcare.entity.UserEntity;
import com.crowdcare.model.Admin;
import com.crowdcare.model.Donor;
import com.crowdcare.model.Fundraiser;
import com.crowdcare.model.User;
import com.crowdcare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DatabaseUserService {

    private final UserRepository userRepository;

    public DatabaseUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<UserEntity> found = userRepository.findByUsernameIgnoreCase(username);

        if (found.isEmpty()) return Optional.empty();

        UserEntity entity = found.get();
        if (!entity.getPassword().equals(password)) return Optional.empty();

        return Optional.of(toModel(entity));
    }

    public User register(String fullName, String username, String password, String selectedRole) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username sudah digunakan.");
        }

        if (username == null || username.isBlank() || username.length() < 3) {
            throw new IllegalArgumentException("Username minimal 3 karakter.");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        }

        String role = "Penggalang Dana".equalsIgnoreCase(selectedRole) ? "FUNDRAISER" : "DONOR";
        String generatedId = "USR-" + String.format("%03d", userRepository.count() + 1);

        UserEntity entity = new UserEntity(generatedId, fullName, username, password, role);
        userRepository.save(entity);

        return toModel(entity);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updateUser(String id, String fullName, String email, String phone, String address, String bio) {
        UserEntity entity = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Pengguna tidak ditemukan."));

        if (fullName != null && !fullName.isBlank()) entity.setFullName(fullName);
        if (email != null) entity.setEmail(email);
        if (phone != null) entity.setPhone(phone);
        if (address != null) entity.setAddress(address);
        if (bio != null) entity.setBio(bio);

        userRepository.save(entity);
        return entity;
    }

    @Transactional
    public void updatePassword(String id, String oldPassword, String newPassword) {
        UserEntity entity = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Pengguna tidak ditemukan."));

        if (!entity.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Kata sandi lama tidak sesuai.");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Kata sandi baru minimal 6 karakter.");
        }

        entity.setPassword(newPassword);
        userRepository.save(entity);
    }

    @Transactional
    public void updateSettings(String id, String language, Boolean darkMode, Boolean animationEnabled,
                                Boolean notifNewCampaign, Boolean notifCampaignProgress,
                                Boolean notifDonationStatus, Boolean notifPromoEmail,
                                Boolean notifCampaignReminder, Boolean privacyShowName,
                                Boolean privacyShowProfile, Boolean privacyHideAmount) {
        UserEntity entity = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Pengguna tidak ditemukan."));

        if (language != null) entity.setLanguage(language);
        if (darkMode != null) entity.setDarkMode(darkMode);
        if (animationEnabled != null) entity.setAnimationEnabled(animationEnabled);
        if (notifNewCampaign != null) entity.setNotifNewCampaign(notifNewCampaign);
        if (notifCampaignProgress != null) entity.setNotifCampaignProgress(notifCampaignProgress);
        if (notifDonationStatus != null) entity.setNotifDonationStatus(notifDonationStatus);
        if (notifPromoEmail != null) entity.setNotifPromoEmail(notifPromoEmail);
        if (notifCampaignReminder != null) entity.setNotifCampaignReminder(notifCampaignReminder);
        if (privacyShowName != null) entity.setPrivacyShowName(privacyShowName);
        if (privacyShowProfile != null) entity.setPrivacyShowProfile(privacyShowProfile);
        if (privacyHideAmount != null) entity.setPrivacyHideAmount(privacyHideAmount);

        userRepository.save(entity);
    }

    @Transactional
    public void updateLastLogin(String id) {
        userRepository.findById(id).ifPresent(entity -> {
            entity.setLastLogin(LocalDateTime.now());
            userRepository.save(entity);
        });
    }

    @Transactional
    public void updateAvatar(String id, byte[] avatar) {
        userRepository.findById(id).ifPresent(entity -> {
            entity.setAvatar(avatar);
            userRepository.save(entity);
        });
    }

    @Transactional
    public void removeAvatar(String id) {
        userRepository.findById(id).ifPresent(entity -> {
            entity.setAvatar(null);
            userRepository.save(entity);
        });
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserEntity findEntityById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findById(String id) {
        return userRepository.findById(id).map(this::toModel).orElse(null);
    }

    public User toModel(UserEntity entity) {
        User user = switch (entity.getRole()) {
            case "ADMIN" -> new Admin(entity.getId(), entity.getFullName(),
                    entity.getUsername(), entity.getPassword());
            case "FUNDRAISER" -> new Fundraiser(entity.getId(), entity.getFullName(),
                    entity.getUsername(), entity.getPassword());
            default -> new Donor(entity.getId(), entity.getFullName(),
                    entity.getUsername(), entity.getPassword());
        };
        return user;
    }
}