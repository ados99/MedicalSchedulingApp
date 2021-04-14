package com.example.medicalschedulingapp.user;
import com.example.medicalschedulingapp.ui.appointments.Appointment;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String sex;
    private String password;
    private ArrayList<Appointment> appointments;

    public User(String firstName, String lastName, String email, String dob, String sex, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dob;
        this.sex = sex;
        this.password = password;
        this.appointments = new ArrayList<Appointment>();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
