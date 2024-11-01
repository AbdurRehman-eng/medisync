package com.example.layouts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DiscussionForum extends AppCompatActivity {

    EditText et_name;
    Button b_login;
    String status;
    String name;
    String[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);


        et_name = findViewById(R.id.et_Name);
        b_login = findViewById(R.id.b_Login);


        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        name = preferences.getString("name", "");


        if (!name.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), DiscussionForum1.class));
            finish();
        } else {
            new RefreshData().execute();
        }

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString().trim();
                et_name.setText("");

                if (!name.isEmpty()) {
                    boolean userExists = false;
                    if (users != null) {
                        for (String user : users) {
                            if (name.equals(user)) {
                                userExists = true;
                                break;
                            }
                        }
                    }
                    if (!userExists) {
                        new SendData().execute();
                    } else {
                        Toast.makeText(DiscussionForum.this, "User exists!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DiscussionForum.this, "Write any name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SendData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            status = NetworkHandler.sendData("users", name);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            b_login.setText("Loading...");
            b_login.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            switch (status) {
                case "SUCCESS":
                    // Save name and login
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", name);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), DiscussionForum1.class));
                    finish();
                    break;
                case "ERROR":
                    Toast.makeText(DiscussionForum.this, "Network error", Toast.LENGTH_SHORT).show();
                    b_login.setText("Login");
                    b_login.setEnabled(true);
                    break;
            }
        }
    }

    private class RefreshData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            status = NetworkHandler.downloadData("users");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            b_login.setText("Loading...");
            b_login.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (!status.isEmpty()) {
                users = status.split("\n");
            }
            b_login.setText("Login");
            b_login.setEnabled(true);
        }
    }
}
