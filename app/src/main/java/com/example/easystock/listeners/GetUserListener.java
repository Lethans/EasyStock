package com.example.easystock.listeners;

import com.example.easystock.models.User;

public interface GetUserListener {

    void onGetUser(User user);
    void onNotGetUser(String msj);
}
