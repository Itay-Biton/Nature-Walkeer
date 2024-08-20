package com.myapp.naturewalker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import com.myapp.naturewalker.R;
import com.myapp.naturewalker.custom_views.CircularProgressView;
import com.myapp.naturewalker.custom_views.WeeklyStepsBarChart;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.utils.Formater;
import com.myapp.naturewalker.utils.LocalData;

import java.time.LocalDate;

public class Activity_Sport extends AppCompatActivity {
    private AppCompatImageButton BTN_back;
    private TextView TXT_title, TXT_avg_steps,
            TXT_challenge_progress, TXT_daily_progress, TXT_weekly_progress,
            TXT_challenge_step_count, TXT_daily_step_count, TXT_weekly_step_count,
            TXT_challenge_description, TXT_daily_description, TXT_weekly_description,
            LBL_step_change, TXT_step_change;
    private CircularProgressView challengeProgress, dailyProgress, weeklyProgress;
    private WeeklyStepsBarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        findViews();
        setListeners();
        updateUI();
    }

    private void findViews() {
        BTN_back = findViewById(R.id.BTN_back);

        TXT_title = findViewById(R.id.TXT_title);
        TXT_title.setText(getString(R.string.sport));

        TXT_avg_steps = findViewById(R.id.TXT_avg_steps);
        LBL_step_change = findViewById(R.id.LBL_step_change);
        TXT_step_change = findViewById(R.id.TXT_step_change);

        TXT_challenge_progress = findViewById(R.id.TXT_challenge_progress);
        TXT_daily_progress = findViewById(R.id.TXT_daily_progress);
        TXT_weekly_progress = findViewById(R.id.TXT_weekly_progress);

        TXT_challenge_step_count = findViewById(R.id.TXT_challenge_step_count);
        TXT_daily_step_count = findViewById(R.id.TXT_daily_step_count);
        TXT_weekly_step_count = findViewById(R.id.TXT_weekly_step_count);


        TXT_challenge_description = findViewById(R.id.TXT_challenge_description);
        TXT_daily_description = findViewById(R.id.TXT_daily_description);
        TXT_weekly_description = findViewById(R.id.TXT_weekly_description);


        challengeProgress = findViewById(R.id.challengeProgress);
        dailyProgress = findViewById(R.id.dailyProgress);
        weeklyProgress = findViewById(R.id.weeklyProgress);

        challengeProgress.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        dailyProgress.setColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
        weeklyProgress.setColor(ContextCompat.getColor(this, android.R.color.holo_green_light));

        barChart = findViewById(R.id.barChart);
    }

    private void setListeners() {
        BTN_back.setOnClickListener(v -> gotoDashboard());
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private void updateUI() {
        float challengePercent = Formater.getChallengeProgress();
        float dailyPercent = Formater.getDailyProgress();
        float weeklyPercent = Formater.getWeeklyProgress();
        challengeProgress.setProgress(challengePercent);
        dailyProgress.setProgress(dailyPercent);
        weeklyProgress.setProgress(weeklyPercent);

        TXT_challenge_progress.setText(String.format("%s%%", Math.round(challengePercent * 10.0) / 10.0));
        TXT_daily_progress.setText(String.format("%s%%", Math.round(dailyPercent * 10.0) / 10.0));
        TXT_weekly_progress.setText(String.format("%s%%", Math.round(weeklyPercent * 10.0) / 10.0));

        if (!LocalData.activeUserChallenges.isEmpty()) {
            TXT_challenge_step_count.setText(String.format("%s", LocalData.activeUserChallenges.get(0).getStepsTaken()));
            TXT_challenge_description.setText(LocalData.activeChallenges.get(0).toString());
        }
        else {
            TXT_challenge_step_count.setText(String.format("%s", 0));
            TXT_challenge_description.setText("No Active Challenge");
        }
        TXT_daily_step_count.setText(String.format("%s", User.getInstance().getTodaySteps()));
        TXT_daily_description.setText(User.getInstance().getDailyGoal().toString());
        TXT_weekly_step_count.setText(String.format("%s", User.getInstance().weeklyStepsSUM()));
        TXT_weekly_description.setText(User.getInstance().getWeeklyGoal().toString());

        TXT_avg_steps.setText(User.getInstance().weeklyStepsAVG()+ " steps");

        int today = LocalDate.now().getDayOfWeek().getValue();
        today = (today == 7) ? 0 : today;
        if (today != 0) {
            if (User.getInstance().yesterdayStepsChange() >= 0)
                LBL_step_change.setText("Your steps today\nare up by");
            else
                LBL_step_change.setText("Your steps today\nare down by");
            TXT_step_change.setText(User.getInstance().yesterdayStepsChange()+ " steps");
        }
        else {
            LBL_step_change.setText("First day of\nthis week");
            TXT_step_change.setText("");
        }

        barChart.updateValues();
    }
}