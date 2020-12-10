package com.example.easystock.views.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.models.ParentItem;
import com.example.easystock.models.Product;
import com.example.easystock.utils.NumberHelper;
import com.example.easystock.utils.viewPagerEfects.RotateUpPageTransformer;
import com.example.easystock.views.adapters.CartShopItemsAdapter;
import com.example.easystock.views.adapters.CheckoutViewPagerAdapter;
import com.example.easystock.views.adapters.ParentItemAdapter;
import com.example.easystock.views.fragments.FinalSellDetailsFragment;
import com.example.easystock.views.fragments.PaymentMethodFragment;
import com.example.easystock.views.fragments.ProductSellFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements ProductSellFragment.NotificableProductSell {

    private TextView mTotal;
    private ProductViewModel mProductViewModel;
    private List<Product> mProductList;
    private List<Product> mProductsToSell = new ArrayList<>();
    private RecyclerView mCartShopRecycler;
    private CartShopItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mProductViewModel = new ViewModelProvider(CheckoutActivity.this).get(ProductViewModel.class);

        ViewPager mViewPager = findViewById(R.id.checkout_ViewPager);
        TabLayout tabLayout = findViewById(R.id.checkout_tab_layout);
        mCartShopRecycler = findViewById(R.id.cartShopRecycler);
        mTotal = findViewById(R.id.checkoutTotalTextView);

        List<Fragment> checkoutFragmentList = new ArrayList<>();

        mProductViewModel.getAllProducts().observe(this, productList -> {
            this.mProductList = productList;
            checkoutFragmentList.add(ProductSellFragment.newInstance(mProductList));
            checkoutFragmentList.add(PaymentMethodFragment.newInstance(mProductList));
            checkoutFragmentList.add(FinalSellDetailsFragment.newInstance(mProductList));
            CheckoutViewPagerAdapter viewPagerAdapter = new CheckoutViewPagerAdapter(
                    getSupportFragmentManager(), CheckoutViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, checkoutFragmentList);

            mViewPager.setAdapter(viewPagerAdapter);
            mViewPager.setPageTransformer(true, new RotateUpPageTransformer());
            tabLayout.setupWithViewPager(mViewPager, true);

        });

        mAdapter = new CartShopItemsAdapter(this, mProductsToSell, product -> {
            Toast.makeText(this, "Funciona ?", Toast.LENGTH_SHORT).show();
            List<Product> newProductList = new ArrayList<>();
            boolean alreadyDeleted = false;
            for (int i = 0; i < mProductsToSell.size(); i++) {
                if (!alreadyDeleted) {
                    if (!TextUtils.equals(String.valueOf(mProductsToSell.get(i).getId()), String.valueOf(product.getId())))
                        newProductList.add(mProductsToSell.get(i));
                    else
                        alreadyDeleted = true;
                } else {
                    newProductList.add(mProductsToSell.get(i));
                }
            }
            mAdapter.setProductList(newProductList);
            mAdapter.notifyDataSetChanged();
            setTotalAmount(newProductList);
            mProductsToSell = newProductList;
        });
        mCartShopRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCartShopRecycler.setAdapter(mAdapter);
    }

    private void setTotalAmount(List<Product> productList) {
        double total = 0.0;
        for (int i = 0; i < productList.size(); i++) {
            total += Double.parseDouble(productList.get(i).getPrice());
        }

        mTotal.setText(String.valueOf(NumberHelper.getRoundingNumber(total)));
    }

    @Override
    public void insertProduct(Product product) {
        mProductsToSell.add(product);
        setTotalAmount(mProductsToSell);
        mAdapter.notifyDataSetChanged();
    }
}
