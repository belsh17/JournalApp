package com.example.emptyactivity;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.emptyactivity.Database.RoomDB;

public class MyApplication extends Application {
    private static RoomDB database;
     @Override
    public void onCreate() {
         super.onCreate();
         database = Room.databaseBuilder(getApplicationContext(),
                 RoomDB.class, "NoteApp")
                 .allowMainThreadQueries()
                 .build();
     }

     public static RoomDB getDatabase(Context context){
         if (database == null){
             database = Room.databaseBuilder(context, RoomDB.class,
                     "NoteApp")
                     .allowMainThreadQueries()
                     .build();
         }
         return database;
     }
}
