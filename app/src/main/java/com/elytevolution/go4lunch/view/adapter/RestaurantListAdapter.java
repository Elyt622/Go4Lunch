package com.elytevolution.go4lunch.view.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.view.activity.DetailRestaurantActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

    public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RecyclerViewHolder> {

        private final List<Restaurant> restaurants;

        private final LatLng location;

        private final String key;

        public RestaurantListAdapter(List<Restaurant> restaurants, LatLng location, String key){
            this.location = location;
            this.restaurants = restaurants;
            this.key = key;
        }

    @NonNull
    @Override
    public RestaurantListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        printTextRestaurant(holder.textViewName, restaurants.get(position).getName(),25);
        printTextRestaurant(holder.textViewAddress, restaurants.get(position).getAddress(), 30);

        printPhotoRef(holder.imageViewRestaurant, restaurants.get(position).getPhotoRef());
        printCurrentOpen(holder.textViewOpen, restaurants.get(position).isCurrentOpen());

        printParticipants(restaurants.get(position).getParticipation(), holder.textViewVote, holder.imageViewVote);

        printStarsRating(restaurants.get(position).getRating(), holder.imageViewRate1, holder.imageViewRate2, holder.imageViewRate3);

        int distance = (int) (distFrom(location.latitude, location.longitude, restaurants.get(position).getLat(), restaurants.get(position).getLgt())*1000);

        String distanceToPrint = (distance+"m");
        holder.textViewDistance.setText(distanceToPrint);

        holder.constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailRestaurantActivity.class);
            intent.putExtra("ID", restaurants.get(position).getIdPlace());
            v.getContext().startActivity(intent);
        });
    }

    private void printParticipants(int participants, TextView textViewVote, ImageView imageViewVote){
        imageViewVote.setImageResource(R.drawable.baseline_person_black_18);
        if(participants != 0) {
            String participantsToPrint = ("(" +participants+ ")");
            textViewVote.setText(participantsToPrint);
            imageViewVote.setVisibility(View.VISIBLE);
        }
        else {
            textViewVote.setText("");
            imageViewVote.setVisibility(View.INVISIBLE);
        }
    }

    private void printCurrentOpen(TextView textViewOpen, Boolean isCurrentOpen){
        if (isCurrentOpen != null) {
            if (isCurrentOpen) {
                textViewOpen.setText(R.string.currently_open);
                textViewOpen.setTextColor(ContextCompat.getColor(textViewOpen.getContext(), R.color.open));
            } else {
                textViewOpen.setText(R.string.currently_close);
                textViewOpen.setTextColor(ContextCompat.getColor(textViewOpen.getContext(), R.color.close));
            }
        }
        else {
            textViewOpen.setText("");
        }
    }

    private void printPhotoRef(ImageView imageView, String photoRef){
        if(photoRef != null) {
            String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    +photoRef
                    +"&key="+key;
            Glide.with(imageView.getContext()).load(imgUrl).centerCrop().into(imageView);
        }else {
            Glide.with(imageView.getContext()).clear(imageView);
        }
    }

    private void printTextRestaurant(TextView textView, String textToPrint, int size){
            if (textToPrint != null) {
                if (textToPrint.length() > size) {
                    String phrase = textToPrint.substring(0, size) + "...";
                    textView.setText(phrase);
                } else {
                    textView.setText(textToPrint);
                }
            }else{
                textView.setText("");
            }
    }

    private void printStarsRating(Double rate, ImageView imageViewRate1, ImageView imageViewRate2, ImageView imageViewRate3){
        if (rate > 3.0) {
            if (rate < 4.0) {
                imageViewRate1.setVisibility(View.VISIBLE);
                imageViewRate2.setVisibility(View.INVISIBLE);
                imageViewRate3.setVisibility(View.INVISIBLE);
            } else if (rate < 4.5 && rate >= 4.0) {
                imageViewRate1.setVisibility(View.VISIBLE);
                imageViewRate2.setVisibility(View.VISIBLE);
                imageViewRate3.setVisibility(View.INVISIBLE);
            } else if (rate >= 4.5) {
                imageViewRate1.setVisibility(View.VISIBLE);
                imageViewRate2.setVisibility(View.VISIBLE);
                imageViewRate3.setVisibility(View.VISIBLE);
            }
        } else{
            imageViewRate1.setVisibility(View.INVISIBLE);
            imageViewRate2.setVisibility(View.INVISIBLE);
            imageViewRate3.setVisibility(View.INVISIBLE);
        }
    }

    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
            double earthRadius = 3958.75;
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lng2-lng1);
            double sindLat = Math.sin(dLat / 2);
            double sindLng = Math.sin(dLng / 2);
            double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                    * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
        }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewAddress;
        private final TextView textViewOpen;
        private final TextView textViewDistance;
        private final TextView textViewVote;
        private final ImageView imageViewRestaurant;
        private final ImageView imageViewVote;
        private final ImageView imageViewRate1;
        private final ImageView imageViewRate2;
        private final ImageView imageViewRate3;
        private final ConstraintLayout constraintLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRate1 = itemView.findViewById(R.id.image_view_rate_restaurant1);
            imageViewRate2 = itemView.findViewById(R.id.image_view_rate_restaurant2);
            imageViewRate3 = itemView.findViewById(R.id.image_view_rate_restaurant3);
            imageViewVote = itemView.findViewById(R.id.image_view_vote_restaurant);
            imageViewRestaurant = itemView.findViewById(R.id.image_view_restaurant);

            textViewName = itemView.findViewById(R.id.name_restaurant);
            textViewAddress = itemView.findViewById(R.id.text_view_address_type_restaurant);
            textViewOpen = itemView.findViewById(R.id.text_view_open_restaurant);
            textViewDistance = itemView.findViewById(R.id.text_view_distance_restaurant);
            textViewVote = itemView.findViewById(R.id.text_view_vote_restaurant);

            constraintLayout = itemView.findViewById(R.id.constraint_layout_item);

        }
    }
}
