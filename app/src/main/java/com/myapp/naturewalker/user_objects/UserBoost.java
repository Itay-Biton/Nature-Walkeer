package com.myapp.naturewalker.user_objects;

import com.myapp.naturewalker.utils.Status;

public class UserBoost {
    private String id;
    private String boostId;
    private Status status;
    private long start;

    public UserBoost() {

    }

    public UserBoost(String boostId, Status status) {
        this.boostId = boostId;
        this.status = status;
        this.start = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status newStatus) {
        status = newStatus;
    }

    public String getBoostId() {
        return boostId;
    }

    public void setBoostId(String boostId) {
        this.boostId = boostId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
}
