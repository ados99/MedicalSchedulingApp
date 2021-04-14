package com.example.medicalschedulingapp;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.ui.appointments.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditAppointmentFragment extends Fragment {
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");
    private String key;
    public EditAppointmentFragment(String key){
        this.key = key;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_edit_appointment, container, false);
        EditText loc = v.findViewById(R.id.location);
        EditText add = v.findViewById(R.id.address);
        EditText doc = v.findViewById(R.id.doctor_name);
        EditText rea = v.findViewById(R.id.reason);
        DatePicker date = v.findViewById(R.id.datePicker1);

        dbUsers.child(fUser.getUid()).child("appointments").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Appointment.class)!=null)
                {
                    String l = dataSnapshot.getValue(Appointment.class).getLocation();
                    loc.setText(l);
                    String a = dataSnapshot.getValue(Appointment.class).getAddress();
                    add.setText(a);
                    String r = dataSnapshot.getValue(Appointment.class).getReason();
                    rea.setText(r);
                    String doctor = dataSnapshot.getValue(Appointment.class).getDoctor();
                    doc.setText(doctor);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button newApt = v.findViewById(R.id.edit_appointment_button);
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
                dbUsers.child(fUser.getUid()).child("appointments").child(key).setValue(nApt);
                Toast.makeText(getContext(), "Appointment edited!", Toast.LENGTH_SHORT).show();

                getActivity().onBackPressed();
            }



        });
        return v;
    }
}
