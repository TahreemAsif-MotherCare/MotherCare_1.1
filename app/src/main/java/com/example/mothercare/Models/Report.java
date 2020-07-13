package com.example.mothercare.Models;

import android.graphics.Bitmap;

public class Report {
    public String reportID, doctorID, patientID, description;
    public Bitmap reportPicture;
    public String date;

    public Report() {
    }

    public Report(String reportID, String doctorID, String patientID, String description, String date) {
        this.reportID = reportID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.description = description;
        this.date = date;
    }

    public Bitmap getReportPicture() {
        return reportPicture;
    }

    public void setReportPicture(Bitmap reportPicture) {
        this.reportPicture = reportPicture;
    }
}
