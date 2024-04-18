package com.example.assignment03;

// DatabaseHelper.java

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "orientation_data.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ORIENTATION = "orientation";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PITCH = "pitch";
    public static final String COLUMN_ROLL = "roll";
    public static final String COLUMN_YAW = "yaw";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ORIENTATION + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TIMESTAMP + " INTEGER, " +
                    COLUMN_PITCH + " REAL, " +
                    COLUMN_ROLL + " REAL, " +
                    COLUMN_YAW + " REAL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORIENTATION);
        onCreate(db);
    }
}
