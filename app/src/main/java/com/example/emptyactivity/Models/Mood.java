package com.example.emptyactivity.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Update;

import java.io.Serializable;

@Entity(tableName="moodTrack")
public class Mood implements Serializable {

    @PrimaryKey(autoGenerate = true) //autoGenerate gen. ID auto.
    private int moodID;

    @ColumnInfo(name = "userMood")
    private String userMood;

    @ColumnInfo(name = "user_id")
    private int userId;

    //getter and setter methods for columns in database
    public int getMoodID() {
        return moodID;
    }
    public void setMoodID(int moodID) {
        this.moodID = moodID;
    }
    public String getUserMood() {
        return userMood;
    }

    public void setUserMood(String userMood) {
        this.userMood = userMood;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
