package com.myapp.naturewalker.abstracts;

import java.time.Duration;

public abstract class ShopItem {
    private String name;
    private double price;

    public abstract String generateDescription();

    public ShopItem() {}

    public ShopItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
