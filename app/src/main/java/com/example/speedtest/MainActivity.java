package com.example.speedtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.speedtest.databinding.ActivityMainBinding;
import com.example.speedtest.fragments.AnalyzerFragment;
import com.example.speedtest.fragments.CheckResultFragment;
import com.example.speedtest.fragments.SpeedTestFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SpeedTestFragment speedTestFragment = new SpeedTestFragment();
    AnalyzerFragment analiyzerFragment = new AnalyzerFragment();
    CheckResultFragment checkResultFragment = new CheckResultFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        initView();
        setActionMenu();
    }

    public void initView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, speedTestFragment).commit();
        binding.navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.speedtest:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, speedTestFragment).commit();
                        return true;
                    case R.id.analist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, analiyzerFragment).commit();
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, checkResultFragment).commit();
                        return true;

                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, speedTestFragment).commit();
                        return true;
                }
            }
        });
    }
    public void setActionMenu(){
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerContainer.openDrawer(GravityCompat.START);
            }
        });
        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerContainer.close();
            }
        });
    }
}