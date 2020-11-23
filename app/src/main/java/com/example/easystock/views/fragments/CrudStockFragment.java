package com.example.easystock.views.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.easystock.BuildConfig;
import com.example.easystock.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrudStockFragment extends Fragment {


    static final int REQUEST_FOR_PICTURE = 101;
    public final static int PICK_PHOTO_CODE = 1046;
    private String mCurrentPhotoPath;

    private EditText editStock, editType, editMaterial, editcolorCode, editSpecificColor, editColorDescription,
            editCostPrice, editPrice, editPriceOne, editPriceTwo, editPriceThree, editSize, editProductCode,
            editDescription, editSuplierBill;
    private Button btnPhoto, btnGallery, btnCancel, btnAcept;
    private ImageView productImage;

    public CrudStockFragment() {
    }

    public static CrudStockFragment newInstance() {
        CrudStockFragment fragment = new CrudStockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        editType = v.findViewById(R.id.productType);
        editMaterial = v.findViewById(R.id.productMaterial);
        editStock = v.findViewById(R.id.productStock);
        editcolorCode = v.findViewById(R.id.productGeneralColor);
        editSpecificColor = v.findViewById(R.id.productSpecificColor);
        editColorDescription = v.findViewById(R.id.productDescriptionColor);
        editCostPrice = v.findViewById(R.id.productCostPrice);
        editPrice = v.findViewById(R.id.productPrice);
        editPriceOne = v.findViewById(R.id.productPriceOne);
        editPriceTwo = v.findViewById(R.id.productPriceTwo);
        editPriceThree = v.findViewById(R.id.productPriceThree);
        editSize = v.findViewById(R.id.productSize);
        editProductCode = v.findViewById(R.id.productCode);
        editDescription = v.findViewById(R.id.productDescription);
        editSuplierBill = v.findViewById(R.id.productSupplierBill);


        btnPhoto = v.findViewById(R.id.btnTakePhoto);
        btnGallery = v.findViewById(R.id.btnRequestGallery);
        btnCancel = v.findViewById(R.id.btnCancelProduct);
        btnAcept = v.findViewById(R.id.btnAceptProduct);

        productImage = v.findViewById(R.id.productImage);

    }

    private String getEditextValue(EditText editText) {
        return editText.getText().toString().trim();
    }








}