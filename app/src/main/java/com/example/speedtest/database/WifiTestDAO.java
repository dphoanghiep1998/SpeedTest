package com.example.speedtest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.speedtest.model.WifiTestModel;

import java.util.List;

@Dao
public interface WifiTestDAO {
    @Insert
    void insertWifiTestResult(WifiTestModel wifiTestModel);

    @Delete
    void deleteWifiTestResult(WifiTestModel wifiTestModel);

    @Query("DELETE FROM wifi_model")
    void deleteAllWifiTestResult();

    @Query("SELECT * FROM wifi_model")
    LiveData<List<WifiTestModel>> getAllWifiTestResults();



}
