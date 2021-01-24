package com.example.easystock.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.easystock.BuildConfig;
import com.example.easystock.R;
import com.example.easystock.models.User;

public class UserPreferences {

    private Context mCtx;
    private SharedPreferences mPrefs;

    private static final String PREF_FILE = BuildConfig.APPLICATION_ID.replace(".", "_");
    private static SharedPreferences sharedPreferences = null;


    private static void openPref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static String getValue(Context context, String key, String defaultValue) {
        UserPreferences.openPref(context);
        String result = UserPreferences.sharedPreferences.getString(key, defaultValue);
        UserPreferences.sharedPreferences = null;
        return result;
    }

    public static int getValue(Context context, String key, int defaultValue) {
        UserPreferences.openPref(context);
        int result = UserPreferences.sharedPreferences.getInt(key, defaultValue);
        UserPreferences.sharedPreferences = null;
        return result;
    }

    public static void setValue(Context context, String key, String value) {
        UserPreferences.openPref(context);
        SharedPreferences.Editor prefsPrivateEditor = UserPreferences.sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.apply();
        UserPreferences.sharedPreferences = null;
    }

    public static void setValue(Context context, String key, int value) {
        UserPreferences.openPref(context);
        SharedPreferences.Editor prefsPrivateEditor = UserPreferences.sharedPreferences.edit();
        prefsPrivateEditor.putInt(key, value);
        prefsPrivateEditor.apply();
        UserPreferences.sharedPreferences = null;
    }

    public static void saveUser(Context mCtx, User user) {
        setValue(mCtx, mCtx.getResources().getString(R.string.user_id), user.getId());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_username), user.getUsername());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_name), user.getName());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_lastname), user.getLastName());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_password), user.getPassword());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_email), user.getEmail());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_phone), user.getPhone());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_role), user.getRole());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_fingerprint), user.getAndroidIdFingerprint());
        setValue(mCtx, mCtx.getResources().getString(R.string.user_isLogged), "true");
    }

    public static User getUser(Context mCtx) {
        if (getValue(mCtx, mCtx.getResources().getString(R.string.user_isLogged), "false").equals("true")) {
            User user = new User(
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_id), 0),
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_username), ""),
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_name), ""),
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_lastname), ""),
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_password), ""),
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_email), ""),
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_phone), ""),
                    getValue(mCtx, mCtx.getResources().getString(R.string.user_role), ""));
            user.setAndroidIdFingerprint(getValue(mCtx, mCtx.getResources().getString(R.string.user_fingerprint), ""));
            user.setShowMenu(false);
            return user;
        } else
            return null;

    }


    public static void logOut(Context mCtx) {
        setValue(mCtx, mCtx.getResources().getString(R.string.user_isLogged), "false");
    }

    public static boolean isLoged(Context mCtx) {
        return getValue(mCtx, mCtx.getResources().getString(R.string.user_isLogged), "").equals("true");
    }



/*    public UserPreferences(Context context) {
        this.mCtx = context;
        mPrefs = context.getSharedPreferences(mCtx.getString(R.string.sharedPreferencesKey), Context.MODE_PRIVATE);
    }


    public boolean isSharedPreferencesEmpty(SharedPreferences sharedPreferences) {
        return sharedPreferences.getAll().size() == 0;
    }

    public int getCurrentUserId() {
        return mPrefs.getInt(mCtx.getResources().getString(R.string.currentUser_id), 0);
    }

    public String getCurrentUserRole() {
        return mPrefs.getString(mCtx.getResources().getString(R.string.currentUser_rol), "1");
    }

    public String getCurrentUserName() {
        return mPrefs.getString(mCtx.getResources().getString(R.string.currentUser_username), "username");
    }

    public void setCurrentUserId(int currentUserId) {
        SharedPreferences.Editor sharedEditor = mPrefs.edit();
        sharedEditor.putInt(mCtx.getResources().getString(R.string.currentUser_id), currentUserId);
        sharedEditor.apply();
    }

    public void setCurrentUserRole(String currentUserRole) {
        SharedPreferences.Editor sharedEditor = mPrefs.edit();
        sharedEditor.putString(mCtx.getResources().getString(R.string.currentUser_rol), currentUserRole);
        sharedEditor.apply();
    }

    public void setCurrentUserName(String currentUserName) {
        SharedPreferences.Editor sharedEditor = mPrefs.edit();
        sharedEditor.putString(mCtx.getResources().getString(R.string.currentUser_username), currentUserName);
        sharedEditor.apply();
    }*/


}
