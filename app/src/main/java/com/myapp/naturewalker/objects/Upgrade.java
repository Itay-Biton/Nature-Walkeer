package com.myapp.naturewalker.objects;

import com.myapp.naturewalker.abstracts.ShopItem;

import java.time.Duration;

public class Upgrade extends ShopItem {
    private double cps; // coins per step

    public Upgrade() {}

    public Upgrade(String name, double price, double cps) {
        super(name, price);
        this.cps = cps;
    }

    @Override
    public String generateDescription() {
        return "This upgrade give you +" + cps + " cps permanently.";
    }

    public double getCps() {
        return cps;
    }

    public void setCps(double cps) {
        this.cps = cps;
    }
}
