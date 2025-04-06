package com.example.emptyactivity;

import static org.junit.Assert.assertEquals;

import com.example.emptyactivity.Models.Mood;

import org.junit.Test;

public class testMoodModel {

    @Test
    public void testMoodModel() {
        Mood mood = new Mood();
        mood.setUserMood("Excited");
        mood.setUserId(2);

        // Verify the getter methods
        assertEquals("Excited", mood.getUserMood());
        assertEquals(2, mood.getUserId());
    }

}
