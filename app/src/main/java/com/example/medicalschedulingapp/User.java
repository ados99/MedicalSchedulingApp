package com.example.medicalschedulingapp;
import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String sex;
    private ArrayList<Appointment> appointments;

    public User(String firstName, String lastName, String email, LocalDate dob, String sex){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dob;
        this.sex = sex;
        this.appointments = new ArrayList<Appointment>();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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
}
