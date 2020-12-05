package com.example.easystock.models;

import java.util.List;

public class ParentItem {

    private String mProductGeneralCode;
    private List<ChildItem> ChildItemList;


    public ParentItem(String ParentItemTitle, List<ChildItem> ChildItemList) {

        this.mProductGeneralCode = ParentItemTitle;
        this.ChildItemList = ChildItemList;
    }


    public String getParentItemTitle() {
        return mProductGeneralCode;
    }

    public void setParentItemTitle(String parentItemTitle) {
        mProductGeneralCode = parentItemTitle;
    }

    public List<ChildItem> getChildItemList() {
        return ChildItemList;
    }

    public void setChildItemList(List<ChildItem> childItemList) {
        ChildItemList = childItemList;
    }
}