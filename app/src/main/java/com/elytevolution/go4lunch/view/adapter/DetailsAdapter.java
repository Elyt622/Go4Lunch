package com.elytevolution.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.presenter.DetailsPresenter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.RecyclerViewHolder> {

    private String joiningToPrint;

    private final DetailsPresenter presenter;

    public DetailsAdapter(DetailsPresenter presenter){ this.presenter = presenter; }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_restaurant, parent, false);

        joiningToPrint = parent.getResources().getString(R.string.is_joining);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.textViewUser.setText(presenter.showUserIsJoining(position, joiningToPrint));

        Glide.with(holder.itemView).load(presenter.getUrlPicture(position)).apply(RequestOptions.circleCropTransform()).into(holder.imageViewUser);
    }

    @Override
    public int getItemCount() {
        return presenter.getListParticipantSize();
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
