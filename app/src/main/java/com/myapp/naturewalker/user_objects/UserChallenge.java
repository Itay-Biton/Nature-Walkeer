package com.myapp.naturewalker.user_objects;

import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.Status;

import java.time.LocalDate;

public class UserChallenge {
    private String id;
    private String challengeId;
    private long startDate;
    private int stepsTaken;
    private Status status;

    public UserChallenge() {}

    public UserChallenge(String challengeId, Status status) {
        this.challengeId = challengeId;
        this.status = status;
        this.startDate = LocalDate.now().toEpochDay();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status newStatus) {
        status = newStatus;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public void addSteps(int amount) {
        stepsTaken = stepsTaken+amount;
        DataManager.updateUserActiveChallengeSteps(this);
    }
}
