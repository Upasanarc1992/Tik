package com.joahquin.app.tik.Utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BasicUtils {

    Context context;

    DatabaseHandler db;

    public BasicUtils(Context context) {
        this.context = context;
        this.db = DatabaseHandler.getInstance(context);
    }

    public void showSnackAlert(CoordinatorLayout coordinatorLayout, String msg) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT);

        View sbView = snackbar.getView();
        sbView.bringToFront();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static boolean isStringNull(String s) {
        if (s == null || s.isEmpty()
                || s.equalsIgnoreCase("") || s.equalsIgnoreCase("null"))
            return true;
        return false;
    }

    public final String getFormattedTimeZoneDateString(SimpleDateFormat sdf, Date date) {
        if(date!=null) {
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(date);
        }
        else
            return "";
    }

    public int getDpsInPixels(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, context.getResources().getDisplayMetrics());
    }


}
