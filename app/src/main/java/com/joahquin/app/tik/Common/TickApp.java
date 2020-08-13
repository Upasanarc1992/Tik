package com.joahquin.app.tik.Common;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.evernote.android.job.JobManager;
import com.joahquin.app.tik.Utils.Services.DemoJobCreator;
import com.joahquin.app.tik.Utils.Templates;

public class TickApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new DemoJobCreator());

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notifChanel1 = new NotificationChannel(
                    Templates.WATER_MONITOR.NOTIF_CHANNEL,
                    Templates.WATER_MONITOR.NOTIF_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notifChanel1.setDescription(Templates.WATER_MONITOR.ASSIGNMENT_DESC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notifChanel1);
        }
    }
}
