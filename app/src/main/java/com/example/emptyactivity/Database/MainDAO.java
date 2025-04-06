package com.example.emptyactivity.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.emptyactivity.Models.Notes;

import java.util.List;

@Dao
public interface MainDAO {
    //creating an insert method
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    //insert data in db + deal with conflicts auto.
    void insert(Notes journal);

    @Delete
    void delete(Notes journal);

    //gets all data from journal tbl
    @Query("SELECT * FROM journal ORDER BY journalID DESC") //orders by ID in descending order
    List<Notes> getAll(); //gets all items from journal tbl

    //updates data in database columns
    @Update
    void update(Notes journal);

    // Fetch all notes for a specific user
    @Query("SELECT * FROM journal WHERE user_Id = :userId ORDER BY date DESC")
    List<Notes> getNotesForUser(int userId);

    // Fetch a specific note by user ID and journal ID
    @Query("SELECT * FROM journal WHERE user_Id = :userId AND journalId = :journalId LIMIT 1")
    Notes getNoteByUserIdAndJournalId(int userId, int journalId);

}