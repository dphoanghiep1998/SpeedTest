package com.example.speedtest.activities;

import android.app.Application;
import android.content.Context;

import com.example.speedtest.view_model.ShareDataViewModel;

public class SpeedApplication extends Application {


    private ShareDataViewModel instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public synchronized ShareDataViewModel getShareData() {
        if (instance == null) {
            instance = new ShareDataViewModel();
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
