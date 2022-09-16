package com.example.speedtest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.example.speedtest.config.SettingGlobal;
import com.example.speedtest.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    CountDownTimer countDownTimer;

    Intent intent;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        onLoad();

    }

    // permisison == true -> to Main else to Permission
    public void handlePermissionFlow() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            intent = new Intent(SplashActivity.this, PermissionActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }


    public void onLoad() {
        String strFixed_3 = "Hành động này có thể xuất hiện quảng cáo ...";
        String strFixed = "Hành động này có thể xuất hiện quảng cáo .";
        String strFixed_2 = "Hành động này có thể xuất hiện quảng cáo ..";
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                i++;
                binding.pbLoading.setProgress(i * 100 / (10000 / 1000));
                if (i % 3 == 0) {
                    binding.tvActionText.setText(strFixed);
                } else if (i % 3 == 1) {
                    binding.tvActionText.setText(strFixed_2);
                } else {
                    binding.tvActionText.setText(strFixed_3);
                }
            }

            @Override
            public void onFinish() {
             handlePermissionFlow();
            }
        };
        countDownTimer.start();

    }

    public void openAds() {

    }


}