package com.example.mothercare.Models;

import android.graphics.Bitmap;

public class Patient {
    public String patientID, username, email, phoneNumber;
    public Bitmap profilePic;
    public int pregnancyMonth = 0;
    public int trimester = 0;
    private String address;
    public int pregnancyWeek = 0;

    public Patient() {
    }

    public Patient(String patientID, String username, String email, String phoneNumber, int trimester, int pregnancyMonth, int pregnancyWeek) {
        this.patientID = patientID;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.trimester = trimester;
        this.pregnancyMonth = pregnancyMonth;
        this.pregnancyWeek = pregnancyWeek;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
