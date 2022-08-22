package com.example.speedtest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.speedtest.model.ConnectivityTestModel;

import java.util.List;

@Dao
public interface ConnectivityTestDAO {
    @Insert
    void insertWifiTestResult(ConnectivityTestModel connectivityTestModel);

    @Delete
    void deleteWifiTestResult(ConnectivityTestModel connectivityTestModel);

    @Query("DELETE FROM connectivity_model")
    void deleteAllWifiTestResult();

    @Query("SELECT * FROM connectivity_model ")
    LiveData<List<ConnectivityTestModel>> getAllWifiTestResults();



}
