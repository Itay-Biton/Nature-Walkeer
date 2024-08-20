package com.myapp.naturewalker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapp.naturewalker.R;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Plant;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.LocalData;

import java.util.List;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.PlantViewHolder> {

    private List<Plant> plants;
    private GeneralCallback generalCallback;

    public PlantsAdapter(List<Plant> plants, GeneralCallback generalCallback) {
        this.plants = plants;
        this.generalCallback = generalCallback;
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView TXT_plant_name, TXT_plant_description;
        AppCompatButton BTN_plant_now;
        ImageView IMG_plant_img;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            TXT_plant_name = itemView.findViewById(R.id.TXT_plant_name);
            TXT_plant_description = itemView.findViewById(R.id.TXT_plant_description);
            BTN_plant_now = itemView.findViewById(R.id.BTN_plant_now);
            IMG_plant_img = itemView.findViewById(R.id.IMG_plant_img);
        }
    }

    @NonNull
    @Override
    public PlantsAdapter.PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_item, parent, false);
        return new PlantsAdapter.PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsAdapter.PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.TXT_plant_name.setText(plant.getName());
        holder.TXT_plant_description.setText(plant.generateDescription());

        DataManager.getIconURL(plant.getName(), new GeneralCallback() {
            @Override
            public void success(Object o) {
                Glide.with(holder.itemView)
                        .load((String) o)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .fitCenter()
                        .into(holder.IMG_plant_img);
            }
            @Override
            public void failed(Object o) {}
        });

        holder.BTN_plant_now.setOnClickListener(v -> {
            if (LocalData.activePlant == null)
                generalCallback.success(plant);
            else
                generalCallback.failed(null);
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }
}
