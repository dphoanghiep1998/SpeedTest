package com.example.speedtest.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.speedtest.adapter.ConnectivityTestAdapter;
import com.example.speedtest.databinding.FragmentAnalyzerBinding;
import com.example.speedtest.databinding.FragmentCheckResultBinding;
import com.example.speedtest.model.ConnectivityTestModel;
import com.example.speedtest.view_model.WifiTestViewModel;

import java.util.List;

public class CheckResultFragment extends Fragment {
    FragmentCheckResultBinding binding;
    WifiTestViewModel viewModel;
    ConnectivityTestAdapter adapter = new ConnectivityTestAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckResultBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(WifiTestViewModel.class);
        rcv_init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void rcv_init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcvConnectTestResult.setLayoutManager(linearLayoutManager);
        binding.rcvConnectTestResult.setAdapter(adapter);
        viewModel.getListResultTest().observe(requireActivity(), new Observer<List<ConnectivityTestModel>>() {
            @Override
            public void onChanged(List<ConnectivityTestModel> list) {
                adapter.setData(list);
            }
        });
    }
}
