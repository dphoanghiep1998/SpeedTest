package com.example.speedtest.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.speedtest.model.ConnectivityTestModel;

@Database(entities = ConnectivityTestModel.class,version = 1)
public abstract class ConnectivityTestDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "wifiTestDB.db";
    public static ConnectivityTestDatabase instance;
    public static synchronized ConnectivityTestDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,ConnectivityTestDatabase.class,DATABASE_NAME)
                    .build();
        }
        return instance;
    }
    public abstract ConnectivityTestDAO connectivityTestDAO();
}
