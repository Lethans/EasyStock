package com.example.easystock.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.easystock.R;

public class SharedController {

    private Context mCtx;
    private SharedPreferences sharedPreferences;

    public SharedController(Context context) {
        this.mCtx = context;
        sharedPreferences = context.getSharedPreferences(mCtx.getString(R.string.sharedPreferencesKey), Context.MODE_PRIVATE);
    }


    public boolean isSharedPreferencesEmpty(SharedPreferences sharedPreferences) {
        return sharedPreferences.getAll().size() == 0;
    }

    public int getCurrentUserId() {
        return sharedPreferences.getInt(mCtx.getResources().getString(R.string.currentUser_id), 0);
    }

    public String getCurrentUserRole() {
        return sharedPreferences.getString(mCtx.getResources().getString(R.string.currentUser_rol), "1");
    }

    public String getCurrentUserName() {
        return sharedPreferences.getString(mCtx.getResources().getString(R.string.currentUser_username), "username");
    }

    public void setCurrentUserId(int currentUserId) {
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.putInt(mCtx.getResources().getString(R.string.currentUser_id), currentUserId);
        sharedEditor.apply();
    }

    public void setCurrentUserRole(String currentUserRole) {
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.putString(mCtx.getResources().getString(R.string.currentUser_rol), currentUserRole);
        sharedEditor.apply();
    }

    public void setCurrentUserName(String currentUserName) {
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.putString(mCtx.getResources().getString(R.string.currentUser_username), currentUserName);
        sharedEditor.apply();
    }


}
