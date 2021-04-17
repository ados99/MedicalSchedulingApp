package com.example.medicalschedulingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.ui.appointments.Appointment;
import com.example.medicalschedulingapp.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RegisterFragment extends Fragment implements View.OnClickListener{
    private TextView rUsernameText, rPasswordText, registerHeader, rFName, rLName;
    private DatePicker dob;
    private FirebaseAuth fAuth;
    private ProgressBar registerProgBar;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");
    private Spinner rSexSpinner;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w\\p{Punct}]{1,64}@[a-z0-9_-]+.[a-z]{2,}$");
    private static final Pattern PASS_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    @Override
    public void onAttach(@NonNull Context context){super.onAttach(context);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View v;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Register User");
        v = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = v.findViewById(R.id.signup_from_register);
        rUsernameText = v.findViewById(R.id.signup_email);
        rPasswordText = v.findViewById(R.id.signup_password);
        rFName = v.findViewById(R.id.signup_firstName);
        rLName = v.findViewById(R.id.signup_lastName);
        dob = v.findViewById(R.id.datePicker1);
        registerProgBar = v.findViewById(R.id.signup_progbar);
        rSexSpinner = v.findViewById(R.id.signup_sex_spinner);
        fAuth = FirebaseAuth.getInstance();

        //Necessary for spinner to work correctly
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rSexSpinner.setAdapter(adapter);

        registerButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {

        String username = rUsernameText.getText().toString().trim();
        String password = rPasswordText.getText().toString().trim();
        String fName = rFName.getText().toString().trim();
        String lName = rLName.getText().toString().trim();
        String sex = rSexSpinner.getSelectedItem().toString().trim();
        Matcher m = EMAIL_REGEX.matcher(username);
        Matcher p = PASS_REGEX.matcher(password);

        if(TextUtils.isEmpty(username)){
            rUsernameText.setError("Need a username");
            return;
        }
        if(dob == null){
            rUsernameText.setError("Need a date of birth");
            return;
        }
        if(!m.matches()){
            rUsernameText.setError("Email is not valid!");
            return;
        }
        if(!p.matches()){
            rPasswordText.setError("Password needs to have at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 of @,$,!,%,*,?,&");
            return;
        }
        if(TextUtils.isEmpty(fName)){
            rFName.setError("First Name cannot be empty!");
            return;
        }
        if(TextUtils.isEmpty(lName)) {
            rLName.setError("Last Name cannot be empty!");
            return;
        }

        int   day  = dob.getDayOfMonth();
        int   month= dob.getMonth();
        int   year = dob.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = sdf.format(calendar.getTime());

        registerProgBar.setVisibility(View.VISIBLE);
        fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener((task) -> {
            if(task.isSuccessful()){
                Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                User newUser = new User(fName, lName, username, formatedDate,sex, password);
                dbUsers.child(fAuth.getUid()).setValue(newUser);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
            }
            registerProgBar.setVisibility(View.INVISIBLE);
        });

    }
}
