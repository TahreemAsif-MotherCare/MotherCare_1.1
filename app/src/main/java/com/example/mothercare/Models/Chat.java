package com.example.mothercare.Models;

import java.util.Date;

public class Chat {
    public String userID, userName, chatMessage, discussionID;
    public Date date;

    public Chat() {
    }

    public Chat(String discussionID, String userID, String userName, String chatMessage, Date date) {
        this.userID = userID;
        this.userName = userName;
        this.chatMessage = chatMessage;
        this.discussionID = discussionID;
        this.date = date;
    }
}
