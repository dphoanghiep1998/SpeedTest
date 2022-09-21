package com.example.speedtest.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.speedtest.model.ConnectivityTestModel;
import com.example.speedtest.repository.AppRepository;

import java.util.List;

public class WifiTestViewModel  extends AndroidViewModel {
    AppRepository appRepository;

    public WifiTestViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }
    public void insertResultTest(ConnectivityTestModel model){
        appRepository.insertTestResult(model);
    }
    public void deleteResultTest(ConnectivityTestModel model){
        appRepository.deleteTestResult(model);
    }
    public void deleteAllResultTest(){
        appRepository.deleteAllTestResult();
    }
    public LiveData<List<ConnectivityTestModel>> getListResultTest(){
        return appRepository.getTestResultList();
    }
    public MutableLiveData<Boolean> hasInternetConnection = new MutableLiveData<>();

}
