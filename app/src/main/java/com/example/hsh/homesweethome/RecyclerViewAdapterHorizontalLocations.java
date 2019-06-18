package com.example.hsh.homesweethome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapterHorizontalLocations extends RecyclerView.Adapter<RecyclerViewAdapterHorizontalLocations.CategoryViewHolder> {

    private Context mContext;
    private ArrayList<String> mData;

    public RecyclerViewAdapterHorizontalLocations(Context mContext, ArrayList<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.furniture_location_card, parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.location.setText(mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView location;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
        }
    }
}

