package com.example.easystock.views.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.models.Product;
import com.example.easystock.views.adapters.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowStock extends AppCompatActivity {

    private ImageView mFilterStockUp, mFilterStockDwn, mFilterPriceUp, mFilterPriceDwn, mFilterSizeUp, mFilterSizeDwn;
    private CardView mFilterStock, mFilterPrice, mFilterSize;
    private ProductAdapter mAdapter;
    private RecyclerView mRecycler;
    private ProductViewModel mProductViewModel;
    private List<Product> mProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stock);
        mProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        init();
        setRecycler();

        mFilterStock.setOnClickListener(v -> {

            if (mFilterStockUp.getVisibility() == View.GONE) {
                setAscendentStockFilter();
            } else if (mFilterStockDwn.getVisibility() == View.GONE) {
                setDescentedStockFilter();
            } else {
                setAscendentStockFilter();
            }
        });

        mFilterPrice.setOnClickListener(v -> {
            if (mFilterPriceUp.getVisibility() == View.GONE) {
                setAscendentPriceFilter();
            } else if (mFilterPriceDwn.getVisibility() == View.GONE) {
                setDescentedPriceFilter();
            } else {
                setAscendentPriceFilter();
            }
        });

        mFilterSize.setOnClickListener(v -> {
            if (mFilterSizeUp.getVisibility() == View.GONE) {
                setAscendentSizeFilter();
            } else if (mFilterSizeDwn.getVisibility() == View.GONE) {
                setDescentedSizeFilter();
            } else {
                setAscendentSizeFilter();
            }
        });


        mAdapter = new ProductAdapter(this, new ProductAdapter.NotificableDelClickRecycler() {
            @Override
            public void showProduct(Product product) {
                Intent intent = new Intent(ShowStock.this, ShowProduct.class);
                intent.putExtra(ShowProduct.PRODUCT, product);
                startActivity(intent);
            }

            @Override
            public void notificaUpdate(Product product) {
                Intent intent = new Intent(ShowStock.this, StockActivity.class);
                intent.putExtra(StockActivity.PRODUCT_UPDATE, product);
                startActivity(intent);
            }

            @Override
            public void notificaDelete(Product product) {
                new AlertDialog.Builder(ShowStock.this)
                        .setIcon(R.drawable.ic_baseline_arrow_right_24)
                        .setTitle("Borrar producto")
                        .setMessage("¿ Desea eliminar al producto: " + product.getType() + " " + product.getMaterial() + " ?")
                        .setPositiveButton("Si", (dialog, which) -> {
                            mProductViewModel.deleteProduct(product);
                            //Fixme 12-11 Agregar actualizacion firebase
                            //if(settingRepository!=null)settingRepository.updateSettingSyncUserData("true");
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        mProductViewModel.getAllProducts().observe(this, products -> {
            mProductList = products;
            mAdapter.setProductList(products);
            mRecycler.setAdapter(mAdapter);
        });

        EditText mSearchEdit = findViewById(R.id.productSearchEdit);
        ImageView mClearSearch = findViewById(R.id.searchClearImage);

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty())
                    mAdapter.getFilter().filter(charSequence.toString());
                else {
                    mAdapter.setProductList(mProductList);
                    //mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mClearSearch.setOnClickListener(v -> {
            mSearchEdit.getText().clear();
        });
    }

    private void init() {
        mFilterStockUp = findViewById(R.id.stockFilterUp);
        mFilterStockDwn = findViewById(R.id.stockFilterDown);
        mFilterPriceUp = findViewById(R.id.priceFilterUp);
        mFilterPriceDwn = findViewById(R.id.priceFilterDown);
        mFilterSizeUp = findViewById(R.id.sizeFilterUp);
        mFilterSizeDwn = findViewById(R.id.sizeFilterDown);

        mFilterStock = findViewById(R.id.stockFilter);
        mFilterPrice = findViewById(R.id.priceFilter);
        mFilterSize = findViewById(R.id.sizeFilter);
    }

    private void setRecycler() {
        mRecycler = findViewById(R.id.recyclerProducts);
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
    }

    private void setAscendentStockFilter() {
        setAllFilterVisible();
        setViewVisible(mFilterStockUp, mFilterStockDwn);
        setProducFilter("ASC", "Stock");
    }

    private void setDescentedStockFilter() {
        setAllFilterVisible();
        setViewVisible(mFilterStockDwn, mFilterStockUp);
        setProducFilter("DESC", "Stock");
    }

    private void setAscendentPriceFilter() {
        setAllFilterVisible();
        setViewVisible(mFilterPriceUp, mFilterPriceDwn);
        setProducFilter("ASC", "Price");
    }

    private void setDescentedPriceFilter() {
        setAllFilterVisible();
        setViewVisible(mFilterPriceDwn, mFilterPriceUp);
        setProducFilter("DESC", "Price");
    }

    private void setAscendentSizeFilter() {
        setAllFilterVisible();
        setViewVisible(mFilterSizeUp, mFilterSizeDwn);
        setProducFilter("ASC", "Size");
    }

    private void setDescentedSizeFilter() {
        setAllFilterVisible();
        setViewVisible(mFilterSizeDwn, mFilterSizeUp);
        setProducFilter("DESC", "Size");
    }

    private void setAllFilterVisible() {
        mFilterStockUp.setVisibility(View.VISIBLE);
        mFilterStockDwn.setVisibility(View.VISIBLE);
        mFilterPriceUp.setVisibility(View.VISIBLE);
        mFilterPriceDwn.setVisibility(View.VISIBLE);
        mFilterSizeUp.setVisibility(View.VISIBLE);
        mFilterSizeDwn.setVisibility(View.VISIBLE);
    }

    private void setViewVisible(ImageView visibleView, ImageView goneView) {
        visibleView.setVisibility(View.VISIBLE);
        goneView.setVisibility(View.GONE);
    }

    private void setProducFilter(String orderBy, String filter) {
        mProductViewModel.getProductFilter(orderBy, filter).observe(this, products -> {
            mProductList = products;
            mAdapter.setProductList(products);
            mRecycler.setAdapter(mAdapter);
        });
    }

}