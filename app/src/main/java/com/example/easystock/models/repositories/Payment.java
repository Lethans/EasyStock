package com.example.easystock.models.repositories;

import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.example.easystock.models.Product;
import com.example.easystock.models.converters.ProductTypeConverter;

import java.io.Serializable;
import java.util.List;

public class Payment implements Serializable {

    private String date;
    private String amount;
    private String method;
    @TypeConverters(ProductTypeConverter.class)
    private List<Product> productList;

    @Ignore
    public Payment(String date, String amount, String method,List<Product> productList) {
        this.date = date;
        this.amount = amount;
        this.method = method;
        this.productList = productList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Product> getProductReturn() {
        return productList;
    }

    public void setProductReturn(List<Product> productReturn) {
        this.productList = productReturn;
    }
}