package com.example.easystock.models.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.easystock.models.Product;
import com.example.easystock.models.User;
import com.example.easystock.models.converters.StringTypeConvert;
import com.example.easystock.models.dao.ProductDao;
import com.example.easystock.models.dao.UserDao;


@Database(entities = {Product.class, User.class}, version = 1, exportSchema = false)
@TypeConverters({StringTypeConvert.class})

public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "easy_stock.db";
    private static AppDatabase INSTANCE;

    public abstract ProductDao mProductDao();

    public abstract UserDao mUserDao();


    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}