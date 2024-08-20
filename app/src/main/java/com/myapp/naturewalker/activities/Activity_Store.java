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
import com.myapp.naturewalker.abstracts.ShopItem;
import com.myapp.naturewalker.adapters.ShopAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Boost;
import com.myapp.naturewalker.objects.Plant;
import com.myapp.naturewalker.objects.Upgrade;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class Activity_Store extends AppCompatActivity {
    private AppCompatImageButton BTN_back;
    private TextView TXT_title, CoinsTextView;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNav;

    private List<ShopItem> shopItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        shopItems = new ArrayList<>();
        loadItems(getString(R.string.category_1));

        findViews();
        setListeners();
    }

    private void findViews() {
        BTN_back = findViewById(R.id.BTN_back);

        TXT_title = findViewById(R.id.TXT_title);
        TXT_title.setText(getString(R.string.store));

        CoinsTextView = findViewById(R.id.CoinsTextView);
        updateCoins();

        recyclerView = findViewById(R.id.itemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomNav = findViewById(R.id.bottomNavigation);
    }

    private void setListeners() {
        BTN_back.setOnClickListener(v -> gotoDashboard());

        swapAdapter();

        bottomNav.setOnItemSelectedListener(item -> {
            loadItems(item.getTitle().toString());
            swapAdapter();
            return true;
        });
    }

    private void swapAdapter() {
        recyclerView.swapAdapter(new ShopAdapter(shopItems, new GeneralCallback() {
            @Override
            public void success(Object o) {
                updateCoins();
                loadItems(bottomNav.getMenu().findItem(bottomNav.getSelectedItemId()).getTitle().toString());
                Toast.makeText(Activity_Store.this, "Success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void failed(Object o) {
                Toast.makeText(Activity_Store.this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
            }
        }), true);
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private boolean loadItems(String category) {
        shopItems.clear();
        if (category.equals(getString(R.string.category_1)))
            DataManager.getAllBoosts(new GeneralCallback() {
                @Override
                public void success(Object o) {
                    for (Object item : (ArrayList<Object>) o)
                        shopItems.add((Boost) item);
                    swapAdapter();
                }
                @Override
                public void failed(Object o) {}
            });
        else if (category.equals(getString(R.string.category_2)))
            DataManager.getUpgradesFromList(false, User.getInstance().getUpgradesIds(), new GeneralCallback() {
                @Override
                public void success(Object o) {
                    for (Object item : (ArrayList<Object>) o)
                        shopItems.add((Upgrade) item);
                    swapAdapter();
                }
                @Override
                public void failed(Object o) {}
            });
        else if (category.equals(getString(R.string.category_3)))
            DataManager.getAllPlants(new GeneralCallback() {
                @Override
                public void success(Object o) {
                    for (Object item : (ArrayList<Object>) o)
                        shopItems.add((Plant) item);
                    swapAdapter();
                }
                @Override
                public void failed(Object o) {}
            });
        return true;
    }

    public void updateCoins() {
        CoinsTextView.setText(""+User.getInstance().getCoins());
    }
}