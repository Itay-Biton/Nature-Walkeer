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
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.utils.LocalData;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView TXT_rank, TXT_name, TXT_coins;
        ImageView IMG_profile;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            TXT_rank = itemView.findViewById(R.id.TXT_rank);
            TXT_name = itemView.findViewById(R.id.TXT_name);
            TXT_coins = itemView.findViewById(R.id.TXT_coins);
            IMG_profile = itemView.findViewById(R.id.IMG_profile);
        }
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.TXT_rank.setText(String.valueOf(position+1));
        holder.TXT_name.setText(user.getEmail().split("@")[0]);
        holder.TXT_coins.setText(String.valueOf(user.getCoins()));
        Glide.with(holder.itemView)
                .load(user.getImgURL())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .fitCenter()
                .into(holder.IMG_profile);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
