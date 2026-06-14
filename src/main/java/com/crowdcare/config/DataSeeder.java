package com.crowdcare.config;

import com.crowdcare.entity.UserEntity;
import com.crowdcare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        // Hanya seed jika belum ada data
        if (userRepository.count() > 0) return;

        userRepository.save(new UserEntity(
                "USR-ADMIN-001",
                "Administrator",
                "admin",
                "admin",
                "ADMIN"
        ));

        userRepository.save(new UserEntity(
                "USR-DONOR-001",
                "Donatur",
                "user",
                "user",
                "DONOR"
        ));

        userRepository.save(new UserEntity(
                "USR-FUNDRAISER-001",
                "Penggalang Dana",
                "penggalang",
                "penggalang",
                "FUNDRAISER"
        ));

        System.out.println("[CrowdCare] Database H2 berhasil di-seed dengan akun default.");
    }
}