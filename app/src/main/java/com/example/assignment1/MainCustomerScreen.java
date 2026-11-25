package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainCustomerScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer_screen);
    }
    public void toOrderScreen(View v) {
        Intent nextIntent = new Intent(MainCustomerScreen.this, OrderScreen.class);
        startActivity(nextIntent);
    }
}