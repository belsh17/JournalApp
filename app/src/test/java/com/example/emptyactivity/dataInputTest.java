package com.example.emptyactivity;


import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.emptyactivity.Database.MoodDAO;
import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Models.Mood;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)  // Run this test as an Android instrumentation test
@SmallTest  // Optional, used to indicate this is a unit/instrumentation test
public class dataInputTest {

    private RoomDB db;
    private MoodDAO moodDAO;  // Use MoodDAO for testing

    @Before
    public void setUp() {
        // Create an in-memory database for testing purposes
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RoomDB.class)
                .allowMainThreadQueries()  // Allow querying on the main thread for testing
                .build();
        moodDAO = db.moodDAO();  // Initialize MoodDAO
    }

    @Test
    public void testInsertMood() {
        // Insert a mood entry
        Mood mood = new Mood();
        mood.setUserMood("Happy");
        mood.setUserId(1);
        moodDAO.insert(mood);  // Insert using moodDAO

        // Retrieve all moods
        List<Mood> moods = moodDAO.getAll();  // Use moodDAO to get all moods

        // Assert that the mood was inserted and retrieved correctly
        assertEquals(1, moods.size());
        assertEquals("Happy", moods.get(0).getUserMood());
        assertEquals(1, moods.get(0).getUserId());
    }

    @After
    public void tearDown() {
        db.close();  // Close the database after the test
    }

}
