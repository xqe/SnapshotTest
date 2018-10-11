package com.example.data.tracker.mylibrary.state;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class ShakeState implements SensorEventListener {

    private static final String TAG = "ShakeState";
    private ShakeListener shakeListener;
    private SensorManager sensorManager;
    private static final int STANDARD_VALUE = 15;

    public ShakeState(ShakeListener shakeListener) {
        this.shakeListener = shakeListener;
    }

    public void startListen(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {
            Log.e(TAG, "can't get sensorManager!");
            Toast.makeText(context,"can't get sensorManager!",Toast.LENGTH_LONG).show();
            return;
        }
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void cancelListen() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xValue = Math.abs(event.values[0]);
        float yValue = Math.abs(event.values[1]);
        float zValue = Math.abs(event.values[2]);
        if (xValue > STANDARD_VALUE || yValue > STANDARD_VALUE || zValue > STANDARD_VALUE) {
            Log.e(TAG, "onSensorChanged: shaking!!!!!!!!!");
            shakeListener.onShake();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }

    public interface ShakeListener{
        void onShake();
    }
}
