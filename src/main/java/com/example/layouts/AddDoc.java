package com.example.layouts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoc extends AppCompatActivity {

    private EditText nameEditText, addressEditText, phoneEditText;
    private Button addButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doc);

        nameEditText = findViewById(R.id.editTextName);
        addressEditText = findViewById(R.id.editTextAddress);
        phoneEditText = findViewById(R.id.editTextPhone);
        addButton = findViewById(R.id.addButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("enrolled");

        addButton.setOnClickListener(v -> addDoctor());
    }

    private void addDoctor() {
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        try {
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Doctor doctor = new Doctor(name);

        // Here, we assume the address and phone are stored as separate fields in Firebase
        // You can adjust this according to your Firebase database structure
        doctor.setAddress(address);
        doctor.setPhone(phone);

        String doctorId = databaseReference.push().getKey();
        if (doctorId != null) {
            databaseReference.child(doctorId).setValue(doctor);
            Toast.makeText(this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add doctor", Toast.LENGTH_SHORT).show();
        }

        nameEditText.setText("");
        addressEditText.setText("");
        phoneEditText.setText("");
    }
}
