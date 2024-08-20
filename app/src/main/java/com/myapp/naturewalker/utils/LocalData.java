package com.myapp.naturewalker.utils;

import android.os.Handler;

import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Boost;
import com.myapp.naturewalker.objects.Challenge;
import com.myapp.naturewalker.objects.Plant;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserBoost;
import com.myapp.naturewalker.user_objects.UserChallenge;
import com.myapp.naturewalker.user_objects.UserPlant;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

public class LocalData {
    static public ArrayList<Plant> plants = new ArrayList<>();
    static public ArrayList<UserPlant> userPlants = new ArrayList<>();
    static public Plant activePlant;
    static public UserPlant activeUserPlant;
    static public ArrayList<UserChallenge> activeUserChallenges = new ArrayList<>();
    static public ArrayList<Challenge> activeChallenges = new ArrayList<>();
    static public ArrayList<Challenge> currentChallenges = new ArrayList<>();
    static public ArrayList<Challenge> pastChallenges = new ArrayList<>();
    static public ArrayList<Boost> boosts = new ArrayList<>();
    static public ArrayList<UserBoost> userBoosts = new ArrayList<>();
    static public Boost activeBoost;

    static public void destroy() {
        plants = new ArrayList<>();
        userPlants = new ArrayList<>();
        activePlant = null;
        activeUserPlant = null;
        activeUserChallenges = new ArrayList<>();
        activeChallenges = new ArrayList<>();
        currentChallenges = new ArrayList<>();
        pastChallenges = new ArrayList<>();
        boosts = new ArrayList<>();
        userBoosts = new ArrayList<>();
        activeBoost = null;
        handler.removeCallbacks(runnable);
    }

    static public void extractActivePlant() {
        for (UserPlant userPlant : userPlants) {
            if (userPlant.getStatus() == Status.ACTIVE) {
                activeUserPlant = userPlant;
                userPlants.remove(activeUserPlant);
                activePlant = getPlantFromId(userPlant.getPlantId());
                plants.remove(activePlant);
                break;
            }
        }
    }

    static public void setActivePlant(UserPlant userPlant) {
        userPlant.setStatus(Status.ACTIVE);
        DataManager.updateUserPlantStatus(userPlant);
        userPlant.setPlantingDate(Instant.now().toEpochMilli());
        DataManager.updateUserPlantPlantingDate(userPlant);
        extractActivePlant();
    }

    static public Plant getPlantFromId(String name) {
        for (Plant plant : plants) {
            if (plant.getName().equals(name))
                return plant;
        }
        return null;
    }

    static public UserPlant getUserPlantFromPlant(Plant plant) {
        for (UserPlant userPlant : userPlants) {
            if (userPlant.getPlantId().equals(plant.getName()))
                return userPlant;
        }
        return null;
    }

    static public Challenge getCurrentChallengeFromId(String title) {
        for (Challenge challenge : currentChallenges) {
            if (challenge.getTitle().equals(title))
                return challenge;
        }
        return null;
    }

    static public boolean isPlantReady() {
        if (activePlant != null) {
            long harvestDate = activePlant.getGrowTime() + activeUserPlant.getPlantingDate();
            return Instant.now().toEpochMilli() >= harvestDate;
        }
        return false;
    }

    static public void checkAndUpdatePlantStatus(GeneralCallback generalCallback) {
        if (activeUserPlant != null) {
            if (isPlantReady()) {
                activeUserPlant.setStatus(Status.DONE);
                DataManager.updateUserPlantStatus(activeUserPlant);
                User.getInstance().addCoins(activePlant.getReward());
                activePlant = null;
                activeUserPlant = null;
                generalCallback.success(null);
            }
            generalCallback.failed(Status.ACTIVE);
        }
        else
            generalCallback.failed(Status.NULL);
    }


    static public Challenge getActiveChallengeFromId(String title) {
        for (Challenge challenge : activeChallenges) {
            if (challenge.getTitle().equals(title))
                return challenge;
        }
        return null;
    }

    static public void updateActiveUserChallenges(int stepCount) {
        for (UserChallenge activeUserChallenge : activeUserChallenges) {
            activeUserChallenge.addSteps(stepCount);
        }
        checkForFinishedChallenges();
    }

    static public void checkForFinishedChallenges() {
        for (UserChallenge activeUserChallenge : activeUserChallenges) {
            Challenge activeChallenge = getActiveChallengeFromId(activeUserChallenge.getChallengeId());
            long today = LocalDate.now().toEpochDay();
            if (activeUserChallenge.getStepsTaken() >= activeChallenge.getRequiredSteps()) {
                User.getInstance().addCoins(activeChallenge.getReward());
                activeUserChallenge.setStatus(Status.DONE);
                activeUserChallenges.remove(activeUserChallenge);
                activeChallenges.remove(activeChallenge);
                DataManager.updateUserActiveChallengeStatus(activeUserChallenge);
            }
            else if (today - activeUserChallenge.getStartDate() >= activeChallenge.getDuration()) {
                activeUserChallenge.setStatus(Status.NULL);
                DataManager.updateUserActiveChallengeStatus(activeUserChallenge);
                activeUserChallenges.remove(activeUserChallenge);
                activeChallenges.remove(activeChallenge);
            }
        }
    }



    static public Boost getBoostFromId(String name) {
        for (Boost boost : boosts) {
            if (boost.getName().equals(name))
                return boost;
        }
        return null;
    }
    static public UserBoost getReadyBoost() {
        for (UserBoost userBoost : userBoosts)
            if (userBoost.getStatus() == Status.READY)
                return userBoost;
        return null;
    }

    static public UserBoost getActiveUserBoost() {
        for (UserBoost userBoost : userBoosts)
            if (userBoost.getStatus() == Status.ACTIVE)
                return userBoost;
        return null;
    }

    static public boolean isActiveBoostRunning() {
        UserBoost activeUserBoost = getActiveUserBoost();
        if (activeUserBoost != null && activeBoost != null)
                return Instant.now().toEpochMilli() - activeUserBoost.getStart() < activeBoost.getDuration();
        return false;
    }

    static public void checkAndUpdateBoostStatus() {
        UserBoost userBoost = getActiveUserBoost();
        if (userBoost != null && !isActiveBoostRunning()) {
            activeBoost = null;
            userBoost.setStatus(Status.DONE);
            DataManager.updateUserBoostStatus(userBoost);
        }
    }

    static public int getReadyBoostPosition() {
        if (getReadyBoost() != null)
            return boosts.indexOf(getBoostFromId(getReadyBoost().getBoostId()));
        else
            return -1;
    }

    static public void setAsReadyBoost(String boostId) {
        UserBoost oldReadyBoost = LocalData.getReadyBoost();
        if (oldReadyBoost != null) {
            if (oldReadyBoost.getBoostId().equals(boostId))
                return;
            oldReadyBoost.setStatus(Status.OWNED);
            DataManager.updateUserBoostStatus(oldReadyBoost);
        }

        for (UserBoost userBoost : userBoosts) {
            if (userBoost.getBoostId().equals(boostId) && userBoost.getStatus() == Status.OWNED) {
                userBoost.setStatus(Status.READY);
                DataManager.updateUserBoostStatus(userBoost);
                return;
            }
        }
    }

    static public void activateBoost(GeneralCallback generalCallback) {
        UserBoost activeUserBoost = LocalData.getActiveUserBoost();
        if (activeUserBoost != null) {
            generalCallback.failed(Status.ACTIVE);
            return;
        }
        UserBoost readyBoost = LocalData.getReadyBoost();
        if (readyBoost == null) {
            generalCallback.failed(Status.NULL);
            return;
        }
        activeBoost = getBoostFromId(readyBoost.getBoostId());
        readyBoost.setStatus(Status.ACTIVE);
        DataManager.updateUserBoostStatus(readyBoost);
        readyBoost.setStart(Instant.now().toEpochMilli());
        DataManager.updateUserBoostStart(readyBoost);
        generalCallback.success(null);
    }

    static public final Handler handler = new Handler();
    static public final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkAndUpdateBoostStatus();
            handler.postDelayed(runnable, 1000);
        }
    };
}
