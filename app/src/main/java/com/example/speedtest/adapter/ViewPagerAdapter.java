package com.example.speedtest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.speedtest.fragments.AnalyzerFragment;
import com.example.speedtest.fragments.CheckResultFragment;
import com.example.speedtest.fragments.RootFragment;
import com.example.speedtest.fragments.SpeedTestFragment;
import com.example.speedtest.fragments.VpnFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private FragmentActivity fragmentActivity;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SpeedTestFragment();
            case 1:
                return new AnalyzerFragment();
            case 2:
                return new RootFragment();
            case 3:
                return new CheckResultFragment();
            default:
                return new SpeedTestFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
