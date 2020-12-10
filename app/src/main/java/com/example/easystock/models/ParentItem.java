package com.example.easystock.models;

import java.util.List;

public class ParentItem {

    private String mProductGeneralCode;
    private List<Product> ChildItemList;


    public ParentItem(String ParentItemTitle, List<Product> ChildItemList) {

        this.mProductGeneralCode = ParentItemTitle;
        this.ChildItemList = ChildItemList;
    }


    public String getParentItemTitle() {
        return mProductGeneralCode;
    }

    public void setParentItemTitle(String parentItemTitle) {
        mProductGeneralCode = parentItemTitle;
    }

    public List<Product> getChildItemList() {
        return ChildItemList;
    }

    public void setChildItemList(List<Product> childItemList) {
        ChildItemList = childItemList;
    }
}