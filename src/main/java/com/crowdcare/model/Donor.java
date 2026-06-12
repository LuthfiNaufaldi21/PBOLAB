package com.crowdcare.model;

/*
 * INHERITANCE:
 * Donor merupakan turunan dari User.
 */
public class Donor extends User {

    private long totalDonation;

    public Donor(
            String id,
            String fullName,
            String username,
            String password
    ) {
        super(
                id,
                fullName,
                username,
                password
        );

        totalDonation = 0;
    }

    public long getTotalDonation() {
        return totalDonation;
    }

    public void addDonation(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Nominal donasi harus lebih dari nol."
            );
        }

        totalDonation += amount;
    }

    @Override
    public String getRoleName() {
        return "Donatur";
    }

    @Override
    public String getDashboardFxml() {
        return "/view/dashboard.fxml";
    }

    @Override
    public String getWindowTitle() {
        return "CrowdCare - Dashboard Donatur";
    }

    @Override
    public boolean canDonate() {
        return true;
    }

    @Override
    public boolean canCreateCampaign() {
        return true;
    }

    @Override
    public boolean canApproveCampaign() {
        return false;
    }
}