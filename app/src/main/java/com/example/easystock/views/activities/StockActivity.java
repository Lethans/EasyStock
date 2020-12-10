package com.example.easystock.views.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.easystock.BuildConfig;
import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.models.Product;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    static final int REQUEST_FOR_PICTURE = 101;
    public final static int PICK_PHOTO_CODE = 102;
    public final static String PRODUCT_UPDATE = "PRODUCT_UPDATE";
    private String mCurrentPhotoPath = null;
    private ProductViewModel mProductViewModel;
    private EditText editStock, editType, editMaterial, editcolorCode, editColorDescription,
            editCostPrice, editPrice, editPriceOne, editPriceTwo, editPriceThree, editProductCode,
            editDescription, editSuplierBill;
    private Button btnPhoto, btnCancel, btnAcept;
    private CheckBox xsCheck, sCheck, mCheck, lCheck, xlCheck, xxlCheck, xxxlCheck, unicCheck, chkDeleteFields, chkMultiplePrices;
    private ImageView productImage;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        mProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        init();
        setCheckboxListeners();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mProduct = (Product) intent.getExtras().getSerializable(PRODUCT_UPDATE);
            btnAcept.setText("Modificar");
            setProductData(mProduct);
        }


        chkMultiplePrices.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ConstraintLayout priceLayout = findViewById(R.id.constraintMultiplePrices);
            priceLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        btnPhoto.setOnClickListener(v -> selectImage());

        btnCancel.setOnClickListener(v -> {
            resetValues();
        });
        btnAcept.setOnClickListener(v -> {
            List<String> priceList = new ArrayList<>();
            priceList.add(getEditextValue(editPriceOne));
            priceList.add(getEditextValue(editPriceTwo));
            priceList.add(getEditextValue(editPriceThree));
            if (TextUtils.equals(btnAcept.getText().toString().toUpperCase(), "ACEPTAR")) {
                //Guardo producto.
                Product product = new Product(getEditextValue(editType), getEditextValue(editMaterial), getEditextValue(editStock),
                        getEditextValue(editcolorCode), getEditextValue(editColorDescription),
                        getEditextValue(editCostPrice), getEditextValue(editPrice), priceList, getProductSize(), getEditextValue(editProductCode),
                        getEditextValue(editDescription), getEditextValue(editSuplierBill), mCurrentPhotoPath);

                mProductViewModel.insertProduct(product);
                Toast.makeText(this, "Producto agregado con exito", Toast.LENGTH_SHORT).show();
            } else {
                mProduct.setType(getEditextValue(editType));
                mProduct.setMaterial(getEditextValue(editMaterial));
                mProduct.setStock(getEditextValue(editStock));
                mProduct.setColor(getEditextValue(editcolorCode));
                mProduct.setColorDescription(getEditextValue(editColorDescription));
                mProduct.setCostPrice(getEditextValue(editCostPrice));
                mProduct.setPrice(getEditextValue(editPrice));
                mProduct.setPriceList(priceList);
                mProduct.setSize(getProductSize());
                mProduct.setCode(getEditextValue(editProductCode));
                mProduct.setDescription(getEditextValue(editDescription));
                mProduct.setSupplierBill(getEditextValue(editSuplierBill));
                mProduct.setImage(mCurrentPhotoPath != null ? mCurrentPhotoPath : mProduct.getImage());
                mProductViewModel.updateProduct(mProduct);
                Toast.makeText(this, "Producto actualizado con exito", Toast.LENGTH_SHORT).show();
            }
            if (chkDeleteFields.isChecked())
                resetValues();
        });

    }

    private void init() {
        editType = findViewById(R.id.productType);
        editMaterial = findViewById(R.id.productMaterial);
        editStock = findViewById(R.id.productStock);
        editcolorCode = findViewById(R.id.productGeneralColor);
        editColorDescription = findViewById(R.id.productDescriptionColor);
        editCostPrice = findViewById(R.id.productCostPrice);
        editPrice = findViewById(R.id.productPrice);
        editPriceOne = findViewById(R.id.productPriceOne);
        editPriceTwo = findViewById(R.id.productPriceTwo);
        editPriceThree = findViewById(R.id.productPriceThree);

        xsCheck = findViewById(R.id.xsSize);
        sCheck = findViewById(R.id.sSize);
        mCheck = findViewById(R.id.mSize);
        lCheck = findViewById(R.id.lSize);
        xlCheck = findViewById(R.id.xlSize);
        xxlCheck = findViewById(R.id.xxlSize);
        xxxlCheck = findViewById(R.id.xxxlSize);
        unicCheck = findViewById(R.id.unicSize);

        chkMultiplePrices = findViewById(R.id.multiplePriceProductsCheck);


        editProductCode = findViewById(R.id.productCode);
        editDescription = findViewById(R.id.productDescription);
        editSuplierBill = findViewById(R.id.productSupplierBill);

        chkDeleteFields = findViewById(R.id.deletAllFields);
        btnPhoto = findViewById(R.id.btnTakePhoto);
        //btnGallery = findViewById(R.id.btnRequestGallery);
        btnCancel = findViewById(R.id.btnCancelProduct);
        btnAcept = findViewById(R.id.btnAceptProduct);

        productImage = findViewById(R.id.productImage);

    }

    private void setProductData(Product product) {
        editType.setText(product.getType());
        editMaterial.setText(product.getMaterial());
        editStock.setText(product.getStock());
        editcolorCode.setText(product.getColor());
        editColorDescription.setText(product.getColorDescription());
        editCostPrice.setText(product.getCostPrice());
        editPrice.setText(product.getPrice());
        editPriceOne.setText(product.getPriceList().get(0));
        editPriceTwo.setText(product.getPriceList().get(1));
        editPriceThree.setText(product.getPriceList().get(2));
        setProductSize(product.getSize());
        editProductCode.setText(product.getCode());
        editDescription.setText(product.getDescription());
        editSuplierBill.setText(product.getSupplierBill());

        if (product.getImage() != null)
            Glide.with(this).load(product.getImage()).into(productImage);
    }

    private String getEditextValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void selectImage() {
        final CharSequence[] options = {"Camara", "Galería", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camara")) {
                    openCamara();

                } else if (options[item].equals("Galería")) {
                    onPickPhoto();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
            mCurrentPhotoPath = photoUri.getPathSegments().get(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void openCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_FOR_PICTURE);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_FOR_PICTURE:
                if (resultCode == RESULT_OK) {
                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        productImage.setImageBitmap(bitmap);
                    }
                } else {
                    mCurrentPhotoPath = null;
                }
                break;
            case PICK_PHOTO_CODE:
                if (resultCode == RESULT_OK) {
                    Uri photoUri = data.getData();
                    // Load the image located at photoUri into selectedImage
                    Bitmap selectedImage = loadFromUri(photoUri);
                    // Load the selected image into a preview
                    productImage.setImageBitmap(selectedImage);
                } else {
                    mCurrentPhotoPath = null;
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Stock" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void resetValues() {
        editType.setText("");
        editMaterial.setText("");
        editStock.setText("");
        editcolorCode.setText("");
        editColorDescription.setText("");
        editCostPrice.setText("");
        editPrice.setText("");
        editPriceOne.setText("");
        editPriceTwo.setText("");
        editPriceThree.setText("");
        uncheckAllBox();
        editProductCode.setText("");
        editDescription.setText("");
        editSuplierBill.setText("");
        productImage.setImageResource(R.drawable.stock_icon);
    }

    private void uncheckAllBox() {
        xsCheck.setChecked(false);
        sCheck.setChecked(false);
        mCheck.setChecked(false);
        lCheck.setChecked(false);
        xlCheck.setChecked(false);
        xxlCheck.setChecked(false);
        xxxlCheck.setChecked(false);
        unicCheck.setChecked(false);
    }

    private String getProductSize() {
        if (xsCheck.isChecked())
            return "XS";
        if (sCheck.isChecked())
            return "S";
        if (mCheck.isChecked())
            return "M";
        if (lCheck.isChecked())
            return "L";
        if (xlCheck.isChecked())
            return "XL";
        if (xxlCheck.isChecked())
            return "XXL";
        if (xxxlCheck.isChecked())
            return "XXXL";
        if (unicCheck.isChecked())
            return "UNIC";

        return "";
    }

    private void setProductSize(String size) {
        if (size.toUpperCase().equals("XS"))
            xsCheck.setChecked(true);
        if (size.toUpperCase().equals("S"))
            sCheck.setChecked(true);
        if (size.toUpperCase().equals("M"))
            mCheck.setChecked(true);
        if (size.toUpperCase().equals("L"))
            lCheck.setChecked(true);
        if (size.toUpperCase().equals("XL"))
            xlCheck.setChecked(true);
        if (size.toUpperCase().equals("XXL"))
            xxlCheck.setChecked(true);
        if (size.toUpperCase().equals("XXXL"))
            xxxlCheck.setChecked(true);
        if (size.toUpperCase().equals("UNIC"))
            unicCheck.setChecked(true);
    }

    private void setCheckboxListeners() {
        xsCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            xsCheck.setChecked(isChecked);
        });
        sCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            sCheck.setChecked(isChecked);
        });
        mCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            mCheck.setChecked(isChecked);
        });
        lCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            lCheck.setChecked(isChecked);
        });
        xlCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            xlCheck.setChecked(isChecked);
        });
        xxlCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            xxlCheck.setChecked(isChecked);
        });
        xxxlCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            xxxlCheck.setChecked(isChecked);
        });
        unicCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            uncheckAllBox();
            unicCheck.setChecked(isChecked);
        });
    }
}



