package com.example.medicalschedulingapp;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.SettingsFragment;
import com.example.medicalschedulingapp.SingleFragmentActivity;

public class SettingsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new SettingsFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}