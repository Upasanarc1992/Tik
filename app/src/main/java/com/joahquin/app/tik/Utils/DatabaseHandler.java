package com.joahquin.app.tik.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TAG = "Database Handler";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tik.db";
    // Table Names
    private static final String TABLE_1 = "user";

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

}
