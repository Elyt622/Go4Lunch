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
import com.elytevolution.go4lunch.model.User;
import com.elytevolution.go4lunch.view.activity.DetailRestaurantActivity;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipationCollection;

public class WorkmateListAdapter extends RecyclerView.Adapter<WorkmateListAdapter.RecyclerViewHolder> {

    private final List<User> users;

    private String namePlace, messageJoin, messageNoChoice;

    public WorkmateListAdapter(@Nullable List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public WorkmateListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        messageJoin = view.getResources().getString(R.string.restaurant_message_join);
        messageNoChoice = view.getResources().getString(R.string.restaurant_message_no_choice);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateListAdapter.RecyclerViewHolder holder, int position) {
        if (users != null) {
            getParticipationCollection().whereArrayContains("uid", users.get(position).getUid()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        namePlace = document.getString("namePlace");
                        String messageToPrint = (users.get(position).getDisplayName() + " " + messageJoin + " " + namePlace);
                        holder.textViewUser.setText(messageToPrint);
                    }
                    if(namePlace == null){
                        String messageToPrint = (users.get(position).getDisplayName() + " " + messageNoChoice);
                        holder.textViewUser.setText(messageToPrint);
                    }
                    namePlace=null;
                }
            });
            Glide.with(holder.itemView).load(users.get(position).getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewUser);
        }

        holder.constraintLayout.setOnClickListener(v -> {
            if (users != null && users.get(position).getIdPlace() != null && !users.get(position).getIdPlace().isEmpty()) {
                Intent intent = new Intent(v.getContext(), DetailRestaurantActivity.class);
                intent.putExtra("ID", users.get(position).getIdPlace());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
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
