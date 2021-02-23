package com.elytevolution.go4lunch.view.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.presenter.ListPresenter;
import com.elytevolution.go4lunch.view.activity.DetailsActivity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.RecyclerViewHolder> {

        private final ListPresenter presenter;

        public ListAdapter(ListPresenter presenter){
            this.presenter = presenter;
        }

    @NonNull
    @Override
    public ListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        showTextRestaurant(holder.textViewName, presenter.getName(position),25);
        showTextRestaurant(holder.textViewAddress, presenter.getAddress(position), 30);

        showPhotoReference(holder.imageViewRestaurant, presenter.getPhotoReference(position));
        showCurrentOpen(holder.textViewOpen, presenter.getOpenRestaurant(position));

        showParticipants(presenter.getParticipants(position), holder.textViewVote, holder.imageViewVote);

        showStarsRating(presenter.getRating(position), holder.imageViewRate1, holder.imageViewRate2, holder.imageViewRate3);

        int distance = (int) (presenter.distFrom(presenter.getCurrentLocation().latitude, presenter.getCurrentLocation().longitude, presenter.getRestaurantLocation(position).latitude, presenter.getRestaurantLocation(position).longitude)*1000);

        String distanceToPrint = (distance+"m");
        holder.textViewDistance.setText(distanceToPrint);

        holder.constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("ID", presenter.getRestaurantId(position));
            v.getContext().startActivity(intent);
        });
    }

    private void showParticipants(int participants, TextView textViewVote, ImageView imageViewVote){
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

    private void showCurrentOpen(TextView textViewOpen, Boolean isCurrentOpen){
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

    private void showPhotoReference(ImageView imageView, String photoRef){
        if(photoRef != null) {
            String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    +photoRef
                    +"&key="+presenter.getGoogleKey();
            Glide.with(imageView.getContext()).load(imgUrl).centerCrop().into(imageView);
        }else {
            Glide.with(imageView.getContext()).clear(imageView);
        }
    }

    private void showTextRestaurant(TextView textView, String textToPrint, int size){
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

    private void showStarsRating(Double rate, ImageView imageViewRate1, ImageView imageViewRate2, ImageView imageViewRate3){
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

    @Override
    public int getItemCount() {
        return presenter.getSizeRestaurant();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName, textViewAddress, textViewOpen, textViewDistance, textViewVote;
        private final ImageView imageViewRestaurant, imageViewVote, imageViewRate1, imageViewRate2, imageViewRate3;
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
