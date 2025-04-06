package com.example.emptyactivity.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.emptyactivity.Models.Mood;

import java.util.List;

@Dao
public interface MoodDAO {

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    //Insert data into database and avoid conflict automatically
    void insert(Mood moodTrack);

    @Delete
    void delete(Mood moodTrack);

    //gets all data from mood tbl
    @Query("SELECT * FROM moodTrack ORDER BY moodID DESC")
    List<Mood> getAll();

    @Update
    void update(Mood moodTrack);

    // Fetch all moods for a specific user
    @Query("SELECT * FROM moodTrack WHERE user_id = :userId ORDER BY moodID DESC")
    List<Mood> getMoodsForUser(int userId);

    // Delete all moods for a specific user
    @Query("DELETE FROM moodTrack WHERE user_id = :userId")
    void deleteMoodsForUser(int userId);  // Deletes all moods for a specific user

    // Delete all moods
    @Query("DELETE FROM moodTrack")
    void deleteAllMoods(); // Delete all moods
}
