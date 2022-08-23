package com.example.speedtest.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.speedtest.AnalyticActivity;
import com.example.speedtest.adapter.WifiChannelAdapter;
import com.example.speedtest.databinding.FragmentAnalyzerBinding;
import com.example.speedtest.interfaces.ItemTouchHelper;
import com.example.speedtest.model.Wifi;
import com.example.speedtest.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerFragment extends Fragment implements ItemTouchHelper {
    FragmentAnalyzerBinding binding;
    IntentFilter intentFilter;
    BroadcastReceiver wifiReciver;
    WifiChannelAdapter adapter = new WifiChannelAdapter(this);
    List<ScanResult> scanResultList;
    boolean duplicated = false;
    WifiManager mainWifi;
    List<Wifi> wifiList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalyzerBinding.inflate(inflater, container, false);
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mainWifi = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        rcvWifi_init();
        initBroadcast();
        requireActivity().registerReceiver(wifiReciver,intentFilter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainWifi.startScan();


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


    public void initBroadcast() {
        wifiReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                scanResultList = NetworkUtils.getListWifi(getContext(), mainWifi);
                String activeWifiName = NetworkUtils.getNameWifi(getContext());
                Log.d("TAG", "onReceive: "+scanResultList +activeWifiName);
                if (scanResultList != null) {
                    for (ScanResult result : scanResultList) {
                        if (result != null) {
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
                            if (activeWifiName.equals(result.SSID)) {
                                if (!duplicated) {
                                    wifiList.add(0, new Wifi(name, NetworkUtils.wifiIpAddress(requireContext()),"0.0.0.0" ,secure, level+"", frequency+"", result.BSSID, channelWidth, "100 m","" ,true));
                                    duplicated = true;
                                }

                            } else {
                                wifiList.add(new Wifi(name, "0.0.0.0","0.0.0.0" ,secure, level +"", frequency + "", result.BSSID, channelWidth, "100 m","" ,false));
                            }
                        }
                    }

                }
                adapter.setData(wifiList);
            }
        };

    }
    private void unregisterReceiver(){
        requireActivity().unregisterReceiver(wifiReciver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterReceiver();
    }

    @Override
    public void onClickItem(Wifi wifi) {
        Intent intent = new Intent(requireActivity(), AnalyticActivity.class);
        intent.putExtra("wifi",wifi);
        startActivity(intent);
    }
}
