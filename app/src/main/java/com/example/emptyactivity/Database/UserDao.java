package com.example.emptyactivity.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.emptyactivity.Models.User;

import java.util.List;

@Dao
public interface UserDao {
    //Insert a new user into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    // Get a user by username
    @Query("SELECT * FROM Users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    // Checking if a username already exists
    @Query("SELECT COUNT(*) FROM Users WHERE username = :username")
    int checkUsernameExists(String username);

    // Retrieve all users
    @Query("SELECT * FROM Users")
    List<User> getAllUsers();


}

