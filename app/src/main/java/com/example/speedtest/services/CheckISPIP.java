package com.example.speedtest.services;

import android.os.StrictMode;

import java.util.concurrent.Callable;

public class CheckISPIP implements Callable {

    @Override
    public String call() throws Exception {
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
//                System.out.println("My current IP address is " + s.next());
                return s.next();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
