package com.example.speedtest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.speedtest.fragments.FreeFragment;
import com.example.speedtest.fragments.HistoryFragment;
import com.example.speedtest.fragments.PremiumFragment;


public class ViewPagerLocationAdapter extends FragmentStateAdapter {
    public ViewPagerLocationAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FreeFragment();
            case 1:
                return new PremiumFragment();
            case 2:
                return new HistoryFragment();
            default:
                return new FreeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
