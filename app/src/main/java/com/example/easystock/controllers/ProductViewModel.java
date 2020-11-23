package com.example.easystock.controllers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.easystock.models.Product;
import com.example.easystock.models.User;
import com.example.easystock.models.repositories.ProductRepository;
import com.example.easystock.models.repositories.UserRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository mProductRepository;
    private LiveData<List<Product>> mAllProducts;


    public ProductViewModel(@NonNull Application application) {
        super(application);
        mProductRepository = new ProductRepository(application);
        mAllProducts = mProductRepository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return mAllProducts;
    }

    public void insertProduct(Product product) {
        mProductRepository.insertProduct(product);
    }

    public void deleteProduct(Product product) {
        mProductRepository.deleteProduct(product);
    }

    public void updateProduct(Product product) {
        mProductRepository.updateProduct(product);
    }

}

