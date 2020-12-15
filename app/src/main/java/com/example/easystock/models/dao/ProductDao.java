package com.example.easystock.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easystock.models.Product;
import com.example.easystock.models.User;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM Product")
    LiveData<List<Product>> getAllProducts();

    /*Query filtros*/

    @Query("SELECT * FROM Product ORDER BY price ASC")
    LiveData<List<Product>> getAscPriceFilter();

    @Query("SELECT * FROM Product ORDER BY price DESC")
    LiveData<List<Product>> getDescPriceFilter();

    @Query("SELECT * FROM Product ORDER BY stock ASC")
    LiveData<List<Product>> getAscStockFilter();

    @Query("SELECT * FROM Product ORDER BY stock DESC")
    LiveData<List<Product>> getDescStockFilter();

    @Query("SELECT * FROM Product ORDER BY size ASC")
    LiveData<List<Product>> getAscSizeFilter();

    @Query("SELECT * FROM Product ORDER BY size DESC")
    LiveData<List<Product>> getDescSizeFilter();

    /*Fin query filtros*/

    @Query("SELECT * FROM Product WHERE material LIKE '%'||:material||'%' AND type LIKE '%'||:type||'%' AND id NOT IN (SELECT id FROM Product WHERE id = :id)")
    List<Product> getSimilarbyTypeMaterial(String type, String material, int id);

    @Query("SELECT * FROM Product WHERE code LIKE '%'||:code||'%'")
    List<Product> getProductsByCode(String code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(Product product);

    @Update
    void updateProduct(Product... product);

    @Delete
    void deleteProduct(Product... product);
}
