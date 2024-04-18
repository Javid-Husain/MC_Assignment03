package com.example.assignment03;

import android.content.ContentValues;
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

    // Method to insert orientation data into the database
    public long insertOrientationData(long timestamp, float pitch, float roll, float yaw) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_PITCH, pitch);
        values.put(COLUMN_ROLL, roll);
        values.put(COLUMN_YAW, yaw);

        // Insert the values into the database
        long newRowId = db.insert(TABLE_ORIENTATION, null, values);

        // Close the database connection
        db.close();

        return newRowId; // Return the row ID of the newly inserted row
    }
}
