package com.example.medicalschedulingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.EditUserActivity;
import com.example.medicalschedulingapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment implements View.OnClickListener{
    private Button mLogout, mEditUserInfo;
    private FirebaseAuth fAuth;
    @Override
    public void onAttach(@NonNull Context context){super.onAttach(context);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View v;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);
        v = inflater.inflate(R.layout.fragment_settings, container, false);

        //Initiate Buttons
        mLogout = v.findViewById(R.id.logout_button);
        mEditUserInfo = v.findViewById(R.id.edit_user_info_button);

        mLogout.setOnClickListener(this);
        mEditUserInfo.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v){
        final int viewId = v.getId();
        if(viewId == R.id.logout_button){
            fAuth.getInstance().signOut();
            Intent registerIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(registerIntent);
        }else if(viewId == R.id.edit_user_info_button){
            Intent registerIntent = new Intent(getActivity(), EditUserActivity.class);
            startActivity(registerIntent);
        }else{
            Log.i("Error", "Invalid Button Clicked!");
        }
    }
}