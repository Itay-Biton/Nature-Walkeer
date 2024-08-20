package com.myapp.naturewalker.utils;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.myapp.naturewalker.objects.Plant;

import java.time.Duration;
import java.time.LocalDate;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        new DataManager();
        new UserManager();
        // TODO: page animations
        // TODO: plant animations
        // TODO: better icon
//        DataManager.addBoost(new Boost("Speed Boost", 300.0,Duration.ofMinutes(40).plusMinutes(5), 2.0));
//        DataManager.addBoost(new Boost("Power Boost", 500.0, Duration.ofHours(1).plusMinutes(20), 3.0));
//        DataManager.addBoost(new Boost("Endurance Boost", 200.0, Duration.ofMinutes(30), 1.5));
//        DataManager.addBoost(new Boost("Strength Boost", 400.0, Duration.ofHours(2), 2.5));
//        DataManager.addBoost(new Boost("Agility Boost", 350.0, Duration.ofMinutes(50), 2.2));
//
//        DataManager.addPlant(new Plant("Sunflower", 500.0, 600.0, Duration.ofMinutes(1), "img1"));
//        DataManager.addPlant(new Plant("Rose", 1000.0, 1200.0, Duration.ofMinutes(30), "img2"));
//        DataManager.addPlant(new Plant("Tulip", 2000.0, 2400.0, Duration.ofHours(1), "img3"));
//        DataManager.addPlant(new Plant("Daisy", 3000.0, 3800.0, Duration.ofHours(3), "img4"));
//        DataManager.addPlant(new Plant("Orchid", 5000.0, 6600.0, Duration.ofHours(8), "img5"));
//
//        DataManager.addUpgrade(new Upgrade("Running Shoes", 2500.0, 1.5));
//        DataManager.addUpgrade(new Upgrade("Energy Drink", 3000.0, 2.0));
//        DataManager.addUpgrade(new Upgrade("Protein Bar", 2000.0, 1.2));
//        DataManager.addUpgrade(new Upgrade("Gym Membership", 5000.0, 2.5));
//        DataManager.addUpgrade(new Upgrade("Personal Trainer", 6000.0, 3.0));
//
//        DataManager.addChallenge(new Challenge("Step Sprint", 50.0, Duration.ofDays(1), 10000));
//        DataManager.addChallenge(new Challenge("Weekend Warrior", 75.0, Duration.ofDays(2), 15000));
//        DataManager.addChallenge(new Challenge("Midweek Mover", 100.0, Duration.ofDays(5), 25000));
//        DataManager.addChallenge(new Challenge("Weeklong Walker", 150.0, Duration.ofDays(7), 35000));
//        DataManager.addChallenge(new Challenge("Ten-Day Trailblazer", 200.0, Duration.ofDays(10), 50000));
    }
}
