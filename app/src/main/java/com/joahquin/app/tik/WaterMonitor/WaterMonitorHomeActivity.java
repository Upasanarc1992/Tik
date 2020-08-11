package com.joahquin.app.tik.WaterMonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.joahquin.app.tik.Common.ChipViewAdapter;
import com.joahquin.app.tik.Common.NotifyInterface;
import com.joahquin.app.tik.Items.AssignmentItem;
import com.joahquin.app.tik.Items.TaskItem;
import com.joahquin.app.tik.R;
import com.joahquin.app.tik.Utils.BasicUtils;
import com.joahquin.app.tik.Utils.CustomViews.DateTimeEditText;
import com.joahquin.app.tik.Utils.CustomViews.LabelView;
import com.joahquin.app.tik.Utils.DatabaseHandler;
import com.joahquin.app.tik.Utils.Templates;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WaterMonitorHomeActivity extends AppCompatActivity{

    Context context;
    DatabaseHandler db;
    BasicUtils basicUtils;

    CoordinatorLayout cL;
    Button bAddRoutine;

    Date startTime;
    Date endTime;

    double selectedInterval;
    ArrayList<Date> timeIntervalList;

    public static final String TAG = "WaterMonitorHome";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_monitor_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBackground));

        init();
        initView();
    }

    private void init() {
        context = this;
        db = new DatabaseHandler(context);
        basicUtils = new BasicUtils(context);

        initData();
    }

    private void initData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        startTime = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,22);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        endTime = cal.getTime();
    }

    private void initView() {
        cL = findViewById(R.id.cL);
        bAddRoutine = findViewById(R.id.bAddRoutine);

        bAddRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntervalSelectDialog();
            }
        });
    }

    public void showIntervalSelectDialog(){
        final BottomSheetDialog intervalDialog = new BottomSheetDialog(context);
        intervalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        intervalDialog.getWindow().setBackgroundDrawable
                (new ColorDrawable(Color.TRANSPARENT));
        intervalDialog.setContentView(R.layout.dialog_water_routine_interval);


        RecyclerView rvIntervals = intervalDialog.findViewById(R.id.rvIntervals);
        final LinearLayout llTiming = intervalDialog.findViewById(R.id.llTiming);
        final LabelView lvStart = intervalDialog.findViewById(R.id.lvStart);
        final DateTimeEditText dtStart = lvStart.getDTData();
        final LabelView lvEnd = intervalDialog.findViewById(R.id.lvEnd);
        final DateTimeEditText dtEnd = lvEnd.getDTData();
        final Button bPreviewRoutine = intervalDialog.findViewById(R.id.bPreviewRoutine);

        dtStart.setTimeFormat("hh:mm a");
        dtEnd.setTimeFormat("hh:mm a");

        dtStart.setDate(startTime);
        dtEnd.setDate(endTime);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        rvIntervals.setLayoutManager(layoutManager);

        ChipViewAdapter<KeyValueItem> chipViewAdapter = new ChipViewAdapter<>(context, getIntervals(), new NotifyInterface() {
            @Override
            public void notify(int pos) {
                selectedInterval = getIntervals().get(pos).getValue();
                llTiming.setVisibility(View.VISIBLE);
            }
        });

        bPreviewRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = dtStart.getDate();
                endTime = dtEnd.getDate();

                intervalDialog.dismiss();
                getDateWithIntervals();
                previewIntervalSelectedDialog();
            }
        });
        rvIntervals.setAdapter(chipViewAdapter);

        intervalDialog.show();
    }

    public void previewIntervalSelectedDialog(){
        final Dialog previewDialog = new Dialog(context, R.style.DialogAnimation);

        previewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(R.layout.dialog_water_routine_preview);

        previewDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        previewDialog.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBackground));
        previewDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        RecyclerView rvPreview = previewDialog.findViewById(R.id.rvPreview);
        Button bDone = previewDialog.findViewById(R.id.bDone);
        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvPreview.setLayoutManager(layoutManager);
        timeIntervalList = getDateWithIntervals();
        WaterMonitorIntervalAdapter waterMonitorIntervalAdapter = new WaterMonitorIntervalAdapter(
                context, timeIntervalList, "hh:mm a", new NotifyInterface() {
            @Override
            public void notify(int pos) {

            }
        });
        rvPreview.setAdapter(waterMonitorIntervalAdapter);


        previewDialog.show();
    }

    private void saveReminder() {
        ArrayList<TaskItem> taskList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        Calendar calInterval = Calendar.getInstance();
        int todayDate = cal.get(Calendar.DATE);
        for(Date dt : timeIntervalList){
            TaskItem item = new TaskItem();
            calInterval.setTimeInMillis(dt.getTime());
            calInterval.set(Calendar.DATE, todayDate);

            long timeInterval = calInterval.getTimeInMillis() - cal.getTimeInMillis();
            item.setTimeInterval(timeInterval);
            taskList.add(item);
        }

        AssignmentItem assignmentItem = new AssignmentItem();
        assignmentItem.setType(Templates.WATER_MONITOR.ASSIGNMENT_ID);
        assignmentItem.setDescription(Templates.WATER_MONITOR.ASSIGNMENT_DESC);
        assignmentItem.setReccursive(Templates.WATER_MONITOR.ASSIGNMENT_RECCURSIVE);
        assignmentItem.setInterval(Templates.WATER_MONITOR.ASSIGNMENT_INTERVAL);
        assignmentItem.setLastPass(basicUtils.convertCalToString(cal));
        assignmentItem.setTaskList(taskList);

        db.replaceAssignment(assignmentItem);
    }


    public ArrayList<KeyValueItem> getIntervals(){
        ArrayList<KeyValueItem> intervals = new ArrayList<>();

        intervals.add(new KeyValueItem("30 min",0.5));
        intervals.add(new KeyValueItem("every hour",1));
        intervals.add(new KeyValueItem("2 hours",2));
        intervals.add(new KeyValueItem("3 hours",3));
        intervals.add(new KeyValueItem("4 hours",4));
        intervals.add(new KeyValueItem("6 hours",6));
        intervals.add(new KeyValueItem("12 hours",12));

        return intervals;
    }

    public ArrayList<Date> getDateWithIntervals(){
        ArrayList<Date> dateList = new ArrayList<>();

        Date tmpTime = startTime;
        Calendar cal = Calendar.getInstance();
        cal.setTime(tmpTime);

        while(tmpTime.before(endTime))
        {
            cal.add(Calendar.MINUTE,(int)(selectedInterval*60));
            tmpTime = cal.getTime();
            dateList.add(tmpTime);
        }

        Log.d(TAG, "getDateWithIntervals: "+ dateList.toString());
        return dateList;
    }

}