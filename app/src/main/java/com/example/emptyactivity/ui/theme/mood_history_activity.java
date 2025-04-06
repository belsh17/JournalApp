package com.example.emptyactivity.ui.theme;

//import static com.google.android.material.button.MaterialButtonToggleGroup.CornerData.start;
import static java.util.Collections.emptyList;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emptyactivity.Adapters.MoodAdapter;
import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Models.Mood;
import com.example.emptyactivity.R;

import java.util.Collections;
import java.util.List;

public class mood_history_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoodAdapter moodAdapter;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_history);

        //back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        //bin functionality
        ImageButton binButton = findViewById(R.id.binButton);

        //code for mood tabs
        //recyclerTabs is the recycler view from mood history layout
        recyclerView = findViewById(R.id.recyclerTabs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = RoomDB.getInstance(this);

        moodAdapter = new MoodAdapter(Collections.emptyList());  // Initialize with empty list
        recyclerView.setAdapter(moodAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        Log.d("MoodHistory", "UserId Retrieved: " + userId);

        if(userId != -1) {
            new Thread(() -> {
                List<Mood> moodList = database.moodDAO().getMoodsForUser(userId);
                runOnUiThread(() -> {
                    moodAdapter.updateData(moodList);

                });
            }).start();
        }

        //button to delete the list of moods for specific user
        binButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sets up alert dialog to ask user if they are sure they want to delete all moods
                new AlertDialog.Builder(mood_history_activity.this)
                        .setMessage("Are you sure you want to delete all moods?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            //method defined below to delete all moods
                            deleteMoods();
                        })
                        .setNegativeButton("No", (dialog, id) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });

    }

    //method to delete all moods for specific user
    private void deleteMoods() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);  // Get the userId of the logged-in user

        if (userId != -1) {
            new Thread(() -> {
                //method defined in moodDAO to delete all moods for specific user
                database.moodDAO().deleteMoodsForUser(userId);
                runOnUiThread(() -> {
                    moodAdapter.updateData(Collections.emptyList());
                    Toast.makeText(mood_history_activity.this, "All moods cleared!", Toast.LENGTH_SHORT).show();
                });
            }).start();
        } else {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
        }
    }
}

//No adapter attached; skipping layout