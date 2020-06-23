package com.example.mothercare.Models;

import android.graphics.Bitmap;

public class EmergencyContact {
    public String emergencyID, name, email, contactNumber;
    public Bitmap profilePic;

    public EmergencyContact() {
    }

    public EmergencyContact(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public EmergencyContact(String emergencyID, String name, String email, String contactNumber) {
        this.emergencyID = emergencyID;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setEmergencyID(String emergencyID) {
        this.emergencyID = emergencyID;
    }

    public String getEmergencyID() {
        return emergencyID;
    }
}
