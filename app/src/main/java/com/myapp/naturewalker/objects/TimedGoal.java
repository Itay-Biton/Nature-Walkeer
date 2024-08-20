package com.myapp.naturewalker.objects;

import android.util.Log;

import com.myapp.naturewalker.utils.DataManager;

import java.time.Duration;
import java.time.Instant;

public class TimedGoal {
    static public int changeFactor = 5; // %
    private long duration;
    private int steps;
    private int reward;
    private boolean collected;

    public TimedGoal() {}

    public TimedGoal makeDailyGoal() {
        duration = Duration.ofDays(1).toMillis();
        steps = 1000;
        reward = 100;
        return this;
    }
    public TimedGoal makeWeeklyGoal() {
        duration = Duration.ofDays(7).toMillis();
        steps = 14000;
        reward = 1000;
        return this;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public void updateGoal(boolean success) {
        if (success)
            User.getInstance().addCoins(reward);
        collected = true;

        int dailySteps = steps / (int) Duration.ofMillis(duration).toDays();
        int adjustment = dailySteps * changeFactor / 100;
        dailySteps += success ? adjustment : -adjustment;

        setSteps(dailySteps);

        if (duration == 7)
            DataManager.updateUserWeeklyGoal(this);
        else
            DataManager.updateUserDailyGoal(this);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (duration == Duration.ofDays(1).toMillis())
            str.append("Daily Goal");
        else
            str.append("Weekly Goal");
        str.append("\nRequired Steps: ").append(steps);
        str.append("\nReward: ").append(reward);
        return str.toString();
    }
}
