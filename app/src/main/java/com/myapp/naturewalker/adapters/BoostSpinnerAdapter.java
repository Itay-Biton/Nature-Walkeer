package com.myapp.naturewalker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myapp.naturewalker.R;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Boost;
import com.myapp.naturewalker.utils.DataManager;

import java.util.ArrayList;

public class BoostSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Boost> items;

    public BoostSpinnerAdapter(Context context, ArrayList<Boost> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);

        convertView.findViewById(R.id.itemDescription).setVisibility(View.GONE);

        if (position == -1) {
            convertView.findViewById(R.id.itemName).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.itemIcon).setVisibility(View.INVISIBLE);
            return convertView;
        }
        else {
            TextView itemName = convertView.findViewById(R.id.itemName);
            itemName.setText(items.get(position).getName());
            itemName.setTextSize(10f);

            ImageView itemIcon = convertView.findViewById(R.id.itemIcon);
            DataManager.getIconURL(items.get(position).getName(), new GeneralCallback() {
                @Override
                public void success(Object o) {
                    Glide.with(parent)
                            .load((String) o)
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile)
                            .fitCenter()
                            .into(itemIcon);
                }
                @Override
                public void failed(Object o) {}
            });

            return convertView;
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemDescription = convertView.findViewById(R.id.itemDescription);
        ImageView itemIcon = convertView.findViewById(R.id.itemIcon);
        itemName.setText(items.get(position).getName());
        itemDescription.setText(items.get(position).generateDescription());
        itemIcon.setMinimumWidth(48);
        itemIcon.setMinimumHeight(48);

        DataManager.getIconURL(items.get(position).getName(), new GeneralCallback() {
            @Override
            public void success(Object o) {
                Glide.with(parent)
                        .load((String) o)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .fitCenter()
                        .into(itemIcon);
            }
            @Override
            public void failed(Object o) {}
        });
        return convertView;
    }
}
