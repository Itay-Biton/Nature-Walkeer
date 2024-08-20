package com.myapp.naturewalker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.naturewalker.R;
import com.myapp.naturewalker.adapters.ChallengeAdapter;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Boost;
import com.myapp.naturewalker.objects.Challenge;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserChallenge;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.LocalData;
import com.myapp.naturewalker.utils.Status;

import java.util.ArrayList;
import java.util.List;

public class Activity_Challenges extends AppCompatActivity {
    private AppCompatImageButton BTN_back;
    private TextView TXT_title;
    private RecyclerView activeChallengesRecyclerView, challengeYourselfRecyclerView, pastChallengesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        findViews();
        setListeners();
        getData();
    }

    private void findViews() {
        BTN_back = findViewById(R.id.BTN_back);

        TXT_title = findViewById(R.id.TXT_title);
        TXT_title.setText(getString(R.string.challenges));

        activeChallengesRecyclerView = findViewById(R.id.active_challenges_recyclerview);
        challengeYourselfRecyclerView = findViewById(R.id.challenge_yourself_recyclerview);
        pastChallengesRecyclerView = findViewById(R.id.past_challenges_recyclerview);

        activeChallengesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        challengeYourselfRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pastChallengesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        swapAdapters();
    }

    private void setListeners() {
        BTN_back.setOnClickListener(v -> gotoDashboard());
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private void swapAdapters() {
        activeChallengesRecyclerView.swapAdapter(new ChallengeAdapter(LocalData.activeChallenges, Status.ACTIVE, null), true);
        challengeYourselfRecyclerView.swapAdapter(new ChallengeAdapter(LocalData.currentChallenges, Status.NULL, new GeneralCallback() {
            @Override
            public void success(Object o) {
                Toast.makeText(Activity_Challenges.this, "Success", Toast.LENGTH_SHORT).show();
                swapAdapters();
                getData();
            }
            @Override
            public void failed(Object o) {
                Toast.makeText(Activity_Challenges.this, "Already have an active challenge", Toast.LENGTH_SHORT).show();
            }
        }), true);
        pastChallengesRecyclerView.swapAdapter(new ChallengeAdapter(LocalData.pastChallenges, Status.DONE, null), true);
    }

    private void getData() {
        ArrayList<String> activeChallengesIds = new ArrayList<>();
        ArrayList<String> pastChallengesIds = new ArrayList<>();
        DataManager.getAllUserChallenges(new GeneralCallback() {
            @Override
            public void success(Object o) {
                LocalData.currentChallenges.clear();
                LocalData.activeUserChallenges.clear();
                LocalData.activeChallenges.clear();
                LocalData.pastChallenges.clear();

                ArrayList<UserChallenge> userChallenges = (ArrayList<UserChallenge>) o;
                for (UserChallenge userChallenge : userChallenges) {
                    if (userChallenge.getStatus() == Status.ACTIVE) {
                        LocalData.activeUserChallenges.add(userChallenge);
                        activeChallengesIds.add(userChallenge.getChallengeId());
                    }
                    else if (userChallenge.getStatus() == Status.DONE)
                        pastChallengesIds.add(userChallenge.getChallengeId());
                }
                final int[] callbackCount = {2};
                DataManager.getChallengesFromList(true, activeChallengesIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.activeChallenges = (ArrayList<Challenge>) o;
                        callbackCount[0]--;
                        if (callbackCount[0] == 0)
                            thirdCallback();
                    }
                    @Override
                    public void failed(Object o) {}
                });
                DataManager.getChallengesFromList(true, pastChallengesIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.pastChallenges = (ArrayList<Challenge>) o;
                        callbackCount[0]--;
                        if (callbackCount[0] == 0)
                            thirdCallback();
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }
            @Override
            public void failed(Object o) {
                LocalData.currentChallenges.clear();

                DataManager.getAllChallenges(new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        for (Object item : (ArrayList<Object>) o)
                            LocalData.currentChallenges.add((Challenge) item);
                        swapAdapters();
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }

            private void thirdCallback() {
                ArrayList<String> currentChallengesIds = activeChallengesIds;
                currentChallengesIds.addAll(pastChallengesIds);
                DataManager.getChallengesFromList(false, currentChallengesIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.currentChallenges = (ArrayList<Challenge>) o;
                        swapAdapters();
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }
        });
    }
}