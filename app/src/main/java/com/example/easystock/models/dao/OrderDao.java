package com.example.easystock.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.easystock.models.Product;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT products FROM `Order`")
    LiveData<List<Product>> getAllProducts();




}
