package com.crowdcare.services;

import com.crowdcare.model.Admin;
import com.crowdcare.model.Donor;
import com.crowdcare.model.Fundraiser;
import com.crowdcare.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserService {

    private static final UserService INSTANCE =
            new UserService();

    private final List<User> users =
            new ArrayList<>();

    /*
     * Constructor dibuat private agar UserService
     * hanya mempunyai satu instance.
     */
    private UserService() {
        seedDefaultUsers();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    private void seedDefaultUsers() {
        users.add(
                new Admin(
                        "USR-ADMIN-001",
                        "Administrator",
                        "admin",
                        "admin"
                )
        );

        users.add(
                new Donor(
                        "USR-DONOR-001",
                        "Argha",
                        "user",
                        "user"
                )
        );

        users.add(
                new Fundraiser(
                        "USR-FUNDRAISER-001",
                        "Penggalang Dana",
                        "penggalang",
                        "penggalang"
                )
        );
    }

    public Optional<User> authenticate(
            String username,
            String password
    ) {
        return users.stream()
                .filter(user ->
                        user.getUsername()
                                .equalsIgnoreCase(username)
                )
                .filter(user ->
                        user.matchesPassword(password)
                )
                .findFirst();
    }

    public User register(
            String fullName,
            String username,
            String password,
            String selectedRole
    ) {
        if (usernameExists(username)) {
            throw new IllegalArgumentException(
                    "Email atau username sudah digunakan."
            );
        }

        String generatedId =
                String.format(
                        "USR-%03d",
                        users.size() + 1
                );

        User newUser;

        if ("Penggalang Dana".equalsIgnoreCase(
                selectedRole
        )) {
            newUser = new Fundraiser(
                    generatedId,
                    fullName,
                    username,
                    password
            );
        } else {
            newUser = new Donor(
                    generatedId,
                    fullName,
                    username,
                    password
            );
        }

        users.add(newUser);

        return newUser;
    }

    public boolean usernameExists(String username) {
        return users.stream()
                .anyMatch(user ->
                        user.getUsername()
                                .equalsIgnoreCase(username)
                );
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }
}