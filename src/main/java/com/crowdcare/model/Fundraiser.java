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
        return "/view/dashboard.fxml";
    }

    @Override
    public String getWindowTitle() {
        return "CrowdCare - Dashboard Penggalang Dana";
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