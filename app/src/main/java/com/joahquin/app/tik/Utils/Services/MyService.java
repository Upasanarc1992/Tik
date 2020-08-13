package com.joahquin.app.tik.Utils.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.joahquin.app.tik.Common.MainActivity;
import com.joahquin.app.tik.Common.TickApp;
import com.joahquin.app.tik.Items.ScheduleItem;
import com.joahquin.app.tik.R;
import com.joahquin.app.tik.Utils.DatabaseHandler;
import com.joahquin.app.tik.Utils.Templates;

import java.util.Calendar;

public class MyService extends Service {

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        if(intent.getAction() == ACTION_STOP_FOREGROUND_SERVICE){
            killService();
        }
        else {
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, TickApp.SERVICE_NOTIF_CHANNEL)
                    .setContentTitle("Test Service")
                    .setContentText("Test")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(TickApp.SERVICE_NOTIF_ID, notification);
            return START_REDELIVER_INTENT;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    public static void createJob(long timeToNextAlarm){
        new JobRequest.Builder(ShowNotificationJob.TAG)
                .setExact(timeToNextAlarm)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    public void killService(){
        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }
}
