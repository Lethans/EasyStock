package com.example.easystock.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String name;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String role;
    private boolean isLogged;
    private boolean sync;
    private String androidIdFingerprint;
    private boolean isShowMenu;

    @Ignore
    public User(int id, String username, String name, String lastName, String password, String email, String phone, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.isLogged = false;
        this.androidIdFingerprint = "";
        this.sync = false;
        this.isShowMenu = false;
    }

    @Ignore
    public User(String username, String name, String lastName, String password, String email, String phone, String role) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.isLogged = false;
        this.androidIdFingerprint = "";
        this.sync = false;
        this.isShowMenu = false;
    }

    public User() {
        this.isLogged = false;
        this.androidIdFingerprint = "";
        this.sync = false;
        this.isShowMenu = false;
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

    public String getAndroidIdFingerprint() {
        return androidIdFingerprint;
    }

    public void setAndroidIdFingerprint(String androidIdFingerprint) {
        this.androidIdFingerprint = androidIdFingerprint;
    }

    public boolean isShowMenu() {
        return isShowMenu;
    }

    public void setShowMenu(boolean showMenu) {
        isShowMenu = showMenu;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
