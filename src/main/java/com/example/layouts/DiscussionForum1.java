
package com.example.layouts;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;


public class DiscussionForum1 extends AppCompatActivity {

    EditText et_input;
    Button b_send;
    ScrollView scrollView1, scrollView2;
    LinearLayout linearLayout1, linearLayout2;
    String status;
    String name;
    String message;
    boolean isScrollView1Visible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum1);


        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        name = preferences.getString("name", "");

        et_input = findViewById(R.id.et_input);
        b_send = findViewById(R.id.b_send);
        scrollView1 = findViewById(R.id.scrollView1);
        scrollView2 = findViewById(R.id.scrollView2);
        linearLayout1 = findViewById(R.id.linear_layout1);
        linearLayout2 = findViewById(R.id.linear_layout2);


        et_input.requestFocus();

        b_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        refreshChatData();
    }

    private void sendMessage() {
        message = et_input.getText().toString().trim();
        if (!message.isEmpty()) {
            et_input.setText(""); // Clear the input field
            message = name + ": " + message;
            new SendData().execute();
        }
    }

    private void refreshChatData() {
        new RefreshData().execute();
    }

    private class SendData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            status = NetworkHandler.sendData("chat", message);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if ("ERROR".equals(status)) {
                Toast.makeText(DiscussionForum1.this, "Network error", Toast.LENGTH_SHORT).show();
            } else {
                // Scroll to the bottom of the visible ScrollView
                if (isScrollView1Visible) {
                    scrollView1.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView1.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } else {
                    scrollView2.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView2.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        }
    }

    private class RefreshData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            status = NetworkHandler.downloadData("chat");
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (!status.isEmpty()) {

                if (isScrollView1Visible) {
                    updateScrollView(linearLayout1, status);
                    isScrollView1Visible = false;
                } else {
                    updateScrollView(linearLayout2, status);
                    isScrollView1Visible = true;
                }
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshChatData();
                }
            }, 3000);
        }


        private void updateScrollView(LinearLayout linearLayout, String data) {
            linearLayout.removeAllViews(); // Clear existing views
            TextView textView = new TextView(DiscussionForum1.this);
            textView.setText(data.trim());
            linearLayout.addView(textView);
            scrollView1.setVisibility(isScrollView1Visible ? View.VISIBLE : View.GONE);
            scrollView2.setVisibility(isScrollView1Visible ? View.GONE : View.VISIBLE);
        }
    }
}
