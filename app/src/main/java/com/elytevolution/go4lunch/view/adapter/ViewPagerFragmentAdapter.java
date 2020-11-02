package com.elytevolution.go4lunch.view.adapter;

import com.elytevolution.go4lunch.view.fragment.ListFragment;
import com.elytevolution.go4lunch.view.fragment.MapFragment;
import com.elytevolution.go4lunch.view.fragment.WorkmateFragment;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private FragmentManager fm;

    private LatLng location;

    public ViewPagerFragmentAdapter(LatLng location, @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.fm = fragmentManager;
        this.location = location;
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
                return MapFragment.newInstance(location);
            case 1:
                return ListFragment.newInstance(location);
            default:
                return WorkmateFragment.newInstance();
        }
    }
}

