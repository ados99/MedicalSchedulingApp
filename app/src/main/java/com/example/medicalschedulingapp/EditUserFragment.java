package com.example.medicalschedulingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.medicalschedulingapp.ui.home.MainActivity;
import com.example.medicalschedulingapp.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditUserFragment extends Fragment{

    private static final Pattern PASS_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w\\p{Punct}]{1,64}@[a-z0-9_-]+.[a-z]{2,}$");
    private EditText fNameText, lNameText, emailText, passwordText;
    private Spinner editSex;
    private DatePicker dob;
    private TextView header, greeting;
    private Button editInfoButton, deleteAccountButton;
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");

    @Override
    public void onAttach(@NonNull Context context){super.onAttach(context);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View v;
        v = inflater.inflate(R.layout.fragment_edit_user, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Edit User Information");
        actionBar.setDisplayHomeAsUpEnabled(true);
        fNameText = v.findViewById(R.id.editFirstName);
        lNameText = v.findViewById(R.id.editLastName);
        emailText = v.findViewById(R.id.editEmail);
        dob = v.findViewById(R.id.datePicker1);
        editSex = v.findViewById(R.id.edit_sex_spinner);
        passwordText = v.findViewById(R.id.editPassword);
        deleteAccountButton = v.findViewById(R.id.delete_account_button);
        editInfoButton = v.findViewById(R.id.edit_info_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSex.setAdapter(adapter);

        dbUsers.child(fUser.getUid()).child("firstName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                    greeting.append(" User!");
//                }
//                else {
//                    if(task!=null) {
//                        greeting.append(" " + task.getResult().getValue().toString());
//                    }
//                }
            }
        }).toString();

        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = fNameText.getText().toString().trim();
                String lName = lNameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String sex = editSex.getSelectedItem().toString().trim();
                String password = passwordText.getText().toString().trim();
                Matcher m = EMAIL_REGEX.matcher(email);
                Matcher p = PASS_REGEX.matcher(password);

                if(TextUtils.isEmpty(password)){
                    passwordText.setError("Need a password");
                    return;
                }
                if(!m.matches()){
                    emailText.setError("Email is not valid!");
                    return;
                }
                if(!p.matches()){
                    passwordText.setError("Password needs to have at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 of @,$,!,%,*,?,&");
                    return;
                }
                if(TextUtils.isEmpty(fName)){
                    fNameText.setError("First Name cannot be empty!");
                    return;
                }
                if(TextUtils.isEmpty(lName)) {
                    lNameText.setError("Last Name cannot be empty!");
                    return;
                }

                int   day  = dob.getDayOfMonth();
                int   month= dob.getMonth();
                int   year = dob.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formatedDate = sdf.format(calendar.getTime());

                fUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Notification", "User email updated");
                        }
                    }
                });

                fUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Notification", "User password updated");
                        }
                    }
                });

                //Update the Db
                User newUser = new User(fName, lName, email, formatedDate, sex, password);
                dbUsers.child(fUser.getUid()).setValue(newUser);

                //Start MainActivity, basically go back to menu
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUsers.child(fUser.getUid()).removeValue();
                fUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Deleted Account", "User account deleted.");
                        }
                    }
                });
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }


}
