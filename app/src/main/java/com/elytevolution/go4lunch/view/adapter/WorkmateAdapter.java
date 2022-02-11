package com.elytevolution.go4lunch.view.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.presenter.WorkmatePresenter;
import com.elytevolution.go4lunch.view.activity.DetailsActivity;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import static com.elytevolution.go4lunch.firestorerequest.ParticipationHelper.getParticipationCollection;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateAdapter.RecyclerViewHolder> {

    private final WorkmatePresenter presenter;

    private String textWantToJoin, textNoDecision;

    private int textColorNoDecision;

    public WorkmateAdapter(WorkmatePresenter presenter){
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public WorkmateAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        textWantToJoin = parent.getResources().getString(R.string.restaurant_message_join);
        textNoDecision = parent.getResources().getString(R.string.restaurant_message_no_choice);
        textColorNoDecision = parent.getResources().getColor(R.color.quantum_grey400);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateAdapter.RecyclerViewHolder holder, int position) {

        getParticipationCollection().whereArrayContains("uid", presenter.getUserId(position)).get().addOnCompleteListener(task -> {
            if (presenter.getPlaceId(position) != null && !presenter.getPlaceId(position).isEmpty()) {
                String namePlace;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        namePlace = document.getString("namePlace");
                        holder.textViewUser.setText(presenter.getUserName(position)+ " " + textWantToJoin + " " + namePlace);
                    }
                }
            }
            else {
                holder.textViewUser.setText(presenter.getUserName(position) + " " + textNoDecision + " ");
                holder.textViewUser.setTextColor(textColorNoDecision);
            }
        });

        Glide.with(holder.itemView).load(presenter.getUrlPicture(position)).apply(RequestOptions.circleCropTransform()).into(holder.imageViewUser);

        holder.constraintLayout.setOnClickListener(v -> {
            if (presenter.getPlaceId(position) != null && !presenter.getPlaceId(position).isEmpty()) {
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra("ID", presenter.getPlaceId(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return presenter.getUserListSize();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewUser;
        private final TextView textViewUser;
        private final ConstraintLayout constraintLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.text_view_user_choose_workmates);
            imageViewUser = itemView.findViewById(R.id.image_view_picture_workmates);
            constraintLayout = itemView.findViewById(R.id.constraint_layout_user_workmate_list);
        }
    }
}
