package com.crowdcare;

import com.crowdcare.model.Admin;
import com.crowdcare.model.Donor;
import com.crowdcare.model.Fundraiser;
import com.crowdcare.model.User;

import java.util.List;

public class PboPillarsDemo {

    public static void main(String[] args) {

        /*
         * ABSTRACTION:
         * List menggunakan tipe abstract User.
         *
         * INHERITANCE:
         * Isi list berupa Admin, Donor, Fundraiser
         * yang semuanya extends User.
         */
        List<User> users = List.of(
                new Admin(
                        "U001",
                        "Administrator",
                        "admin",
                        "admin"
                ),
                new Donor(
                        "U002",
                        "Argha",
                        "user",
                        "user"
                ),
                new Fundraiser(
                        "U003",
                        "Penggalang Dana",
                        "penggalang",
                        "penggalang"
                )
        );

        /*
         * POLYMORPHISM:
         * Method yang sama memberikan hasil berbeda
         * sesuai object aslinya.
         */
        for (User user : users) {
            System.out.println(
                    "Nama      : "
                            + user.getFullName()
            );

            System.out.println(
                    "Role      : "
                            + user.getRoleName()
            );

            System.out.println(
                    "Dashboard : "
                            + user.getDashboardFxml()
            );

            System.out.println(
                    "Bisa approve: "
                            + user.canApproveCampaign()
            );

            System.out.println("--------------------");
        }
    }
}