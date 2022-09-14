package com.example.speedtest.activities;

import android.util.MutableBoolean;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ShareDataViewModel {
    public MutableLiveData<Boolean> isScanning = new MutableLiveData<>();

    public MutableLiveData<Boolean> isPermissionRequested = new MutableLiveData<>();
}
