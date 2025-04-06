package com.example.emptyactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Models.Notes;

import java.util.List;

public class ViewNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        //Code to display the text from the database
        TextView date = findViewById(R.id.dateView);
        TextView content = findViewById(R.id.contentView);
        ImageView imageViewNote = findViewById(R.id.imageViewNote);

        // Retrieve the journalId passed in the intent
        Intent intent = getIntent();

        //linking user to journal
        // Retrieve the logged-in user's ID
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        int journalId = intent.getIntExtra("JOURNAL_ID", -1);

        if (userId != -1 && journalId != -1) {
            new Thread(() -> {
                RoomDB database = RoomDB.getInstance(this);
                Notes selectedNote = database.mainDAO().getNoteByUserIdAndJournalId(userId, journalId); // Fetch the note by both userId and journalId

                runOnUiThread(() -> {
                    if (selectedNote != null) {
                        content.setText(selectedNote.getContent());
                        date.setText(selectedNote.getDate());

                        //LOad the image from the database
                        String imagePath = selectedNote.getImagePath();
                        if(imagePath != null && !imagePath.isEmpty()) {
                            try {
                                //method from Uri class to parse the image path into a URI
                                Uri imageUri = Uri.parse(imagePath);
                                //converts the image URI into a bitmap. used to display image in image view
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                                // Get EXIF metadata
                                ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(imageUri));
                                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                                // Rotate the image if needed
                                Matrix matrix = new Matrix();
                                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                                    matrix.postRotate(90);
                                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                                    matrix.postRotate(180);
                                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                                    matrix.postRotate(270);
                                }

                                // Create a rotated bitmap
                                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                                // Set the rotated image
                                imageViewNote.setImageBitmap(rotatedBitmap);
                                imageViewNote.setVisibility(View.VISIBLE);
                            } catch(Exception e) {
                                imageViewNote.setVisibility(View.GONE);
                            }
                            } else{
                                imageViewNote.setVisibility(View.GONE);
                            }
                    } else {
                        content.setText("Note not found.");
                        date.setText("");
                        imageViewNote.setVisibility(View.GONE);
                    }

                    //}
                });
            }).start();
        } else {
            // Handle case when userId is not found in SharedPreferences
            content.setText("No user found.");
            date.setText("");
            imageViewNote.setVisibility(View.GONE);
        }

        //end of linking user and journal

    }
}
