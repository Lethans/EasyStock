package com.example.easystock.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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
    private Fragment frameNewProduct;
    private Button btnNewProduct;
    private ProductViewModel mProductViewModel;
    private EditText editStock, editType, editMaterial, editcolorCode, editSpecificColor, editColorDescription,
            editCostPrice, editPrice, editPriceOne, editPriceTwo, editPriceThree, editSize, editProductCode,
            editDescription, editSuplierBill;
    private Button btnPhoto, btnGallery, btnCancel, btnAcept;
    private ImageView productImage;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        mProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        init();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mProduct = (Product) intent.getExtras().getSerializable(PRODUCT_UPDATE);
            btnAcept.setText("Modificar");
            setProductData(mProduct);
        }


        btnPhoto.setOnClickListener(v -> openCamara());
        btnGallery.setOnClickListener(v -> onPickPhoto());

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
                        getEditextValue(editcolorCode), getEditextValue(editSpecificColor), getEditextValue(editColorDescription),
                        getEditextValue(editCostPrice), getEditextValue(editPrice), priceList, getEditextValue(editSize), getEditextValue(editProductCode),
                        getEditextValue(editDescription), getEditextValue(editSuplierBill), mCurrentPhotoPath);

                mProductViewModel.insertProduct(product);
                Toast.makeText(this, "Producto agregado con exito", Toast.LENGTH_SHORT).show();
            } else {
                mProduct.setType(getEditextValue(editType));
                mProduct.setMaterial(getEditextValue(editMaterial));
                mProduct.setStock(getEditextValue(editStock));
                mProduct.setColor(getEditextValue(editcolorCode));
                mProduct.setSpecificColor(getEditextValue(editSpecificColor));
                mProduct.setColorDescription(getEditextValue(editColorDescription));
                mProduct.setOriginalPrice(getEditextValue(editCostPrice));
                mProduct.setPrice(getEditextValue(editPrice));
                mProduct.setPriceList(priceList);
                mProduct.setSize(getEditextValue(editSize));
                mProduct.setCode(getEditextValue(editProductCode));
                mProduct.setDescription(getEditextValue(editDescription));
                mProduct.setSupplierBill(getEditextValue(editSuplierBill));
                mProduct.setImage(mCurrentPhotoPath != null ? mCurrentPhotoPath : mProduct.getImage());
                mProductViewModel.updateProduct(mProduct);
                Toast.makeText(this, "Producto actualizado con exito", Toast.LENGTH_SHORT).show();
            }
            resetValues();
        });

    }

    private void init() {
        editType = findViewById(R.id.productType);
        editMaterial = findViewById(R.id.productMaterial);
        editStock = findViewById(R.id.productStock);
        editcolorCode = findViewById(R.id.productGeneralColor);
        editSpecificColor = findViewById(R.id.productSpecificColor);
        editColorDescription = findViewById(R.id.productDescriptionColor);
        editCostPrice = findViewById(R.id.productCostPrice);
        editPrice = findViewById(R.id.productPrice);
        editPriceOne = findViewById(R.id.productPriceOne);
        editPriceTwo = findViewById(R.id.productPriceTwo);
        editPriceThree = findViewById(R.id.productPriceThree);
        editSize = findViewById(R.id.productSize);
        editProductCode = findViewById(R.id.productCode);
        editDescription = findViewById(R.id.productDescription);
        editSuplierBill = findViewById(R.id.productSupplierBill);


        btnPhoto = findViewById(R.id.btnTakePhoto);
        btnGallery = findViewById(R.id.btnRequestGallery);
        btnCancel = findViewById(R.id.btnCancelProduct);
        btnAcept = findViewById(R.id.btnAceptProduct);

        productImage = findViewById(R.id.productImage);

    }

    private void setProductData(Product product) {
        editType.setText(product.getType());
        editMaterial.setText(product.getMaterial());
        editStock.setText(product.getStock());
        editcolorCode.setText(product.getColor());
        editSpecificColor.setText(product.getSpecificColor());
        editColorDescription.setText(product.getColorDescription());
        editCostPrice.setText(product.getOriginalPrice());
        editPrice.setText(product.getPrice());
        editPriceOne.setText(product.getPriceList().get(0));
        editPriceTwo.setText(product.getPriceList().get(1));
        editPriceThree.setText(product.getPriceList().get(2));
        editSize.setText(product.getSize());
        editProductCode.setText(product.getCode());
        editDescription.setText(product.getDescription());
        editSuplierBill.setText(product.getSupplierBill());

        if (product.getImage() != null)
            Glide.with(this).load(product.getImage()).into(productImage);
    }

    private String getEditextValue(EditText editText) {
        return editText.getText().toString().trim();
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
        editSpecificColor.setText("");
        editColorDescription.setText("");
        editCostPrice.setText("");
        editPrice.setText("");
        editPriceOne.setText("");
        editPriceTwo.setText("");
        editPriceThree.setText("");
        editSize.setText("");
        editProductCode.setText("");
        editDescription.setText("");
        editSuplierBill.setText("");
        //productImage.setBackgroundResource(R.drawable.stock_icon);
        //productImage.setImageResource(android.R.color.transparent);
        productImage.setImageResource(R.drawable.stock_icon);
    }


}



