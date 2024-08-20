package com.myapp.naturewalker.objects;

import com.myapp.naturewalker.abstracts.ShopItem;
import com.myapp.naturewalker.utils.Formater;

import java.time.Duration;

public class Boost extends ShopItem {
    private long duration;
    private double cpsMultiplier;

    public Boost() {}

    public Boost(String name, double price, Duration duration, double cpsMultiplier) {
        super(name, price);
        this.duration = duration.toMillis();
        this.cpsMultiplier = cpsMultiplier;
    }

    @Override
    public String generateDescription() {
        return "This boost give you x" + cpsMultiplier + " cps for " +
                Formater.stringDuration(duration);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getCpsMultiplier() {
        return cpsMultiplier;
    }

    public void setCpsMultiplier(double cpsMultiplier) {
        this.cpsMultiplier = cpsMultiplier;
    }


}
