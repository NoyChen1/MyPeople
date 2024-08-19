package com.example.mystore;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserData.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
