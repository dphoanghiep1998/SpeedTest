package com.example.speedtest.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.speedtest.database.ConnectivityTestDatabase;
import com.example.speedtest.model.ConnectivityTestModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private ConnectivityTestDatabase connectivityTestDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AppRepository(Context context){
        connectivityTestDatabase = ConnectivityTestDatabase.getInstance(context);
    }

    public void insertTestResult(ConnectivityTestModel model){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                connectivityTestDatabase.connectivityTestDAO().insertWifiTestResult(model);
            }
        });
    }
    public void deleteAllTestResult(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                connectivityTestDatabase.connectivityTestDAO().deleteAllWifiTestResult();
            }
        });
    }
    public void deleteTestResult(ConnectivityTestModel model){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                connectivityTestDatabase.connectivityTestDAO().deleteWifiTestResult(model);
            }
        });
    }
    public LiveData<List<ConnectivityTestModel>> getTestResultList(){
        return connectivityTestDatabase.connectivityTestDAO().getAllWifiTestResults();
    }
}
