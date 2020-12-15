package com.example.easystock.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.listeners.GetProductsListener;
import com.example.easystock.models.Product;
import com.example.easystock.views.adapters.SimilarProductAdapter;

import java.util.List;

public class ShowProduct extends AppCompatActivity {

    public final static String PRODUCT = "PRODUCT";

    private TextView mType, mMaterial, mStock, mSimilarText;
    private ImageView mImage;

    private Button btnSimilar;
    private RecyclerView mSimilarRecycler;
    private ConstraintLayout mSimilarLayout;
    private ProductViewModel mProductViewModel;
    private Product mProduc;
    private SimilarProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        mProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        init();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mProduc = (Product) intent.getExtras().getSerializable(PRODUCT);
            assert mProduc != null;
            loadProduct(mProduc);
        }

        btnSimilar.setOnClickListener(v -> {
            mAdapter = new SimilarProductAdapter(this, product -> {
                mProduc = product;
                loadProduct(mProduc);
            });

            mProductViewModel.getSimilarbyTypeMaterial(mProduc.getType(), mProduc.getMaterial(), mProduc.getId(), new GetProductsListener() {
                @Override
                public void onReceivedProducts(List<Product> productList) {
                    setRecycler();
                    mAdapter.setProductList(productList);
                    //mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onVoidProducts(String msj) {
                    mSimilarRecycler.setVisibility(View.GONE);
                    mSimilarText.setVisibility(View.VISIBLE);
                }
            });
            mSimilarLayout.setVisibility(View.VISIBLE);
        });




/*        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void init() {
        mType = findViewById(R.id.productDetailType);
        mMaterial = findViewById(R.id.productDetailMaterial);
        mStock = findViewById(R.id.productDetailStock);
        mImage = findViewById(R.id.productDetailImage);


        mSimilarLayout = findViewById(R.id.simiarProductConstraint);
        mSimilarRecycler = findViewById(R.id.similarProductsRecycler);
        btnSimilar = findViewById(R.id.similarProductsLogin);
        mSimilarText = findViewById(R.id.textNoSimilarProducts);


    }

    private void loadProduct(Product product) {
        mType.setText(product.getType());
        mMaterial.setText(product.getMaterial());
        mStock.setText(product.getStock());
        Glide.with(this).load(product.getImage()).into(mImage);
    }

    private void setRecycler() {
        mSimilarRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mSimilarRecycler.setLayoutManager(linearLayoutManager);
        mSimilarRecycler.setAdapter(mAdapter);
    }


}