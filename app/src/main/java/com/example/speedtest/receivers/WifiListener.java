package com.example.speedtest.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.example.speedtest.activities.SpeedApplication;


public class WifiListener extends BroadcastReceiver {
    SpeedApplication application;
    Context context;

    public WifiListener(Context context) {
        this.context = context;
        application = SpeedApplication.create(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiStateExtra) {
            case WifiManager.WIFI_STATE_ENABLED:
                application.getShareData().isWifiEnabled.postValue(true);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                application.getShareData().isWifiEnabled.postValue(false);
                break;

        }
    }
}
