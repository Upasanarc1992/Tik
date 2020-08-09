package com.joahquin.app.tik.Utils.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
        if (intent.getAction() == "FOO_ACTION") {
            String fooString = intent.getStringExtra("KEY_FOO_STRING");
            Toast.makeText(context, fooString, Toast.LENGTH_LONG).show();
        }
    }
}
