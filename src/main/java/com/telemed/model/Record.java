package com.telemed.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Optional;

@Entity
public class Record {

    @Id
    @GeneratedValue
    private int id;
    private int sysPressure;
    private int diasPressure;
    private int heartRate;
    private String note;
    private String date;
    private String time;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean emergency;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Record() {

    }

    public Record(int sysPressure, int diasPressure, int heartRate, String note, String date, String time, User user) {
        this.sysPressure = sysPressure;
        this.diasPressure = diasPressure;
        this.heartRate = heartRate;
        this.note = note;
        this.date = date;
        this.time = time;
        this.user = user;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSysPressure() {
        return sysPressure;
    }

    public void setSysPressure(int sysPressure) {
        this.sysPressure = sysPressure;
    }

    public int getDiasPressure() {
        return diasPressure;
    }

    public void setDiasPressure(int diasPressure) {
        this.diasPressure = diasPressure;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getEmergency() {return emergency;}

    public void setEmergency(boolean emergency) { this.emergency = emergency;}
}
