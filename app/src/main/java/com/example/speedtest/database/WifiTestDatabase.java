package com.example.speedtest.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.speedtest.model.WifiTestModel;

@Database(entities = WifiTestModel.class,version = 1)
public abstract class WifiTestDatabase extends RoomDatabase {
}
