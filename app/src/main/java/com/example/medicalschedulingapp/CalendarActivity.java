package com.example.medicalschedulingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {

  TextView tvDate;
  TextView c_heckDateTextView;
  EditText etDate;
  Button sButton;
  DatePickerDialog.OnDateSetListener setListener;
  String gAvailable = "";
  int Year, Month, Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        tvDate = findViewById(R.id.tv_date);
        etDate = findViewById(R.id.et_date);
        c_heckDateTextView = findViewById(R.id.checkDateTextView);
        sButton = findViewById(R.id.schedule_button);
       // Button appButton = findViewById(R.id.myApp_button);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

tvDate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CalendarActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();

    }
});

setListener = new DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month+1;
        String date = month +"/"+day+"/"+year;
        tvDate.setText(date);


    }
};

etDate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(CalendarActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                Year = year;
                Month = month;
                Day = day;

                month = month+1;
                String date = month+"/"+day+"/"+year;
                etDate.setText(date);

             //   c_heckDateTextView.setText(date);

                gAvailable = date;
            }
        }, year,month,day);

       datePickerDialog.show();

    }
});


//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.covid_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner.setAdapter(adapter);

    }

    public void available(View view) {

        Toast.makeText(getBaseContext(), "Date Available",
                Toast.LENGTH_LONG).show();
      //  Intent intent = new Intent(this, AppointmentReview.class);
        c_heckDateTextView.setText(gAvailable);
        sButton.setVisibility(sButton.VISIBLE);

        //intent.putExtra("test", "tester");  //;gAvailable);
//        intent.putExtra(intent.EXTRA_TEXT, "tester");
//        startActivity(intent);
    }
    public void scheduleNow(View view) {
        AddCalendarEvent();

//        @Override
//        public void onComplete(@NonNull Task<Void> task) {
//            if (task.isSuccessful()) {
//                Log.d("Deleted Account", "User account deleted.");
//            }
//        }

    }

    public void myAppButton(View view) {

        Toast.makeText(getBaseContext(), "Your Appointments are located in main",
                Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, CalendarActivity.class);
//        intent.putExtra("key", "Schedule Appointment");
//        startActivity(intent);
    }

    public void AddCalendarEvent() {
        Calendar calendarEvent = Calendar.getInstance();
        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");
        i.putExtra(CalendarContract.Events.TITLE, "Your Medical Appointment");
        i.putExtra(CalendarContract.Events.EVENT_LOCATION, "5585 E. State St, Columbus, OH 43230");
        i.putExtra(CalendarContract.Events.DESCRIPTION,"Your medical appointment have been scheduled through the Medical app");

        GregorianCalendar getDate = new GregorianCalendar(Year, Month, Day);
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getDate.getTimeInMillis()); //.getTimeInMillis());
        i.putExtra("allDay", true);
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getDate.getTimeInMillis());

        //i.putExtra("beginTime", calendarEvent.getTimeInMillis());
       // i.putExtra("allDay", true);
        //i.putExtra("rule", "FREQ=YEARLY");
       // i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
       // i.putExtra("title", "Calendar Event");
        startActivity(i);
    }
}