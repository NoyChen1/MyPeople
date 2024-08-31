package com.example.mystore.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mystore.Models.UserData;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addUser(UserData user);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllUsers(List<UserData> users);
    @Update
    public void updateUser(UserData user);
    @Delete
    public void deleteUser(UserData user);

    @Query("SELECT * FROM Users WHERE id == :userId")
    public UserData getUser(int userId);
    @Query("SELECT * FROM Users WHERE email == :userEmail")
    public UserData getUser(String userEmail);
    @Query("SELECT * FROM Users")
    public List<UserData> getAllUsers();
    @Query("DELETE FROM Users")
    public void delete();

}
