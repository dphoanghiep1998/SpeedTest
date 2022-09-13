package com.example.speedtest.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.speedtest.R;
import com.example.speedtest.SpeedApplication;
import com.example.speedtest.adapter.WifiChannelAdapter;
import com.example.speedtest.databinding.FragmentAnalyzerBinding;
import com.example.speedtest.interfaces.ItemTouchHelper;
import com.example.speedtest.model.Wifi;
import com.example.speedtest.utils.NetworkUtils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

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
    private SpeedApplication application;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView: ");
        application = SpeedApplication.create(requireContext());
        binding = FragmentAnalyzerBinding.inflate(inflater, container, false);
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mainWifi = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        initView();
        rcvWifi_init();
        initBroadcast();
        requireActivity().registerReceiver(wifiReciver, intentFilter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d("TAG", "onResume: ");
        super.onResume();

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

    public void initView() {
        binding.btnSetting.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
            intent.setData(uri);
            requireActivity().startActivity(intent);
        });
        application.getShareData().isPermissionRequested.observe(getViewLifecycleOwner(), isPermissionRequested -> {
            if (isPermissionRequested) {
                Log.d("TAG", "onChanged: ");
                binding.requestContainer.setVisibility(View.GONE);
                mainWifi.startScan();
            } else {
                binding.requestContainer.setVisibility(View.VISIBLE);

            }
        });
    }

    public void initBroadcast() {
        binding.loadingPanel.setVisibility(View.VISIBLE);
        wifiReciver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onReceive(Context context, Intent intent) {
                scanResultList = NetworkUtils.getListWifi(getContext(), mainWifi);
                String activeWifiName = NetworkUtils.getNameWifi(getContext());
                if (scanResultList != null) {
                    for (ScanResult result : scanResultList) {
                        if (result != null) {
                            String level = String.valueOf(result.level);
                            String frequency = String.valueOf(result.frequency);

                            String channelWidth = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                channelWidth = String.valueOf(result.channelWidth);
                            }
                            int channel = ScanResult.convertFrequencyMhzToChannelIfSupported(result.frequency);
                            int r_range = NetworkUtils.getRangeWifi(channel, result.channelWidth)[0];
                            int l_range = NetworkUtils.getRangeWifi(channel, result.channelWidth)[1];

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

                            if (activeWifiName != null && activeWifiName.equals(result.SSID)) {
                                if (!duplicated) {
                                    Log.d("TAG", "onReceive: " + result);

                                    wifiList.add(0, new Wifi(name, NetworkUtils.wifiIpAddress(requireContext()),
                                            "0.0.0.0", secure, level + "", frequency + "",
                                            result.BSSID, channel + "", "100 m", "", true, r_range, l_range));
                                    duplicated = true;
                                }

                            } else {
                                wifiList.add(new Wifi(name, "0.0.0.0", "0.0.0.0", secure, level + "",
                                        frequency + "", result.BSSID, channel + "",
                                        "100 m", "", false, r_range, l_range));
                            }
                        }
                    }

                }
                adapter.setData(wifiList);
                setDataChart(wifiList);
                binding.loadingPanel.setVisibility(View.GONE);

            }
        };

    }

    private void unregisterReceiver() {
        requireActivity().unregisterReceiver(wifiReciver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterReceiver();
    }

    @Override
    public void onClickItemWifi(Wifi wifi) {
        Intent intent = new Intent(requireActivity(), AnalyticActivity.class);
        intent.putExtra("wifi", wifi);
        startActivity(intent);
    }


    private void setDataChart(List<Wifi> wifiList) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Wifi channel";
        ArrayList<String> xAxisValues = new ArrayList<>();
        for (int i = 0; i <= 14; i++) {
            xAxisValues.add(i + "");
        }


        binding.lcWifiChannel.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

        for (Wifi wifi : wifiList) {
            if (Integer.parseInt(wifi.getWifi_channel()) < 14) {
                BarEntry barEntry = new BarEntry(Integer.parseInt(wifi.getWifi_channel()), Integer.parseInt(wifi.getWifi_level()));
                entries.add(barEntry);
            }
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);
        BarData data = new BarData(barDataSet);

        binding.lcWifiChannel.getXAxis().setCenterAxisLabels(true);

        binding.lcWifiChannel.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.lcWifiChannel.getXAxis().setTextColor(getResources().getColor(R.color.white));
        binding.lcWifiChannel.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
        binding.lcWifiChannel.getXAxis().setLabelCount(10, false);
        binding.lcWifiChannel.getLegend().setTextColor(getResources().getColor(R.color.white));
        binding.lcWifiChannel.getXAxis().setAxisMinimum(0f);
        binding.lcWifiChannel.getXAxis().setAxisMaximum(13f);
        binding.lcWifiChannel.getAxisLeft().setStartAtZero(false);

        binding.lcWifiChannel.setVisibleYRange(0,90, YAxis.AxisDependency.LEFT);


        binding.lcWifiChannel.setTouchEnabled(false);
        binding.lcWifiChannel.setData(data);
        binding.lcWifiChannel.notifyDataSetChanged();
        binding.lcWifiChannel.setDrawValueAboveBar(false);
        binding.lcWifiChannel.animateX(1000);
        binding.lcWifiChannel.invalidate();

    }

    private class MyAxisValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return super.getFormattedValue(value);
        }
    }
}
