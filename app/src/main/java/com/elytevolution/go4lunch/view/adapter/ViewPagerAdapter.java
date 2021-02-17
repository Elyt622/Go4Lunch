package com.elytevolution.go4lunch.view.adapter;

import com.elytevolution.go4lunch.view.fragment.ListFragment;
import com.elytevolution.go4lunch.view.fragment.MapFragment;
import com.elytevolution.go4lunch.view.fragment.WorkmateFragment;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final LatLng location;

    public ViewPagerAdapter(LatLng location, @NonNull FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.location = location;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MapFragment.newInstance(location);
            case 1:
                return ListFragment.newInstance(location);
            default:
                return WorkmateFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

