package com.example.emptyactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;

import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Database.UserDao;
import com.example.emptyactivity.Models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginScreen extends AppCompatActivity {

    //Declare variables
    private EditText lsUsername, lsPassword;
    private Button btnLogin;
    private RoomDB db;
    private UserDao userDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //Initialize room database
        db = Room.databaseBuilder(getApplicationContext(), RoomDB.class, "user-database")
                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();

        //Executor for background tasks
        executorService = Executors.newSingleThreadExecutor();

        //back button to return to previous activity
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        //link layout to login variables
        lsUsername = findViewById(R.id.lsUsername);
        lsPassword = findViewById(R.id.lsPassword);
        btnLogin = findViewById(R.id.login2_btn);

        //function for login button from login screen
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //initialize variables
                String username = lsUsername.getText().toString();
                String password = lsPassword.getText().toString();

                //check if fields are empty
                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginScreen.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; //stops further execution
                }

                //check if password is valid
                if(password.length() < 8 || !password.matches(".*\\d.*")){
                    Toast.makeText(LoginScreen.this, "Password must be at least 8 characters and contain at least 1 digit", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(() -> {
                try{
                    //method from userDAO retrieve user by username
                    User user = userDao.getUserByUsername(username);

                    //user is not null
                    if (user != null) {
                        //Uses verifyPassword method from Password UTil class
                        boolean passwordMatches = PasswordUtil.verifyPassword(password, user.getPassword());
                        Log.d("LoginScreen", "User found: " + username + ", Password Match: " + passwordMatches);

                        if (passwordMatches) {
                            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            //Editor used to make changes to shared preference data
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("userId", user.getUserID());
                            editor.apply();

                            runOnUiThread(() -> {
                                Toast.makeText(LoginScreen.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                                intent.putExtra("USERNAME", username);
                                startActivity(intent);
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(LoginScreen.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        Log.d("LoginScreen", "User not found in database: " + username);
                        runOnUiThread(() -> {
                            Toast.makeText(LoginScreen.this, "User does not exist.", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    Log.e("LoginScreen", "Error during login", e);
                }

                });


            }
        });


    }

}
