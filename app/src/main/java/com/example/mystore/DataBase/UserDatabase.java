package com.example.mystore.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mystore.Models.UserData;

@Database(entities = {UserData.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
