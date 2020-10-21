package com.elytevolution.go4lunch.view.adapter;

import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.view.fragment.ListFragment;
import com.elytevolution.go4lunch.view.fragment.MapsFragment;
import com.elytevolution.go4lunch.view.fragment.WorkmatesFragment;
import com.google.android.gms.maps.MapFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private FragmentManager fm;

    private List<Restaurant> restaurants;

    public ViewPagerFragmentAdapter(List<Restaurant> restaurants, @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.fm = fragmentManager;
        this.restaurants = restaurants;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MapsFragment.newInstance();
            case 1:
                return ListFragment.newInstance(restaurants);
            default:
                return WorkmatesFragment.newInstance();
        }
    }
}

