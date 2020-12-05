package com.example.easystock.listeners;

import com.example.easystock.models.Product;

import java.util.List;

public interface GetSimilarProductsListener {
    void onReceivedProducts(List<Product> productList);

    void onVoidProducts(String msj);
}
