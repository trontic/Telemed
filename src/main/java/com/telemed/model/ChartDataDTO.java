package com.telemed.model;

public class ChartDataDTO {
    private String date;
    private String time;
    private int sysPressure;
    private int diasPressure;

    public ChartDataDTO() {
    }

    // Constructor with parameters
    public ChartDataDTO(String date, String time, int sysPressure, int diasPressure ) {
        this.date = date;
        this.time = time;
        this.sysPressure = sysPressure;
        this.diasPressure = diasPressure;


    }

    // Getters and setters
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
}
