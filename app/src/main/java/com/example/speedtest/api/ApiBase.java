package com.example.speedtest.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class ApiBase implements Callable<String> {
    private static final String BASE_DOMAIN = "http://ip-api.com/json/";
    private String ip;
    public ApiBase(String ip){
        this.ip = ip;
    }
    public static String getJSONdataFromIspIp(String ip){
        try {
            URL url = new URL(BASE_DOMAIN + ip + "?fields=status,isp");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                System.out.printf(inputLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String call() throws Exception {
        return getJSONdataFromIspIp(ip);
    }
}
