package com.example.mothercare.Models;

public class PatientMedicalHistory {
    private String medicationID , title, date, time, status;
    private int notificationID;
    public PatientMedicalHistory() {
    }

    public PatientMedicalHistory(String MedicationID , String Title, String date , String time, int notificationID, String status) {
        this.title = Title;
        this.date = date;
        this.time = time;
        this.medicationID = MedicationID;
        this.notificationID = notificationID;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
