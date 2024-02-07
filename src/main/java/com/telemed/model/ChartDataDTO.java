package com.telemed.model;

public class ChartDataDTO {
    private String date;
    private int sysPressure;

    public ChartDataDTO() {
    }

    // Constructor with parameters
    public ChartDataDTO(String date, int sysPressure ) {
        this.date = date;
        this.sysPressure = sysPressure;


    }

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSysPressure() {
        return sysPressure;
    }

    public void setSysPressure(int sysPressure) {
        this.sysPressure = sysPressure;
    }


}
