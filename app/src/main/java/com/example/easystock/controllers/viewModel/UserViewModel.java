package com.example.easystock.controllers.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.easystock.listeners.GetExistingUserEmailListener;
import com.example.easystock.listeners.GetUserListener;
import com.example.easystock.listeners.GetUserPasswordListener;
import com.example.easystock.listeners.GetUsersCountListener;
import com.example.easystock.models.User;
import com.example.easystock.models.repositories.UserRepository;

import java.util.List;


public class UserViewModel extends AndroidViewModel {

    private UserRepository mUserRepository;
    private LiveData<List<User>> mAllUsers;


    public UserViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application);
        mAllUsers = mUserRepository.getAllUsers();
    }


    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }


    /*public LiveData<User> getUserFingerPrint() {
        return mUserRepository.getUserFingerPrint();
    }*/

    public void insertUser(User user) {
        mUserRepository.insertUser(user);
    }

    public void deleteUser(User user) {
        mUserRepository.deleteUser(user);
    }

    public void updateUser(User user) {
        mUserRepository.updateUser(user);
    }


    public void getLoginUser(String username, String password, GetUserListener listener) {
        mUserRepository.getLoginUser(username, password, listener);
    }

    public void getUserFingerprint(String androidId, GetUserListener listener) {
        mUserRepository.getUserFingerprint(androidId, listener);
    }

    public void getUsersCount(GetUsersCountListener listener) {
        mUserRepository.getUsersCount(listener);
    }

    public void getExistingUserEmail(String email, GetExistingUserEmailListener listener) {
        mUserRepository.getExistingUserEmail(email, listener);
    }
}
