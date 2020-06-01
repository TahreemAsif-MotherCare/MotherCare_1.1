package com.example.mothercare.Utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mothercare.R;
import com.example.mothercare.Views.Activities.ViewAppointmentRequests;
import com.example.mothercare.Views.Activities.AppointmentActivity;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class AppointmentNotificationReciever extends BroadcastReceiver {
    private String CHANNEL_ID = null;
    private FirebaseUtil firebaseUtil;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        firebaseUtil = new FirebaseUtil(context);
        Intent notificationIntent = new Intent(context, AppointmentActivity.class);
        CHANNEL_ID = intent.getExtras().getString("notificationChannelID");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ViewAppointmentRequests.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        firebaseUtil.completeAppointment(CHANNEL_ID);
        firebaseUtil.saveNotification("You had an appointment.", "Appointment", firebaseUtil.getCurrentUserID());
        Notification notification = builder.setContentTitle("Mother Care")
                .setContentText("You had added a Appointment")
                .setTicker("View more!")
                .setSmallIcon(R.drawable.logo)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Appointment Notification",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);
    }
}
