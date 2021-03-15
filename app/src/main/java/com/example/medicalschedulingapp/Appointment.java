package com.example.medicalschedulingapp;
import java.util.Date;
public class Appointment {
    private String medCenterName;
    private String streetName;
    private String city;
    private String state;
    private int zip;
    private Date aptTime;

    public Appointment( String medCenterName, String streetName, String city, String state, int zip, Date aptTime){
        this.medCenterName = medCenterName;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.aptTime = aptTime;
    }

    public String getMedCenterName() {
        return medCenterName;
    }

    public void setMedCenterName(String medCenterName) {
        this.medCenterName = medCenterName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public Date getAptTime() {
        return aptTime;
    }

    public void setAptTime(Date aptTime) {
        this.aptTime = aptTime;
    }
}
