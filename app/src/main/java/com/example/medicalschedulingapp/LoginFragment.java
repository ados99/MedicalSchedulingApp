package com.example.medicalschedulingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.ui.home.MainActivity;
import com.example.medicalschedulingapp.user.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private TextView mUsernameText, mPasswordText, loginHeader;
    private ProgressBar loginProgBar;
    private FirebaseAuth fAuth;

    @Override
    public void onAttach(@NonNull Context context){super.onAttach(context);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View v;
        Activity activity = requireActivity();
        v = inflater.inflate(R.layout.fragment_login, container, false);
        loginHeader = v.findViewById(R.id.login_page_header);
        mUsernameText = v.findViewById(R.id.loginEmail);
        mPasswordText = v.findViewById(R.id.loginPassword);
        fAuth = FirebaseAuth.getInstance();
        loginProgBar = v.findViewById(R.id.progressBar);

        Button loginButton = v.findViewById(R.id.login_button);
        Button registerButton = v.findViewById(R.id.register_button);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v){
        final int viewId = v.getId();
        if(viewId == R.id.login_button){
            login();
        }else if(viewId == R.id.register_button){
            Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(registerIntent);
        }else{
            Log.i("Error", "Invalid Button Clicked!");
        }
    }

    /**
     * Login function that gets user fields and authenticates with FirebaseAuth.
     */
    private void login(){
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
                Toast.makeText(getContext(), "Signed in successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Sign in failed", Toast.LENGTH_SHORT).show();
            }
            loginProgBar.setVisibility(View.INVISIBLE);
        });
    }

}
