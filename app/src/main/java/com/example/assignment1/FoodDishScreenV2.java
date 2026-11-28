package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FoodDishScreenV2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_dish_screen_v2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fooddish, menu);
        return true;
    }
    // To link menu items to restaurant pages
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        int id = item.getItemId();
        if (id == R.id.addFoodDishRecord){
            //load AddRecord fragment that has entry form
            loadFragment(new AddFoodDish());
            return true;
        }
        else if (id == R.id.viewFoodDishRecords){
            //load ManageRecords fragment for displaying records and clearing records
            loadFragment(new ViewFoodDish());
            return true;
        }
        if (id == R.id.editFoodDishRecord){
            //load Edit Record Fragment that has entry form
            loadFragment(new SelectEditFoodDish());
            return true;
        } else if(id == R.id.adminHome) {
            //To go back to main admin screen
            Intent nextIntent = new Intent(FoodDishScreenV2.this, MainAdminScreen.class);
            startActivity(nextIntent);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }
    // To transition to next fragment
    public void loadFragment(Fragment fragment){
        //create an instance of FragmentManager
        androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        // with new Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}