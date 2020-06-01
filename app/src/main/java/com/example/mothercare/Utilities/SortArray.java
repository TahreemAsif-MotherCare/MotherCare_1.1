package com.example.mothercare.Utilities;

import com.example.mothercare.Models.Chat;

import java.util.Date;

public class SortArray implements Comparable<Chat> {

    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }

    @Override
    public int compareTo(Chat o) {
        return 0;
    }
}
