package com.example.emptyactivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Models.Mood;
import com.example.emptyactivity.ui.theme.mood_history_activity;

public class mood_track extends AppCompatActivity {

    private SeekBar progressSeekBar;
    private EditText editTextMood;
    private Button saveMoodBtn;
    private RoomDB database;
    //private TextView progressText; //displays the progress %
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_track_screen);

        //initialize database
        database = RoomDB.getInstance(this);
        //back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        //edit text for thoughts at the bottom of screen
        editTextMood = findViewById(R.id.moodThought);
        saveMoodBtn = findViewById(R.id.saveMoodBtn);

        //mood buttons
        ImageButton worstBtn = findViewById(R.id.moodWorst);
        ImageButton madBtn = findViewById(R.id.moodMad);
        ImageButton sosoBtn = findViewById(R.id.moodSoso);
        ImageButton goodBtn = findViewById(R.id.moodGood);
        ImageButton greatBtn = findViewById(R.id.moodGreat);

        //seek bar variables
        SeekBar seekBar1 = findViewById(R.id.seekBar1);
        SeekBar seekBar2 = findViewById(R.id.seekBar2);
        SeekBar seekBar3 = findViewById(R.id.seekBar3);

        //code for mood button function
        worstBtn.setOnClickListener(v -> {
            float currentScale = v.getScaleX();

            //code to make button bigger when clicked
            if (currentScale == 1f) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            } else {
                v.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        madBtn.setOnClickListener(v -> {
            float currentScale = v.getScaleX();

            if (currentScale == 1f) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            } else {
                v.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        sosoBtn.setOnClickListener(v -> {
            float currentScale = v.getScaleX();

            if (currentScale == 1f) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            } else {
                v.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        goodBtn.setOnClickListener(v -> {
            float currentScale = v.getScaleX();

            if (currentScale == 1f) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            } else {
                v.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        greatBtn.setOnClickListener(v -> {
            float currentScale = v.getScaleX();

            if (currentScale == 1f) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            } else {
                v.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        //code for the seek bar function
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animateSeekBar(seekBar, progress);
            }

            //used to show interaction when the bar is clicked
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            }

            //used to show no interaction and not clicking anymore
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        //code for the seek bar function
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animateSeekBar(seekBar, progress);
            }

            //used to show interaction when the bar is clicked
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            }

            //used to show no interaction and not clicking anymore
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        //code for the seek bar function
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animateSeekBar(seekBar, progress);
            }

            //used to show interaction when the bar is clicked
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
            }

            //used to show no interaction and not clicking anymore
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.animate().scaleX(1f).scaleY(1f).setDuration(200);
            }
        });

        //code to save mood and thoughts
        saveMoodBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //method created outside of onCreate method so can be used anywhere in this class
                String moodText = editTextMood.getText().toString().trim();
                saveMood(moodText);

            }
        });

    }

    //method to save mood
    public void saveMood(String moodText) {
        //checks if user entered a mood
        if(moodText.isEmpty()) {
            Toast.makeText(this, "Please enter a mood!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1); // Get userID

        Log.d("SaveMood", "UserId Retrieved: " + userId);

        if (userId != -1) { // Ensure valid user
            RoomDB database = RoomDB.getInstance(this);
            Mood mood = new Mood();
            mood.setUserMood(moodText);
            mood.setUserId(userId); // Store user ID

            new Thread(() -> {
                database.moodDAO().insert(mood);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Mood Saved!", Toast.LENGTH_SHORT).show();
                    editTextMood.setText(""); // Clear text field
                    //finish();
                    Intent intent = new Intent(mood_track.this, mood_history_activity.class);
                    startActivity(intent);
                });
            }).start();

        } else {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
        }
    }

    //code for animation when user moves seek bar
    private void animateSeekBar(SeekBar seekBar, int progress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(seekBar, "progress", progress);
        animator.setDuration(300);
        animator.start();

        if (progress < 30) {
            seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else if(progress < 70) {
            seekBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        } else {
            seekBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
    }

}
