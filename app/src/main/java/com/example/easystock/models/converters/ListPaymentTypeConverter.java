package com.example.easystock.models.converters;

import androidx.room.TypeConverter;

import com.example.easystock.models.Payment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public class ListPaymentTypeConverter {

    @TypeConverter
    public static List<Payment> stringToSomeObjectList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        // Type listType = new TypeToken<List<Product>>(){}.getType();
        List<Payment> productList = gson.fromJson(data, new TypeToken<List<Payment>>() {
        }.getType());
        return productList;
    }

    @TypeConverter
    public static String someObjectListToString(List<Payment> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);

    }

}