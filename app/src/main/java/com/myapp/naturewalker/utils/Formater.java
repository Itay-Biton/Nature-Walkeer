package com.myapp.naturewalker.utils;

import android.util.Log;

import com.myapp.naturewalker.objects.User;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

public class Formater {
    static public String getInterval(Duration duration) {
        if (duration.toDays() > 1)
            return "days";
        else if (duration.toHours() > 1)
            return "hours";
        else if (duration.toMinutes() > 1)
            return "minutes";
        else if (duration.toMillis()/1000 > 1)
            return "seconds";
        else
            return "milliseconds";
    }

    public static String stringDuration(long durationInMillis) {
        Duration duration = Duration.ofMillis(durationInMillis);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();

        StringBuilder res = new StringBuilder();
        if (hours > 0) {
            res.append(hours);
            if (hours == 1)
                res.append(" hour");
            else
                res.append(" hours");
        }
        if (minutes > 0) {
            if (res.length() > 0)
                res.append(" and ");
            res.append(minutes);
            if (minutes == 1)
                res.append(" minute");
            else
                res.append(" minutes");
        }
        if (seconds > 0) {
            if (res.length() > 0)
                res.append(" and ");
            res.append(seconds);
            if (seconds == 1)
                res.append(" second");
            else
                res.append(" seconds");
        }

        return res.toString();
    }

    public static String stringDurationAsClock(long durationInMillis) {
        Duration duration = Duration.ofMillis(durationInMillis);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();

        StringBuilder res = new StringBuilder();
        if (hours > 0) {
            if (hours < 10)
                res.append("0");
            res.append(hours);
        }
        else
            res.append("00");
        if (minutes > 0) {
            if (res.length() > 0)
                res.append(":");
            if (minutes < 10)
                res.append("0");
            res.append(minutes);
        }
        else
            res.append(":00");
        if (seconds > 0) {
            if (res.length() > 0)
                res.append(":");
            if (seconds < 10)
                res.append("0");
            res.append(seconds);
        }
        else
            res.append(":00");

        return res.toString();
    }

    static public float getChallengeProgress() {
        if (LocalData.activeUserChallenges.isEmpty())
            return 0;
        int now = LocalData.activeUserChallenges.get(0).getStepsTaken();
        int end = LocalData.activeChallenges.get(0).getRequiredSteps();
        float progress = (float) 100*now / end;
        if (now == 0)
            progress = 0;
        else if (progress > 100)
            progress = 100;

        if (progress >= 100)
            LocalData.checkForFinishedChallenges();
        return progress;
    }
    static public float getDailyProgress() {
        User user = User.getInstance();
        int now = user.getTodaySteps();
        int end = user.getDailyGoal().getSteps();
        float progress = (float) 100*now / end;
        if (now == 0)
            progress = 0;
        else if (progress > 100)
            progress = 100;

        if (progress >= 100 && !user.getDailyGoal().isCollected())
            user.checkDailyGoal();
        return progress;
    }
    static public float getWeeklyProgress() {
        User user = User.getInstance();
        int now = user.getTodaySteps();
        for (Integer weeklyStep : user.getWeeklySteps())
            now += weeklyStep;
        int end = user.getWeeklyGoal().getSteps();
        float progress = (float) 100*now / end;
        if (now == 0)
            progress = 0;
        else if (progress > 100)
            progress = 100;

        if (progress >= 100 && !user.getWeeklyGoal().isCollected())
            user.checkWeeklyGoal();
        return progress;
    }
    static public float getPlantProgress() {
        if (LocalData.activePlant == null)
            return 0;
        long timeframe = LocalData.activePlant.getGrowTime();
        long grow = Instant.now().toEpochMilli() - LocalData.activeUserPlant.getPlantingDate();

        float progress = (float) 100*grow / timeframe;
        if (grow == 0)
            progress = 0;
        else if (progress > 100)
            progress = 100;
        return progress;
    }

    static public String getPlanHarvestDuration() {
        if (LocalData.activePlant == null)
            return "";
        long timeframe = LocalData.activePlant.getGrowTime();
        long grow = Instant.now().toEpochMilli() - LocalData.activeUserPlant.getPlantingDate();
        return stringDuration(timeframe - grow);
    }

    static public String getPlanHarvestDurationAsClock() {
        if (LocalData.activePlant == null)
            return "";
        long timeframe = LocalData.activePlant.getGrowTime();
        long grow = Instant.now().toEpochMilli() - LocalData.activeUserPlant.getPlantingDate();
        return stringDurationAsClock(timeframe - grow);
    }

    static public String getBoostDurationAsClock() {
        if (LocalData.activeBoost == null)
            return "";
        long timeframe = LocalData.activeBoost.getDuration();
        long progress = Instant.now().toEpochMilli() - LocalData.getActiveUserBoost().getStart();
        return stringDurationAsClock(timeframe - progress);
    }

}
