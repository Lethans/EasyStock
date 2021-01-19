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

/*    @Query("SELECT * FROM user WHERE androidIdFingerprint = 'YES' LIMIT 1")
    LiveData<User> getUserFingerPrint();*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListUsers(List<User> userList);

    /*@Query("UPDATE user SET fingerprint = 'NO' WHERE id NOT IN (SELECT id FROM user WHERE id = :id)")
    void setUserFinger(int id);*/

    @Query("SELECT * FROM user WHERE email=:email")
    User getExistingUserEmail(String email);

    @Query("SELECT * FROM user WHERE androidIdFingerprint=:androidId limit 1 ")
    User getUserFingerprint(String androidId);

    @Query("SELECT * FROM user WHERE username=:username AND password=:password ")
    User loginUser(String username, String password);

    @Query("SELECT COUNT(*) FROM user")
    int getUsersCount();

    @Update
    void updateUser(User... user);

    @Delete
    void deleteUser(User... user);


    @Query("SELECT id FROM user ORDER BY id DESC LIMIT 1")
    int getUserNumber();

}
