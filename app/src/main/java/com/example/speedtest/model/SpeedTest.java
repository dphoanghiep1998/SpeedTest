package com.example.speedtest.model;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class SpeedTest extends AsyncTask<Void, Void, SpeedTestReport> {
    private SpeedTestReport mFinalReport;
    Object sync = new Object();

    @Override
    protected SpeedTestReport doInBackground(Void... voids) {
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
               mFinalReport = report;
               synchronized (sync){
                   sync.notify();
               }
            }


            @Override
            public void onProgress(float percent, SpeedTestReport report) {

            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                Log.e("ERROR", "onError: "  +errorMessage );
            }
        });
        speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/10M.iso");
        speedTestSocket.startUpload("http://ipv4.ikoula.testdebit.info/", 1000000);
        try{
            sync.wait();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mFinalReport;
    }

    @Override
    protected void onPostExecute(SpeedTestReport speedTestReport) {
        super.onPostExecute(speedTestReport);
    }
}
