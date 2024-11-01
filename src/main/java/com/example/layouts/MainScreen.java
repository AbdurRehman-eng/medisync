package com.example.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    private ViewPager bannerViewPager;
    private BannerAdapter bannerAdapter;
    private List<Integer> imageResIds;
    private Handler handler;
    private Runnable runnable;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        TextView usernameTextView = findViewById(R.id.nameshow);
        usernameTextView.setText("Welcome, " + username + "!");

        // Setup banner images
        bannerViewPager = findViewById(R.id.banner_view_pager);
        imageResIds = new ArrayList<>();
        imageResIds.add(R.drawable.doctor_pic_magic_zbjo);
        imageResIds.add(R.drawable.__1_dimens_magic_hwur);
        imageResIds.add(R.drawable.medicine_delivery___made_with_postermywall);
        imageResIds.add(R.drawable.__1_dimens_magic_vqlv);

        bannerAdapter = new BannerAdapter(this, imageResIds);
        bannerViewPager.setAdapter(bannerAdapter);


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == imageResIds.size()) {
                    currentPage = 0;
                }
                bannerViewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(runnable);


        Button adminPanelButton = findViewById(R.id.adminpanel);
        adminPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminPanelIntent = new Intent(MainScreen.this, AdminAdd.class);
                startActivity(adminPanelIntent);
            }
        });


        Button dButton = findViewById(R.id.dforum);
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dIntent = new Intent(MainScreen.this, DiscussionForum.class);
                startActivity(dIntent);
            }
        });


        Button searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainScreen.this, Search.class);
                startActivity(searchIntent);
            }
        });


        Button appointmentButton = findViewById(R.id.appoint);
        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointmentIntent = new Intent(MainScreen.this, Appointment.class);
                startActivity(appointmentIntent);
            }
        });


        Button myAppointmentsButton = findViewById(R.id.my);
        myAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAppointmentsIntent = new Intent(MainScreen.this, MyAppointments.class);
                startActivity(myAppointmentsIntent);
            }
        });


        Button doc = findViewById(R.id.doc);
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mdocIntent = new Intent(MainScreen.this, AdminAdd1.class);
                startActivity(mdocIntent);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab_logout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();

        // Redirect to the login screen
        Intent intent = new Intent(MainScreen.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
}
