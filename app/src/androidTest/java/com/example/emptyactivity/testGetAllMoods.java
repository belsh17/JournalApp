package com.example.emptyactivity;

import static org.junit.Assert.assertEquals;

import com.example.emptyactivity.Models.Mood;
import com.example.emptyactivity.Database.MoodDAO;

import org.junit.Test;

import java.util.List;

public class testGetAllMoods {

    private MoodDAO moodDAO;

    @Test
    public void testGetAllMoods() {
        Mood mood1 = new Mood();
        mood1.setUserMood("Happy");
        mood1.setUserId(1);
        moodDAO.insert(mood1);

        Mood mood2 = new Mood();
        mood2.setUserMood("Sad");
        mood2.setUserId(2);
        moodDAO.insert(mood2);

        // Retrieve all moods and verify the results
        List<Mood> moods = moodDAO.getAll();
        assertEquals(2, moods.size());
        assertEquals("Happy", moods.get(0).getUserMood());
        assertEquals("Sad", moods.get(1).getUserMood());
    }

}
