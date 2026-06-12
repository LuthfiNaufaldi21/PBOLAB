package com.crowdcare.session;

import com.crowdcare.model.User;

public class UserSession {

    private static final UserSession INSTANCE =
            new UserSession();

    private User currentUser;

    private UserSession() {
    }

    public static UserSession getInstance() {
        return INSTANCE;
    }

    public void login(User user) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "Pengguna tidak boleh kosong."
            );
        }

        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
    }
}