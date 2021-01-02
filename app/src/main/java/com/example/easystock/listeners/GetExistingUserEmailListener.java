package com.example.easystock.listeners;

import com.example.easystock.models.User;

public interface GetExistingUserEmailListener {
    void onValidEmail(User user);

    void onInvalidEmail();
}