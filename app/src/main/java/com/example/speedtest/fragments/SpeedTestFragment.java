package com.example.speedtest.fragments;


import android.content.Intent;
import android.graphics.Color;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.speedtest.R;
import com.example.speedtest.databinding.FragmentSpeedtestBinding;
import com.example.speedtest.model.WifiTestModel;
import com.example.speedtest.utils.DateTimeUtils;
import com.example.speedtest.utils.NetworkUtils;
import com.github.anastr.speedviewlib.Gauge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import kotlin.Unit;

public class SpeedTestFragment extends Fragment implements View.OnClickListener {
    FragmentSpeedtestBinding binding;
    WifiTestModel wifiTest;
    int duration = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpeedtestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    public void setupView() {
        // Underline wifi name
        String wifi_name = binding.tvWifiName.getText().toString();
        SpannableString underlineWifiName = new SpannableString(wifi_name);
        underlineWifiName.setSpan(new UnderlineSpan(), 0, wifi_name.length(), 0);
        binding.tvWifiName.setText(underlineWifiName);
        // custom tick
        customTick();

        //onClick start scan
        binding.btnStartScan.setOnClickListener(this);

        //selec Wifi
        binding.tvWifiName.setOnClickListener(this);

    }

    public void customTick() {
        List<Float> arList = new ArrayList<>(Arrays.asList(0f, 0.05f, 0.1f, 0.15f, 0.20f, 0.30f, 0.5f, 0.75f, 1f));
        binding.speedView.setTicks(arList);
        binding.speedView.setSpeedTextPosition(Gauge.Position.BOTTOM_CENTER);
        binding.speedView.setUnit("");
        binding.speedView.setOnPrintTickLabel((tickPosition, tick) -> {
            int convertedTick = Math.round(tick);
            if (tick == 0) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 5) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 10) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 15) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 20) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 30) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 50) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 75) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 100) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;

            }
            return null;
        });
        binding.speedView.setOnSpeedChangeListener((gauge, isSpeedUp, isByTremple) -> this.onSpeedChange(gauge, isSpeedUp, isByTremple));
    }

    public Unit onSpeedChange(Gauge gauge, boolean isSpeedUp, boolean isByTremple) {
        Log.d("TAG", "onSpeedChange: " + isByTremple);
        duration += gauge.getTrembleDuration();
        if(duration == 30000){
            binding.speedView.setWithTremble(false);
            binding.speedView.speedTo(0);
            duration = 0;
//            binding.tvDownloadSpeed.setText(downSpeed);
        }

        return null;
    }

    public void onClickStartButton() {
        if (!NetworkUtils.isWifiConnected(this.getContext())) {
            Toast.makeText(this.getContext(), "No wifi connected", Toast.LENGTH_SHORT).show();
            return;
        }
        int downSpeed = NetworkUtils.getDownloadSpeed(this.getContext())/1024;
        int upSpeed = NetworkUtils.getUploadSpeed(this.getContext())/1024;
        WifiInfo wifiInfo = NetworkUtils.getWifiInfo(getContext());
        wifiTest = new WifiTestModel(wifiInfo.getSSID().toString(), DateTimeUtils.getDateNowConverted(),String.valueOf(downSpeed),String.valueOf(upSpeed),"1");
        binding.speedView.stop();
        binding.speedView.speedTo(downSpeed);
//        binding.speedView.setOnSpeedChangeListener();
    }

    public void onCickWifiName() {
        Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_scan:
                onClickStartButton();
                break;
            case R.id.tv_wifi_name:
                onCickWifiName();
                break;
        }
    }
}
