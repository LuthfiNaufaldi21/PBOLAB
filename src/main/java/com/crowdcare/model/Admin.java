package com.crowdcare.model;

/*
 * INHERITANCE:
 * Admin merupakan turunan dari User.
 */
public class Admin extends User {

    public Admin(
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
    }

    /*
     * POLYMORPHISM:
     * Method dari User dioverride dengan perilaku Admin.
     */
    @Override
    public String getRoleName() {
        return "Admin";
    }

    @Override
    public String getDashboardFxml() {
        return "/view/admin-home.fxml";
    }

    @Override
    public String getWindowTitle() {
        return "CrowdCare - Dashboard Admin";
    }

    @Override
    public boolean canDonate() {
        return false;
    }

    @Override
    public boolean canCreateCampaign() {
        return false;
    }

    @Override
    public boolean canApproveCampaign() {
        return true;
    }
}