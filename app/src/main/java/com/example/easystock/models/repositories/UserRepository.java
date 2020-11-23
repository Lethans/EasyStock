package com.example.easystock.models.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.easystock.models.User;
import com.example.easystock.models.dao.UserDao;
import com.example.easystock.models.db.AppDatabase;

import java.util.List;

public class UserRepository {

    private LiveData<List<User>> mAllUsers;
    private UserDao mUserDao;


    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserDao = db.mUserDao();
        mAllUsers = mUserDao.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public LiveData<User> getUserLogged() {
        return mUserDao.getUserLogged();
    }

    public void insertUser(User user) {
        new insertAsyncTask(mUserDao).execute(user);
    }

    public void insertListUsers(List<User> users) {
        new insertListUserAsyncTask(mUserDao).execute(users);
    }

    public void updateUser(User user) {
        new UpdateUserStatusAsyncTask(mUserDao).execute(user);
    }

    public void deleteUser(User user) {
        new deleteUserAsyncTask(mUserDao).execute(user);
    }

    public void updateUsersLogged(User user, int idUserLogged) {
        new UpdateUsersLoggedsyncTask(mUserDao, user, idUserLogged).execute();
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insertUser(params[0]);
            return null;
        }
    }

    private static class insertListUserAsyncTask extends AsyncTask<List<User>, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertListUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<User>... params) {
            mAsyncTaskDao.insertListUsers(params[0]);
            return null;
        }
    }

    private class UpdateUserStatusAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        public UpdateUserStatusAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.updateUser(params[0]);
            return null;
        }
    }

    private static class deleteUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        deleteUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.deleteUser(params[0]);
            return null;
        }
    }

    private static class UpdateUsersLoggedsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;
        private User mUser;
        private int mUserLoggedId;

        public UpdateUsersLoggedsyncTask(UserDao dao, User user, int userLoggedId) {
            mAsyncTaskDao = dao;
            mUser = user;
            mUserLoggedId = userLoggedId;
        }

        @Override
        protected Void doInBackground(User... voids) {
            mAsyncTaskDao.updateUsersLogged(mUser, mUserLoggedId);
            return null;
        }
    }

}
