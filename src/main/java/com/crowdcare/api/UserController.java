package com.crowdcare.api;

import com.crowdcare.entity.UserEntity;
import com.crowdcare.model.User;
import com.crowdcare.service.DatabaseUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final DatabaseUserService userService;

    public UserController(DatabaseUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(error("Username dan password harus diisi."));
        }

        Optional<User> result = userService.authenticate(username, password);

        if (result.isEmpty()) {
            return ResponseEntity.status(401).body(error("Username atau password salah."));
        }

        User user = result.get();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("username", user.getUsername());
        response.put("role", user.getRoleName());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String fullName = body.get("fullName");
        String username = body.get("username");
        String password = body.get("password");
        String role     = body.get("role");

        if (fullName == null || username == null || password == null || role == null) {
            return ResponseEntity.badRequest().body(error("Semua field harus diisi."));
        }

        try {
            User registered = userService.register(fullName, username, password, role);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Akun berhasil dibuat.");
            response.put("username", registered.getUsername());
            response.put("role", registered.getRoleName());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        boolean exists = userService.usernameExists(username);
        Map<String, Object> response = new HashMap<>();
        response.put("available", !exists);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> err = new HashMap<>();
        err.put("success", false);
        err.put("message", message);
        return err;
    }
}