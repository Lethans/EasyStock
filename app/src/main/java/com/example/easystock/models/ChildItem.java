package com.example.easystock.models;

public class ChildItem {

    private int mProductId;
    private String mProductIdType;
    private String mProductIdPrice;
    private String mProductIdSize;
    private String mProductIdImage;


    public ChildItem(int id, String type, String price, String size, String image) {
        this.mProductId = id;
        this.mProductIdType = type;
        this.mProductIdPrice = price;
        this.mProductIdSize = size;
        this.mProductIdImage = image;
    }

    public int getmProductId() {
        return mProductId;
    }

    public void setmProductId(int mProductId) {
        this.mProductId = mProductId;
    }

    public String getmProductIdType() {
        return mProductIdType;
    }

    public void setmProductIdType(String mProductIdType) {
        this.mProductIdType = mProductIdType;
    }

    public String getmProductIdPrice() {
        return mProductIdPrice;
    }

    public void setmProductIdPrice(String mProductIdPrice) {
        this.mProductIdPrice = mProductIdPrice;
    }

    public String getmProductIdSize() {
        return mProductIdSize;
    }

    public void setmProductIdSize(String mProductIdSize) {
        this.mProductIdSize = mProductIdSize;
    }

    public String getmProductIdImage() {
        return mProductIdImage;
    }

    public void setmProductIdImage(String mProductIdImage) {
        this.mProductIdImage = mProductIdImage;
    }
}