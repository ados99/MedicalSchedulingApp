package com.example.medicalschedulingapp.ui.appointments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalschedulingapp.R;

public class AppointmentFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i(AppointmentFragment.class.getSimpleName(), "This is onCreateView() for Dashboard Fragment.");
        View root = inflater.inflate(R.layout.fragment_appointment, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        return root;
    }
}