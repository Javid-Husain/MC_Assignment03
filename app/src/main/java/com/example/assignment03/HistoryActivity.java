package com.example.assignment03;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends Activity {

    private TextView textViewPitch, textViewRoll, textViewYaw;
    private DatabaseHelper dbHelper;
    private LineChart pitchChart, rollChart, yawChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        textViewPitch = findViewById(R.id.text_view_pitch_history);
        textViewRoll = findViewById(R.id.text_view_roll_history);
        textViewYaw = findViewById(R.id.text_view_yaw_history);
        pitchChart = findViewById(R.id.pitch_chart);
        rollChart = findViewById(R.id.roll_chart);
        yawChart = findViewById(R.id.yaw_chart);

        dbHelper = new DatabaseHelper(this);
        displayData();
    }

    private void displayData() {
        List<Entry> pitchEntries = new ArrayList<>();
        List<Entry> rollEntries = new ArrayList<>();
        List<Entry> yawEntries = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORIENTATION, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") float timestamp = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP));
                @SuppressLint("Range") float pitch = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_PITCH));
                @SuppressLint("Range") float roll = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROLL));
                @SuppressLint("Range") float yaw = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_YAW));

                pitchEntries.add(new Entry(timestamp, pitch));
                rollEntries.add(new Entry(timestamp, roll));
                yawEntries.add(new Entry(timestamp, yaw));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Plot pitch graph
        LineDataSet pitchDataSet = new LineDataSet(pitchEntries, "Pitch");
        LineData pitchData = new LineData(pitchDataSet);
        pitchChart.setData(pitchData);
        pitchChart.invalidate(); // Refresh chart

        // Plot roll graph
        LineDataSet rollDataSet = new LineDataSet(rollEntries, "Roll");
        LineData rollData = new LineData(rollDataSet);
        rollChart.setData(rollData);
        rollChart.invalidate(); // Refresh chart

        // Plot yaw graph
        LineDataSet yawDataSet = new LineDataSet(yawEntries, "Yaw");
        LineData yawData = new LineData(yawDataSet);
        yawChart.setData(yawData);
        yawChart.invalidate(); // Refresh chart
    }
}
