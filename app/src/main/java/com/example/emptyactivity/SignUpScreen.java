package com.example.emptyactivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Database.UserDao;
import com.example.emptyactivity.Models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpScreen extends AppCompatActivity {
    //declare variables
    private EditText suUsername, suPassword, suEmail, suPasswordConfirm;
    private Button suButton;
    private RoomDB db;
    private UserDao userDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Initialize Room Database
        db = Room.databaseBuilder(getApplicationContext(), RoomDB.class, "user-database").build();
        userDao = db.userDao();

        // Executor for background tasks
        executorService = Executors.newSingleThreadExecutor();

        // Link the layout to variables
        suUsername = findViewById(R.id.suUsername);
        suPassword = findViewById(R.id.suPassword);
        suPasswordConfirm = findViewById(R.id.suPasswordConfirm);
        suEmail = findViewById(R.id.suEmail);
        suButton = findViewById(R.id.suButton);

        suButton.setOnClickListener(view -> {
            String username2 = suUsername.getText().toString();
            String password2 = suPassword.getText().toString();
            String password3 = suPasswordConfirm.getText().toString();
            String email2 = suEmail.getText().toString();

            // Check if fields are empty
            if (username2.isEmpty() || password2.isEmpty() || email2.isEmpty()) {
                Toast.makeText(SignUpScreen.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;  // Return early if fields are empty
            }

            // Password validation (min 8 chars, at least 1 digit)
            if (password2.length() < 8 || !password2.matches(".*\\d.*")) {
                Toast.makeText(SignUpScreen.this, "Password must be at least 8 characters and contain at least 1 digit", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password2.equals(password3)) {
                Toast.makeText(SignUpScreen.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Basic email validation
            if (!email2.contains("@") || !email2.contains(".")) {
                Toast.makeText(SignUpScreen.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {
                try {
                    // Check if the username already exists
                    int count = userDao.checkUsernameExists(username2);
                    if (count > 0) {
                        runOnUiThread(() ->
                                Toast.makeText(SignUpScreen.this, "Username already exists", Toast.LENGTH_SHORT).show()
                        );
                        return;  // Stop execution if username already exists
                    }

                    // Hash the password before saving it
                    String hashedPassword = hashPassword(password2);

                    // Create a new user object
                    User newUser = new User(username2, email2, hashedPassword);

                    // Insert the new user into the database
                    userDao.insert(newUser);

                    // Store userId in SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("UserId", newUser.getUserID());
                    editor.apply();
                    Log.d("SignUpScreen", "UserId saved: " + newUser.getUserID());


                    runOnUiThread(() -> {
                        Toast.makeText(SignUpScreen.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpScreen.this, MainActivity.class);
                        intent.putExtra("USERNAME", username2);
                        startActivity(intent);
                        finish();  // Close the sign-up activity
                    });
                } catch (Exception e) {
                    Log.e("SignUpScreen", "Error inserting user", e);
                    runOnUiThread(() -> Toast.makeText(SignUpScreen.this, "Signup failed", Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    // Helper method to hash passwords using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;  // Return null if hashing fails
        }
    }
}
