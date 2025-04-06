package com.example.emptyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    //Declare variables
    private TextView textView;
    private Button loginBtn;
    private Button signUpBtn;


    //portected - accessed from classes within same package
    //onCreate method name, called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //content visble underneath status bar
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        //sets listener on specific view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //applies padding to view
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initialize
        textView = findViewById(R.id.textView1);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);

        //login button
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //code to execute when button is clicked
                Toast.makeText(LoginActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(LoginActivity.this, LoginScreen.class);
                startActivity(intent2);
            }
        });

        //sign up button
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Welcome to your journal!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, SignUpScreen.class);
                startActivity(intent);
            }
        });

    }
}