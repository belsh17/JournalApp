
package com.example.emptyactivity.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.emptyactivity.Models.Mood;
import com.example.emptyactivity.Models.Notes;
import com.example.emptyactivity.Models.User;

@androidx.room.Database(
        entities = {Notes.class, User.class, Mood.class},
        //version must be updated each time table is edited with new columns...
        version = 5,
//        autoMigrations = {
//                @AutoMigration(from = 1, to = 2),
//                @AutoMigration(from = 2, to = 3)
//        },
        exportSchema = true
)

//abstract class for database
public abstract class RoomDB extends RoomDatabase {

    //declare variables
    private static RoomDB database;
    private static String DATABASE_NAME = "NoteApp";

//    method to get the instance of the database, getInstance method
    public synchronized static RoomDB getInstance(Context context){
        if(database == null){
            //if it is null then creates a new database
            database = Room.databaseBuilder(context.getApplicationContext(),
                    //specifies class of database to be created
                    RoomDB.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    //abstract method for DAO (abstract class has no method body)
    public abstract MainDAO mainDAO();
    public abstract UserDao userDao();
    public abstract MoodDAO moodDAO();
}

