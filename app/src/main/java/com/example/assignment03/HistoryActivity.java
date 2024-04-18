package com.example.assignment03;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends Activity {

    private TextView textViewPitch, textViewRoll, textViewYaw;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        textViewPitch = findViewById(R.id.text_view_pitch_history);
        textViewRoll = findViewById(R.id.text_view_roll_history);
        textViewYaw = findViewById(R.id.text_view_yaw_history);

        dbHelper = new DatabaseHelper(this);
        displayData();
    }

    private void displayData() {
        List<String> pitchList = new ArrayList<>();
        List<String> rollList = new ArrayList<>();
        List<String> yawList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORIENTATION, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") long timestamp = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP));
                @SuppressLint("Range") float pitch = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_PITCH));
                @SuppressLint("Range") float roll = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROLL));
                @SuppressLint("Range") float yaw = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_YAW));

                pitchList.add("Timestamp: " + timestamp + ", Pitch: " + pitch);
                rollList.add("Timestamp: " + timestamp + ", Roll: " + roll);
                yawList.add("Timestamp: " + timestamp + ", Yaw: " + yaw);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Display pitch data
        StringBuilder pitchBuilder = new StringBuilder();
        for (String pitch : pitchList) {
            pitchBuilder.append(pitch).append("\n");
        }
        textViewPitch.setText(pitchBuilder.toString());

        // Display roll data
        StringBuilder rollBuilder = new StringBuilder();
        for (String roll : rollList) {
            rollBuilder.append(roll).append("\n");
        }
        textViewRoll.setText(rollBuilder.toString());

        // Display yaw data
        StringBuilder yawBuilder = new StringBuilder();
        for (String yaw : yawList) {
            yawBuilder.append(yaw).append("\n");
        }
        textViewYaw.setText(yawBuilder.toString());
    }
}
