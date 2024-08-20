package com.myapp.naturewalker.utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Boost;
import com.myapp.naturewalker.objects.Challenge;
import com.myapp.naturewalker.objects.Plant;
import com.myapp.naturewalker.objects.TimedGoal;
import com.myapp.naturewalker.objects.Upgrade;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserBoost;
import com.myapp.naturewalker.user_objects.UserChallenge;
import com.myapp.naturewalker.user_objects.UserPlant;

import java.util.ArrayList;

public class DataManager {
    static public DataManager instance = null;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    static private final String collectionUsers = "users";
    static private final String collectionBoosts = "boosts";
    static private final String collectionPlants = "plants";
    static private final String collectionUpgrades = "upgrades";
    static private final String collectionChallenges = "challenges";

    static private final String collectionUserBoosts = "userBoosts";
    static private final String collectionUserPlants = "userPlants";
    static private final String collectionUserChallenges = "userChallenges";

    public DataManager() {
        if (instance != null)
            return;
        instance = this;
        instance.db = FirebaseFirestore.getInstance();
        instance.storageReference = FirebaseStorage.getInstance().getReference();
    }

    static public void getIconURL(String name, GeneralCallback generalCallback) {
        StorageReference iconRef = instance.storageReference.child("icons/ic_" + name + ".png");

        iconRef.getDownloadUrl().addOnCompleteListener(uri -> {
            generalCallback.success(uri.getResult().toString());
        }).addOnFailureListener(exception -> {
            generalCallback.failed(null);
        });
    }

    static public void getUser(GeneralCallback generalCallback) {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User.setInstance(document.toObject(User.class));
                            generalCallback.success(null);
                        }
                    }
        });
    }
    static public void addUser() {
        instance.db.collection(collectionUsers).document(UserManager.getUserId()).set(new User(true));
    }
    static public void updateUserCoins() {
        instance.db.collection(collectionUsers).document(UserManager.getUserId())
                .update("coins", User.getInstance().getCoins());
    }
    static public void updateUserCPS() {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .update("cps", User.getInstance().getCps());
    }
    static public void updateUserTodaySteps() {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .update("todaySteps", User.getInstance().getTodaySteps());
    }
    static public void updateUserWeeklySteps() {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .update("weeklySteps", User.getInstance().getWeeklySteps());
    }
    static public void updateUserActiveChallengeStatus(UserChallenge userChallenge) {
        instance.db.collection(collectionUserChallenges)
                .document(userChallenge.getId())
                .update("status", userChallenge.getStatus());
    }
    static public void updateUserActiveChallengeSteps(UserChallenge userChallenge) {
        instance.db.collection(collectionUserChallenges)
                .document(userChallenge.getId())
                .update("stepsTaken", userChallenge.getStepsTaken());
    }
    static public void updateUserPlantStatus(UserPlant userPlant) {
        instance.db.collection(collectionUserPlants)
                .document(userPlant.getId())
                .update("status", userPlant.getStatus());
    }
    static public void updateUserPlantPlantingDate(UserPlant userPlant) {
        instance.db.collection(collectionUserPlants)
                .document(userPlant.getId())
                .update("plantingDate", userPlant.getPlantingDate());
    }
    static public void updateUserBoostStatus(UserBoost userBoost) {
        instance.db.collection(collectionUserBoosts)
                .document(userBoost.getId())
                .update("status", userBoost.getStatus());
    }
    static public void updateUserBoostStart(UserBoost userBoost) {
        instance.db.collection(collectionUserBoosts)
                .document(userBoost.getId())
                .update("start", userBoost.getStart());
    }
    static public void updateUserLastRecordedDate() {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .update("lastRecordedDate", User.getInstance().getLastRecordedDate());
    }
    static public void updateUserWeeklyGoal(TimedGoal weeklyGoal) {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .update("weeklyGoal", weeklyGoal);
    }
    static public void updateUserDailyGoal(TimedGoal dailyGoal) {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .update("dailyGoal", dailyGoal);
    }
    static public void updateUserImage(String url) {
        instance.db.collection(collectionUsers)
                .document(UserManager.getUserId())
                .update("imgURL", url);
    }

    static public void addUserBoost(UserBoost userBoost) {
        instance.db.collection(collectionUserBoosts).add(userBoost)
                .addOnSuccessListener(documentReference -> {
                    String userBoostId = documentReference.getId();
                    documentReference.update("id", userBoostId).addOnSuccessListener(v -> {
                        User.getInstance().getUserBoostsIds().add(userBoostId);
                        instance.db.collection(collectionUsers).document(UserManager.getUserId())
                                .update("userBoostsIds", User.getInstance().getUserBoostsIds());
                    });
                });
    }
    static public void addUserPlant(UserPlant userPlant) {
        instance.db.collection(collectionUserPlants).add(userPlant)
                .addOnSuccessListener(documentReference -> {
                    String userPlantId = documentReference.getId();
                    documentReference.update("id", userPlantId).addOnSuccessListener(v -> {
                        User.getInstance().getUserPlantsIds().add(userPlantId);
                        instance.db.collection(collectionUsers).document(UserManager.getUserId())
                                .update("userPlantsIds", User.getInstance().getUserPlantsIds());
                    });
                });
    }
    static public void addUserUpgrade(Upgrade upgrade) {
        User.getInstance().upgradeCPS(upgrade.getCps());
        User.getInstance().getUpgradesIds().add(upgrade.getName());
        instance.db.collection(collectionUsers).document(UserManager.getUserId())
                .update("upgradesIds", User.getInstance().getUpgradesIds());
    }
    static public void addUserChallenge(UserChallenge userChallenge) {
        Challenge challenge = LocalData.getCurrentChallengeFromId(userChallenge.getChallengeId());
        LocalData.currentChallenges.remove(challenge);
        LocalData.activeChallenges.add(challenge);
        instance.db.collection(collectionUserChallenges).add(userChallenge)
                .addOnSuccessListener(documentReference -> {
                    String userChallengeId = documentReference.getId();
                    documentReference.update("id", userChallengeId).addOnSuccessListener(v -> {
                        User.getInstance().getUserChallengesIds().add(userChallengeId);
                        instance.db.collection(collectionUsers).document(UserManager.getUserId())
                                .update("userChallengesIds", User.getInstance().getUserChallengesIds());
                    });
                });
    }

    static public void getAllUserChallenges(GeneralCallback generalCallback) {
        if (User.getInstance().getUserChallengesIds().isEmpty()) {
            generalCallback.failed(null);
            return;
        }
        instance.db.collection(collectionUserChallenges)
                .whereIn(FieldPath.documentId(), User.getInstance().getUserChallengesIds())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<UserChallenge> userChallenges = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult())
                            userChallenges.add(document.toObject((UserChallenge.class)));
                        generalCallback.success(userChallenges);
                    }
                    else
                        generalCallback.failed(null);
                });
    }

    static public void addBoost(Boost boost) {
        instance.db.collection(collectionBoosts).document(boost.getName()).set(boost);
    }
    static public void addPlant(Plant plant) {
        instance.db.collection(collectionPlants).document(plant.getName()).set(plant);
    }
    static public void addUpgrade(Upgrade upgrade) {
        instance.db.collection(collectionUpgrades).document(upgrade.getName()).set(upgrade);
    }
    static public void addChallenge(Challenge challenge) {
        instance.db.collection(collectionChallenges).document(challenge.getTitle()).set(challenge);
    }

    static public void getAllBoosts(GeneralCallback generalCallback) {
        getAllItems(generalCallback, collectionBoosts, Boost.class);
    }
    static public void getAllPlants(GeneralCallback generalCallback) {
        getAllItems(generalCallback, collectionPlants, Plant.class);
    }
    static public void getAllUpgrades(GeneralCallback generalCallback) {
        getAllItems(generalCallback, collectionUpgrades, Upgrade.class);
    }
    static public void getAllChallenges(GeneralCallback generalCallback) {
        getAllItems(generalCallback, collectionChallenges, Challenge.class);
    }
    static public void getTopUsers(int topAmount, GeneralCallback generalCallback) {
        instance.db.collection(collectionUsers)
                .orderBy("coins", Query.Direction.DESCENDING)
                .limit(topAmount)
                .get()
                .addOnCompleteListener(task -> {
                    returnResult(task, generalCallback, User.class);
                });
    }
    static public void getUserRank(GeneralCallback generalCallback) {
        instance.db.collection(collectionUsers)
                .orderBy("coins", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int rank = 0;
                        boolean found = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            rank++;
                            if (document.getId().equals(UserManager.getUserId())) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                           generalCallback.success(rank);
                        }
                        else
                            generalCallback.failed(0);
                    }
                    else
                        generalCallback.failed(0);
                });
    }

    static public void getPlantsFromList(Boolean whereInList, ArrayList<String> plantsIds, GeneralCallback generalCallback) {
        if (whereInList)
            getObjectsFromList(plantsIds, generalCallback, collectionPlants, Plant.class);
        else
            getObjectsNotFromList(plantsIds, generalCallback, collectionPlants, Plant.class);
    }
    static public void getUserPlantsFromList(Boolean whereInList, ArrayList<String> userPlantsIds, GeneralCallback generalCallback) {
        if (whereInList)
            getObjectsFromList(userPlantsIds, generalCallback, collectionUserPlants, UserPlant.class);
        else
            getObjectsNotFromList(userPlantsIds, generalCallback, collectionUserPlants, UserPlant.class);
    }
    static public void getChallengesFromList(Boolean whereInList, ArrayList<String> challengesIds, GeneralCallback generalCallback) {
        if (whereInList)
            getObjectsFromList(challengesIds, generalCallback, collectionChallenges, Challenge.class);
        else
            getObjectsNotFromList(challengesIds, generalCallback, collectionChallenges, Challenge.class);
    }
    static public void getUpgradesFromList(Boolean whereInList, ArrayList<String> upgradesIds, GeneralCallback generalCallback) {
        if (whereInList)
            getObjectsFromList(upgradesIds, generalCallback, collectionUpgrades, Upgrade.class);
        else
            getObjectsNotFromList(upgradesIds, generalCallback, collectionUpgrades, Upgrade.class);
    }
    static public void getUserBoostsFromList(Boolean whereInList, ArrayList<String> userBoostsIds, GeneralCallback generalCallback) {
        if (whereInList)
            getObjectsFromList(userBoostsIds, generalCallback, collectionUserBoosts, UserBoost.class);
        else
            getObjectsNotFromList(userBoostsIds, generalCallback, collectionUserBoosts, UserBoost.class);
    }
    static public void getBoostsFromList(Boolean whereInList, ArrayList<String> boostsIds, GeneralCallback generalCallback) {
        if (whereInList)
            getObjectsFromList(boostsIds, generalCallback, collectionBoosts, Boost.class);
        else
            getObjectsNotFromList(boostsIds, generalCallback, collectionBoosts, Boost.class);
    }

    static private void getObjectsFromList(ArrayList<String> objectsIds, GeneralCallback generalCallback, String collection, Class c) {
        if (objectsIds.isEmpty()) {
            generalCallback.success(new ArrayList<Object>());
            return;
        }
        instance.db.collection(collection)
            .whereIn(FieldPath.documentId(), objectsIds)
            .get().addOnCompleteListener(task -> {
                    returnResult(task, generalCallback, c);
            });
    }
    static private void getObjectsNotFromList(ArrayList<String> objectsIds, GeneralCallback generalCallback, String collection, Class c) {
        if (objectsIds.isEmpty()) {
            getAllItems(generalCallback, collection, c);
            return;
        }
        instance.db.collection(collection)
                .whereNotIn(FieldPath.documentId(), objectsIds)
                .get().addOnCompleteListener(task -> {
                    returnResult(task, generalCallback, c);
                });
    }
    static private void getAllItems(GeneralCallback generalCallback, String collection, Class c) {
        instance.db.collection(collection).get().addOnCompleteListener(task -> {
            returnResult(task, generalCallback, c);
        });
    }

    static private void returnResult(Task<QuerySnapshot> task, GeneralCallback generalCallback, Class c) {
        if (task.isSuccessful()) {
            ArrayList<Object> objects = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult())
                objects.add(document.toObject(c));
            generalCallback.success(objects);
        }
        else
            generalCallback.failed(null);
    }
}
