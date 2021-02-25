package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.presenter.WorkmatePresenter;
import com.elytevolution.go4lunch.view.adapter.WorkmateAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class WorkmateFragment extends Fragment implements WorkmatePresenter.View{

    private WorkmateAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private WorkmatePresenter presenter;

    private RecyclerView recyclerView;

    public WorkmateFragment() { }

    public static WorkmateFragment newInstance() {
        return new WorkmateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new WorkmatePresenter(this);
        presenter.onCreate();

        configureAdapter();
        configureSwipeRefreshLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_users_workmates);
        swipeRefreshLayout = view.findViewById(R.id.swipe_workmate_fragment);

        return view;
    }

    private void configureAdapter(){
        adapter = new WorkmateAdapter(presenter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getAllUsers());
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