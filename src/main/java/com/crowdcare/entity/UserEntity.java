package com.crowdcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
}