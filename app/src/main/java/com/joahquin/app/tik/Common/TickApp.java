package com.joahquin.app.tik.Common;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.evernote.android.job.JobManager;
import com.joahquin.app.tik.Utils.Services.DemoJobCreator;
import com.joahquin.app.tik.Utils.Templates;

public class TickApp extends Application {

    public static final String SERVICE_NOTIF_CHANNEL = "service_notif";
    public static final int SERVICE_NOTIF_ID = 999;

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new DemoJobCreator());

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            NotificationChannel notifChanel = new NotificationChannel(
                    SERVICE_NOTIF_CHANNEL,
                    SERVICE_NOTIF_CHANNEL,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notifChanel.setDescription(Templates.WATER_MONITOR.ASSIGNMENT_DESC);

            NotificationChannel notifChanel1 = new NotificationChannel(
                    Templates.WATER_MONITOR.NOTIF_CHANNEL,
                    Templates.WATER_MONITOR.NOTIF_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notifChanel1.setDescription(Templates.WATER_MONITOR.ASSIGNMENT_DESC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notifChanel);
            manager.createNotificationChannel(notifChanel1);
        }
    }
}
