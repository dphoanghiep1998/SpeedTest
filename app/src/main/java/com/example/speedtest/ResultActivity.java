package com.example.speedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.speedtest.databinding.ActivityResultBinding;
import com.example.speedtest.model.ConnectivityTestModel;

public class ResultActivity extends AppCompatActivity {
    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        ConnectivityTestModel connectivityTestModel = (ConnectivityTestModel) getIntent().getSerializableExtra("test_result");
        binding.tvSignalStrength.setText(connectivityTestModel >= -60 ? "TỐT" : level < -60 && level >= -90 ? "BÌNH THƯỜNG" : "YẾU");
        binding.btnClose.setOnClickListener(view -> {
            finish();
        });
    }

}