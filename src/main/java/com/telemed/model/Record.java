package com.telemed.model;

import jakarta.persistence.*;

@Entity
public class Record {

    @Id
    @GeneratedValue
    private int id;
    static int idCounter = 0;
    private int sysPressure;
    private int diasPressure;
    private int heartRate;
    private float bodyTemperature;
    private String date;
    private String time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Record() {

    }

    public Record(int sysPressure, int diasPressure, int heartRate, float bodyTemperature, String date, String time, User user) {
        this.sysPressure = sysPressure;
        this.diasPressure = diasPressure;
        this.heartRate = heartRate;
        this.bodyTemperature = bodyTemperature;
        this.date = date;
        this.time = time;
        this.user = user;

        id = idCounter++;
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

    public float getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(float bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
