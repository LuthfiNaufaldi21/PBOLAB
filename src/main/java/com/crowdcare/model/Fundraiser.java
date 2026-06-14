package com.crowdcare.model;

/*
 * INHERITANCE:
 * Fundraiser merupakan turunan dari User.
 */
public class Fundraiser extends User {

    private int totalCampaignCreated;

    public Fundraiser(
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

        totalCampaignCreated = 0;
    }

    public int getTotalCampaignCreated() {
        return totalCampaignCreated;
    }

    public void addCreatedCampaign() {
        totalCampaignCreated++;
    }

    @Override
    public String getRoleName() {
        return "Penggalang Dana";
    }

    @Override
    public String getDashboardFxml() {
        return "/view/dashboard-fundraiser.fxml";
    }

    @Override
    public String getWindowTitle() {
        return "CrowdCare - Dashboard Penggalang Dana";
    }

    /*
     * FIX: Fundraiser tidak bisa berdonasi.
     * Hanya Donor yang bisa berdonasi.
     */
    @Override
    public boolean canDonate() {
        return false;
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