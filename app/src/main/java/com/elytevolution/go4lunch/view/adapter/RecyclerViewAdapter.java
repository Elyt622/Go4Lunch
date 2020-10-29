package com.elytevolution.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

        private List<NearBySearch.Results> results;

        private LatLng myLocation;

        public RecyclerViewAdapter(List<NearBySearch.Results> results, LatLng location){
            myLocation = location;
            this.results = results;
        }

    @NonNull
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        if(results.get(position).getName().length() > 25){
            String phrase = results.get(position).getName().substring(0, 25)+"...";
            holder.textViewName.setText(phrase);
        }
        else {
            holder.textViewName.setText(results.get(position).getName());

        }

        if(results.get(position).getVicinity().length() > 30){
            String phrase = results.get(position).getVicinity().substring(0, 30)+"...";
            holder.textViewAddress.setText(phrase);
        }else {
            holder.textViewAddress.setText(results.get(position).getVicinity());
        }

        if(results.get(position).getPhotos().size() > 0) {
            String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    +results.get(position).getPhotos().get(0).getPhoto_reference()
                    +"&key=AIzaSyBAzeJeEsP2gNXjE_7XYMaywZECaJvmQAg";
            Glide.with(holder.itemView.getContext()).load(imgUrl).centerCrop().into(holder.imageViewRestaurant);
        }

        if (results.get(position).getOpening_hours() != null) {
            if (results.get(position).getOpening_hours().getOpen_now()) {
                holder.textViewOpen.setText("Currently open");
                holder.textViewOpen.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.open));
            } else {
                holder.textViewOpen.setText("Currently close");
                holder.textViewOpen.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.close));
            }
        }

        holder.imageViewVote.setImageResource(R.drawable.baseline_person_black_18);

        if(results.get(position).getRating() > 3){
            if (results.get(position).getRating() < 4){
                holder.imageViewRate1.setVisibility(View.VISIBLE);
            }
            if(results.get(position).getRating() < 4.5 && results.get(position).getRating() >= 4){
                holder.imageViewRate1.setVisibility(View.VISIBLE);
                holder.imageViewRate2.setVisibility(View.VISIBLE);
            }
            if(results.get(position).getRating() >= 4.5){
                holder.imageViewRate1.setVisibility(View.VISIBLE);
                holder.imageViewRate2.setVisibility(View.VISIBLE);
                holder.imageViewRate3.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.imageViewRate1.setVisibility(View.INVISIBLE);
            holder.imageViewRate2.setVisibility(View.INVISIBLE);
            holder.imageViewRate3.setVisibility(View.INVISIBLE);
        }

        int distance = (int) (distFrom(myLocation.latitude, myLocation.longitude, results.get(position).getGeometry().getLocation().getLat(), results.get(position).getGeometry().getLocation().getLng())*1000);
        holder.textViewDistance.setText(distance+"m");

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        return results.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName, textViewAddress, textViewOpen, textViewDistance, textViewVote;
        private ImageView imageViewRestaurant, imageViewVote, imageViewRate1, imageViewRate2, imageViewRate3;
        private ConstraintLayout constraintLayout;

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

            constraintLayout = itemView.findViewById(R.id.constraint_layout_item);
        }
    }
}
