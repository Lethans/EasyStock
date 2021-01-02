package com.example.easystock.models.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.easystock.listeners.GetExistingUserEmailListener;
import com.example.easystock.listeners.GetUserListener;
import com.example.easystock.listeners.GetUsersCountListener;
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

    public UserRepository(Context ctx) {
        AppDatabase db = AppDatabase.getDatabase(ctx);
        mUserDao = db.mUserDao();
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public LiveData<User> getUserLogged() {
        return mUserDao.getUserLogged();
    }

    /*public LiveData<User> getUserFingerPrint() {
        return mUserDao.getUserFingerPrint();
    }*/

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

    public void updateUsersLogged(User user) {
        new UpdateUsersLoggedAsyncTask(mUserDao, user).execute();
    }

    public void getLoginUser(String username, String password, GetUserListener listener) {
        new LoginUserAsyncTask(mUserDao, username, password, listener).execute();
    }

    public void getUsersCount(GetUsersCountListener listener) {
        new GetUsersCountAsyncTask(mUserDao, listener).execute();
    }

    public void getUserFingerprint(String androidId, GetUserListener listener) {
        new GetUserFingerPrintAsyncTask(mUserDao, androidId, listener).execute();
    }

    public void getExistingUserEmail(String email, GetExistingUserEmailListener listener) {
        new GetExistingUserEmail(mUserDao, email, listener).execute();
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

    private static class UpdateUserStatusAsyncTask extends AsyncTask<User, Void, Void> {

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

    private static class UpdateUsersLoggedAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;
        private User mUser;

        public UpdateUsersLoggedAsyncTask(UserDao dao, User user) {
            mAsyncTaskDao = dao;
            mUser = user;
        }

        @Override
        protected Void doInBackground(User... voids) {
            mAsyncTaskDao.updateUsersLogged(mUser);
            return null;
        }
    }

    private static class GetExistingUserEmail extends AsyncTask<Void, Void, User> {

        private UserDao mAsyncTaskDao;
        private GetExistingUserEmailListener mListener;
        private String mEmail;

        public GetExistingUserEmail(UserDao dao, String email, GetExistingUserEmailListener listener) {
            this.mAsyncTaskDao = dao;
            this.mEmail = email;
            this.mListener = listener;
        }

        @Override
        protected User doInBackground(Void... voids) {
            return mAsyncTaskDao.getExistingUserEmail(mEmail);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null)
                mListener.onValidEmail(user);
            else
                mListener.onInvalidEmail();
        }
    }

    private static class GetUserFingerPrintAsyncTask extends AsyncTask<Void, Void, User> {

        private UserDao mAsyncTaskDao;
        private GetUserListener mListener;
        private String mAndroidId;

        public GetUserFingerPrintAsyncTask(UserDao dao, String androidId, GetUserListener listener) {
            this.mAsyncTaskDao = dao;
            this.mAndroidId = androidId;
            this.mListener = listener;
        }

        @Override
        protected User doInBackground(Void... voids) {
            return mAsyncTaskDao.getUserFingerprint(mAndroidId);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null)
                mListener.onGetUser(user);
            else
                mListener.onNotGetUser("La primera vez debe loguearse manualmente");
        }
    }

    private static class LoginUserAsyncTask extends AsyncTask<Void, Void, User> {

        private UserDao mAsyncTaskDao;
        private GetUserListener mListener;
        private String mUsername;
        private String mPassword;

        public LoginUserAsyncTask(UserDao dao, String username, String password, GetUserListener listener) {
            this.mAsyncTaskDao = dao;
            this.mUsername = username;
            this.mPassword = password;
            this.mListener = listener;
        }

        @Override
        protected User doInBackground(Void... voids) {
            return mAsyncTaskDao.loginUser(mUsername, mPassword);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null)
                mListener.onGetUser(user);
            else
                mListener.onNotGetUser("Usuario Inexistente");
            //mListener.onNotGetUser("Ingrese su cuenta manual y vuelva a seleccionar Mantener sesión");
        }
    }

    public class GetUsersCountAsyncTask extends AsyncTask<Void, Void, Integer> {

        private UserDao mAsyncTaskDao;
        private GetUsersCountListener mListener;

        public GetUsersCountAsyncTask(UserDao dao, GetUsersCountListener listener) {
            this.mAsyncTaskDao = dao;
            this.mListener = listener;
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            return mAsyncTaskDao.getUsersCount();
        }

        @Override
        synchronized protected void onPostExecute(Integer isAnyReceived) {
            super.onPostExecute(isAnyReceived);

            if (isAnyReceived != 0) {
                mListener.onExistUsers();
            } else {
                mListener.onEmptyUsers();
            }
        }
    }


}
