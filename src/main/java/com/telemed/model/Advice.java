package com.telemed.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Advice {

    @Id
    @GeneratedValue
    private int id;

    private String advice;

    public Advice() {
    }

    public Advice(String advice) {
        this.advice = advice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
