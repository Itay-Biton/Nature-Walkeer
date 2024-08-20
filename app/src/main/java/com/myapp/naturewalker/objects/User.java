package com.myapp.naturewalker.objects;

import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.UserManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    static private User instance;

    private String email;
    private String imgURL;
    private double cps; // coins per step
    private double coins;
    private int todaySteps;
    private ArrayList<Integer> weeklySteps;
    private ArrayList<String> upgradesIds;
    private ArrayList<String> userBoostsIds;
    private ArrayList<String> userPlantsIds;
    private ArrayList<String> userChallengesIds;
    private TimedGoal dailyGoal;
    private TimedGoal weeklyGoal;
    private long lastRecordedDate;

    static public User getInstance() {
        return instance;
    }
    static public void setInstance(User user) {
        instance = user;
    }

    public User() {}

    public User(boolean newUser) {
        email = UserManager.getUserEmail();
        cps = 0.5;
        coins = 0;
        todaySteps = 0;
        weeklySteps = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            weeklySteps.add(0);
        upgradesIds = new ArrayList<>();
        userBoostsIds = new ArrayList<>();
        userPlantsIds = new ArrayList<>();
        userChallengesIds = new ArrayList<>();
        dailyGoal = new TimedGoal().makeDailyGoal();
        weeklyGoal = new TimedGoal().makeWeeklyGoal();
    }

    public double getCps() {
        return cps;
    }

    public void setCps(double cps) {
        this.cps = Math.round(cps * 10.0) / 10.0;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = Math.round(coins * 10.0) / 10.0;;
    }

    public int getTodaySteps() {
        return todaySteps;
    }

    public void setTodaySteps(int todaySteps) {
        this.todaySteps = todaySteps;
    }

    public ArrayList<Integer> getWeeklySteps() {
        return weeklySteps;
    }

    public void setWeeklySteps(ArrayList<Integer> weeklySteps) {
        this.weeklySteps = weeklySteps;
    }

    public ArrayList<String> getUpgradesIds() {
        return upgradesIds;
    }

    public void setUpgradesIds(ArrayList<String> upgradesIds) {
        this.upgradesIds = upgradesIds;
    }

    public ArrayList<String> getUserBoostsIds() {
        return userBoostsIds;
    }

    public void setUserBoostsIds(ArrayList<String> userBoostsIds) {
        this.userBoostsIds = userBoostsIds;
    }

    public ArrayList<String> getUserPlantsIds() {
        return userPlantsIds;
    }

    public void setUserPlantsIds(ArrayList<String> userPlantsIds) {
        this.userPlantsIds = userPlantsIds;
    }

    public ArrayList<String> getUserChallengesIds() {
        return userChallengesIds;
    }

    public void setUserChallengesIds(ArrayList<String> userChallengesIds) {
        this.userChallengesIds = userChallengesIds;
    }

    public TimedGoal getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(TimedGoal dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public TimedGoal getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(TimedGoal weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }

    public long getLastRecordedDate() {
        return lastRecordedDate;
    }

    public void setLastRecordedDate(long lastRecordedDate) {
        this.lastRecordedDate = lastRecordedDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void pay(double price) {
        coins = Math.round((coins-price) * 10.0) / 10.0;
        DataManager.updateUserCoins();
    }

    public void addCoins(double amount) {
        coins = Math.round((coins+amount) * 10.0) / 10.0;
        DataManager.updateUserCoins();
    }

    public void upgradeCPS(double upgradeCPS) {
        cps = Math.round((cps+upgradeCPS) * 10.0) / 10.0;
        DataManager.updateUserCPS();
    }

    public void checkWeeklyGoal() {
        weeklyGoal.updateGoal(weeklyStepsSUM() >= weeklyGoal.getSteps());
    }

    public void checkDailyGoal() {
        dailyGoal.updateGoal(todaySteps >= dailyGoal.getSteps());
    }

    public int weeklyStepsAVG() {
        int today = LocalDate.now().getDayOfWeek().getValue();
        today = (today == 7) ? 0 : today;
        return weeklyStepsSUM()/(today+1);
    }

    public int weeklyStepsSUM() {
        int today = LocalDate.now().getDayOfWeek().getValue();
        today = (today == 7) ? 0 : today;
        int sum = 0;
        for (int i=0; i<today; i++)
            sum += weeklySteps.get(i);
        sum += todaySteps;
        return sum;
    }

    public int yesterdayStepsChange() {
        int today = LocalDate.now().getDayOfWeek().getValue();
        today = (today == 7) ? 0 : today;
        int res = 0;
        if (today != 0)
            res = weeklySteps.get(today-1);
        return todaySteps - res;
    }

}
