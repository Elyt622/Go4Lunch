package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.di.DI;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.service.DummyRestaurantApiService;
import com.elytevolution.go4lunch.view.adapter.RecyclerViewAdapter;

import java.util.List;

public class ListFragment extends Fragment {

    private static List<Restaurant> restaurants1;
    private RecyclerView recyclerView;

    private DummyRestaurantApiService apiService = DI.getRestaurantApiService();

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(List<Restaurant> restaurants) {
        restaurants1 = restaurants;
        return (new ListFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_fragment_list);
        recyclerView.setAdapter(new RecyclerViewAdapter(restaurants1));
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}