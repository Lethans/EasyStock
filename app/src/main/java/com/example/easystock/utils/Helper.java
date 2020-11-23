package com.example.easystock.utils;

import android.widget.EditText;

public class Helper{

    public String getEditextValue(EditText editText) {
        return editText.getText().toString().trim();
    }
}
