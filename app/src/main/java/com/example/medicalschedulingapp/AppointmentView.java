package com.example.medicalschedulingapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.example.medicalschedulingapp.ui.appointments.Appointment;
import com.example.medicalschedulingapp.ui.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AppointmentView extends AppCompatActivity {
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");
    SwipeMenuListView lv;
    ArrayList<String> appointments = new ArrayList<String>();
    final ArrayList<String> keyList = new ArrayList<>();
    ArrayAdapter<String> appointmentsAdapter;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_view);

        lv = findViewById(R.id.listviewtxt);
        appointmentsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,appointments);
        lv.setAdapter(appointmentsAdapter);

        DatabaseReference dbRef = dbUsers.child(fUser.getUid()).child("appointments");
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(Appointment.class).getTime() + " on " + snapshot.getValue(Appointment.class).getDate() + " for "
                        + snapshot.getValue(Appointment.class).getReason();
                appointments.add(value);
                keyList.add(snapshot.getKey());
                appointmentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(Appointment.class).getTime() + " on " + snapshot.getValue(Appointment.class).getDate() + " for "
                        + snapshot.getValue(Appointment.class).getReason();
                appointments.add(value);
                appointmentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot messages : dataSnapshot.getChildren()) {
                            keyList.add(messages.getKey());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        /*handle errors*/
                    }
                });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(R.color.purple_500);
                // set item width
                openItem.setWidth(300);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(R.color.purple_200);
                // set item width
                editItem.setWidth(300);
                // set item title
                editItem.setTitle("Edit");
                // set item title fontsize
                editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(500);
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitle("Delete");
                // set a icon
                // add to menu
                menu.addMenuItem(deleteItem);
                
                
                
            }
        };

// set creator
        lv.setMenuCreator(creator);

        lv.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String item = appointmentsAdapter.getItem(position);
                switch (index) {
                    case 0:
                        // open
                        openView(keyList.get(position));
                        break;
                    case 1:
                        appointmentsAdapter.remove(item);
                       editAppointment(keyList.get(position));
                        break;
                    case 2:
                        dbRef.child(keyList.get(position)).removeValue();
                        keyList.remove(position);
                        appointmentsAdapter.remove(item);
                        appointmentsAdapter.notifyDataSetChanged();
                        showMessage();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });




        getSupportActionBar().setTitle("Your Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button newApt = findViewById(R.id.btnAddItem);
        newApt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addAppointment();
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        BottomNavigationItemView home = findViewById(R.id.navigation_home);
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                openHome();

            }
        });
        BottomNavigationItemView appointments = findViewById(R.id.navigation_notifications);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        MenuItem menuItem = navView.getMenu().getItem(2);
        menuItem.setChecked(true);
    }

    public void openHome()
    {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openView(String key)
    {
        Intent intent = new Intent(this, ViewAppointmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void addAppointment()
    {
        Intent intent = new Intent(this,NewAppointmentActivity.class);
        startActivity(intent);
    }

    public void editAppointment(String key)
    {
        Intent intent = new Intent(this,EditAppointmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        intent.putExtras(bundle);
        startActivity(intent);
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

    public void showMessage(){
        Toast.makeText(this, "Appointment deleted!", Toast.LENGTH_SHORT).show();
    }
}
