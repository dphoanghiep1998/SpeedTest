package com.example.speedtest.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.speedtest.MainActivity;
import com.example.speedtest.SplashActivity;
import com.example.speedtest.adapter.WifiChannelAdapter;
import com.example.speedtest.databinding.FragmentAnalyzerBinding;
import com.example.speedtest.model.Wifi;
import com.example.speedtest.utils.NetworkUtils;
import com.github.pwittchen.reactivewifi.ReactiveWifi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnalyzerFragment extends Fragment {
    FragmentAnalyzerBinding binding;
    IntentFilter intentFilter;
    BroadcastReceiver wifiReciver;
    WifiChannelAdapter adapter = new WifiChannelAdapter();
    List<ScanResult> scanResultList;
    Disposable disposable;
    boolean duplicated = false;
    WifiManager mainWifi;
    List<Wifi> wifiList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalyzerBinding.inflate(inflater, container, false);
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        rcvWifi_init();
        setListWifi();
        mainWifi.startScan();
//
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void rcvWifi_init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcvWifi.setLayoutManager(linearLayoutManager);
        binding.rcvWifi.setAdapter(adapter);


    }

    @SuppressLint("MissingPermission")
    public void setListWifi() {
        mainWifi = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String connectedWifi = NetworkUtils.getNameWifi(mainWifi);

        disposable = ReactiveWifi.observeWifiAccessPoints(requireContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scanResults -> {
                    for (ScanResult result : scanResults) {
                        String level = String.valueOf(result.level);
                        String frequency = String.valueOf(result.frequency);
                        String channelWidth = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            channelWidth = String.valueOf(result.channelWidth);
                        }
                        String name = result.SSID.length() > 0 ? result.SSID : "wifi";
                        String secure = "";

                        if (result.capabilities.contains("WPA2")) {
                            secure += "WPA2 ";
                        }
                        if (result.capabilities.contains("WPA")) {
                            secure = "WPA ";
                        }
                        if (result.capabilities.contains("WPS")) {
                            secure = "WPS ";
                        }
                        if (connectedWifi.equals(result.SSID)) {
                            if (!duplicated) {
                                wifiList.add(0, new Wifi(name, NetworkUtils.wifiIpAddress(requireContext()), secure, level + " dBm", frequency + " MHz", result.BSSID, channelWidth, "100 m", true));
                                duplicated = true;
                            }

                        } else {
                            wifiList.add(new Wifi(name, "0.0.0.0", secure, level + " dBm", frequency + " MHz", result.BSSID, channelWidth, "100 m", false));
                        }
                    }
                    adapter.setData(wifiList);
                    disposable.dispose();
                });
    }


}
