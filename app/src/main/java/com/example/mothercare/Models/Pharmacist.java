package com.example.mothercare.Models;

import android.graphics.Bitmap;

public class Pharmacist {
    public String pharmacistID, username, email, phoneNumber, address;
    public Bitmap profilePic;

    public Pharmacist() {
    }

    public Pharmacist(String pharmacistID, String username, String email, String phoneNumber, String address) {
        this.pharmacistID = pharmacistID;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setPharmacistID(String pharmacistID) {
        this.pharmacistID = pharmacistID;
    }

}
