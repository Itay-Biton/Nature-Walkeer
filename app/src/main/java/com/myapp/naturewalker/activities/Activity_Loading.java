package com.myapp.naturewalker.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.myapp.naturewalker.R;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Challenge;
import com.myapp.naturewalker.objects.Plant;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserChallenge;
import com.myapp.naturewalker.user_objects.UserPlant;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.LocalData;
import com.myapp.naturewalker.utils.Status;
import com.myapp.naturewalker.utils.StepCounterService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Activity_Loading extends AppCompatActivity {
    private static final int REQUEST_CODE_ACTIVITY_RECOGNITION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        DataManager.getUser(new GeneralCallback() {
            @Override
            public void success(Object o) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_Loading.this,
                            new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                            REQUEST_CODE_ACTIVITY_RECOGNITION);
                }
                else
                    startStepCounter();
                LocalData.handler.postDelayed(LocalData.runnable, 1000);
                scheduleDailyReset();
                checkDayTransition();
                getData();
                gotoDashboard();
            }
            @Override
            public void failed(Object o) {}
        });
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private void startStepCounter() {
        Intent i = new Intent(getApplicationContext(), StepCounterService.class);
        startService(i);
    }

    private void updateWeeklySteps() {
        User user = User.getInstance();
        ArrayList<Integer> weeklySteps = user.getWeeklySteps();
        // this method is called after day change so today is the day before change
        // getValue() returns: monday = 1, sunday = 7
        int today = LocalDate.now().getDayOfWeek().getValue() - 1;
        if (today == 6) { // today is sunday
            if (!user.getWeeklyGoal().isCollected())
                user.checkWeeklyGoal();
            weeklySteps.clear();
            user.getWeeklyGoal().setCollected(false);
        }
        weeklySteps.set(today, user.getTodaySteps());
        if (!user.getDailyGoal().isCollected())
            user.checkDailyGoal();
        user.getDailyGoal().setCollected(false);
        user.setTodaySteps(0);
        StepCounterService.resetInitialStepCount();

        DataManager.updateUserWeeklyGoal(user.getWeeklyGoal());
        DataManager.updateUserDailyGoal(user.getDailyGoal());
        DataManager.updateUserWeeklySteps();
    }

    private void checkDayTransition() {
        long today = LocalDate.now().toEpochDay();

        if (User.getInstance().getLastRecordedDate() == 0 || User.getInstance().getLastRecordedDate() != today) {
            updateWeeklySteps();
            LocalData.checkForFinishedChallenges();
            User.getInstance().setLastRecordedDate(today);
            DataManager.updateUserLastRecordedDate();
        }
    }

    private long calculateInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1);
        return Duration.between(now, midnight).toMillis();
    }

    private void scheduleDailyReset() {
        WorkManager.getInstance(this).enqueue(
                new PeriodicWorkRequest.Builder(DailyResetWorker.class, 1, TimeUnit.DAYS)
                        .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                        .build());
    }

    public class DailyResetWorker extends Worker {
        public DailyResetWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            updateWeeklySteps();
            return Result.success();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startStepCounter();
            }
            else {
                Toast.makeText(this, "Permission denied to access activity recognition", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getData() {
        ArrayList<String> activeChallengesIds = new ArrayList<>();
        ArrayList<String> pastChallengesIds = new ArrayList<>();
        DataManager.getAllUserChallenges(new GeneralCallback() {
            @Override
            public void success(Object o) {
                ArrayList<UserChallenge> userChallenges = (ArrayList<UserChallenge>) o;
                for (UserChallenge userChallenge : userChallenges) {
                    if (userChallenge.getStatus() == Status.ACTIVE) {
                        LocalData.activeUserChallenges.add(userChallenge);
                        activeChallengesIds.add(userChallenge.getChallengeId());
                    }
                    else if (userChallenge.getStatus() == Status.DONE)
                        pastChallengesIds.add(userChallenge.getChallengeId());
                }
                final int[] callbackCount = {2};
                DataManager.getChallengesFromList(true, activeChallengesIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.activeChallenges = (ArrayList<Challenge>) o;
                        callbackCount[0]--;
                        if (callbackCount[0] == 0)
                            thirdCallback();
                    }
                    @Override
                    public void failed(Object o) {}
                });
                DataManager.getChallengesFromList(true, pastChallengesIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.pastChallenges = (ArrayList<Challenge>) o;
                        callbackCount[0]--;
                        if (callbackCount[0] == 0)
                            thirdCallback();
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }
            @Override
            public void failed(Object o) {
                DataManager.getAllChallenges(new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        for (Object item : (ArrayList<Object>) o)
                            LocalData.currentChallenges.add((Challenge) item);
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }

            private void thirdCallback() {
                ArrayList<String> currentChallengesIds = activeChallengesIds;
                currentChallengesIds.addAll(pastChallengesIds);
                DataManager.getChallengesFromList(false, currentChallengesIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.currentChallenges = (ArrayList<Challenge>) o;
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }
        });
        DataManager.getUserPlantsFromList(true, User.getInstance().getUserPlantsIds(), new GeneralCallback() {
            @Override
            public void success(Object o) {
                ArrayList<UserPlant> userPlants = (ArrayList<UserPlant>) o;
                ArrayList<String> plantsIds = new ArrayList<>();
                for (UserPlant userPlant : userPlants) {
                    if (userPlant.getStatus() != Status.DONE) {
                        plantsIds.add(userPlant.getPlantId());
                        LocalData.userPlants.add(userPlant);
                    }
                }
                DataManager.getPlantsFromList(true, plantsIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.plants = (ArrayList<Plant>) o;
                        LocalData.extractActivePlant();
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }
            @Override
            public void failed(Object o) {}
        });
    }
}