package com.example.speedtest.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
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

import com.example.speedtest.databinding.FragmentAnalyzerBinding;
import com.example.speedtest.model.Wifi;
import com.example.speedtest.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerFragment extends Fragment {
    FragmentAnalyzerBinding binding;
    List<Wifi> wifiList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalyzerBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    public void wifiReceiver_init(){
        BroadcastReceiver wifiReciver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                List<ScanResult> scanResults = new ArrayList<>();
                scanResults = NetworkUtils.getListWifi(requireContext());
                for(ScanResult result : scanResults){
                    if(NetworkUtils.isWifiConnected(context,result.SSID)){
                        String level = String.valueOf(result.level);
                        String frequency = String.valueOf(result.frequency);
                        String channelWidth = String.valueOf(result.channelWidth);
                        wifiList.add(new Wifi(result.SSID,NetworkUtils.wifiIpAddress(context),result.capabilities,level,frequency,result.BSSID,channelWidth,"100 m",true));
                    }else {
                        NetworkUtils.isWifiConnected(context,result.SSID)){
                            String level = String.valueOf(result.level);
                            String frequency = String.valueOf(result.frequency);
                            String channelWidth = String.valueOf(result.channelWidth);
                            wifiList.add(new Wifi(result.SSID,"0.0.0.0",result.capabilities,level,frequency,result.BSSID,channelWidth,"100 m",false));
                    }
                }

            }
        };
    }

    public void getWifi(){
//        Log.d("TAG", "getWifi: " + NetworkUtils.getListWifi(requireContext()));
    }
}
