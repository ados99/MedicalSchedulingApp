package com.example.medicalschedulingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppointmentReview extends AppCompatActivity {

    TextView myDate;
  //  Intent intent = getIntent();
//    String myValue = getIntent().getExtras().getString("test");

   // int myValue = getIntent().getExtras().getInt("test");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_review);

     // myDate = findViewById(R.id.datePickedText);
     // myDate.setOnClickListener(new View.OnClickListener() {
Intent intent = getIntent();
String text = intent.getStringExtra(Intent.EXTRA_TEXT);

//TextView textView = (TextView) findViewById(R.id.textView);
//textView.setText(text);
myDate.setText(text);

    }
}