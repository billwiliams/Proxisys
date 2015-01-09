package com.example.ndirangu.proxisys;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.parse.Parse;
import com.parse.ParseCrashReporting;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by NDIRANGU on 12/23/2014.
 * A project utilizing Estimote Beacons and BLE enabled phone to display information based on how close one is to an item.
 * Ensures that BeaconManager is on even if the screen is closed
 */
public class ProxiSys extends Application {
    private static final int NOTIFICATION_ID = 123;
    private NotificationManager notificationManager;
    private BeaconManager beaconManager;
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);


    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Crash Reporting
        ParseCrashReporting.enable(this);

        //parse initialization
        Parse.initialize(this, "Z3dh3hNprxhtb2lSsa8IzjW12b2qKbhks0Y2wRRD", "WALJUx6cYTKK8uBPt5uS3uhC5iOYJ1QOXZQMerTU");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.e("error", "Cannot start ranging", e);
                }
            }
        });
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        beaconManager = new BeaconManager(this);
        /*
        *Default values are 5s of scanning and 25s of waiting time to save CPU cycles.
        *In order for this project to be more responsive and immediate we lower down those values.
        *
        * */
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 2);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                                                @Override
                                                public void onEnteredRegion(Region ALL_ESTIMOTE_BEACONS, final List<Beacon> beacons) {

                                                    postNotification("Welcome to Proxisys");


                                                }

                                                @Override
                                                public void onExitedRegion(Region region) {

                                                    postNotification(" Thanks for choosing Proxisys with us ");
                                                }
                                            }

        );

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