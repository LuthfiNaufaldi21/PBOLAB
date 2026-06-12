package com.crowdcare.model;

/*
 * ABSTRACTION:
 * User dibuat sebagai abstract class karena merupakan
 * gambaran umum dari seluruh jenis pengguna CrowdCare.
 *
 * ENCAPSULATION:
 * Seluruh atribut dibuat private dan hanya dapat
 * diakses melalui getter, setter, atau method khusus.
 */
public abstract class User {

    private final String id;
    private String fullName;
    private String username;
    private String password;

    protected User(
            String id,
            String fullName,
            String username,
            String password
    ) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                    "Nama lengkap tidak boleh kosong."
            );
        }

        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username tidak boleh kosong."
            );
        }

        this.username = username;
    }

    /*
     * Password tidak memiliki getter agar tidak bisa
     * dibaca langsung dari luar class.
     */
    public boolean matchesPassword(String inputPassword) {
        return password != null
                && password.equals(inputPassword);
    }

    public void changePassword(
            String oldPassword,
            String newPassword
    ) {
        if (!matchesPassword(oldPassword)) {
            throw new IllegalArgumentException(
                    "Kata sandi lama tidak sesuai."
            );
        }

        if (newPassword == null
                || newPassword.length() < 6) {

            throw new IllegalArgumentException(
                    "Kata sandi baru minimal 6 karakter."
            );
        }

        password = newPassword;
    }

    /*
     * Method abstract berikut wajib dioverride
     * oleh setiap subclass.
     */
    public abstract String getRoleName();

    public abstract String getDashboardFxml();

    public abstract String getWindowTitle();

    public abstract boolean canDonate();

    public abstract boolean canCreateCampaign();

    public abstract boolean canApproveCampaign();
}