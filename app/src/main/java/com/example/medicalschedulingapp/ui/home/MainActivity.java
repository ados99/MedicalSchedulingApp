package com.example.medicalschedulingapp.ui.home;

import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.medicalschedulingapp.EditUserActivity;
import com.example.medicalschedulingapp.MapActivity;
import com.example.medicalschedulingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.geocoder.service.models.GeocoderFeature;
import com.mapbox.geocoder.service.models.GeocoderResponse;
import com.mapbox.geojson.Point;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Button editUserInfo = (Button) findViewById(R.id.edit_user_info);
        editUserInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){openEditUser();}
        });
        Button urgentCares = (Button)findViewById(R.id.button_Urgent_Care);
        Button hospitals = (Button)findViewById(R.id.button_Hospitals);
        Button mentalHealth = (Button)findViewById(R.id.button_MentalH);
        urgentCares.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrgentCares();
            }
        });
        hospitals.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openHospitals();
            }
        });
        mentalHealth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openMentalHealthClinics();
            }
        });

        BottomNavigationItemView map = findViewById(R.id.navigation_dashboard);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });


    }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    public void openMap(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void openEditUser(){
        Intent intent = new Intent(this, EditUserActivity.class);
        startActivity(intent);
    }

    public void openUrgentCares(){
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", "Urgent Cares");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openHospitals(){
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", "Hospital");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openMentalHealthClinics(){
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", "Mental Health Clinics");
        intent.putExtras(bundle);
        startActivity(intent);
    }


}