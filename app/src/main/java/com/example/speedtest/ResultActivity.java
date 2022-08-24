package com.example.speedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
        String afterScan = getIntent().getStringExtra("EXTRA_MESS_1");
        Log.d("TAG", "initView: " + afterScan);

        int level = connectivityTestModel.getType().equals("wifi") ? Integer.parseInt(connectivityTestModel.getWifi().getWifi_level()) : 0;
        String status = level >= -60 ? "TỐT" : level < -60 && level >= -90 ? "BÌNH THƯỜNG" : "YẾU";
        int progress = level >= -60 ? 100 : level < -60 && level >= -90 ? 50 : 0;
        binding.pbSignal.setProgress(progress);
        binding.pbSignal.setEnabled(false);
        binding.tvSignalStrength.setTextColor(level >= -60 ? getResources().getColor(R.color.signal_good) : level < -60 && level >= -90 ? getResources().getColor(R.color.signal_normal) : getResources().getColor(R.color.signal_poor));

        binding.tvSignalStrength.setText(status);
        binding.tvDownloadValue.setText(connectivityTestModel.getDownloadSpeed());
        binding.tvUploadValue.setText(connectivityTestModel.getUpLoadSpeed());
        binding.tvPingCount.setText(connectivityTestModel.getPing() + " ms");
        binding.tvJitterCount.setText(connectivityTestModel.getJitter() + " ms");
        binding.tvLossCount.setText(connectivityTestModel.getLoss() + " %");
        binding.tvConnectNameValue.setText(connectivityTestModel.getName());
        String type = connectivityTestModel.getType();
        if (type.equals("wifi")) {
            binding.tvIspValue.setText(connectivityTestModel.getWifi().getWifi_ISP());
            binding.tvInternalIpValue.setText(connectivityTestModel.getWifi().getWifi_internal_ip());
            binding.tvExternalIpValue.setText(connectivityTestModel.getWifi().getWifi_external_ip());
        } else {
            binding.tvIspValue.setText(connectivityTestModel.getMobile().getMobile_isp());
            binding.tvInternalIpValue.setText(connectivityTestModel.getMobile().getMobile_internal_ip());
            binding.tvExternalIpValue.setText(connectivityTestModel.getMobile().getMobile_external_ip());
        }

        binding.btnClose.setOnClickListener(view -> {
            finish();
        });

        if (afterScan == null) {
            binding.btnScanAgain.setVisibility(View.GONE);
        } else {
            binding.btnScanAgain.setVisibility(View.VISIBLE);
            binding.btnScanAgain.setOnClickListener(view -> {
//                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
//                intent.putExtra("start_scan","start_scan");
//                setResult(RESULT_OK,intent);
                setResult(100);
                finish();
            });
        }
    }


}