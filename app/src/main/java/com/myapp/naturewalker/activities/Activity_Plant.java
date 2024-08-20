package com.myapp.naturewalker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapp.naturewalker.R;
import com.myapp.naturewalker.adapters.PlantsAdapter;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.custom_views.CircularProgressView;
import com.myapp.naturewalker.objects.Plant;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserPlant;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.Formater;
import com.myapp.naturewalker.utils.LocalData;
import com.myapp.naturewalker.utils.Status;

import java.util.ArrayList;

public class Activity_Plant extends AppCompatActivity {
    private AppCompatImageButton BTN_back;
    private Button BTN_harvest;
    private TextView TXT_title, TXT_harvest;
    private TextView countdownTimerTextView, TXT_plant_name;
    private CircularProgressView plantProgress;
    private RecyclerView my_plants_recyclerview;
    private ImageView IMG_plant_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        findViews();
        setListeners();
        updateUI();

        getData();
    }

    private void findViews() {
        BTN_back = findViewById(R.id.BTN_back);

        TXT_title = findViewById(R.id.TXT_title);
        TXT_title.setText(getString(R.string.plant));

        plantProgress = findViewById(R.id.plantProgress);
        plantProgress.setColor(ContextCompat.getColor(this, R.color.dark_green));
        plantProgress.setStrokeWidth(40f);
        plantProgress.setStartAngle(-225);
        plantProgress.setSweepAngle(270);

        IMG_plant_img = findViewById(R.id.IMG_plant_img);
        TXT_plant_name = findViewById(R.id.TXT_plant_name);
        countdownTimerTextView = findViewById(R.id.countdownTimer);

        BTN_harvest = findViewById(R.id.BTN_harvest);
        TXT_harvest = findViewById(R.id.TXT_harvest);

        my_plants_recyclerview = findViewById(R.id.my_plants_recyclerview);
        my_plants_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        swapAdapter();
    }

    private void setListeners() {
        BTN_back.setOnClickListener(v -> gotoDashboard());

        BTN_harvest.setOnClickListener(v -> {
            LocalData.checkAndUpdatePlantStatus(new GeneralCallback() {
                @Override
                public void success(Object o) {
                    updateUI();
                }
                @Override
                public void failed(Object o) {
                    if (o == Status.ACTIVE)
                        Toast.makeText(Activity_Plant.this, "Not Ready To Harvest Yet", Toast.LENGTH_SHORT).show();
                    else if (o == Status.NULL)
                        Toast.makeText(Activity_Plant.this, "No Plant To Harvest", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private void updateUI() {
        plantProgress.setProgress(Formater.getPlantProgress());

        TXT_harvest.setText(Formater.getPlanHarvestDuration());
        countdownTimerTextView.setText(Formater.getPlanHarvestDurationAsClock());
        if (LocalData.activePlant != null) {
            TXT_plant_name.setText(LocalData.activePlant.getName());
            DataManager.getIconURL(LocalData.activePlant.getName(), new GeneralCallback() {
                @Override
                public void success(Object o) {
                    Glide.with(Activity_Plant.this)
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
            Glide.with(Activity_Plant.this)
                    .load(R.drawable.ic_plant)
                    .fitCenter()
                    .into(IMG_plant_img);
        }
    }

    private void swapAdapter() {
        my_plants_recyclerview.swapAdapter(new PlantsAdapter(LocalData.plants, new GeneralCallback() {
            @Override
            public void success(Object o) {
                LocalData.setActivePlant(LocalData.getUserPlantFromPlant((Plant) o));
                swapAdapter();
                updateUI();
            }

            @Override
            public void failed(Object o) {
                Toast.makeText(Activity_Plant.this, "Something is already planted", Toast.LENGTH_SHORT).show();
            }
        }), true);
    }

    private void getData() {
        DataManager.getUserPlantsFromList(true, User.getInstance().getUserPlantsIds(), new GeneralCallback() {
            @Override
            public void success(Object o) {
                ArrayList<UserPlant> userPlants = (ArrayList<UserPlant>) o;
                ArrayList<String> plantsIds = new ArrayList<>();
                LocalData.userPlants.clear();
                for (UserPlant userPlant : userPlants) {
                    if (userPlant.getStatus() != Status.DONE) {
                        plantsIds.add(userPlant.getPlantId());
                        LocalData.userPlants.add(userPlant);
                    }
                }
                DataManager.getPlantsFromList(true, plantsIds, new GeneralCallback() {
                    @Override
                    public void success(Object o) {
                        LocalData.plants = (ArrayList<Plant>) o;
                        LocalData.extractActivePlant();
                        swapAdapter();
                        updateUI();
                    }
                    @Override
                    public void failed(Object o) {}
                });
            }
            @Override
            public void failed(Object o) {}
        });
    }

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            handler.postDelayed(runnable, 1000);
        }
    };

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