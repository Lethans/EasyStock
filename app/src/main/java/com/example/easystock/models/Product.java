package com.example.easystock.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String stock;
    private String type;
    private String costPrice;
    private String price;
    private String color;
    private String material;
    private String colorDescription;
    private String description;
    private String size;
    private String code;
    private String image;
    private String supplierBill;

    @Ignore
    public Product(String type, String material, String stock, String generalColor, String colorDescription, String costPrice, String price,
                   String size, String productCode, String description, String supplierBill, String image) {
        this.type = type;
        this.material = material;
        this.stock = stock;
        this.color = generalColor;
        this.colorDescription = colorDescription;
        this.costPrice = costPrice;
        this.price = price;
        this.size = size;
        this.code = productCode;
        this.description = description;
        this.supplierBill = supplierBill;
        this.image = image;
    }

    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColorDescription() {
        return colorDescription;
    }

    public void setColorDescription(String colorDescription) {
        this.colorDescription = colorDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSupplierBill() {
        return supplierBill;
    }

    public void setSupplierBill(String supplierBill) {
        this.supplierBill = supplierBill;
    }

}
