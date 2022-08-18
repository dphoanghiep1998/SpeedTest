package com.example.speedtest.fragments;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.speedtest.R;
import com.example.speedtest.databinding.FragmentSpeedtestBinding;
import com.example.speedtest.model.WifiTestModel;
import com.example.speedtest.services.CheckISPIP;
import com.example.speedtest.utils.NetworkUtils;
import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.components.Style;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.Unit;

public class SpeedTestFragment extends Fragment implements View.OnClickListener {
    FragmentSpeedtestBinding binding;
    WifiTestModel wifiTest;
    int duration = 0;
    Boolean isConnectivityChanged = false;

    private BroadcastReceiver internetBroad;
    private IntentFilter internetFilter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpeedtestBinding.inflate(inflater, container, false);
        initBroadCast();
        getActivity().registerReceiver(internetBroad, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();


    }

    @Override
    public void onResume() {
        Log.d("msg", "onResume: ");
        super.onResume();
        if(isConnectivityChanged){
            if(NetworkUtils.isWifiConnected(getContext())){
                SpannableString content = new SpannableString(NetworkUtils.getNameWifi(getContext().getApplicationContext()).substring(1,NetworkUtils.getNameWifi(getContext().getApplicationContext()).length()-1));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                binding.tvWifiName.setText(content);
            }
        }
        isConnectivityChanged = false;
    }

    private void initBroadCast() {
        internetFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        internetBroad = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isConnectivityChanged = true;
            }
        };
    }



    private void unRegisterBroadCast() {
        requireContext().unregisterReceiver(internetBroad);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBroadCast();
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

        //select Wifi
        binding.tvWifiName.setOnClickListener(this);


    }

    public void customTick() {
        List<Float> arList = new ArrayList<>(Arrays.asList(0f, 0.05f, 0.1f, 0.15f, 0.20f, 0.30f, 0.5f, 0.75f, 1f));
        binding.speedView.clearSections();
        binding.speedView.makeSections(1,Color.GRAY, Style.BUTT);

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
        duration += 100;
        if(duration >= 30000){
            binding.speedView.speedTo(0);
            binding.speedView.setWithTremble(false);
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
        ExecutorService executors = Executors.newFixedThreadPool(4);
        Callable<String> callable = new CheckISPIP();
        Future<String> future = executors.submit(callable);
//        Callable<String> callable1 = new ApiBase(future.toString());
//        Future<String> future1 = executors.submit(callable1);
//        Log.d("TAG", "onClickStartButton: " + future1);
//


    }

    public void onCickWifiName() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

    public void testPing(){
        try{
            ProcessBuilder ps = new ProcessBuilder("ping", "-c " + 4, "192.168.1.1");

            ps.redirectErrorStream(true);
            Process pr = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                Log.d("TAG", line.toString());
            }
            pr.waitFor();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

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
