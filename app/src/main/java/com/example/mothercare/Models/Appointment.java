package com.example.mothercare.Models;

public class Appointment {
    private String appointmentID, title, date, time, status, doctorID, patientID, appointmentType;
    private int notificationID;

    public Appointment() {
    }

    public Appointment(String appointmentID, String Title, String date, String time, String appointmentType, int notificationID, String status, String doctorID, String patientID) {
        this.title = Title;
        this.date = date;
        this.time = time;
        this.appointmentType = appointmentType;
        this.appointmentID = appointmentID;
        this.notificationID = notificationID;
        this.status = status;
        this.doctorID = doctorID;
        this.patientID = patientID;
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

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
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

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }
}
