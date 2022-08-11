package com.example.speedtest.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.speedtest.databinding.FragmentAnalyzerBinding;
import com.example.speedtest.databinding.FragmentCheckResultBinding;

public class CheckResultFragment extends Fragment {
    FragmentCheckResultBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckResultBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}
