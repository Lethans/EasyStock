package com.example.easystock.models.converters;

import androidx.room.TypeConverter;

import com.example.easystock.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class UserTypeConverter {

    @TypeConverter
    public static User stringToSomeObjectList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<User>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(User someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
