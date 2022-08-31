package com.example.speedtest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.example.speedtest.databinding.ActivitySplashBinding;
import com.example.speedtest.model.Wifi;
import com.example.speedtest.utils.NetworkUtils;
import com.github.pwittchen.reactivewifi.ReactiveWifi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    CountDownTimer countDownTimer;
    private SpeedApplication application;

    Intent intent;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = SpeedApplication.create(this);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        onLoad();
        intent=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void onLoad() {
        String strFixed_3 = "Hành động này có thể xuất hiện quảng cáo ...";
        String strFixed = "Hành động này có thể xuất hiện quảng cáo .";
        String strFixed_2 = "Hành động này có thể xuất hiện quảng cáo ..";
//        countDownTimer = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long l) {
//                i++;
//                binding.pbLoading.setProgress(i * 100 / (10000 / 1000));
//                if (i % 3 == 0) {
//                    binding.tvActionText.setText(strFixed);
//                } else if (i % 3 == 1) {
//                    binding.tvActionText.setText(strFixed_2);
//                } else {
//                    binding.tvActionText.setText(strFixed_3);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        };
//        countDownTimer.start();

    }

    public void openAds() {

    }


}