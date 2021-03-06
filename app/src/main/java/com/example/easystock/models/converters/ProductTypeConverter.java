package com.example.easystock.models.converters;

import androidx.room.TypeConverter;

import com.example.easystock.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public class ProductTypeConverter {

    @TypeConverter
    public static List<Product> stringToSomeObjectList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        // Type listType = new TypeToken<List<Product>>(){}.getType();
        List<Product> productList = gson.fromJson(data, new TypeToken<List<Product>>() {
        }.getType());
        return productList;
    }

    @TypeConverter
    public static String someObjectListToString(List<Product> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
