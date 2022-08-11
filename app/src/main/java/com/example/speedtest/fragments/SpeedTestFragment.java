package com.example.speedtest.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.speedtest.databinding.FragmentSpeedtestBinding;

public class SpeedTestFragment extends Fragment {
    FragmentSpeedtestBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpeedtestBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}
