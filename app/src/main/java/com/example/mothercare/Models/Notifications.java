package com.example.mothercare.Models;

public class Notifications {
    private String notificationTitle, time;

    public Notifications() {
    }

    public Notifications(String Title, String time) {
        this.notificationTitle = Title;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }
}
