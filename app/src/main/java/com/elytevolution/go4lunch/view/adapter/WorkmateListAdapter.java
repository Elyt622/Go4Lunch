package com.elytevolution.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WorkmateListAdapter extends RecyclerView.Adapter<WorkmateListAdapter.RecyclerViewHolder> {

    public WorkmateListAdapter(@Nullable List<User> users){

    }

    @NonNull
    @Override
    public WorkmateListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateListAdapter.RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
