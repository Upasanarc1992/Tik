package com.joahquin.app.tik.Utils.CustomViews;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.ViewCompat;

import com.joahquin.app.tik.R;
import com.joahquin.app.tik.Utils.BasicUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeEditText extends AppCompatEditText implements View.OnClickListener {

    Context context;
    BasicUtils basicUtils;

    int mode;
    String dateFormat;
    String timeFormat;
    String dateTimeFormat;
    Date date;
    Calendar calendar;
    long maxDateMillis;
    long minDateMillis;
    BufferType bufferType;

    private DateChangedListener listener;

    public static final int MODE_DATE_PICK = 0;
    public static final int MODE_TIME_PICK = 1;
    public static final int MODE_DATE_TIME_PICK = 2;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIME_FORMAT = "hh:mm:ss a";

    public DateTimeEditText(Context context) {
        super(context);
        this.context = context;
        basicUtils = new BasicUtils(context);
    }

    public DateTimeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        basicUtils = new BasicUtils(context);
        getAttributes(context, attrs, 0);
    }

    public DateTimeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        basicUtils = new BasicUtils(context);
        getAttributes(context, attrs, defStyleAttr);
    }

    private void getAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimeEditText, defStyleAttr, 0);
        if (a != null) {
            mode = a.getInt(R.styleable.DateTimeEditText_mode, MODE_DATE_PICK);

            dateFormat = a.getString(R.styleable.DateTimeEditText_dateFormat);
            if (BasicUtils.isStringNull(dateFormat)) {
                dateFormat = DATE_FORMAT;
            }
            timeFormat = a.getString(R.styleable.DateTimeEditText_timeFormat);
            if (BasicUtils.isStringNull(timeFormat)) {
                timeFormat = TIME_FORMAT;
            }
            dateTimeFormat = dateFormat + " " + timeFormat;

            String dateStr = a.getString(R.styleable.DateTimeEditText_date);
            if (BasicUtils.isStringNull(dateStr)) {
                /*String s = String.valueOf(Calendar.getInstance().getTimeInMillis());
                date = getDateFromStringMillis(s);*/
                date = null;
            } else {
                date = getDateFromStringMillis(dateStr);
            }

            String maxDateStr = a.getString(R.styleable.DateTimeEditText_maxDate);
            if (!(BasicUtils.isStringNull(maxDateStr))) {
                maxDateMillis = Long.parseLong(maxDateStr);
            } else {
                maxDateMillis = -1;
            }

            String minDateStr = a.getString(R.styleable.DateTimeEditText_minDate);
            if (!(BasicUtils.isStringNull(minDateStr))) {
                minDateMillis = Long.parseLong(minDateStr);
            } else {
                minDateMillis = -1;
            }
        }

        setOnClickListener(this);
        setFocusableInTouchMode(false);

        setText(date);
        a.recycle();
    }

    private Date getDateFromStringMillis(String s) {
        Long dateL = Long.parseLong(s);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateL);

        switch (mode) {
            case MODE_DATE_PICK:
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;

            case MODE_TIME_PICK:
                break;

            case MODE_DATE_TIME_PICK:
                break;
        }

        date = calendar.getTime();
        return date;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.bufferType = type;
        super.setText(text, type);
    }

    public void setText(Date d) {
        this.date = d;
        setText(getDisplayableDate(d), bufferType);
    }

    public String getDisplayableDate(Date d) {

        if (d == null) {
            calendar = null;
            return "";
        }

        calendar = Calendar.getInstance();
        calendar.setTime(d);

        String s;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        switch (mode) {
            case MODE_DATE_PICK:
                sdf = new SimpleDateFormat(dateFormat);
                break;

            case MODE_TIME_PICK:
                sdf = new SimpleDateFormat(timeFormat);
                break;

            case MODE_DATE_TIME_PICK:
                sdf = new SimpleDateFormat(dateTimeFormat);
                break;
        }
        s = basicUtils.getFormattedTimeZoneDateString(sdf, d);
        return s;
    }

    public void setDate(Date date) {
        this.date = date;
        setText(date);
        if(listener!=null)
        {
            listener.onDateChange();
            listener.onTimeChange();
        }
        invalidate();
        requestLayout();
    }

    public void setDateOnly(Date newDate){
        if(calendar == null) {
            this.date = null;
            return;
        }
        Calendar nCal = Calendar.getInstance();
        if(newDate != null)
            nCal.setTime(newDate);
        calendar.set(Calendar.DAY_OF_MONTH, nCal.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.MONTH, nCal.get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, nCal.get(Calendar.YEAR));
        this.date = calendar.getTime();
    }

    public void setTimeOnly(Date newDate){
        if(calendar == null) {
            this.date = null;
            return;
        }
        if(newDate != null) {
            Calendar nCal = Calendar.getInstance();
            nCal.setTime(newDate);

            calendar.set(Calendar.HOUR_OF_DAY, nCal.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, nCal.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, nCal.get(Calendar.SECOND));
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        this.date = calendar.getTime();
    }

    public Date getDate() {
        return date;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        invalidate();
        requestLayout();
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        invalidate();
        requestLayout();
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        invalidate();
        requestLayout();
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
        invalidate();
        requestLayout();
    }

    public long getMaxDateMillis() {
        return maxDateMillis;
    }

    public void setMaxDateMillis(long maxDateMillis) {
        this.maxDateMillis = maxDateMillis;
        invalidate();
        requestLayout();
    }

    public long getMinDateMillis() {
        return minDateMillis;
    }

    public void setMinDateMillis(long minDateMillis) {
        this.minDateMillis = minDateMillis;
        invalidate();
        requestLayout();
    }

    public void setTint(int color){
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        ViewCompat.setBackgroundTintList(this, colorStateList);
    }

    public void setDefault(){
        this.setPadding(0,0,0,0);
    }

    public Calendar getCalender(){
        if(date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public String getDisplayDateString(){
        return getDisplayableDate(date);
    }

    @Override
    public void onClick(View v) {

        switch (mode) {
            case MODE_DATE_PICK:
                showDatePickerDialog();
                break;

            case MODE_TIME_PICK:
                showTimePickerDialog();
                break;

            case MODE_DATE_TIME_PICK:
                showDatePickerDialog();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (mode == MODE_DATE_PICK) {
                date = calendar.getTime();
                setText(date);
            } else if (mode == MODE_DATE_TIME_PICK) {
                showTimePickerDialog();
            }
            if(listener!=null)
                listener.onDateChange();
        }
    };

    public void showDatePickerDialog() {

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        DatePickerDialog dDialog = new DatePickerDialog(context,
                R.style.MyDatePickerDialogTheme, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        if (minDateMillis != -1) {
            dDialog.getDatePicker().setMinDate(minDateMillis);
        }
        if (maxDateMillis != -1) {
            dDialog.getDatePicker().setMaxDate(maxDateMillis);
        }
        dDialog.show();
    }

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            date = calendar.getTime();
            setText(date);

            if(listener!=null){
                listener.onTimeChange();
            }

        }
    };

    public void showTimePickerDialog() {

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        TimePickerDialog dDialog = new TimePickerDialog(context,
                R.style.MyDatePickerDialogTheme, timeSetListener,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                false);
        dDialog.show();
    }

    public DateChangedListener getDateChangedListener() {
        return listener;
    }

    public void setDateChangedListener(DateChangedListener listener) {
        this.listener = listener;
    }

    public interface DateChangedListener {
        void onDateChange();
        void onTimeChange();
    }

}
