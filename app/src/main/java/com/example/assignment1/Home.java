package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    // Takes user to login Screen
    public void loginScreen(View v) {
        Intent nextIntent = new Intent(Home.this, LoginScreen.class);
        startActivity(nextIntent);
    }
}