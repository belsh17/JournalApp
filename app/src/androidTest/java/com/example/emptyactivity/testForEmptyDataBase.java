package com.example.emptyactivity;

import static org.junit.Assert.assertEquals;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.emptyactivity.Database.MoodDAO;
import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Models.Mood;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class testForEmptyDataBase {

    private RoomDB db;
    private MoodDAO moodDAO;

    @Before
    public void setUp() {
        // Create an in-memory database for testing purposes
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RoomDB.class)
                .allowMainThreadQueries() // Allow main thread queries for testing
                .build();
        moodDAO = db.moodDAO();  // Initialize the MoodDAO
    }

    @Test
    public void testDatabaseIsEmpty() {
        // Retrieve all moods
        List<Mood> moods = moodDAO.getAll();

        // Verify that no moods exist in the database
        assertEquals(0, moods.size());
    }

    @After
    public void tearDown() {
        db.close();  // Close the database after the test
    }
}
