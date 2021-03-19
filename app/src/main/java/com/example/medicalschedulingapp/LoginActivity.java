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

public class LoginActivity extends AppCompatActivity {
    private Button loginButton, registerButton;
    private TextView mUsernameText, mPasswordText, loginHeader;
    private FirebaseAuth fAuth;
    private ProgressBar loginProgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginHeader = findViewById(R.id.login_page_header);
        loginButton = findViewById(R.id.login_button);
        mUsernameText = findViewById(R.id.loginEmail);
        mPasswordText = findViewById(R.id.loginPassword);
        loginProgBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsernameText.getText().toString().trim();
                String password = mPasswordText.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    mUsernameText.setError("Need a username!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPasswordText.setError("Need a password!");
                    return;
                }
                loginProgBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener((task) -> {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, EditUserActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    }
                    loginProgBar.setVisibility(View.INVISIBLE);
                });

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
