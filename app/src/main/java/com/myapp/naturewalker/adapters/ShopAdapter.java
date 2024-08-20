package com.myapp.naturewalker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapp.naturewalker.R;
import com.myapp.naturewalker.abstracts.ShopItem;
import com.myapp.naturewalker.activities.Activity_Store;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Boost;
import com.myapp.naturewalker.objects.Plant;
import com.myapp.naturewalker.objects.Upgrade;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserBoost;
import com.myapp.naturewalker.user_objects.UserPlant;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.Status;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<ShopItem> items;
    private GeneralCallback generalCallback;

    public ShopAdapter(List<ShopItem> items, GeneralCallback generalCallback) {
        this.items = items;
        this.generalCallback = generalCallback;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        TextView itemDescription;
        AppCompatImageButton BTN_purchase;
        ImageView itemIcon;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            BTN_purchase = itemView.findViewById(R.id.BTN_purchase);
            itemIcon = itemView.findViewById(R.id.itemIcon);
        }
    }

    @NonNull
    @Override
    public ShopAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopItem item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.valueOf(item.getPrice()));
        holder.itemDescription.setText(item.generateDescription());

        DataManager.getIconURL(item.getName(), new GeneralCallback() {
            @Override
            public void success(Object o) {
                Glide.with(holder.itemView)
                        .load((String) o)
                        .placeholder(R.drawable.loading_gif)
                        .fitCenter()
                        .into(holder.itemIcon);
            }
            @Override
            public void failed(Object o) {}
        });

        holder.BTN_purchase.setOnClickListener(view -> {
            if (item.getPrice() > User.getInstance().getCoins()) {
                generalCallback.failed(null);
                return;
            }
            User.getInstance().pay(item.getPrice());
            generalCallback.success(null);
            if (item.getClass() == Boost.class) {
                DataManager.addUserBoost(new UserBoost(item.getName(), Status.OWNED));
            }
            else if (item.getClass() == Upgrade.class) {
                DataManager.addUserUpgrade((Upgrade) item);
            }
            else if (item.getClass() == Plant.class) {
                DataManager.addUserPlant(new UserPlant(item.getName()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
