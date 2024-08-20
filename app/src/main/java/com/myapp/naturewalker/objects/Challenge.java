package com.myapp.naturewalker.objects;

import com.myapp.naturewalker.utils.Formater;

import java.time.Duration;

public class Challenge {
    private String title;
    private double reward;
    private long duration;
    private int requiredSteps;

    public Challenge() {}

    public Challenge(String title, double reward, Duration duration, int requiredSteps) {
        this.title = title;
        this.reward = reward;
        this.duration = duration.toMillis();
        this.requiredSteps = requiredSteps;
    }

    public String generateDescription() {
        Duration duration = Duration.ofMillis(this.duration);
        String interval = Formater.getInterval(duration);
        StringBuilder description = new StringBuilder("Walk " + requiredSteps + " steps in ");
        switch (interval) {
            case "days":
                if (duration.toDays() == 1)
                    description.append("a day.");
                else
                    description.append(duration.toDays()).append(" consecutive days.");
                break;
            case "hours":
                if (duration.toHours() == 1)
                    description.append("an hour.");
                else
                    description.append(duration.toHours()).append(" hours.");
                break;
            case "minutes":
                if (duration.toMinutes() == 1)
                    description.append("a minute.");
                else
                    description.append(duration.toMinutes()).append(" minutes.");
                break;
            default:
                break;
        }
        return description.toString();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(title);
        str.append("\nRequired Steps: ").append(requiredSteps);
        str.append("\nReward: ").append(reward);
        return str.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getRequiredSteps() {
        return requiredSteps;
    }

    public void setRequiredSteps(int requiredSteps) {
        this.requiredSteps = requiredSteps;
    }
}
