package com.example.medicalschedulingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.ui.home.MainActivity;
import com.example.medicalschedulingapp.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserFragment extends Fragment{


    private EditText fNameText, lNameText, emailText, passwordText;
    private Spinner editSex;
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
        editSex = v.findViewById(R.id.edit_sex_spinner);
        passwordText = v.findViewById(R.id.editPassword);
        header = v.findViewById(R.id.edit_account_header);
        greeting = v.findViewById(R.id.user_greeting);
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
                User newUser = new User(fName, lName, email, sex, password);
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
