package com.elytevolution.go4lunch.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipationCollection;
import static com.elytevolution.go4lunch.api.RestaurantHelper.getRestaurantCollection;

public class WorkmateListAdapter extends RecyclerView.Adapter<WorkmateListAdapter.RecyclerViewHolder> {

    private List<User> users;

    private String idPlace, namePlace;

    public WorkmateListAdapter(@Nullable List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public WorkmateListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateListAdapter.RecyclerViewHolder holder, int position) {
        getParticipationCollection().whereArrayContains("uid", users.get(position).getId()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document: task.getResult()){
                    idPlace = document.getString("idPlace");
                    Log.d("TAG", idPlace);
                }
                getRestaurantNameWithId(idPlace, holder.textViewUser, position);
                idPlace = null;
            }
        });
        Glide.with(holder.itemView).load(users.get(position).getUrlPicture()).into(holder.imageViewUser);
    }

    private String getRestaurantNameWithId(String idPlace, TextView textView, int position) {
        if(idPlace != null) {
            getRestaurantCollection().whereEqualTo("idPlace", idPlace).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        namePlace = document.getString("name");
                        textView.setText(users.get(position).getFirstName() + " want to join " + namePlace);
                        namePlace = null;
                    }
                }
            });
        }
        else {
            textView.setText(users.get(position).getFirstName() + " hasn't decided yet!");
        }
        return namePlace;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewUser;
        private TextView textViewUser;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.text_view_user_choose_workmates);
            imageViewUser = itemView.findViewById(R.id.image_view_picture_workmates);
        }
    }
}
