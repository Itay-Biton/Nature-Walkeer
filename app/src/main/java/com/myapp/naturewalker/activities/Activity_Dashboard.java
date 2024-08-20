package com.myapp.naturewalker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.myapp.naturewalker.R;
import com.myapp.naturewalker.adapters.BoostSpinnerAdapter;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.custom_views.CircularProgressView;
import com.myapp.naturewalker.objects.Boost;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserBoost;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.Formater;
import com.myapp.naturewalker.utils.LocalData;
import com.myapp.naturewalker.utils.NetworkUtil;
import com.myapp.naturewalker.utils.Status;
import com.myapp.naturewalker.utils.UserManager;

import java.util.ArrayList;

public class Activity_Dashboard extends AppCompatActivity {

    CircularProgressView plantProgress, dailyGoalProgress, weeklyGoalProgress, activeChallengeProgress;
    RelativeLayout BTN_profile, BTN_shop, BTN_sport, BTN_plant, BTN_challenges;
    AppCompatButton BTN_use_boost;
    AppCompatTextView TXT_boost_timer, TXT_plant_name, TXT_cps, TXT_coins, TXT_steps;
    Spinner boost_spinner;
    ImageView IMG_plant_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViews();
        setListeners();
        updateUI();

        loadToLocalData();
    }

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();

            handler.postDelayed(runnable, 1000);
        }
    };

    private void findViews() {
        plantProgress = findViewById(R.id.plantProgress);
        dailyGoalProgress = findViewById(R.id.dailyGoalProgress);
        weeklyGoalProgress = findViewById(R.id.weeklyGoalProgress);
        activeChallengeProgress = findViewById(R.id.activeChallengeProgress);

        TXT_boost_timer = findViewById(R.id.TXT_boost_timer);
        TXT_plant_name = findViewById(R.id.TXT_plant_name);
        TXT_cps = findViewById(R.id.TXT_cps);
        TXT_coins = findViewById(R.id.TXT_coins);
        TXT_steps = findViewById(R.id.TXT_steps);

        IMG_plant_img = findViewById(R.id.IMG_plant_img);

        boost_spinner = findViewById(R.id.boost_spinner);

        plantProgress.setColor(ContextCompat.getColor(this, R.color.dark_green));
        plantProgress.setStrokeWidth(40f);
        plantProgress.setStartAngle(-225);
        plantProgress.setSweepAngle(270);

        dailyGoalProgress.setColor(ContextCompat.getColor(this, R.color.yellow));

        weeklyGoalProgress.setColor(ContextCompat.getColor(this, R.color.orange));

        activeChallengeProgress.setColor(ContextCompat.getColor(this, R.color.blue));

        BTN_profile = findViewById(R.id.BTN_profile);
        BTN_shop = findViewById(R.id.BTN_shop);
        BTN_sport = findViewById(R.id.BTN_sport);
        BTN_plant = findViewById(R.id.BTN_plant);
        BTN_challenges = findViewById(R.id.BTN_challenges);
        BTN_use_boost = findViewById(R.id.BTN_use_boost);
    }

    private void setListeners() {
        BTN_profile.setOnClickListener(v -> {
            showProfileOptions();
        });
        plantProgress.setOnClickListener(v -> {
            gotoPlant();
        });
        BTN_shop.setOnClickListener(v -> {
            gotoShop();
        });
        BTN_sport.setOnClickListener(v -> {
            gotoSport();
        });
        BTN_plant.setOnClickListener(v -> {
            gotoPlant();
        });
        BTN_challenges.setOnClickListener(v -> {
            gotoChallenges();
        });
        BTN_use_boost.setOnClickListener(v -> {
            if (NetworkUtil.isInternetAvailable(this)) {
                LocalData.activateBoost(new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        Toast.makeText(Activity_Dashboard.this, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failed(Object o) {
                        Status status = (Status) o;
                        if (status == Status.ACTIVE)
                            Toast.makeText(Activity_Dashboard.this, "A Boost is Already Active", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Activity_Dashboard.this, "No Ready Boost", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        boost_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Boost selectedBoost = (Boost) parent.getItemAtPosition(position);
                LocalData.setAsReadyBoost(selectedBoost.getName());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showProfileOptions() {
        String[] options = {"Logout", "Profile", "Leaderboard", "Close"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        UserManager.signOut();
                        LocalData.destroy();
                        gotoLogin();
                        break;
                    case 1:
                        gotoProfile();
                        break;
                    case 2:
                        gotoLeaderboard();
                        break;
                    case 3:
                        break;
                }
            }
        });

        builder.show();
    }

    private void gotoProfile() {
        Intent i = new Intent(getApplicationContext(), Activity_Profile.class);
        startActivity(i);
        finish();
    }

    private void gotoLeaderboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Leaderboard.class);
        startActivity(i);
        finish();
    }

    private void gotoLogin() {
        Intent i = new Intent(getApplicationContext(), Activity_Login.class);
        startActivity(i);
        finish();
    }

    private void gotoShop() {
        Intent i = new Intent(getApplicationContext(), Activity_Store.class);
        startActivity(i);
        finish();
    }

    private void gotoSport() {
        Intent i = new Intent(getApplicationContext(), Activity_Sport.class);
        startActivity(i);
        finish();
    }

    private void gotoPlant() {
        Intent i = new Intent(getApplicationContext(), Activity_Plant.class);
        startActivity(i);
        finish();
    }

    private void gotoChallenges() {
        Intent i = new Intent(getApplicationContext(), Activity_Challenges.class);
        startActivity(i);
        finish();
    }

    private void updateUI() {
        TXT_coins.setText(""+User.getInstance().getCoins());
        TXT_steps.setText(""+User.getInstance().getTodaySteps());
        if (LocalData.activeBoost != null)
            TXT_cps.setText(""+User.getInstance().getCps()+" x"+LocalData.activeBoost.getCpsMultiplier());
        else
            TXT_cps.setText(""+User.getInstance().getCps());

        if (LocalData.activeBoost != null) {
            TXT_boost_timer.setText("Boost end in "+Formater.getBoostDurationAsClock());
            TXT_boost_timer.setVisibility(View.VISIBLE);
        }
        else {
            TXT_boost_timer.setText("Boost end in 00:00:00");
            TXT_boost_timer.setVisibility(View.GONE);
        }

        if (LocalData.activePlant != null) {
            TXT_plant_name.setText(LocalData.activePlant.getName());
            DataManager.getIconURL(LocalData.activePlant.getName(), new GeneralCallback() {
                @Override
                public void success(Object o) {
                    Glide.with(Activity_Dashboard.this)
                            .load((String) o)
                            .placeholder(R.drawable.ic_plant)
                            .error(R.drawable.ic_plant)
                            .fitCenter()
                            .into(IMG_plant_img);
                }
                @Override
                public void failed(Object o) {}
            });
        }
        else {
            TXT_plant_name.setText("Plant Something");
            Glide.with(Activity_Dashboard.this)
                    .load(R.drawable.ic_plant)
                    .fitCenter()
                    .into(IMG_plant_img);
        }
        plantProgress.setProgress(Formater.getPlantProgress());
        activeChallengeProgress.setProgress(Formater.getChallengeProgress());
        dailyGoalProgress.setProgress(Formater.getDailyProgress());
        weeklyGoalProgress.setProgress(Formater.getWeeklyProgress());
    }

    private void loadToLocalData() {
        if (NetworkUtil.isInternetAvailable(this)) {
            DataManager.getUserBoostsFromList(true, User.getInstance().getUserBoostsIds(), new GeneralCallback() {
                @Override
                public void success(Object o) {
                    LocalData.userBoosts.clear();
                    for (Object item : (ArrayList<Object>) o)
                        LocalData.userBoosts.add((UserBoost) item);
                    ArrayList<String> boostIds = new ArrayList<>();
                    for (UserBoost userBoosts : LocalData.userBoosts)
                        if (userBoosts.getStatus() == Status.OWNED || userBoosts.getStatus() == Status.READY)
                            boostIds.add(userBoosts.getBoostId());
                    DataManager.getBoostsFromList(true, boostIds, new GeneralCallback() {
                        @Override
                        public void success(Object o) {
                            LocalData.boosts.clear();
                            for (Object item : (ArrayList<Object>) o)
                                LocalData.boosts.add((Boost) item);
                            boost_spinner.setAdapter(new BoostSpinnerAdapter(Activity_Dashboard.this, LocalData.boosts));
                            boost_spinner.setSelection(LocalData.getReadyBoostPosition());
                        }

                        @Override
                        public void failed(Object o) {
                        }
                    });
                }

                @Override
                public void failed(Object o) {
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 1000);
    }
}