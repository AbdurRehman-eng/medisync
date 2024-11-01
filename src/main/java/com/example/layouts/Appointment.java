// Appointment.java
package com.example.layouts;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Appointment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private RecyclerView recyclerView;
    private Adapter1 doctorAdapter;
    private List<Doctor> doctorList;
    private DatabaseReference databaseReference;

    private String selectedDate;
    private String selectedTime;
    private String selectedDoctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        doctorList = new ArrayList<>();
        doctorAdapter = new Adapter1(doctorList, new Adapter1.OnItemClickListener() {
            @Override
            public void onItemClick(Doctor doctor) {
                selectedDoctorName = doctor.getName();
                showDatePicker();
            }
        });
        recyclerView.setAdapter(doctorAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("appointment");

        retrieveDoctors();
    }

    private void retrieveDoctors() {
        Query query = FirebaseDatabase.getInstance().getReference("enrolled");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    doctorList.add(doctor);
                }
                doctorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(Appointment.this, "Failed to retrieve doctors: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        showTimePicker();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        selectedTime = hourOfDay + ":" + minute;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();
            saveAppointment(userEmail, selectedDate, selectedTime, selectedDoctorName);
        } else {
            Toast.makeText(Appointment.this, "Please sign in to make an appointment", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAppointment(String userEmail, String selectedDate, String selectedTime, String doctorName) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query to check for duplicate appointments
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean duplicateFound = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AppointmentDetails existingAppointment = snapshot.getValue(AppointmentDetails.class);
                    if (existingAppointment != null && existingAppointment.getDate().equals(selectedDate) && existingAppointment.getTime().equals(selectedTime)) {
                        duplicateFound = true;
                        break;
                    }
                }

                if (duplicateFound) {
                    Toast.makeText(Appointment.this, "Duplicate appointment found for the same date and time.", Toast.LENGTH_SHORT).show();
                } else {
                    // No duplicate found, proceed to save the appointment
                    String appointmentId = databaseReference.push().getKey();
                    AppointmentDetails appointmentDetails = new AppointmentDetails(userEmail, uid, selectedDate, selectedTime);
                    appointmentDetails.setDoctorName(doctorName); // Set the doctor's name

                    if (appointmentId != null) {
                        databaseReference.child(appointmentId).setValue(appointmentDetails);
                        Toast.makeText(Appointment.this, "Appointment saved successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Appointment.this, "Failed to save appointment", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(Appointment.this, "Error checking for duplicate appointments: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
