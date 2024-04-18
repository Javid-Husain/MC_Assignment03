package com.example.assignment03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView textViewPitch, textViewRoll, textViewYaw;
    private Button startButton, stopButton, historyButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        textViewPitch = findViewById(R.id.text_view_pitch);
        textViewRoll = findViewById(R.id.text_view_roll);
        textViewYaw = findViewById(R.id.text_view_yaw);

        // Set initial values to zero
        textViewPitch.setText("Pitch: 0.0");
        textViewRoll.setText("Roll: 0.0");
        textViewYaw.setText("Yaw: 0.0");

        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        historyButton = findViewById(R.id.history_button);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSensor();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSensor();
                // Reset values to zero when stop button is clicked
                textViewPitch.setText("Pitch: 0.0");
                textViewRoll.setText("Roll: 0.0");
                textViewYaw.setText("Yaw: 0.0");
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startSensor() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void stopSensor() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] orientationValues = new float[3];
            float[] rotationMatrix = new float[9];
            try {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                SensorManager.getOrientation(rotationMatrix, orientationValues);

                float pitch = orientationValues[1];
                float roll = orientationValues[2];
                float yaw = orientationValues[0];

                // Check for NaN values
                if (!Float.isNaN(pitch) && !Float.isNaN(roll) && !Float.isNaN(yaw)) {
                    textViewPitch.setText("Pitch: " + Math.toDegrees(pitch));
                    textViewRoll.setText("Roll: " + Math.toDegrees(roll));
                    textViewYaw.setText("Yaw: " + Math.toDegrees(yaw));
                } else {
                    // Handle NaN values
                    Log.e("Sensor", "NaN values detected");
                }
            } catch (Exception e) {
                // Handle any exceptions
                Log.e("Sensor", "Error processing sensor data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing for now
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSensor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensor();
    }
}
