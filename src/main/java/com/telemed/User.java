package com.telemed;

import java.util.Date;

public class User {

    private int id;
    static int idCounter = 0;
    private int type = 0;
    private String fname;
    private String lname;
    private Date birthday;
    private int mbo;
    private String email;
    private String password;

    public User(String fname, String lname, Date birthday, int mbo, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.birthday = birthday;
        this.mbo = mbo;
        this.email = email;
        this.password = password;

        id = idCounter++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        User.idCounter = idCounter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getMbo() {
        return mbo;
    }

    public void setMbo(int mbo) {
        this.mbo = mbo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}