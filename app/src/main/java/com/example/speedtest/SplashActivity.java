package com.example.speedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.example.speedtest.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    CountDownTimer countDownTimer;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        onLoad();
    }

    public void onLoad(){
        String strFixed_3 = "Hành động này có thể xuất hiện quảng cáo ...";
        String strFixed = "Hành động này có thể xuất hiện quảng cáo .";
        String strFixed_2 = "Hành động này có thể xuất hiện quảng cáo .."
                ;
        countDownTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                i++;
                binding.pbLoading.setProgress(i*100/(10000/1000));
                if(i%3 == 0){
                    binding.tvActionText.setText(strFixed);
                }else if(i%3 == 1){
                    binding.tvActionText.setText(strFixed_2);
                }else{
                    binding.tvActionText.setText(strFixed_3);
                }
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }

}