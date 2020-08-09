package com.joahquin.app.tik.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


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
            "email" + " TEXT," +
            "role" + " TEXT," +
            "company" + " INTEGER," +
            "type" + " TEXT," +
            "com_name" + " TEXT," +
            "logo" + " TEXT" + ")";

    // Assignment table create statement
    private static final String CREATE_TABLE_ASSIGNMENTS = "CREATE TABLE " + TABLE_2 +
            "(" + "id" + " INTEGER," +
            "type" + " INTEGER," +
            "description" + " TEXT," +
            "isReccursive" + " INTEGER," +
            "interval" + " INTEGER," +
            "createdOn" + " TEXT," +
            "lastPass" + " TEXT" +
            ")";

    // Task table create statement
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_3 +
            "(" + "id" + " INTEGER," +
            "assignment_id" + " INTEGER," +
            "scheduleDateTime" + " TEXT," +
            "isPending" + " INTEGER," +
            "isActive" + " INTEGER" +
            ")";

    // Steps table create statement
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + TABLE_4 +
            "(" + "id" + " INTEGER," +
            "task_id" + " INTEGER," +
            "info" + " TEXT," +
            "interval" + " INTEGER," +
            "actionToTake" + " INTEGER," +
            "isPending" + " INTEGER" +
            ")";

    // Schedule table create statement
    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE " + TABLE_5 +
            "(" + "id" + " INTEGER," +
            "task_id" + " INTEGER," +
            "step_id" + " INTEGER," +
            "alarmInfo" + " TEXT," +
            "isuccess" + " INTEGER" +
            ")";

}
