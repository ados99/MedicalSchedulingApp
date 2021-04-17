package com.example.medicalschedulingapp;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.ui.appointments.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ViewAppointmentFragment extends Fragment{
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");
    private String key;
    public ViewAppointmentFragment(String key){
        this.key = key;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View v;



        v = inflater.inflate(R.layout.fragment_view_appointment, container, false);

        TextView loc = v.findViewById(R.id.location);
        TextView add = v.findViewById(R.id.address);
        TextView doc = v.findViewById(R.id.doctor_name);
        TextView rea = v.findViewById(R.id.reason);
        TextView date = v.findViewById(R.id.date);
        TextView time = v.findViewById(R.id.time);
         dbUsers.child(fUser.getUid()).child("appointments").child(key).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                String l = dataSnapshot.getValue(Appointment.class).getLocation();
                loc.setText(l);
                String a = dataSnapshot.getValue(Appointment.class).getAddress();
                add.setText(a);
                String d = dataSnapshot.getValue(Appointment.class).getDate();
                date.setText(d);
                String r = dataSnapshot.getValue(Appointment.class).getReason();
                rea.setText(r);
                String doctor = dataSnapshot.getValue(Appointment.class).getDoctor();
                doc.setText(doctor);
                String t = dataSnapshot.getValue(Appointment.class).getTime();
                time.setText(t);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

        Button newApt = v.findViewById(R.id.add_appointment_button);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("View Appointment");
        actionBar.setDisplayHomeAsUpEnabled(true);
        return v;
    }


}
