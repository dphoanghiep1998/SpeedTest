package com.example.speedtest.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.annotation.NonNull;

public class NetworkUtils {
    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null){
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public static boolean isWifiConnected(@NonNull Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isMobileConnected(@NonNull Context context) {
        return isConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isConnected(@NonNull Context context, int type) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                NetworkInfo networkInfo = cm.getNetworkInfo(type);
                return networkInfo != null && networkInfo.isConnected();
            } else {
                return isConnected(cm, type);
            }
        }
        return false;

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isConnected(@NonNull ConnectivityManager cm, int type) {
        Network[] networks = cm.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network network : networks) {
            networkInfo = cm.getNetworkInfo(network);
            if (networkInfo != null && networkInfo.getType() == type && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static int getDownloadSpeed(@NonNull Context context){
        int downloadSpeed = 0;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if( isWifiConnected(context)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                downloadSpeed = nc.getLinkDownstreamBandwidthKbps();
                return downloadSpeed;
            }
        }
        return downloadSpeed;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int getUploadSpeed(@NonNull Context context){
        int uploadSpeed = 0;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if(isWifiConnected(context)){
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            uploadSpeed = nc.getLinkUpstreamBandwidthKbps();
            return uploadSpeed;
        }
        return uploadSpeed;
    }
    public static WifiInfo getWifiInfo(@NonNull Context context){
        WifiManager cm = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        return cm.getConnectionInfo();
    }
//    public static String getNameWifi(@NonNull Context context) {
//        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (networkInfo.isConnected()) {
//            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            return wifiInfo.toString();
//        }
//        return null;
//    }

    public static String getNameWifi(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager != null && manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }


}
