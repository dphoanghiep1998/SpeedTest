package com.example.speedtest.activities;

import androidx.lifecycle.MutableLiveData;

import com.example.speedtest.core.Speedtest;

public class ShareDataViewModel {
    public MutableLiveData<Boolean> isScanning = new MutableLiveData<>();

    public MutableLiveData<Boolean> isPermissionRequested = new MutableLiveData<>();

    public MutableLiveData<Boolean> isWifiEnabled = new MutableLiveData<>();

    public MutableLiveData<Speedtest> speedtest = new MutableLiveData<>();

    public MutableLiveData<Speedtest> getSpeedtest() {
        return speedtest;
    }

    public void setSpeedtest(MutableLiveData<Speedtest> speedtest) {
        this.speedtest = speedtest;
    }

}
