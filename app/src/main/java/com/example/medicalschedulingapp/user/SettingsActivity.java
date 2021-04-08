package com.example.medicalschedulingapp.user;

import androidx.fragment.app.Fragment;
import com.example.medicalschedulingapp.SingleFragmentActivity;

public class SettingsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new SettingsFragment();
    }
}
