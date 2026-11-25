package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    private EditText usernameEdit, passwordEdit;
    private Button login;

    // Hardcoded admin/user details
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        //link variable names declared on top with the UI controls
        usernameEdit = findViewById(R.id.usernameTxt);
        passwordEdit = findViewById(R.id.passwordTxt);
        login = findViewById(R.id.LoginBtn);

        login.setOnClickListener(view -> {
            String username = usernameEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            // If username and password is equal to admin. they are given admin access to enter
            // restaurant and dish screen
            if (username.equals ("admin") && password.equals ("admin")) {
                Intent nextIntent = new Intent(LoginScreen.this, MainAdminScreen.class);
                startActivity(nextIntent);
            } else if (username.equals ("customer") && password.equals ("customer")) {
                // If username and password is equal to customer. they are given customer access to enter
                // order screen
                Intent nextIntent = new Intent(LoginScreen.this, MainCustomerScreen.class);
                startActivity(nextIntent);
            } else {
                // Toast appears tro inform user that username or password is invalid
                Toast.makeText(LoginScreen.this, "Username or Password is invalid, try again", Toast.LENGTH_SHORT).show();
            }


        });
    }
}