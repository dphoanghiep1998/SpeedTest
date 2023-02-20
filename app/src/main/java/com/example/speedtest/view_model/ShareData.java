package com.example.speedtest.view_model;

import androidx.lifecycle.MutableLiveData;

import com.example.speedtest.core.Speedtest;

public class ShareData {
    public MutableLiveData<Boolean> isScanning = new MutableLiveData<>();

    public MutableLiveData<Boolean> isPermissionRequested = new MutableLiveData<>();

    public MutableLiveData<Boolean> isWifiEnabled = new MutableLiveData<>();

    public MutableLiveData<Speedtest> speedtest = new MutableLiveData<>();

    public MutableLiveData<Speedtest> getSpeedtest() {
        return speedtest;
    }

    public MutableLiveData<Boolean> isConnectivityChanged = new MutableLiveData<>();

    public void setSpeedtest(MutableLiveData<Speedtest> speedtest) {
        this.speedtest = speedtest;
    }

}
