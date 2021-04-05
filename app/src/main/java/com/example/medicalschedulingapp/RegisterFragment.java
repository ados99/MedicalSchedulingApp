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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment implements View.OnClickListener{
    private TextView rUsernameText, rPasswordText, registerHeader, rFName, rLName;
    private FirebaseAuth fAuth;
    private ProgressBar registerProgBar;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");
    private Spinner rSexSpinner;

    @Override
    public void onAttach(@NonNull Context context){super.onAttach(context);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View v;
        v = inflater.inflate(R.layout.fragment_register, container, false);
        registerHeader = v.findViewById(R.id.register_page_header);
        Button registerButton = v.findViewById(R.id.signup_from_register);
        rUsernameText = v.findViewById(R.id.signup_email);
        rPasswordText = v.findViewById(R.id.signup_password);
        rFName = v.findViewById(R.id.signup_firstName);
        rLName = v.findViewById(R.id.signup_lastName);
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
        if(TextUtils.isEmpty(username)){
            rUsernameText.setError("Need a username");
            return;
        }
        if(TextUtils.isEmpty(password)){
            rPasswordText.setError("Need a password");
            return;
        }
        if(password.length() < 6){
            rPasswordText.setError("Password is too short!");
            return;
        }
        if(TextUtils.isEmpty(fName)){
            rFName.setError("First Name cannot be empty!");
            return;
        }
        if(TextUtils.isEmpty(lName)){
            rLName.setError("Last Name cannot be empty!");
            return;
        }

        registerProgBar.setVisibility(View.VISIBLE);
        fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener((task) -> {
            if(task.isSuccessful()){
                Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                User newUser = new User(fName, lName, username, sex, password);
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
