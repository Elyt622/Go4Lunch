package com.elytevolution.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailRestaurantListAdapter extends RecyclerView.Adapter<DetailRestaurantListAdapter.RecyclerViewHolder> {

    private List<User> users;

    public DetailRestaurantListAdapter(List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_restaurant, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textViewUser.setText(users.get(position).getDisplayName()+" is joining!");

        Glide.with(holder.itemView).load(users.get(position).getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewUser);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewUser;

        TextView textViewUser;
        public RecyclerViewHolder(View view) {
            super(view);
            imageViewUser = view.findViewById(R.id.image_view_picture_recycler_detail_restaurant);
            textViewUser = view.findViewById(R.id.text_view_recycler_detail_restaurant);
        }
    }
}
