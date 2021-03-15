package com.example.medicalschedulingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private TextView mUsernameText, mPasswordText, registerHeader;
    private FirebaseAuth fAuth;
    private ProgressBar registerProgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerHeader = findViewById(R.id.register_page_header);
        registerButton = findViewById(R.id.signup_from_register);
        mUsernameText = findViewById(R.id.signup_email);
        mPasswordText = findViewById(R.id.signup_password);
        registerProgBar = findViewById(R.id.signup_progbar);
        fAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsernameText.getText().toString().trim();
                String password = mPasswordText.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    mUsernameText.setError("Need a username");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPasswordText.setError("Need a password");
                    return;
                }
                if(password.length() < 6){
                    mPasswordText.setError("Password is too short!");
                    return;
                }
                //TODO - Make sure no more than one email per account!

                registerProgBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener((task) -> {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                    registerProgBar.setVisibility(View.INVISIBLE);
                });

            }
        });
    }
}
