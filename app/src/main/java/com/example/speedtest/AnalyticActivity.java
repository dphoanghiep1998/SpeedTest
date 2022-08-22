package com.example.speedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.speedtest.databinding.ActivityAnalyticBinding;

public class AnalyticActivity extends AppCompatActivity {
    ActivityAnalyticBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalyticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}