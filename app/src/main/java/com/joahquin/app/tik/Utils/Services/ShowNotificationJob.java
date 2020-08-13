package com.joahquin.app.tik.Utils.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.joahquin.app.tik.Common.MainActivity;
import com.joahquin.app.tik.Items.ScheduleItem;
import com.joahquin.app.tik.R;
import com.joahquin.app.tik.Utils.DatabaseHandler;
import com.joahquin.app.tik.Utils.Templates;

import java.util.Calendar;

public class ShowNotificationJob extends Job {

    static final String TAG = "ShowNotificationJob";
    static ScheduleItem scheduleItem;
    static DatabaseHandler db;
    static Context context;

    static Intent serviceIntent;

    private static NotificationManagerCompat notificationManager;

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        String description = "";
        String header = "";
        String notifChaannel = "";
        int notifId = 1;

        if(scheduleItem.getAssignmentType() == Templates.WATER_MONITOR.ASSIGNMENT_ID) {
            notifChaannel = Templates.WATER_MONITOR.NOTIF_CHANNEL;
            header = Templates.WATER_MONITOR.NOTIF_NAME;
            description = Templates.WATER_MONITOR.ASSIGNMENT_DESC;
            notifId = Templates.WATER_MONITOR.ASSIGNMENT_ID;
        }

        Notification notification = new NotificationCompat.Builder(getContext(), notifChaannel)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(header)
                .setContentText(description)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        notificationManager.notify(notifId, notification);

        Log.e(TAG, "onRunJob: HERE RUN JOB");
        db.markSchedule(scheduleItem.getId(), true, true);

        return Result.SUCCESS;
    }

    public static void schedulePeriodic(ScheduleItem scheduleItemm) {

        scheduleItem = scheduleItemm;
        Calendar cal = Calendar.getInstance();
        long timeToNextAlarm = scheduleItem.getTimeStamp() - cal.getTimeInMillis();
        MyService.createJob(timeToNextAlarm);
    }

    public static void schedulePeriodic(Context c) {
        context = c;
        serviceIntent = new Intent(c, MyService.class);
        notificationManager = NotificationManagerCompat.from(c);

        db = new DatabaseHandler(c);
        scheduleItem = db.getNextSchedule();
        Calendar cal = Calendar.getInstance();
        long timeToNextAlarm = scheduleItem.getTimeStamp() - cal.getTimeInMillis();


        Intent serviceIntent = new Intent(context, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        }
        else{
            context.startService(serviceIntent);
        }

        MyService.createJob(timeToNextAlarm);

    }
}