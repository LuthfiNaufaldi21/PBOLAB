package com.crowdcare.services;

import com.crowdcare.CrowdCareApplication;
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
}