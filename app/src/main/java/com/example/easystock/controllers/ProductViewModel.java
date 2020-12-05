package com.example.easystock.controllers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.easystock.listeners.GetSimilarProductsListener;
import com.example.easystock.models.Product;
import com.example.easystock.models.User;
import com.example.easystock.models.repositories.ProductRepository;
import com.example.easystock.models.repositories.UserRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository mProductRepository;
    private LiveData<List<Product>> mAllProducts;

    private LiveData<List<Product>> mPriceAscFilter;
    private LiveData<List<Product>> mPriceDescFilter;
    private LiveData<List<Product>> mStockAscFilter;
    private LiveData<List<Product>> mStockDescFilter;
    private LiveData<List<Product>> mSizeAscFilter;
    private LiveData<List<Product>> mSizeDescFilter;


    public ProductViewModel(@NonNull Application application) {
        super(application);
        mProductRepository = new ProductRepository(application);
        mAllProducts = mProductRepository.getAllProducts();

        mPriceAscFilter = mProductRepository.getProductFilter("ASC", "Price");
        mPriceDescFilter = mProductRepository.getProductFilter("DESC", "Price");
        mStockAscFilter = mProductRepository.getProductFilter("ASC", "Stock");
        mStockDescFilter = mProductRepository.getProductFilter("DESC", "Stock");
        mSizeAscFilter = mProductRepository.getProductFilter("ASC", "Size");
        mSizeDescFilter = mProductRepository.getProductFilter("DESC", "Size");
    }

    public LiveData<List<Product>> getAllProducts() {
        return mAllProducts;
    }

    public LiveData<List<Product>> getProductFilter(String orderBy, String filter) {
        if (orderBy.equals("ASC")) {
            switch (filter.toUpperCase()) {
                case "PRICE":
                    return mPriceAscFilter;
                case "STOCK":
                    return mStockAscFilter;
                case "SIZE":
                    return mSizeAscFilter;
            }
        } else {
            switch (filter.toUpperCase()) {
                case "PRICE":
                    return mPriceDescFilter;
                case "STOCK":
                    return mStockDescFilter;
                case "SIZE":
                    return mSizeDescFilter;
            }
        }
        return mPriceAscFilter;
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

    public void getSimilarbyTypeMaterial(String type, String material, int id, GetSimilarProductsListener getSimilarProductsListener) {
        mProductRepository.getSimilarbyTypeMaterial(type, material, id, getSimilarProductsListener);
    }

}

