package com.joahquin.app.tik.Utils.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ShowNotificationJob extends Job {

    static final String TAG = "ShowNotificationJob";
    static ScheduleItem scheduleItem;
    static DatabaseHandler db;

    private static NotificationManagerCompat notificationManager;

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

//        String description = "";
//        String header = "";
//        String notifChaannel = "";
//        int notifId = 1;
//
//        if(scheduleItem.getAssignmentType() == Templates.WATER_MONITOR.ASSIGNMENT_ID) {
//            notifChaannel = Templates.WATER_MONITOR.NOTIF_CHANNEL;
//            header = Templates.WATER_MONITOR.NOTIF_NAME;
//            description = Templates.WATER_MONITOR.ASSIGNMENT_DESC;
//            notifId = Templates.WATER_MONITOR.ASSIGNMENT_ID;
//        }
//
//        Notification notification = new NotificationCompat.Builder(getContext(), notifChaannel)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(header)
//                .setContentText(description)
//                .setPriority(NotificationManager.IMPORTANCE_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_ALARM)
//                .build();
//
//        notificationManager.notify(notifId, notification);

        Log.e(TAG, "onRunJob: HERE RUN JOB");
        db.markSchedule(scheduleItem.getId(), true, true);

        return Result.SUCCESS;
    }

    public static void schedulePeriodic(Context context) {

        notificationManager = NotificationManagerCompat.from(context);

        db = new DatabaseHandler(context);
        scheduleItem = db.getNextSchedule();
        Calendar cal = Calendar.getInstance();
        long timeToNextAlarm = scheduleItem.getTimeStamp() - cal.getTimeInMillis();

        new JobRequest.Builder(ShowNotificationJob.TAG)
                .setExact(timeToNextAlarm)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}