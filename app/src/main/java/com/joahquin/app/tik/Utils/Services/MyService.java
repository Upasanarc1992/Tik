package com.joahquin.app.tik.Utils.Services;

import android.app.Notification;
import android.app.NotificationManager;
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
import com.joahquin.app.tik.Items.ScheduleItem;
import com.joahquin.app.tik.R;
import com.joahquin.app.tik.Utils.DatabaseHandler;
import com.joahquin.app.tik.Utils.Templates;

import java.util.Calendar;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
