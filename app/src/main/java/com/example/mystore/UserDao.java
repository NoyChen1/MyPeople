package com.example.mystore;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addUser(UserData user);

    @Update
    public void updateUser(UserData user);

    @Delete
    public void deleteUser(UserData user);

    @Query("SELECT * FROM Users WHERE id == :userId")
    public UserData getUser(int userId);
    @Query("SELECT * FROM Users")
    public List<UserData> getAllUsers();
}