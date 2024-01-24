package com.telemed.model;

import jakarta.persistence.*;

@Entity
public class Therapy {

    @Id
    @GeneratedValue
    private int id;
    private String nameMedicine;
    private float dosage;
    private float quantity;
    private String dayPart;
    private boolean iregular;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private Record record;

    public Therapy() {
    }

    public Therapy(String nameMedicine, float dosage, float quantity, String dayPart, boolean iregular, User user, Record record) {
        this.nameMedicine = nameMedicine;
        this.dosage = dosage;
        this.quantity = quantity;
        this.dayPart = dayPart;
        this.iregular = iregular;
        this.user = user;
        this.record = record;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameMedicine() {
        return nameMedicine;
    }

    public void setNameMedicine(String nameMedicine) {
        this.nameMedicine = nameMedicine;
    }

    public float getDosage() {
        return dosage;
    }

    public void setDosage(float dosage) {
        this.dosage = dosage;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getDayPart() {
        return dayPart;
    }

    public void setDayPart(String dayPart) {
        this.dayPart = dayPart;
    }

    public boolean isIregular() {
        return iregular;
    }

    public void setIregular(boolean iregular) {
        this.iregular = iregular;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

}
