package com.example.easystock.listeners;

import com.example.easystock.models.User;

public interface GetUserFingerprintListener {

    void onGetUser(User user);
    void onNotGetUser(String msj);
}
