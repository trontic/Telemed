package com.telemed;

import java.util.Date;

public class Record {

    private int sysPressure;
    private int diasPressure;
    private int heartRate;
    private float bodyTemperature;
    private String note;
    private Date date;

    public Record(int sysPressure, int diasPressure, int heartRate, float bodyTemperature, String note, Date date) {
        this.sysPressure = sysPressure;
        this.diasPressure = diasPressure;
        this.heartRate = heartRate;
        this.bodyTemperature = bodyTemperature;
        this.note = note;
        this.date = date;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
