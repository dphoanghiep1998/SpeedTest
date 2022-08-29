package com.example.speedtest.core.worker;

import com.example.speedtest.core.serverSelector.ServerSpeedtest;
import com.example.speedtest.core.upload.HttpUploadTest;

public abstract class SpeedtestWorker extends Thread {
    private ServerSpeedtest speedtest;

    private boolean stopASAAP=false;
    private double dl=-1,ul=-1,ping=-1,jitter=-1;


    @Override
    public void run() {
        for(;;){
            pingTest();
            dlTest();
            ulTest();
            if(stopASAAP){
                return;
            }
        }
    }

    protected  void ulTest(){
        HttpUploadTest httpUploadTest = new HttpUploadTest(speedtest.getUploadAddress().replace("http://","https://"));

    };

    protected  void dlTest(){

    };

    protected  void pingTest(){

    };

    public abstract void onDownloadUpdate(double dl,double progress);
    public abstract void onUploadUpdate(double dl,double progress);
    public abstract void onPingJitterUpdate(double ping, double jitter, double progress);
    public abstract void onEnd();
    public abstract void onCriticalFailure(String err);
    public abstract void onAbort();

}
