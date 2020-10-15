package com.elytevolution.go4lunch.view.adapter;

import com.elytevolution.go4lunch.view.fragment.ListFragment;
import com.elytevolution.go4lunch.view.fragment.MapsFragment;
import com.elytevolution.go4lunch.view.fragment.WorkmatesFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private FragmentManager fm;

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.fm = fragmentManager;
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
                return ListFragment.newInstance();
            default:
                return WorkmatesFragment.newInstance();
        }
    }
}

