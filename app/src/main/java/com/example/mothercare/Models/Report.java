package com.example.mothercare.Models;

import android.graphics.Bitmap;

public class Report {
    public String reportID, doctorID, patientID, description;
    public Bitmap reportPicture;

    public Report() {
    }

    public Report(String reportID, String doctorID, String patientID, String description) {
        this.reportID = reportID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.description = description;
    }

    public Bitmap getReportPicture() {
        return reportPicture;
    }

    public void setReportPicture(Bitmap reportPicture) {
        this.reportPicture = reportPicture;
    }
}
