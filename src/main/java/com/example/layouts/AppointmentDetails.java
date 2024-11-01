package com.example.layouts;
public class AppointmentDetails {
    private String userEmail;
    private String uid;
    private String date;
    private String time;
    private String doctorName;

    public AppointmentDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(AppointmentDetails.class)
    }

    public AppointmentDetails(String userEmail, String uid, String date, String time) {
        this.userEmail = userEmail;
        this.uid = uid;
        this.date = date;
        this.time = time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
