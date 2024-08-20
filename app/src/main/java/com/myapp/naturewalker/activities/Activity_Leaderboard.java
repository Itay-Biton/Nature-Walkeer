package com.myapp.naturewalker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.naturewalker.R;
import com.myapp.naturewalker.adapters.UserAdapter;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.utils.DataManager;

import java.util.ArrayList;

public class Activity_Leaderboard extends AppCompatActivity {
    private AppCompatImageButton BTN_back;
    private TextView TXT_title, TXT_user_rank;
    private RecyclerView leaderboard_recyclerview;

    private int rank = 0;
    private ArrayList<User> topUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        findViews();
        setListeners();

        getData();
    }

    private void findViews() {
        BTN_back = findViewById(R.id.BTN_back);

        TXT_title = findViewById(R.id.TXT_title);
        TXT_title.setText(getString(R.string.leaderboard));

        TXT_user_rank = findViewById(R.id.TXT_user_rank);

        leaderboard_recyclerview = findViewById(R.id.leaderboard_recyclerview);
        leaderboard_recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners() {
        BTN_back.setOnClickListener(v -> gotoDashboard());
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private void updateUI() {
        leaderboard_recyclerview.swapAdapter(new UserAdapter(topUsers), true);
        TXT_user_rank.setText("Your are ranked " + rank + " worldwide");
    }

    private void getData() {
        DataManager.getUserRank(new GeneralCallback() {
            @Override
            public void success(Object o) {
                rank = (int) o;
                updateUI();
            }
            @Override
            public void failed(Object o) {
                rank = 0;
            }
        });

        DataManager.getTopUsers(20, new GeneralCallback() {
            @Override
            public void success(Object o) {
                topUsers = (ArrayList<User>) o;
                updateUI();
            }
            @Override
            public void failed(Object o) {}
        });
    }
}