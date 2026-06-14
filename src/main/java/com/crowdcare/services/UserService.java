package com.crowdcare.services;

import com.crowdcare.CrowdCareApplication;
import com.crowdcare.entity.UserEntity;
import com.crowdcare.model.User;
import com.crowdcare.service.DatabaseUserService;

import java.util.Optional;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    private UserService() {}

    public static UserService getInstance() {
        return INSTANCE;
    }

    private DatabaseUserService getDbService() {
        return CrowdCareApplication.getContext().getBean(DatabaseUserService.class);
    }

    public Optional<User> authenticate(String username, String password) {
        return getDbService().authenticate(username, password);
    }

    public User register(String fullName, String username, String password, String selectedRole) {
        return getDbService().register(fullName, username, password, selectedRole);
    }

    public boolean usernameExists(String username) {
        return getDbService().usernameExists(username);
    }

    public UserEntity updateUser(String id, String fullName, String email, String phone, String address, String bio) {
        return getDbService().updateUser(id, fullName, email, phone, address, bio);
    }

    public void updatePassword(String id, String oldPassword, String newPassword) {
        getDbService().updatePassword(id, oldPassword, newPassword);
    }

    public void updateSettings(String id, String language, Boolean darkMode, Boolean animationEnabled,
                                Boolean notifNewCampaign, Boolean notifCampaignProgress,
                                Boolean notifDonationStatus, Boolean notifPromoEmail,
                                Boolean notifCampaignReminder, Boolean privacyShowName,
                                Boolean privacyShowProfile, Boolean privacyHideAmount) {
        getDbService().updateSettings(id, language, darkMode, animationEnabled,
                notifNewCampaign, notifCampaignProgress,
                notifDonationStatus, notifPromoEmail,
                notifCampaignReminder, privacyShowName,
                privacyShowProfile, privacyHideAmount);
    }

    public void updateLastLogin(String id) {
        getDbService().updateLastLogin(id);
    }

    public void updateAvatar(String id, byte[] avatar) {
        getDbService().updateAvatar(id, avatar);
    }

    public void deleteUser(String id) {
        getDbService().deleteUser(id);
    }

    public UserEntity findEntityById(String id) {
        return getDbService().findEntityById(id);
    }

    public User findById(String id) {
        return getDbService().findById(id);
    }
}