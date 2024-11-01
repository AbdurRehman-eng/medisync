package com.example.layouts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminPanel extends AppCompatActivity {

    private EditText nameEditText, formulaEditText, priceEditText;
    private Button addButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_panel);


        databaseReference = FirebaseDatabase.getInstance().getReference();


        nameEditText = findViewById(R.id.editTextName);
        formulaEditText = findViewById(R.id.editTextFormula);
        priceEditText = findViewById(R.id.editTextPrice);
        addButton = findViewById(R.id.addButton);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToDatabase();
            }
        });
    }

    private void addProductToDatabase() {
        String name = nameEditText.getText().toString().trim();
        String formula = formulaEditText.getText().toString().trim().toLowerCase();
        String price = priceEditText.getText().toString().trim();

    try {
        if (name.isEmpty() || formula.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
    }catch(Exception e){
        e.printStackTrace();
    }
     try {
         String productId = databaseReference.child("products").push().getKey();
         Product product = new Product(name, formula, price);
         databaseReference.child("products").child(productId).setValue(product);

         Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
     }
     catch (Exception e){
         e.printStackTrace();
     }

        nameEditText.setText("");
        formulaEditText.setText("");
        priceEditText.setText("");
    }
}
