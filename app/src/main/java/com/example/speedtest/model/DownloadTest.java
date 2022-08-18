package com.example.speedtest.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

public class DownloadTest extends Thread {
    // De lay thong tin download can gui file len server, dua vao server response de danh gia cac thong so download file

    //domain cua file can down
    public String fileUrl = "";
    //thoi gian bat dau down (mls)
    long startTime = 0;
    //thoi gian ket thuc down (mls)
    long endTime = 0;
    //elapsed = (end - start)/1000 (s)
    double downloadElapsedTime = 0;
    //download rate mbps : megabit per second = (downloadedByte * 8  / 10^6) / (elapsed time * 1000) (1byte = 10^-6 megabit)
    double finalDownloadRate = 0.0;
    // byte da download duoc
    int downloadedByte = 0;
    // la download rate tai 1 thoi diem xac dinh
    double instantDownloadRate = 0;
    // flag check.
    boolean finished = false;
    //timeout de break loop , khong tiep tuc gui down nua
    int timeout = 8;
    // url server de down file
    HttpURLConnection httpURLConnection = null;


    public double getFinalDownloadRate() {
        return round(finalDownloadRate,2);
    }


    public double getInstantDownloadRate() {
        return instantDownloadRate;
    }

    public void setInstantDownloadRate(int downloadedByte,double downloadElapsedTime) {
        if(downloadedByte >= 0){
            this.instantDownloadRate = round((Double)(((downloadedByte * 8) /10^6) / downloadElapsedTime),2);
        }else{
            this.instantDownloadRate = 0.0;
        }
    }

    public boolean isFinished() {
        return finished;
    }



    // constructor truyen vao file url
    public DownloadTest(String fileUrl){
        this.fileUrl = fileUrl;
    }
    // ham lam tron`
    private double round(double value,int places){
        if(places < 0) throw  new IllegalArgumentException();

        BigDecimal bigDecimal;

        try{
            bigDecimal = new BigDecimal(value);

        }catch (Exception e){
            return 0.0;
        }
        //lam` tron` len
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);

        //tra ve double
        return  bigDecimal.doubleValue();
    }

    @Override
    public void run() {
        URL url;
        downloadedByte = 0;
        int responseCode = 0;

        // cac file can gui - co the sua ....
        List<String> fileUrls = new ArrayList<>();
        fileUrls.add(fileUrl + "10M.iso");

        // tao thoi gian bat dau
        startTime = System.currentTimeMillis();

        loop: for (String filelink: fileUrls){
            try {
                //tao doi tuong url
                url = new URL(filelink);
                //phan nay mau` me` qua, cha can cung duoc
                //exp : https://abc.com/random4000x4000.jpg -> abc.com
                //ket noi server
                httpURLConnection = (HttpURLConnection) url.openConnection();
                //SSLSocketFactory can be used to validate the identity of the HTTPS server against a
                // list of trusted certificates and to authenticate to the HTTPS server using a private key.
                //SSLSocketFactory will enable server authentication when supplied with a truststore file
                // containg one or several trusted certificates. The client secure socket will reject the connection
                // during the SSL session handshake if the target HTTPS server attempts to authenticate itself with a non-trusted certificate.
                //domain name la ten mien, con hostname la ten may chu

                httpURLConnection.connect();
                //lay response code tra ve 200-500 ??
                responseCode = httpURLConnection.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
                break loop;
            }
            try{
                if(responseCode == 200){
                    byte[] buffer = new byte[1024];
                    InputStream inputStream = httpURLConnection.getInputStream();
                    int len = 0;
                    while((len = inputStream.read(buffer)) != 1){
                        downloadedByte += len;
                        endTime = System.currentTimeMillis();
                        downloadElapsedTime = (endTime - startTime)/1000;
                        setInstantDownloadRate(downloadedByte,downloadElapsedTime);
                        if(downloadElapsedTime >= timeout){
                            break loop;
                        }

                    }
                    inputStream.close();
                    httpURLConnection.disconnect();
                }else{
                    System.out.printf("Link not found");
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("DOWNLOAD", "run: "+e.getMessage());
            }


        }
        endTime = System.currentTimeMillis();
        downloadElapsedTime = (endTime - startTime) /1000;
        finalDownloadRate = round((Double)(((downloadedByte * 8 /1000000)) / downloadElapsedTime),2);
        finished = true;


    }
}
