package com.myapp.naturewalker.user_objects;

import com.myapp.naturewalker.utils.Status;

public class UserPlant {
    private String id;
    private String plantId;
    private long plantingDate;
    private Status status;

    public UserPlant() {}

    public UserPlant(String plantId) {
        this.plantId = plantId;
        this.status = Status.OWNED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(long plantingDate) {
        this.plantingDate = plantingDate;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
