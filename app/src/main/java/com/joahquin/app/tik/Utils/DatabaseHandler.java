package com.joahquin.app.tik.Utils;

import android.bluetooth.BluetoothGattDescriptor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.evernote.android.job.JobRequest;
import com.joahquin.app.tik.Items.AssignmentItem;
import com.joahquin.app.tik.Items.ScheduleItem;
import com.joahquin.app.tik.Items.StepItem;
import com.joahquin.app.tik.Items.TaskItem;
import com.joahquin.app.tik.Utils.Services.ShowNotificationJob;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TAG = "Database Handler";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tik.db";
    // Table Names //
    private static final String TABLE_1 = "user";
    private static final String TABLE_2 = "Assignment";
    private static final String TABLE_3 = "Tasks";
    private static final String TABLE_4 = "Steps";
    private static final String TABLE_5 = "Schedule";

    public static DatabaseHandler mInstance;
    SQLiteDatabase dbWritable, dbReadable;
    Context context;

    public static DatabaseHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(context.getApplicationContext());
        }
        mInstance.context = context;
        return mInstance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteDatabase getWritable() {
        if (dbWritable == null) {
            dbWritable = this.getWritableDatabase();
        }
        return dbWritable;
    }

    public SQLiteDatabase getReadable() {
        if (dbReadable == null) {
            dbReadable = this.getReadableDatabase();
        }
        return dbReadable;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // creating required tables
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_ASSIGNMENTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_TASKS);
        sqLiteDatabase.execSQL(CREATE_TABLE_STEPS);
        sqLiteDatabase.execSQL(CREATE_TABLE_SCHEDULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        int version = oldVersion;
        switch (version) {
            case 1:
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
                version = 2;
        }

        if (version != DATABASE_VERSION) {
            // on upgrade drop older tables
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
            // create new tables
            onCreate(sqLiteDatabase);
        }
    }


    // Table Create Statements
    //==============================================================================================
    // ProfileItem table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_1 +
            "(" + "id" + " INTEGER," +
            "authtoken" + " TEXT," +
            "name" + " TEXT," +
            "mobile" + " TEXT," +
            "email" + " TEXT" +
            ")";

    // Assignment table create statement
    private static final String CREATE_TABLE_ASSIGNMENTS = "CREATE TABLE " + TABLE_2 +
            "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "type" + " INTEGER," +
            "description" + " TEXT," +
            "isReccursive" + " INTEGER," +
            "interval" + " TEXT," +
            "createdOn" + " TEXT," +
            "lastPass" + " TEXT" + // Date with 00:00 time
            ")";

    // Task table create statement
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_3 +
            "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "assignment_id" + " INTEGER," +
            "interval" + " TEXT," + // Time gap from lastPass date
            "isPending" + " INTEGER" +
            ")";

    // Steps table create statement
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + TABLE_4 +
            "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "task_id" + " INTEGER," +
            "info" + " TEXT," +
            "interval" + " INTEGER," +
            "actionToTake" + " INTEGER," +
            "isPending" + " INTEGER" +
            ")";

    // Schedule table create statement
    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE " + TABLE_5 +
            "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "assignment_type" + " INTEGER," +
            "assignment_id" + " INTEGER," +
            "task_id" + " INTEGER," +
            "step_id" + " INTEGER," +
            "alarmInfo" + " TEXT," +
            "alarmStepInfo" + " TEXT," +
            "alarmTimeStamp" + " TEXT," +
            "alarmTimeMills" + " INTEGER," +
            "isSuccess" + " INTEGER" +
            ")";

    // ============ Insert Methods =================================================================

    public void replaceAssignment(AssignmentItem assignment) {
        ArrayList<AssignmentItem> oldAssignmnetList = getAssignmentOnlybyType(assignment.getType());

        SQLiteDatabase db = getWritable();
        db.delete(TABLE_2, "type" + "=" + assignment.getType(), null);

        for(AssignmentItem aI : oldAssignmnetList) {
            String where = " assignment_id = '" + aI.getId() + "'";
            db.delete(TABLE_5, where, null);
            db.delete(TABLE_3, where, null);
        }

        ContentValues values = new ContentValues();
        values.put("type", assignment.getType());
        values.put("description", assignment.getDescription());
        values.put("isReccursive", assignment.isReccursive() ? 1 : 0);
        values.put("interval", assignment.getInterval());
        values.put("lastPass", assignment.getLastPass());
        values.put("createdOn", " DATETIME('NOW')");

        int id = (int) db.insert(TABLE_2, null, values);
        assignment.setId(id);

        replaceTask(assignment.getId(), assignment.getTaskList());
        addScheuledTasks(assignment);
        initializeAssignment(assignment);
    }

    public void updateAssignment(int assignmentId) {
        AssignmentItem assignment = getAssignment(assignmentId);
        Date lastPassDate = BasicUtils.convertStringToDate(assignment.getLastPass());
        Date updatedLastPassDate = new Date(lastPassDate.getTime() + assignment.getInterval());
        assignment.setLastPass(BasicUtils.convertDateToString(updatedLastPassDate));

        SQLiteDatabase db = getWritable();
        String where = " id = '" + assignment.getId() + "'";

        ContentValues values = new ContentValues();
        values.put("lastPass", assignment.getLastPass());
        db.update(TABLE_2, values, where, null);

        reInitializeAssignment(assignment.getId());
    }

    public void reInitializeAssignment(int assignmentId) {
        SQLiteDatabase db = getWritable();
        ArrayList<TaskItem> taskList = getTaskList(assignmentId);

        for (TaskItem tI : taskList) {
            markTask(tI.getId(), false, false);
            if (BasicUtils.validateList(tI.getStepList())) {
                for (StepItem sI : tI.getStepList())
                    markStep(sI.getId(), false, false);
            }
        }
    }

    public void replaceTask(int assignmentId, ArrayList<TaskItem> taskList) {
        SQLiteDatabase db = getWritable();
        for (TaskItem tI : taskList) {
            ContentValues values = new ContentValues();
            values.put("assignment_id", assignmentId);
            values.put("isPending", 1);
            values.put("interval", String.valueOf(tI.getTimeInterval()));

            int id = (int) db.insert(TABLE_3, null, values);
            tI.setId(id);
            replaceSteps(tI.getId(), tI.getStepList());
        }
    }

    public void replaceSteps(int taskId, ArrayList<StepItem> stepsList) {
        SQLiteDatabase db = getWritable();
        if (BasicUtils.validateList(stepsList)) {
            db.delete(TABLE_4, "task_id" + "=" + taskId, null);
            for (StepItem sI : stepsList) {
                ContentValues values = new ContentValues();
                values.put("task_id", taskId);
                values.put("info", sI.getInfo());
                values.put("interval", sI.getInterval());
                values.put("actionToTake", sI.getActionToTake());
                values.put("isPending", 1);

                db.insert(TABLE_4, null, values);
            }
        }
    }

    public void addScheuledTasks(AssignmentItem assignmnentItem) {
        SQLiteDatabase db = getWritable();

        Calendar cal = BasicUtils.convertStringToCal(assignmnentItem.getLastPass());

        for (TaskItem tI : assignmnentItem.getTaskList()) {
            if (BasicUtils.validateList(tI.getStepList())) {
                for (StepItem sI : tI.getStepList()) {
                    ContentValues values = new ContentValues();
                    values.put("assignment_type", assignmnentItem.getType());
                    values.put("assignment_id", assignmnentItem.getId());
                    values.put("task_id", tI.getId());
                    values.put("step_id", sI.getId());
                    values.put("alarmInfo", assignmnentItem.getDescription());
                    values.put("alarmStepInfo", sI.getInfo());

                    Date alarmDate = new Date(cal.getTimeInMillis() + tI.getTimeInterval() + sI.getInterval());
                    values.put("alarmTimeStamp", BasicUtils.convertDateToString(alarmDate));
                    values.put("alarmTimeMills", alarmDate.getTime());
                    values.put("isSuccess", 0);

                    db.insert(TABLE_5, null, values);
                }
            } else {
                ContentValues values = new ContentValues();
                values.put("assignment_type", assignmnentItem.getType());
                values.put("assignment_id", assignmnentItem.getId());
                values.put("task_id", tI.getId());
                values.put("step_id", 0);
                values.put("alarmInfo", assignmnentItem.getDescription());
                values.put("alarmStepInfo", "");
                Date alarmDate = new Date(cal.getTimeInMillis() + tI.getTimeInterval());
                values.put("alarmTimeStamp", BasicUtils.convertDateToString(alarmDate));
                values.put("alarmTimeMills", alarmDate.getTime());
                values.put("isSuccess", 0);

                db.insert(TABLE_5, null, values);
            }
        }
    }

    // ============ Actions ========================================================================

    public void initializeAssignment(AssignmentItem assignmentItem) {
        for (TaskItem tI : assignmentItem.getTaskList()) {
            if (BasicUtils.validateList(tI.getStepList())) {
                for (StepItem sI : tI.getStepList()) {
                    if (isStepTimePassed(sI))
                        markStep(sI.getId(), true, true);
                }
            } else {
                if (isTaskTimePassed(tI)) {
                    markTask(tI.getId(), true, true);
                }
            }
        }
    }

    public boolean isTaskTimePassed(TaskItem taskItem) {
        SQLiteDatabase db = getReadable();
        Date stepDate = Calendar.getInstance().getTime();
        Date now = Calendar.getInstance().getTime();
        String selectQuery = "SELECT * FROM " + TABLE_5 + " where task_id = '" + taskItem.getId() + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if (!c.isClosed()) {
            stepDate = BasicUtils.convertStringToDate(
                    c.getString(c.getColumnIndex("alarmTimeStamp")));
        }

        if (stepDate.before(now))
            return false;
        else
            return true;

    }

    public boolean isStepTimePassed(StepItem stepItem) {
        SQLiteDatabase db = getReadable();
        Date stepDate = Calendar.getInstance().getTime();
        Date now = Calendar.getInstance().getTime();
        String selectQuery = "SELECT * FROM " + TABLE_5 + " where step_id = '" + stepItem.getId() + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if (!c.isClosed()) {
            stepDate = BasicUtils.convertStringToDate(
                    c.getString(c.getColumnIndex("alarmTimeStamp")));
        }

        if (stepDate.before(now))
            return false;
        else
            return true;
    }

    public void markTask(int taskId, boolean isDone, boolean moniter) {
        SQLiteDatabase db = getWritable();
        String where = " id = '" + taskId + "'";
        ContentValues values = new ContentValues();
        values.put("isPending", isDone ? 0 : 1);
        long a = db.update(TABLE_3, values, where, null);

        if (moniter && isDone) {
            moniterTask(taskId);
        }
    }

    public void markStep(int stepId, boolean isDone, boolean moniter) {
        SQLiteDatabase db = getWritable();
        String where = " id = '" + stepId + "'";
        ContentValues values = new ContentValues();
        values.put("isPending", isDone ? 0 : 1);
        long a = db.update(TABLE_4, values, where, null);

        if (moniter && isDone)
            moniterStep(stepId);
    }

    public void moniterStep(int stepId) {
        int taskId = getStep(stepId).getTaskId();
        int isPending = 1;
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT SUM(isPending) FROM " + TABLE_4 + " where task_id = '" + taskId + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if (!c.isClosed()) {
            isPending = c.getInt(0);
        }

        if (isPending == 0)
            markTask(taskId, true, true);
    }

    public void moniterTask(int taskId) {
        int assignmentId = getTask(taskId).getAssignment_id();

        int isPending = 1;
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT SUM(isPending) FROM " + TABLE_3 + " where assignment_id = '" + assignmentId + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if (!c.isClosed()) {
            isPending = c.getInt(0);
        }

        if (isPending == 0)
            updateAssignment(assignmentId);
    }

    public void markSchedule(int scheduleId, boolean isDone, boolean isUpdated) {
        SQLiteDatabase db = getWritable();;
        ScheduleItem scheduleItem = getScheduleInfo(scheduleId);
        String where = " id = '" + scheduleId + "'";
        db.delete(TABLE_5, where, null);

        if(scheduleItem.getStepId() > 0)
            markStep(scheduleItem.getStepId(), isDone, true);
        else
            markTask(scheduleItem.getTaskId(), isDone, true);

        if(isUpdated) {
            ScheduleItem nextSchedule = getNextSchedule();

        }
    }

    public void updateJob() {

        ScheduleItem scheduleItem = getNextSchedule();
        Calendar cal = Calendar.getInstance();
        long timeToNextAlarm = scheduleItem.getTimeStamp() - cal.getTimeInMillis();

        new JobRequest.Builder("Test")
                .setExact(timeToNextAlarm)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    // ============ Get Methods ====================================================================


    public AssignmentItem getAssignmentOnly(int assignmentId) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + TABLE_2 + " where id = '" + assignmentId + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        AssignmentItem item = new AssignmentItem();
        c.moveToFirst();
        if (!c.isClosed()) {
            item.setId(c.getInt(c.getColumnIndex("id")));
            item.setType(c.getInt(c.getColumnIndex("type")));
            item.setDescription(c.getString(c.getColumnIndex("description")));
            item.setReccursive(c.getInt(c.getColumnIndex("isReccursive")) == 1);
            item.setInterval(c.getLong(c.getColumnIndex("interval")));
            item.setCreatedOn(c.getString(c.getColumnIndex("createdOn")));
        }
        return item;
    }

    public ArrayList<AssignmentItem> getAssignmentOnlybyType(int assignmentType) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + TABLE_2 + " where type = '" + assignmentType + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<AssignmentItem> list = new ArrayList<>();

        c.moveToFirst();
        if (!c.isClosed()) {
            while(c.getPosition() < c.getCount()) {
                AssignmentItem item = new AssignmentItem();
                item.setId(c.getInt(c.getColumnIndex("id")));
                item.setType(c.getInt(c.getColumnIndex("type")));
                item.setDescription(c.getString(c.getColumnIndex("description")));
                item.setReccursive(c.getInt(c.getColumnIndex("isReccursive")) == 1);
                item.setInterval(c.getLong(c.getColumnIndex("interval")));
                item.setCreatedOn(c.getString(c.getColumnIndex("createdOn")));

                list.add(item);
                c.moveToNext();
            }
        }
        return list;
    }


    public AssignmentItem getAssignment(int assignmentId) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + TABLE_2 + " where id = '" + assignmentId + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        AssignmentItem item = new AssignmentItem();
        c.moveToFirst();
        if (!c.isClosed()) {
            item.setId(c.getInt(c.getColumnIndex("id")));
            item.setType(c.getInt(c.getColumnIndex("type")));
            item.setDescription(c.getString(c.getColumnIndex("description")));
            item.setReccursive(c.getInt(c.getColumnIndex("isReccursive")) == 1);
            item.setInterval(c.getLong(c.getColumnIndex("interval")));
            item.setCreatedOn(c.getString(c.getColumnIndex("createdOn")));

            item.setTaskList(getTaskList(assignmentId));
        }
        return item;
    }

    public ArrayList<TaskItem> getTaskList(int assignmentId) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + TABLE_3 + " where assignment_id = '" + assignmentId + "'";
        ArrayList<TaskItem> list = new ArrayList<>();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if (!c.isClosed()) {
            for (int i = 0; i < c.getCount(); i++) {
                TaskItem tI = new TaskItem();
                tI.setAssignment_id(c.getInt(c.getColumnIndex("assignment_id")));
                tI.setPending(c.getInt(c.getColumnIndex("isPending")) == 1);
                tI.setTimeInterval(c.getLong(c.getColumnIndex("interval")));
                tI.setStepList(getStepList(tI.getId()));
                list.add(tI);
                c.moveToNext();
            }
        }
        return list;
    }

    public TaskItem getTask(int taskId) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + TABLE_3 + " where id = " + taskId;
        TaskItem item = new TaskItem();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst() && !c.isClosed()) {
            item.setAssignment_id(c.getInt(c.getColumnIndex("assignment_id")));
            item.setPending(c.getInt(c.getColumnIndex("isPending")) == 1);
            item.setTimeInterval(c.getLong(c.getColumnIndex("interval")));
            item.setStepList(getStepList(taskId));
        }
        c.close();
        return item;
    }

    public ArrayList<StepItem> getStepList(int taskId) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + TABLE_4 + " where task_id = '" + taskId + "'";
        ArrayList<StepItem> list = new ArrayList<>();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if (!c.isClosed()) {
            for (int i = 0; i < c.getCount(); i++) {
                StepItem sI = new StepItem();
                sI.setId(c.getInt(c.getColumnIndex("task_id")));
                sI.setInfo(c.getString(c.getColumnIndex("info")));
                sI.setInterval(c.getLong(c.getColumnIndex("interval")));
                sI.setActionToTake(c.getInt(c.getColumnIndex("actionToTake")));
                sI.setPending(c.getInt(c.getColumnIndex("isPending")) == 1);
                list.add(sI);
                c.moveToNext();
            }
        }
        return list;

    }

    public StepItem getStep(int stepId) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + TABLE_4 + " where id = '" + stepId + "'";
        StepItem item = new StepItem();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if (!c.isClosed()) {
            item.setId(c.getInt(c.getColumnIndex("task_id")));
            item.setInfo(c.getString(c.getColumnIndex("info")));
            item.setInterval(c.getLong(c.getColumnIndex("interval")));
            item.setActionToTake(c.getInt(c.getColumnIndex("actionToTake")));
            item.setPending(c.getInt(c.getColumnIndex("isPending")) == 1);
        }
        return item;

    }

    public ScheduleItem getScheduleInfo(int scheduleId) {
        SQLiteDatabase db = getReadable();
        ScheduleItem item = new ScheduleItem();
        String selectQuery = "SELECT * FROM " + TABLE_5 + " where id = '" + scheduleId + "'";
        Calendar cal = Calendar.getInstance();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c!=null && !c.isClosed() && c.moveToFirst()) {
            item.setId(c.getInt(c.getColumnIndex("id")));
            item.setAssignmentType(c.getInt(c.getColumnIndex("assignment_type")));
            item.setTaskId(c.getInt(c.getColumnIndex("task_id")));
            item.setStepId(c.getInt(c.getColumnIndex("step_id")));
            item.setAlarmInfo(c.getString(c.getColumnIndex("alarmInfo")));
            item.setAlarmStepInfo(c.getString(c.getColumnIndex("alarmStepInfo")));
            item.setTimeStamp(c.getLong(c.getColumnIndex("alarmTimeMills")));
            item.setDone(c.getInt(c.getColumnIndex("isSuccess")) == 0);

        }
        return item;
    }

    public ScheduleItem getNextSchedule() {
        SQLiteDatabase db = getReadable();
        ScheduleItem item = new ScheduleItem();
        String selectQuery = "SELECT * FROM " + TABLE_5 + " where isSuccess = 0 ORDER BY alarmTimeMills ASC";
        Calendar cal = Calendar.getInstance();
        long alarmTimeStamp;
        Cursor c = db.rawQuery(selectQuery, null);

        c.moveToFirst();
        boolean flag = false;
        if (c!=null && c.getPosition() < c.getCount() && !c.isClosed()) {
            while (!flag) {
                flag = true;
                alarmTimeStamp = c.getLong(c.getColumnIndex("alarmTimeMills"));

                if (cal.getTimeInMillis() - alarmTimeStamp > 0) {
                    flag = false;
                    markSchedule(c.getInt(c.getColumnIndex("id")), true, false);

                    if(c.getPosition() < c.getCount() -1)
                        c.moveToNext();
                }
                else {
                    break;
                }
            }

            if (flag) {
                item.setId(c.getInt(c.getColumnIndex("id")));
                item.setAssignmentType(c.getInt(c.getColumnIndex("assignment_type")));
                item.setTaskId(c.getInt(c.getColumnIndex("task_id")));
                item.setStepId(c.getInt(c.getColumnIndex("step_id")));
                item.setAlarmInfo(c.getString(c.getColumnIndex("alarmInfo")));
                item.setAlarmStepInfo(c.getString(c.getColumnIndex("alarmStepInfo")));
                item.setTimeStamp(c.getLong(c.getColumnIndex("alarmTimeMills")));
                item.setDone(c.getInt(c.getColumnIndex("isSuccess")) == 0);
            }
        }
        c.close();
        return item;
    }

    // ============ LOGS ===========================================================================

    public void showTable(String tableName) {
        SQLiteDatabase db = getReadable();
        String selectQuery = "SELECT * FROM " + tableName ;
        StepItem item = new StepItem();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c!=null && !c.isClosed())
            Log.d(TAG, "showTable: SHOW TABLE " +tableName+ " : "+ DatabaseUtils.dumpCursorToString(c));
    }

}
