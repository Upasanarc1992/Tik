package com.joahquin.app.tik.Utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static Date convertStringToDate(String dateString, String dateFormat){
        Date formattedDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            formattedDate = sdf.parse(dateString);
        } catch (ParseException e) {
            formattedDate = Calendar.getInstance().getTime();
            e.printStackTrace();
        }


        return formattedDate;

    }

    public static Date convertStringToDate(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date formattedDate;
        try {
            formattedDate = sdf.parse(dateString);
        } catch (ParseException e) {
            formattedDate = Calendar.getInstance().getTime();
            e.printStackTrace();
        }
        return formattedDate;

    }

    public static String convertDateToString(Date date, String dateFormat){
        String formattedDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            formattedDate = sdf.format(date);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
            formattedDate = sdf.format(Calendar.getInstance().getTime());
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String convertDateToString(Date date){
        String formattedDate;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            formattedDate = sdf.format(date);
        } catch (Exception e) {
            formattedDate = sdf.format(Calendar.getInstance().getTime());
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Calendar convertStringToCal(String dateString, String dateFormat){

        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(dateString,dateFormat));
        return cal;
    }

    public static Calendar convertStringToCal(String dateString){

        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(dateString));
        return cal;
    }

    public static String convertCalToString(Calendar dateCal, String dateFormat){
        String formattedDate = "";
        formattedDate = convertDateToString(dateCal.getTime(), dateFormat);
        return formattedDate;
    }

    public static String convertCalToString(Calendar dateCal){
        String formattedDate = "";
        formattedDate = convertDateToString(dateCal.getTime());
        return formattedDate;
    }

    public static <T> boolean validateList(ArrayList<T> list){
        if(list == null)
            return false;
        if(list.size() == 0)
            return false;
        else
            return true;
    }




}
