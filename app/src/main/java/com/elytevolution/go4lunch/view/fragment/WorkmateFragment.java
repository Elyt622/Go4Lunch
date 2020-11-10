package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.User;
import com.elytevolution.go4lunch.view.adapter.WorkmateListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;

public class WorkmateFragment extends Fragment {

    private static final String TAG = "WorkmateFragment";

    private List<User> users = new ArrayList<>();

    private WorkmateListAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseUser currentUser;

    public WorkmateFragment() {
        // Required empty public constructor
    }

    public static WorkmateFragment newInstance() {
        return new WorkmateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_users_workmates);
        swipeRefreshLayout = view.findViewById(R.id.swipe_workmate_fragment);

        adapter = new WorkmateListAdapter(users);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getAllUsers();
        configureSwipeRefreshLayout();
        return view;
    }

    private void getAllUsers(){
        getUsersCollection()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(!document.getId().equals(currentUser.getUid()))
                            users.add(new User(document.getString("uid"),
                                    document.getString("firstName"),
                                    document.getString("lastName"),
                                    document.getString("email"),
                                    document.getString("urlPicture")));
                        }
                        updateUI();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            users.clear();
            getAllUsers();
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}