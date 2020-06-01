package com.example.mothercare.Models;

public class Request {
    public String patientID, doctorID;

    public Request() {
    }

    public Request(String patientID, String doctorID) {
        this.doctorID = doctorID;
        this.patientID = patientID;
    }
}
