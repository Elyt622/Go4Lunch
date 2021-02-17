package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.User;
import com.elytevolution.go4lunch.presenter.WorkmatePresenter;
import com.elytevolution.go4lunch.view.adapter.WorkmateAdapter;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class WorkmateFragment extends Fragment implements WorkmatePresenter.View{

    private final List<User> users = new ArrayList<>();

    private WorkmateAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseUser currentUser;

    private WorkmatePresenter presenter;

    public WorkmateFragment() { }

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

        presenter = new WorkmatePresenter(this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_users_workmates);
        swipeRefreshLayout = view.findViewById(R.id.swipe_workmate_fragment);

        configureAdapter(recyclerView);

        currentUser = presenter.getCurrentUser();

        presenter.clearListUser(users);
        presenter.getAllUsers(currentUser, users);
        configureSwipeRefreshLayout();
        return view;
    }

    private void configureAdapter(RecyclerView recyclerView){
        adapter = new WorkmateAdapter(users);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.clearListUser(users);
            presenter.getAllUsers(currentUser, users);
        });
    }

    @Override
    public void updateUI() {
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}