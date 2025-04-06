package com.example.emptyactivity.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.io.Serializable;

@Entity(tableName = "journal")
public class Notes implements Serializable {
    @PrimaryKey(autoGenerate = true) //add annotations to link to usage in database. autoGenerate gen. ID auto.
    private int journalID;

    @ColumnInfo(name = "content") //sets column name to "content"
    private String content = "";

    @ColumnInfo(name = "date")
    private String date = "";

    @ColumnInfo(name = "image_path")  // Store image file path
    private String imagePath = "";

    @ColumnInfo(name = "user_id")
    private int userId;

    //getter and setters for columns in table
    public int getJournalID() {
        return journalID;
    }
    public void setJournalID(int journalID) {
        this.journalID = journalID;
    }


    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
