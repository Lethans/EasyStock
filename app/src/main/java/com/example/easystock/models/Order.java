package com.example.easystock.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.easystock.models.converters.ListPaymentTypeConverter;
import com.example.easystock.models.converters.ListProductsTypeConverter;
import com.example.easystock.models.converters.UserTypeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order implements Serializable {

    @PrimaryKey()
    private int id;
    private String datetime;
    private String paymentMethod;
    private String discount;
    private String finalDiscount;
    private String subTotal;
    private String total;
    @TypeConverters(ListProductsTypeConverter.class)
    private List<Product> products;
    @TypeConverters(UserTypeConverter.class)
    private User user;
    @TypeConverters(ListPaymentTypeConverter.class)
    private List<Payment> payments = new ArrayList<>();

    @Ignore
    public Order(String datetime, String paymentMethod, String discount, String finalDiscount, String subTotal, String total, List<Product> products, User user, List<Payment> payments) {
        this.datetime = datetime;
        this.paymentMethod = paymentMethod;
        this.discount = discount;
        this.finalDiscount = finalDiscount;
        this.subTotal = subTotal;
        this.total = total;
        this.products = products;
        this.user = user;
        this.payments = payments;
    }

    public Order() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFinalDiscount() {
        return finalDiscount;
    }

    public void setFinalDiscount(String finalDiscount) {
        this.finalDiscount = finalDiscount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
