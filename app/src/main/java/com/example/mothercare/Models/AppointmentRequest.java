package com.example.mothercare.Models;

public class AppointmentRequest {
    public String patientID, doctorID, appointmentType, status;

    public AppointmentRequest() {
    }

    public AppointmentRequest(String patientID, String doctorID, String appointmentType, String status) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentType = appointmentType;
        this.status = status;
    }

}
