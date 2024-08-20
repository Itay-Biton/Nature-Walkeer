package com.myapp.naturewalker.objects;

import com.myapp.naturewalker.abstracts.ShopItem;
import com.myapp.naturewalker.utils.Formater;

import java.time.Duration;

public class Plant extends ShopItem {
    private double reward;
    private long growTime;
    private String img;

    public Plant() {}

    public Plant(String name, double price, double reward, Duration growTime, String img) {
        super(name, price);
        this.reward = reward;
        this.growTime = growTime.toMillis();
        this.img = img;
    }

    @Override
    public String generateDescription() {
        return "This plant takes " +
                Formater.stringDuration(growTime) +
                " to grow." +
                "\nReward: " + reward;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public long getGrowTime() {
        return growTime;
    }

    public void setGrowTime(long growTime) {
        this.growTime = growTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
