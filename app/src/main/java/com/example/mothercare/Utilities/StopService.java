package com.example.mothercare.Utilities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class StopService extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseUtil firebaseUtil = new FirebaseUtil();
        Toast.makeText(context, "Called", Toast.LENGTH_LONG).show();
        int notificationID = intent.getIntExtra("notificationID", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
        firebaseUtil.saveNotification(intent.getStringExtra("medicationName"), "Medication", firebaseUtil.getCurrentUserID());
        firebaseUtil.completMedicationSchedule(firebaseUtil.getCurrentUserID(), intent.getStringExtra("ChannelID"));
        Intent stopIntent = new Intent(context, RingtonePlayingService.class);
        context.stopService(stopIntent);
    }
}
