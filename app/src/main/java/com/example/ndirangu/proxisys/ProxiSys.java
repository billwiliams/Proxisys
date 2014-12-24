package com.example.ndirangu.proxisys;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

/**
 * Created by NDIRANGU on 12/23/2014.
 * A project utilizing Estimote Beacons and BLE enabled phone to display information based on how close one is to an item.
 * Ensures that BeaconManager is on even if the screen is closed
 */
public class ProxiSys extends Application {
    private static final int NOTIFICATION_ID = 123;
    private NotificationManager notificationManager;
    private BeaconManager beaconManager;
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);


    @Override
    public void onCreate()
    {
        super.onCreate();

    }

    //POSTING NOTIFICATION
    private void postNotification(String msg) {
        Intent notifyIntent = new Intent(ProxiSys.this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                ProxiSys.this,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(ProxiSys.this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("My Shopping Mate")
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(NOTIFICATION_ID, notification);


    }

}
