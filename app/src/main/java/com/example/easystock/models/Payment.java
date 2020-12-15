package com.example.easystock.models;

import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.example.easystock.models.converters.ProductTypeConverter;

import java.io.Serializable;

public class Payment implements Serializable {

    private String amount;
    private String method;
    @TypeConverters(ProductTypeConverter.class)
    private Product productChange;


    @Ignore
    public Payment(String amount, String method, Product productChange) {
        this.amount = amount;
        this.method = method;
        this.productChange = productChange;
    }

    public Payment() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Product getProductChange() {
        return productChange;
    }

    public void setProductChange(Product productChange) {
        this.productChange = productChange;
    }
}
