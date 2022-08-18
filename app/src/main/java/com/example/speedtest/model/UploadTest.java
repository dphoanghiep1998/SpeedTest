package com.example.speedtest.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

public class UploadTest extends Thread {
    public UploadTest(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String fileUrl = "";
    //thoi gian bat dau gui (mls)
    long startTime = 0;
    //elapsed = (current - start)/1000 (s)
    double uploadElapsedTime = 0;
    //upload rate mbps : megabit per second = (uploadedByte * 8  / 10^6) / (elapsed time * 1000) (1byte = 10^-6 megabit)
    double finalUploadRate = 0.0;
    // byte da download duoc
    static int uploadedByte = 0;
    // flag check.
    boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd;
        try {
            bd = new BigDecimal(value);
        } catch (Exception ex) {
            return 0.0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getInstantUploadRate() {
        if (uploadedByte >= 0) {
            long now = System.currentTimeMillis();
            uploadElapsedTime = (now - startTime) / 1000.0;
            return round((Double) (((uploadedByte / 10 ^ 6) * 8) / uploadElapsedTime), 2);
        } else {
            return 0.0;
        }
    }

    public double getFinalUploadRate() {
        return round(finalUploadRate, 2);
    }

    @Override
    public void run() {
        try {
            URL url = new URL(fileUrl);
            uploadedByte = 0;
            startTime = System.currentTimeMillis();
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            for(int i=0;i<4;i++){
                executorService.execute(new HandlerUpload(url));
            }
            executorService.shutdown();
            while (!executorService.isTerminated()){
                try{
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            long now = System.currentTimeMillis();
            uploadElapsedTime = (now - startTime)/1000.0;
            finalUploadRate = (Double)(((uploadedByte * 8) / 10^6) / uploadElapsedTime);
            finished = true;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HandlerUpload extends Thread {
        URL url;
        public HandlerUpload(URL url) {
            this.url = url;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1000000];
            long startTime = System.currentTimeMillis();
            int timeout = 8;

            while(true){
                try{
                    HttpsURLConnection con;
                    String verifyHost = url.toString().split("://")[1].split(":")[0];
                    con = (HttpsURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Connection","Keep-Alive");
                    con.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
                    con.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            if(url.toString().equals(verifyHost)){
                                return true;
                            }
                            return false;
                        }
                    });
                    con.connect();
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    dos.write(buffer,0,buffer.length);
                    dos.flush();
                    con.getResponseCode();
                    UploadTest.uploadedByte += buffer.length;
                    long endTime = System.currentTimeMillis();
                    double uploadElapsedTime = (endTime - startTime)/1000.0;
                    if(uploadElapsedTime >= timeout){
                        break;
                    }
                    dos.close();
                    con.disconnect();

                }catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
