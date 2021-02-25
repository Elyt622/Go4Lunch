package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.presenter.ListPresenter;
import com.elytevolution.go4lunch.view.adapter.ListAdapter;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListFragment extends Fragment implements ListPresenter.View{

    private static final String RADIUS = "600";

    private static final String TYPE = "restaurant";

    private ListAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private final LatLng location;

    private ListPresenter presenter;

    private RecyclerView recyclerView;

    public ListFragment(LatLng location) {
        this.location = location;
     }

    public static ListFragment newInstance(LatLng location) {
        return (new ListFragment(location));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new ListPresenter(this, location, getString(R.string.google_maps_key));
        presenter.onCreate();

        configureAdapter(view, recyclerView, presenter);
        configureSwipeRefreshLayout();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_fragment_list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_fragment_list);

        return view;
    }

    private void configureAdapter(View view, RecyclerView recyclerView, ListPresenter presenter){
        adapter = new ListAdapter(presenter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(() ->
                presenter.executeHttpRequestWithRetrofit(getString(R.string.google_maps_key), location, RADIUS, TYPE));
    }

    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
