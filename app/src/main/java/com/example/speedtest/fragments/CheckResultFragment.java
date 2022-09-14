package com.example.speedtest.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.speedtest.activities.MainActivity;
import com.example.speedtest.activities.ResultActivity;
import com.example.speedtest.adapter.ConnectivityTestAdapter;
import com.example.speedtest.common.CustomDialog;
import com.example.speedtest.databinding.FragmentCheckResultBinding;
import com.example.speedtest.interfaces.OnDialogClickListener;
import com.example.speedtest.interfaces.ResultTouchHelper;
import com.example.speedtest.model.ConnectivityTestModel;

import java.util.List;

public class CheckResultFragment extends Fragment implements ResultTouchHelper, OnDialogClickListener {
    FragmentCheckResultBinding binding;

    ConnectivityTestAdapter adapter = new ConnectivityTestAdapter(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckResultBinding.inflate(inflater, container, false);

        rcv_init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.btnDelete.setOnClickListener(view -> {
//            AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
//                    .setTitle("Do you wanna delete all results ?")
//                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            ((MainActivity) requireActivity()).viewModel.deleteAllResultTest();
//                        }
//                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).create();
//            alertDialog.show();
            CustomDialog customDialog = new CustomDialog(requireActivity(),this);
            customDialog.show();
            customDialog.setTitle("DELETE ALL RESULTS ?");
            customDialog.setContent("All results will be deleted");

        });
    }


    public void rcv_init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcvConnectTestResult.setLayoutManager(linearLayoutManager);
        binding.rcvConnectTestResult.setAdapter(adapter);
        ((MainActivity) requireActivity()).viewModel.getListResultTest().observe(requireActivity(), new Observer<List<ConnectivityTestModel>>() {
            @Override
            public void onChanged(List<ConnectivityTestModel> list) {
                adapter.setData(list);
            }
        });
    }

    @Override
    public void onClickResultTest(ConnectivityTestModel connectivityTestModel) {
        Intent intent = new Intent(requireActivity(), ResultActivity.class);
        intent.putExtra("test_result",connectivityTestModel);
        startActivity(intent);
    }

    @Override
    public void onClickPositiveButton() {
       ((MainActivity)requireActivity()).viewModel.deleteAllResultTest();
    }
}
