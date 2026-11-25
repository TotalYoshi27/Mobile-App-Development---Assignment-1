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

public class RestaurantScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurant, menu);
        return true;
    }

    // To link menu items to restaurant pages
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        int id = item.getItemId();
        if (id == R.id.addResRecord){
            //load AddRecord fragment that has entry form
            loadFragment(new AddRestaurant());
            return true;
        }
        else if (id == R.id.viewResRecords){
            //load ManageRecords fragment for displaying records and clearing records
            loadFragment(new ViewRestaurant());
            return true;
        } else if (id == R.id.editResRecords){
            //load Edit Record Fragment that has entry form
            loadFragment(new SelectEditRestaurantV2());
            return true;
        } else if(id == R.id.adminHome) {
            //To go back to main admin screen
            Intent nextIntent = new Intent(RestaurantScreen.this, MainAdminScreen.class);
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