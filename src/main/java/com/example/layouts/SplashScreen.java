package com.example.layouts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {

            boolean isLoggedIn = isLoggedIn();
            Intent intent;
            if (isLoggedIn) {

                intent = new Intent(SplashScreen.this, MainScreen.class);
            } else {

                intent = new Intent(SplashScreen.this, loginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }


    private boolean isLoggedIn() {

        SharedPreferences sharedPreferences = getSharedPreferences("login_state", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}
