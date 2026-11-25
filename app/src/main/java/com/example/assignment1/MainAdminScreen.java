package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainAdminScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_screen);
    }
    public void toResScreen(View v) {
        Intent nextIntent = new Intent(MainAdminScreen.this, RestaurantScreen.class);
        startActivity(nextIntent);
    }
    public void toFoodDishScreen(View v) {
        Intent nextIntent = new Intent(MainAdminScreen.this, FoodDishScreenV2.class);
        startActivity(nextIntent);
    }
}