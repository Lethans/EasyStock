package com.example.easystock.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.easystock.models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user where isLogged = 'TRUE'")
    LiveData<User> getUserLogged();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListUsers(List<User> userList);

    @Query("UPDATE user SET isLogged = 'FALSE' WHERE id NOT IN (SELECT id FROM user WHERE id = :id)")
    void setUserLogged(int id);

    @Transaction
    default void updateUsersLogged(User user ,int userLoggedId) {
        updateUser(user);
        setUserLogged(userLoggedId);
    }

    @Update
    void updateUser(User... user);

    @Delete
    void deleteUser(User... user);


    @Query("SELECT id FROM user ORDER BY id DESC LIMIT 1")
    int getUserNumber();

}
