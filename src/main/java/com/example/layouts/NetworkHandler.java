package com.example.layouts;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkHandler {
    public static String downloadData(String filename) {
        StringBuilder data = new StringBuilder();
        try {
            URL url = new URL("https://medisyncapp.000webhostapp.com/" + filename + ".txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.isEmpty()) {
                    data.append(str).append("\n");
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public static String sendData(String fileName, String message) {
        try {
            URL url = new URL("https://medisyncapp.000webhostapp.com/add_data.php");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("file", fileName.trim() + ".txt")
                    .appendQueryParameter("message", message.trim());
            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return "SUCCESS";
            } else {
                return "ERROR";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}

