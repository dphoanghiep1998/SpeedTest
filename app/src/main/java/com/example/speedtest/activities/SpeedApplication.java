package com.example.speedtest.activities;

import android.app.Application;
import android.content.Context;

import com.example.speedtest.view_model.ShareData;

public class SpeedApplication extends Application {


    private ShareData instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public synchronized ShareData getShareData() {
        if (instance == null) {
            instance = new ShareData();
        }
        return instance;
    }

    public static SpeedApplication create(Context context) {

        return SpeedApplication.get(context);
    }

    private static SpeedApplication get(Context context) {
        return (SpeedApplication) context.getApplicationContext();
    }
}
