package com.example.speedtest.fragments;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.speedtest.MainActivity;
import com.example.speedtest.R;
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
        binding = FragmentVpnBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        binding.containerInfor.setOnClickListener(view -> {
            if (binding.containerHidden.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.containerInfor, new AutoTransition());
                binding.containerHidden.setVisibility(View.GONE);
                binding.imvArrowDown2.setRotation(0);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(binding.containerInfor, new AutoTransition());
                binding.containerHidden.setVisibility(View.VISIBLE);
                binding.imvArrowDown2.setRotation(180);
            }
        });

        binding.containerSelector.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition((ViewGroup) view, new AutoTransition());
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.root_fragment,new ChangeLocationFragment(),null).addToBackStack("vpn").commit();
            ((MainActivity)requireActivity()).showBackBtn();
        });
    }

}