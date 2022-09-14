package com.example.speedtest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.speedtest.R;
import com.example.speedtest.databinding.ActivityAnalyticBinding;
import com.example.speedtest.model.Wifi;

public class AnalyticActivity extends AppCompatActivity {
    ActivityAnalyticBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalyticBinding.inflate(getLayoutInflater());
        initView();
        getData();
        setContentView(binding.getRoot());
    }

    public void getData(){
        Wifi wifi  = (Wifi) getIntent().getSerializableExtra("wifi");
        binding.tvWifiName.setText(wifi.getWifi_name());
        int level = Integer.parseInt(wifi.getWifi_level());
        binding.tvSignalStrength.setText(wifi.getWifi_level()+" dBm");
        binding.tvStatus.setText(level >= -60 ? "TỐT" : level < -60 && level >= -90 ? "BÌNH THƯỜNG" : "YẾU");
        binding.tvStatus.setTextColor(level >= -60 ? getResources().getColor(R.color.signal_good) : level < -60 && level >= -90 ? getResources().getColor(R.color.signal_normal) : getResources().getColor(R.color.signal_poor) );
        binding.tvDistanceValue.setText(wifi.getWifi_distance());
        binding.tvBssidValue.setText(wifi.getWifi_bssid());
        binding.tvChannelValue.setText(wifi.getWifi_channel());
        binding.tvFrequencyValue.setText(wifi.getWifi_frequency()+ " MHz");
        binding.tvSecurityTypeValue.setText(wifi.getWifi_secure_type());
    }
    public void initView(){
        binding.btnBack.setOnClickListener(view -> {
            finish();
        });
    }
}