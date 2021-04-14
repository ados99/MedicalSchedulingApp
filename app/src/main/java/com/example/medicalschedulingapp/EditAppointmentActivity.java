package com.example.medicalschedulingapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;


public class EditAppointmentActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){
        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("key");
        return new EditAppointmentFragment(key);}

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