package com.example.mothercare.Utilities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Random;

public class RegenerateMedicationNotification extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "We'll remind you again in 10 minutes", Toast.LENGTH_LONG).show();
        int notificationID = intent.getIntExtra("notificationID", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
        Intent stopIntent = new Intent(context, RingtonePlayingService.class);
        context.stopService(stopIntent);

        startAlarm(context, notificationID);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startAlarm(Context context, int notificationID) {
        Random random = new Random();
        Intent intent = new Intent(context, MedicationNotificationReciever.class);
        intent.putExtra("notificationChannelID", intent.getStringExtra("ChannelID"));
        intent.putExtra("notificationID", notificationID);
        intent.putExtra("Medication Name", intent.getStringExtra("medicationName"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, 0);
        long futureInMillis = SystemClock.elapsedRealtime() + 600000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
