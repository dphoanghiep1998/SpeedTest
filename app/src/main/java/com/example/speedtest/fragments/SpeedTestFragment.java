package com.example.speedtest.fragments;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Build;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.speedtest.MainActivity;
import com.example.speedtest.R;
import com.example.speedtest.SpeedApplication;
import com.example.speedtest.core.download.HttpDownloadTest;
import com.example.speedtest.core.ping.PingTest;
import com.example.speedtest.core.serverSelector.ServerSelector;
import com.example.speedtest.core.serverSelector.ServerSpeedtest;
import com.example.speedtest.core.upload.HttpUploadTest;
import com.example.speedtest.databinding.FragmentSpeedtestBinding;
import com.example.speedtest.model.Mobile;
import com.example.speedtest.model.Wifi;
import com.example.speedtest.utils.NetworkUtils;
import com.github.anastr.speedviewlib.Gauge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class SpeedTestFragment extends Fragment implements View.OnClickListener {
    FragmentSpeedtestBinding binding;
    private boolean isConnectivityChanged = true;
    private boolean isFragmentFreezing = false;
    private boolean isScanning = false;
    private BroadcastReceiver internetBroad;
    private IntentFilter internetFilter;
    private static ServerSelector serverSelector;
    private ServerSpeedtest endPoint;
    HashSet<String> tempBlackList;
    private Wifi wifi = new Wifi();
    private Mobile mobile = new Mobile();
    private String type;
    private SpeedApplication application;


    @Override
    public void onPause() {
        super.onPause();
        isFragmentFreezing = true;
        Log.d("TAG", "onPause: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpeedtestBinding.inflate(inflater, container, false);
        application = SpeedApplication.create(requireContext());
        tempBlackList = new HashSet<>();
        initBroadCast();
        initServer();
        checkFirstWhenStart();
        getActivity().registerReceiver(internetBroad, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();

        application.getShareData().isScanning.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ((MainActivity) requireActivity()).first_time = false;
                if (aBoolean) {
                    if (!NetworkUtils.isWifiConnected(requireContext()) && !NetworkUtils.isMobileConnected(requireContext())) {
                        Toast.makeText(requireContext(), "No connectivity!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((MainActivity) requireActivity()).hideBottomTabWhenScan();
                    ((MainActivity) requireActivity()).showCloseBtn();
                    binding.btnStartScan.setEnabled(false);
                    binding.tvWifiName.setEnabled(false);
                    YoYo.with(Techniques.FadeInDown).onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            binding.btnStartScan.setText("Đang chạy ...");
                        }
                    }).playOn(binding.btnStartScan);
                    ((MainActivity) requireActivity()).binding.imvVip.setEnabled(false);

                } else {

                    if (serverSelector != null) {
                    }

                }
            }
        });

    }

    @Override
    public void onResume() {
        Log.d("msg", "onResume: ");
        super.onResume();
        application.getShareData().isPermissionRequested.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPermissionRequested) {
                if (isPermissionRequested) {
                    isFragmentFreezing = false;
                    if (isConnectivityChanged) {
                        if (NetworkUtils.isWifiConnected(getContext())) {
                            SpannableString content = new SpannableString(NetworkUtils.getNameWifi(getContext().getApplicationContext()));
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            binding.tvWifiName.setText(content);
                            type = "wifi";
                        } else if (NetworkUtils.isMobileConnected(getContext())) {
                            NetworkInfo info = NetworkUtils.getInforMobileConnected(getContext());
                            String type = info.getTypeName() + " - " + info.getSubtypeName();
                            SpannableString content = new SpannableString(type);
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            binding.tvWifiName.setText(content);
                            type = "mobile";
                        } else {
                            SpannableString content = new SpannableString("No connection");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            binding.tvWifiName.setText(content);
                            type = "no-connection";
                        }
                    }
                    isConnectivityChanged = false;
                } else {
                    if (isConnectivityChanged) {
                        if (NetworkUtils.isWifiConnected(getContext())) {
                            SpannableString content = new SpannableString("Wifi");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            binding.tvWifiName.setText(content);
                            type = "wifi";
                        } else if (NetworkUtils.isMobileConnected(getContext())) {
                            NetworkInfo info = NetworkUtils.getInforMobileConnected(getContext());
                            SpannableString content = new SpannableString("Mobile");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            binding.tvWifiName.setText(content);
                            type = "mobile";
                        } else {
                            SpannableString content = new SpannableString("No connection");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            binding.tvWifiName.setText(content);
                            type = "no-connection";
                        }
                    }
                    isConnectivityChanged = false;
                }
            }
        });


    }


    private void initBroadCast() {
        internetFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        internetBroad = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isConnectivityChanged = true;
                application.getShareData().isPermissionRequested.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isPermissionRequested) {
                        if (isPermissionRequested) {
                            if (isConnectivityChanged && !isFragmentFreezing) {
                                if (NetworkUtils.isWifiConnected(getContext())) {
                                    SpannableString content = new SpannableString(NetworkUtils.getNameWifi(getContext().getApplicationContext()));
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    binding.tvWifiName.setText(content);
                                    type = "wifi";

                                } else if (NetworkUtils.isMobileConnected(getContext())) {
                                    NetworkInfo info = NetworkUtils.getInforMobileConnected(getContext());
                                    String type = info.getTypeName() + " - " + info.getSubtypeName();
                                    SpannableString content = new SpannableString(type);
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    binding.tvWifiName.setText(content);
                                    type = "mobile";
                                } else {
                                    SpannableString content = new SpannableString("No connection");
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    binding.tvWifiName.setText(content);
                                    type = "no-connection";
                                }
                                isConnectivityChanged = false;
                            }
                        } else {
                            if (isConnectivityChanged && !isFragmentFreezing) {
                                if (NetworkUtils.isWifiConnected(getContext())) {
                                    SpannableString content = new SpannableString("Wifi");
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    binding.tvWifiName.setText(content);
                                    type = "wifi";

                                } else if (NetworkUtils.isMobileConnected(getContext())) {
                                    NetworkInfo info = NetworkUtils.getInforMobileConnected(getContext());

                                    SpannableString content = new SpannableString("Mobile");
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    binding.tvWifiName.setText(content);
                                    type = "mobile";
                                } else {
                                    SpannableString content = new SpannableString("No connection");
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    binding.tvWifiName.setText(content);
                                    type = "no-connection";
                                }
                                isConnectivityChanged = false;
                            }
                        }
                    }
                });


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

        binding.speedView.setSpeedTextPosition(Gauge.Position.BOTTOM_CENTER);
        binding.speedView.setUnit("");

        binding.speedView.setOnPrintTickLabel((tickPosition, tick) -> {
            int convertedTick = Math.round(tick);
            if (tick == 100) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;

            }
            return null;
        });
    }


    public void onClickStartButton() {
//        ((MainActivity) requireActivity()).setIsScanning(true);
        application.getShareData().isScanning.postValue(true);


    }

    public void onCickWifiName() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
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


    private String format(double d) {
        Locale l = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            l = getResources().getConfiguration().getLocales().get(0);
        } else {
            l = getResources().getConfiguration().locale;
        }
        if (d < 10) return String.format(l, "%.2f", d);
        if (d < 100) return String.format(l, "%.1f", d);
        return "" + Math.round(d);
    }

    private void getConnection() {
        if (NetworkUtils.isWifiConnected(requireContext().getApplicationContext())) {
            WifiInfo wifiInfo = NetworkUtils.getWifiInfo(requireContext().getApplicationContext());
            Log.d("TAG", "getConnection: " + wifiInfo);
            if (wifiInfo != null) {
                wifi.setWifi_name(wifiInfo.getSSID());
                wifi.setWifi_bssid(wifiInfo.getBSSID());
                wifi.setWifi_level(String.valueOf(wifiInfo.getRssi()));
                wifi.setWifi_frequency(String.valueOf(wifiInfo.getFrequency()));
                double exp = (27.55 - (20 * Math.log10(wifiInfo.getFrequency())) + Math.abs(wifiInfo.getRssi())) / 20.0;
                double distanceM = Math.pow(10.0, exp);
                wifi.setWifi_distance(distanceM + "");
                wifi.setWifi_isConnected(true);
                wifi.setWifi_internal_ip(NetworkUtils.wifiIpAddress(requireContext().getApplicationContext()));
                wifi.setWifi_channel(0 + "");
            }

        } else if (NetworkUtils.isMobileConnected(requireContext().getApplicationContext())) {
            NetworkInfo networkInfo = NetworkUtils.getInforMobileConnected(requireContext().getApplicationContext());
            if (networkInfo != null) {
                String type = networkInfo.getTypeName() + " - " + networkInfo.getSubtypeName();
                mobile.setMobile_name(type);
                mobile.setMobile_external_ip("0.0.0.0");
                mobile.setMobile_internal_ip("0.0.0.0");
                mobile.setMobile_isConnected(true);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 100) {
                onClickStartButton();
            }
        }
    }

    public void checkFirstWhenStart() {
        if (NetworkUtils.isWifiConnected(getContext())) {
            type = "wifi";
        } else if (NetworkUtils.isMobileConnected(getContext())) {
            type = "mobile";
        } else {
            type = "no-connection";
        }
    }

    public void resetView() {
        binding.tvSpeedValue.setText("0");
        binding.tvDownloadValue.clearAnimation();
        binding.tvUploadValue.clearAnimation();
        binding.speedView.setWithTremble(false);
        binding.speedView.speedTo(0);
        binding.btnStartScan.setText("BẮT ĐẦU NGAY");
        binding.btnStartScan.setEnabled(true);
        binding.tvWifiName.setEnabled(true);
        binding.tvDownloadValue.setText("0");
        binding.tvUploadValue.setText("0");
        binding.tvPingCount.setText("0");
        binding.tvJitterCount.setText("0");
        ((MainActivity) requireActivity()).showBottomTabAfterScan();
    }

    public void initServer() {
        if (serverSelector == null) {
            serverSelector = new ServerSelector();
            serverSelector.start();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeCount = 300;
                while (!serverSelector.isFinished()) {
                    timeCount--;
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (timeCount <= 0) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Todo : Do something if timeout ??
                            }
                        });
                        serverSelector = null;
                        return;
                    }

                }
                findClosetServer();
                initTest();

            }
        }).start();
    }

    public void findClosetServer() {
        HashMap<Integer, String> mapKey = serverSelector.getMapKey();
        HashMap<Integer, ServerSpeedtest> mapValue = serverSelector.getMapValue();
        Log.d("TAG", "findClosetServer: "+ mapKey);
        Log.d("TAG", "findClosetServer: "+ mapValue);

        double selfLat = serverSelector.getSelfLat();
        double selfLon = serverSelector.getSelfLon();
        double tmp = 19349458;
        double dist = 0.0;
        int findServerIndex = 0;
        for (int index : mapKey.keySet()) {
            if (tempBlackList.contains(mapValue.get(index).getCountry())) {
                continue;
            }
            Location source = new Location("Source");
            source.setLatitude(selfLat);
            source.setLongitude(selfLon);
            ServerSpeedtest st = mapValue.get(index);
            Location dest = new Location("Dest");
            dest.setLatitude(Double.parseDouble(st.getLat()));
            dest.setLongitude(Double.parseDouble(st.getLon()));

            double distance = source.distanceTo(dest);
            if (tmp > distance) {
                tmp = distance;
                dist = distance;
                findServerIndex = index;
            }
        }
//        String testAddr = mapKey.get(findServerIndex).replace("http://", "https://");
        endPoint = mapValue.get(findServerIndex);
        Log.d("TAG" ,"endPoint: " + endPoint);
        final double distance = dist;

        if (endPoint == null) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Todo : Do something when can not get hostlocation
                }
            });
            return;
        }
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Todo set location, distance...
            }
        });
    }

    public void initTest() {
        final List<Double> pingRateList = new ArrayList<>();
        final List<Double> downloadRateList = new ArrayList<>();
        final List<Double> uploadRateList = new ArrayList<>();
        Boolean pingTestStarted = false;
        Boolean pingTestFinished = false;
        Boolean downloadTestStarted = false;
        Boolean downloadTestFinished = false;
        Boolean uploadTestStarted = false;
        Boolean uploadTestFinished = false;
        String testAdrr = endPoint.getUploadAddress();
        final PingTest pingTest = new PingTest(testAdrr.replace(":8080", ""), 3);
        final HttpUploadTest uploadTest = new HttpUploadTest(testAdrr);
        final HttpDownloadTest downloadTest = new HttpDownloadTest(testAdrr.replace(testAdrr.split("/")[testAdrr.split("/").length - 1], ""));

        while (true) {
            if (!pingTestFinished) {
                pingTest.start();
                pingTestStarted = true;
            }
            if (pingTestFinished && !downloadTestStarted) {
                downloadTest.start();
                downloadTestStarted = true;
            }
            if (downloadTestFinished && !uploadTestStarted) {
                uploadTest.start();
                uploadTestStarted = true;
            }
            if (pingTestFinished) {
                if (pingTest.getAvgRtt() == 0) {
                    //Todo : ping test fail -> cancel all test + reset text
                } else {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Todo : Do something if ping test successfully
                        }
                    });
                }
            } else {
                pingRateList.add(pingTest.getInstantRtt());
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Todo : Set ping text to make something "real"
                        Log.d("TAG", "ping : " + pingTest.getInstantRtt());
                    }
                });
            }
            if (pingTestFinished) {
                if (downloadTestFinished) {
                    if (downloadTest.getFinalDownloadRate() == 0) {
                        System.out.println("Download error...");
                    } else {
                        //Success
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Todo : download test fail -> cancel all test + reset view
                            }
                        });
                    }
                } else {
                    double downloadRate = downloadTest.getInstantDownloadRate();
                    downloadRateList.add(downloadRate);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Todo : make speedview run as speed;
                            Log.d("TAG", "downloadRate : " + downloadTest.getInstantDownloadRate());
                        }
                    });
                    //Todo : add to result test

                }
            }
            if(downloadTestFinished){
                if(uploadTestFinished){
                    if(uploadTest.getFinalUploadRate() == 0){
                        //Todo upload failure ??
                    }else{
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Todo update uploadText
                            }
                        });
                    }
                }else {
                    double uploadRate = uploadTest.getInstantUploadRate();
                    uploadRateList.add(uploadRate);
//                    position = getPositionByRate(uploadRate);

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Todo update speedview
                            Log.d("TAG", "uploadTest : " + uploadTest.getInstantUploadRate());


                        }
                    });
                    //Todo add to result
                }
            }
            if(pingTestFinished && downloadTestFinished && uploadTest.isFinished()){
                break;
            }
            if(pingTest.isFinished()){
                pingTestFinished = true;
            }
            if(downloadTest.isFinished()){
                downloadTestFinished = true;

            }
            if(uploadTest.isFinished()){
                uploadTestFinished = true;
            }
            if(pingTestStarted && !pingTestFinished){
                try{
                    Thread.sleep(300);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                try{
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }
}
