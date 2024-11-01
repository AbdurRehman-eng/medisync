package com.example.layouts;



public class Doctor {
    private String name;
    private String address; // Add this field
    private String phone;   // Add this field

    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(Doctor.class)
    }

    public Doctor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Add setAddress method
    public void setAddress(String address) {
        this.address = address;
    }

    // Add setPhone method
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
