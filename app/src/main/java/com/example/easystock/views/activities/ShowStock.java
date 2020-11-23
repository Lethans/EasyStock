package com.example.easystock.views.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.models.Product;
import com.example.easystock.views.adapters.ProductAdapter;

public class ShowStock extends AppCompatActivity {

    private ProductAdapter mAdapter;
    private RecyclerView mRecycler;
    private ProductViewModel mProductViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stock);
        mProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        setRecycler();


        mAdapter = new ProductAdapter(this, new ProductAdapter.NotificableDelClickRecycler() {
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
            mAdapter.setProductList(products);
            mRecycler.setAdapter(mAdapter);
        });
    }

    private void setRecycler() {
        mRecycler = findViewById(R.id.recyclerProducts);
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
    }
}