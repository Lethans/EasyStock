package com.example.easystock.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String lastName;
    private String password;
    private String phone;
    private String role;
    private boolean isLogged;
    private boolean sync;
    private String fingerprint;
    private boolean isShowMenu;

    @Ignore
    public User(int id, String name, String lastName, String password, String phone, String role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.isLogged = false;
        this.fingerprint = "NO";
        this.sync = false;
        this.isShowMenu = false;
    }
    @Ignore
    public User(String name, String lastName, String password, String phone, String role) {
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.isLogged = false;
        this.fingerprint = "NO";
        this.sync = false;
        this.isShowMenu = false;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public boolean isShowMenu() {
        return isShowMenu;
    }

    public void setShowMenu(boolean showMenu) {
        isShowMenu = showMenu;
    }
}
