package com.myapp.naturewalker.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.myapp.naturewalker.objects.User;

public class StepCounterService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;

    static private int initialStepCount = -1;

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int totalSteps = (int) event.values[0];

            if (initialStepCount == -1) {
                initialStepCount = totalSteps;
            }

            int deltaSteps = totalSteps - initialStepCount;
            initialStepCount = totalSteps;

            updateStepsAndCoins(deltaSteps);
        }
    }

    private void updateStepsAndCoins(int deltaSteps) {
        if (User.getInstance() == null)
            return;
        double coinsEarned = deltaSteps * User.getInstance().getCps();
        if (LocalData.activeBoost != null)
            coinsEarned *= LocalData.activeBoost.getCpsMultiplier();

        User.getInstance().setTodaySteps(User.getInstance().getTodaySteps() + deltaSteps);
        User.getInstance().addCoins(coinsEarned);
        LocalData.updateActiveUserChallenges(deltaSteps);
        DataManager.updateUserTodaySteps();

        // TODO: update the server with buffer, not every change.
    }

    static public void resetInitialStepCount() {
        initialStepCount = -1;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}