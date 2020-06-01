package com.example.mothercare.Models;

import android.graphics.Bitmap;

public class Doctor {
    public String doctorID, username, email, phoneNumber, qualification,
            worksat,
            institute;
    public int experience;
    public Bitmap profilePic;
    public int rating, charges;
    public boolean mondayAvailable, tuesdayAvailable, wednesdayAvailable,
            thursdayAvailable,
            fridayAvailable, saturdayAvailable, sundayAvailable;
    private UserLocation userLocation;

    public String getMondayStartTime() {
        return mondayStartTime;
    }

    public void setMondayStartTime(String mondayStartTime) {
        this.mondayStartTime = mondayStartTime;
    }

    public String getMondayEndTime() {
        return mondayEndTime;
    }

    public void setMondayEndTime(String mondayEndTime) {
        this.mondayEndTime = mondayEndTime;
    }

    public String getTuesdayStartTime() {
        return tuesdayStartTime;
    }

    public void setTuesdayStartTime(String tuesdayStartTime) {
        this.tuesdayStartTime = tuesdayStartTime;
    }

    public String getTuesdayEndTime() {
        return tuesdayEndTime;
    }

    public void setTuesdayEndTime(String tuesdayEndTime) {
        this.tuesdayEndTime = tuesdayEndTime;
    }

    public String getWedStartTime() {
        return wedStartTime;
    }

    public void setWedStartTime(String wedStartTime) {
        this.wedStartTime = wedStartTime;
    }

    public String getWedEndTime() {
        return wedEndTime;
    }

    public void setWedEndTime(String wedEndTime) {
        this.wedEndTime = wedEndTime;
    }

    public String getThuStartTime() {
        return thuStartTime;
    }

    public void setThuStartTime(String thuStartTime) {
        this.thuStartTime = thuStartTime;
    }

    public String getThuEndTime() {
        return thuEndTime;
    }

    public void setThuEndTime(String thuEndTime) {
        this.thuEndTime = thuEndTime;
    }

    public String getFriStartTime() {
        return friStartTime;
    }

    public void setFriStartTime(String friStartTime) {
        this.friStartTime = friStartTime;
    }

    public String getFriEndTime() {
        return friEndTime;
    }

    public void setFriEndTime(String friEndTime) {
        this.friEndTime = friEndTime;
    }

    public String getSatStartTime() {
        return satStartTime;
    }

    public void setSatStartTime(String satStartTime) {
        this.satStartTime = satStartTime;
    }

    public String getSatEndTime() {
        return satEndTime;
    }

    public void setSatEndTime(String satEndTime) {
        this.satEndTime = satEndTime;
    }

    public String getSunStartTime() {
        return sunStartTime;
    }

    public void setSunStartTime(String sunStartTime) {
        this.sunStartTime = sunStartTime;
    }

    public String getSunEndTime() {
        return sunEndTime;
    }

    public void setSunEndTime(String sunEndTime) {
        this.sunEndTime = sunEndTime;
    }

    private String mondayStartTime, mondayEndTime, tuesdayStartTime, tuesdayEndTime, wedStartTime, wedEndTime, thuStartTime, thuEndTime, friStartTime,
            friEndTime, satStartTime, satEndTime, sunStartTime, sunEndTime;

    public Doctor() {
    }

    public Doctor(String doctorID, String username, String email, String phoneNumber, int experience, String qualification, String worksat, String institute,
                  boolean mondayAvailable, boolean tuesdayAvailable, boolean wednesdayAvailable, boolean thursdayAvailable, boolean fridayAvailable,
                  boolean saturdayAvailable, boolean sundayAvailable, int rating, int charges) {
        this.doctorID = doctorID;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.experience = experience;
        this.qualification = qualification;
        this.worksat = worksat;
        this.institute = institute;
        this.mondayAvailable = mondayAvailable;
        this.tuesdayAvailable = tuesdayAvailable;
        this.wednesdayAvailable = wednesdayAvailable;
        this.thursdayAvailable = thursdayAvailable;
        this.fridayAvailable = fridayAvailable;
        this.saturdayAvailable = saturdayAvailable;
        this.sundayAvailable = sundayAvailable;
        this.rating = rating;
        this.charges = charges;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }


    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }
}
