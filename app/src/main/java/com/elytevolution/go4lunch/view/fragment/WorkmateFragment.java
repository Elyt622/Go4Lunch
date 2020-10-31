package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.view.adapter.WorkmateListAdapter;

public class WorkmateFragment extends Fragment {

    private RecyclerView recyclerView;

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
        recyclerView = view.findViewById(R.id.recycler_view_users_workmates);
        return view;
    }
}