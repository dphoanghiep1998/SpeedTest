package com.example.speedtest.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.speedtest.databinding.FragmentVpnBinding;

public class VpnFragment extends Fragment {

FragmentVpnBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVpnBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}