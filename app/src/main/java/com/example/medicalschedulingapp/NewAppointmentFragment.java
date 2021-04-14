package com.example.medicalschedulingapp;

import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import org.joda.time.DateTime;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.ui.appointments.Appointment;
import com.example.medicalschedulingapp.ui.home.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NewAppointmentFragment extends Fragment{
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_new_appointment, container, false);
        EditText loc = v.findViewById(R.id.location);
        EditText add = v.findViewById(R.id.address);
        EditText doc = v.findViewById(R.id.doctor_name);
        EditText rea = v.findViewById(R.id.reason);
        DatePicker date = v.findViewById(R.id.datePicker1);
        TimePicker time = v.findViewById(R.id.timePicker1);
        Button newApt = v.findViewById(R.id.add_appointment_button);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Add Appointment");
        actionBar.setDisplayHomeAsUpEnabled(true);
        TimePicker picker= v.findViewById(R.id.timePicker1);
        picker.setIs24HourView(false);
        newApt.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String location = loc.getText().toString().trim();
                String address = add.getText().toString().trim();
                String doctor = doc.getText().toString().trim();
                String reason = rea.getText().toString().trim();

                int   day  = date.getDayOfMonth();
                int   month= date.getMonth();
                int   year = date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formatedDate = sdf.format(calendar.getTime());

                int dec = picker.getHour()/12;
                String amOrPm = dec == 1?"PM":"AM";
                String formatedTime = picker.getHour()%12 + ":" + String.format("%02d", picker.getMinute()) + " " + amOrPm;


                Appointment nApt = new Appointment();
                nApt.setLocation(location);
                nApt.setAddress(address);
                nApt.setDoctor(doctor);
                nApt.setReason(reason);
                nApt.setDate(formatedDate);
                nApt.setTime(formatedTime);

                Map<String, Object> pairs = new HashMap<String, Object>();
                String key = dbUsers.child(fUser.getUid()).child("appointments").push().getKey();
                pairs.put(key, nApt);
                dbUsers.child(fUser.getUid()).child("appointments").updateChildren(pairs);

                Toast.makeText(getContext(), "New appointment added!", Toast.LENGTH_SHORT).show();

                getActivity().onBackPressed();
            }
            


        });
        return v;
    }
}
