package com.example.layouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<AppointmentDetails> appointmentList;

    public AppointmentAdapter(List<AppointmentDetails> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentDetails appointment = appointmentList.get(position);
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTextView;
        private TextView appointmentTimeTextView;
        private TextView appointmentDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            appointmentTimeTextView = itemView.findViewById(R.id.appointmentTimeTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
        }

        public void bind(AppointmentDetails appointment) {
            if (appointment != null) {
                doctorNameTextView.setText(appointment.getDoctorName());
                appointmentTimeTextView.setText(appointment.getTime());
                appointmentDateTextView.setText(appointment.getDate());
            } else {
                // Handle null appointment object
            }
        }
    }
}
