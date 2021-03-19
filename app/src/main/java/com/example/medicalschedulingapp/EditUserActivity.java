package com.example.medicalschedulingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditUserActivity extends AppCompatActivity {
    private EditText fNameText, lNameText, emailText, sexText, passwordText;
    private TextView header, greeting;
    private Button editInfoButton, deleteAccountButton;
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = db.getReference().child("users");

    /**
     * Look, I know that putting everything into one activity is bad practice,
     * but I haven't caught up with school so I don't quite know how to implement
     * a fragment just yet.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        fNameText = findViewById(R.id.editFirstName);
        lNameText = findViewById(R.id.editLastName);
        emailText = findViewById(R.id.editEmail);
        sexText = findViewById(R.id.editSex);
        passwordText = findViewById(R.id.editPassword);
        header = findViewById(R.id.edit_account_header);
        greeting = findViewById(R.id.user_greeting);
        deleteAccountButton = findViewById(R.id.delete_account_button);
        editInfoButton = findViewById(R.id.edit_info_button);

        dbUsers.child(fUser.getUid()).child("firstName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    greeting.append("User!");
                }
                else {
                    greeting.append(task.getResult().getValue().toString());
                }
            }
        }).toString();

        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = fNameText.getText().toString().trim();
                String lName = lNameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String sex = sexText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                fUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Notification", "User email updated");
                        }
                    }
                });

                fUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Notification", "User password updated");
                        }
                    }
                });

                //Update the Db
                User newUser = new User(fName, lName, email, sex, password);
                dbUsers.child(fUser.getUid()).setValue(newUser);

                //Start MainActivity, basically go back to menu
                Intent intent = new Intent(EditUserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUsers.child(fUser.getUid()).removeValue();
                fUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Deleted Account", "User account deleted.");
                        }
                    }
                });
                Intent intent = new Intent(EditUserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
