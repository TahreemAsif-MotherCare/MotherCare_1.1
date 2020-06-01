package com.example.mothercare.Utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mothercare.R;


public class MedicationNotificationReciever extends BroadcastReceiver {
    private String CHANNEL_ID = null;
    private LocalBroadcastManager mLocalBroadcastManager;
    Notification notification;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 12, new Intent(), 0);

        Log.d("MNR", "onReceive: " + "REcieved");

        CHANNEL_ID = intent.getStringExtra("notificationChannelID");
        int notificationID = intent.getIntExtra("notificationID", 0);
        String medicationName = intent.getStringExtra("Medication Name");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Medication Notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        Notification.Builder builder = new Notification.Builder(context);

        Intent takenIntent = new Intent(context, StopService.class);
        takenIntent.putExtra("notificationID", notificationID);
        takenIntent.putExtra("medicationName", medicationName);
        takenIntent.putExtra("ChannelID", CHANNEL_ID);
        takenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent takenPendingIntent = PendingIntent.getBroadcast(context, 0, takenIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent dismissIntent = new Intent(context, RegenerateMedicationNotification.class);
        dismissIntent.putExtra("notificationID", notificationID);
        dismissIntent.putExtra("medicationName", medicationName);
        dismissIntent.putExtra("ChannelID", CHANNEL_ID);
        dismissIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_ONE_SHOT);

        notification = builder.setContentTitle("Mother Care")
                .setContentText("You added a medication schedule")
                .setTicker("Take your medicine now!")
                .setSmallIcon(R.drawable.logo)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.logo, "Taken", takenPendingIntent)
                .addAction(R.drawable.logo, "Dismiss", dismissPendingIntent)
                .setContentIntent(resultPendingIntent)
                .setOngoing(true).build();
        Intent startIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(startIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(notificationID, notification);
//        }
    }

}
