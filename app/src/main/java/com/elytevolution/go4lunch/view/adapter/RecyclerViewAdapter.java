package com.elytevolution.go4lunch.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.Restaurant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
        List<Restaurant> list;

        public RecyclerViewAdapter(List<Restaurant> list1){
            list = list1;
        }

    @NonNull
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textViewName.setText(list.get(position).getName());
        holder.textViewDistance.setText(String.valueOf(list.get(position).getDistance()));
        holder.textViewAddress.setText(list.get(position).getAddress());
        holder.textViewOpen.setText(String.valueOf(list.get(position).getOpen()));
        holder.textViewVote.setText(String.valueOf(list.get(position).getChoiceRestaurantForUser().size()));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName, textViewAddress, textViewOpen, textViewDistance, textViewVote;
        private ImageView imageViewRestaurant, imageViewVote, imageViewRate;
        private ConstraintLayout constraintLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name_restaurant);
            textViewAddress = itemView.findViewById(R.id.text_view_address_type_restaurant);
            textViewOpen = itemView.findViewById(R.id.text_view_open_restaurant);
            textViewDistance = itemView.findViewById(R.id.text_view_distance_restaurant);
            textViewVote = itemView.findViewById(R.id.text_view_vote_restaurant);
            constraintLayout = itemView.findViewById(R.id.constraint_layout_item);
        }
    }
}
