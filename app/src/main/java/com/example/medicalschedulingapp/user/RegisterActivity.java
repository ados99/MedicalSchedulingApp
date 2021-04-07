package com.example.medicalschedulingapp.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.R;
import com.example.medicalschedulingapp.RegisterFragment;
import com.example.medicalschedulingapp.SingleFragmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.HashMap;

public class RegisterActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){return new RegisterFragment();}

}
