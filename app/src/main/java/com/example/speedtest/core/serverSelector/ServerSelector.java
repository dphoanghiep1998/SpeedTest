package com.example.speedtest.core.serverSelector;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ServerSelector extends Thread {

    HashMap<Integer, String> mapKey = new HashMap<>();
    HashMap<Integer, ServerSpeedtest> mapValue = new HashMap<>();
    double selfLat = 0.0;
    double selfLon = 0.0;
    String selfIp = "";
    String selfISP = "";
    boolean finished = false;


    public HashMap<Integer, String> getMapKey() {
        return mapKey;
    }

    public HashMap<Integer,ServerSpeedtest> getMapValue() {
        return mapValue;
    }

    public double getSelfLat() {
        return selfLat;
    }

    public double getSelfLon() {
        return selfLon;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        //Get latitude, longitude
        try {
            URL url = new URL("https://www.speedtest.net/speedtest-config.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int code = urlConnection.getResponseCode();

            if (code == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                urlConnection.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.contains("isp=")) {
                        continue;
                    }
                    selfLat = Double.parseDouble(line.split("lat=\"")[1].split(" ")[0].replace("\"", ""));
                    selfLon = Double.parseDouble(line.split("lon=\"")[1].split(" ")[0].replace("\"", ""));
                    selfIp = line.split("ip=\"")[1].split(" ")[0].replace("\"", "");
                    selfISP  = line.split("isp=\"")[1].split(" ")[0].replace("\"", "");
                    break;
                }

                br.close();
                Log.d("TAG", "run: "+selfIp + selfISP);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }



        //Best server
        int count = 0;
        try {
            URL url = new URL("https://www.speedtest.net/speedtest-servers-static.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int code = urlConnection.getResponseCode();

            if (code == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                urlConnection.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    ServerSpeedtest speedtest = new ServerSpeedtest();
                    if (line.contains("<server url")) {
                        speedtest.setUploadAddress(line.split("server url=\"")[1].split("\"")[0]);
                        speedtest.setLat(line.split("lat=\"")[1].split("\"")[0]);
                        speedtest.setLon(line.split("lon=\"")[1].split("\"")[0]);
                        speedtest.setName(line.split("name=\"")[1].split("\"")[0]);
                        speedtest.setCountry(line.split("country=\"")[1].split("\"")[0]);
                        speedtest.setCc(line.split("cc=\"")[1].split("\"")[0]);
                        speedtest.setSponsor(line.split("sponsor=\"")[1].split("\"")[0]);
                        speedtest.setHost(line.split("host=\"")[1].split("\"")[0]);


                        mapKey.put(count, speedtest.getUploadAddress());
                        mapValue.put(count, speedtest);
                        count++;
                    }
                }

                br.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        finished = true;
    }
}