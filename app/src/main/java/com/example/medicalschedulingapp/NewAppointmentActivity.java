package com.example.medicalschedulingapp;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;


public class NewAppointmentActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){return new NewAppointmentFragment();}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
