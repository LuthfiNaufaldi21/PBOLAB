package com.crowdcare.service;

import com.crowdcare.entity.UserEntity;
import com.crowdcare.model.Admin;
import com.crowdcare.model.Donor;
import com.crowdcare.model.Fundraiser;
import com.crowdcare.model.User;
import com.crowdcare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public User toModel(UserEntity entity) {
        return switch (entity.getRole()) {
            case "ADMIN" -> new Admin(entity.getId(), entity.getFullName(),
                    entity.getUsername(), entity.getPassword());
            case "FUNDRAISER" -> new Fundraiser(entity.getId(), entity.getFullName(),
                    entity.getUsername(), entity.getPassword());
            default -> new Donor(entity.getId(), entity.getFullName(),
                    entity.getUsername(), entity.getPassword());
        };
    }
}