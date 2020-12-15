package com.example.easystock.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.easystock.R;
import com.example.easystock.models.ChildItem;
import com.example.easystock.models.ParentItem;
import com.example.easystock.models.Product;
import com.example.easystock.views.activities.CheckoutActivity;
import com.example.easystock.views.adapters.ParentItemAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductSellFragment extends Fragment {

    private List<Product> mProductList;
    private NotificableProductSell listener;


    public static ProductSellFragment newInstance(List<Product> productList) {
        ProductSellFragment myFragment = new ProductSellFragment();

        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) productList);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_product, container, false);
        RecyclerView ParentRecyclerViewItem = view.findViewById(R.id.parent_recyclerview);

        mProductList = (List<Product>) getArguments().getSerializable("products");


        // Initialise the Linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ParentItemAdapter parentItemAdapter = new ParentItemAdapter(ParentItemList(mProductList), getContext(), new ParentItemAdapter.ListenerParentItem() {
            @Override
            public void onParentItemClick(ParentItem parentItem) {

            }

            @Override
            public void onChildItemClick(Product product) {
                listener.insertProduct(product);
                //Toast.makeText(getContext(), childItem.getmProductIdPrice(), Toast.LENGTH_SHORT).show();
            }
        });
        ParentRecyclerViewItem.setAdapter(parentItemAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);

        return view;

    }

    private List<ParentItem> ParentItemList(List<Product> productList) {
        List<ParentItem> itemList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            boolean existCode = false;
            for (int j = 0; j < itemList.size(); j++) {
                if (productList.get(i).getCode().equals(itemList.get(j).getParentItemTitle())) {
                    existCode = true;
                    break;
                }
            }
            if (!existCode) {
                ParentItem item = new ParentItem(productList.get(i).getCode(), ChildItemList(productList, productList.get(i).getCode()));
                itemList.add(item);
            }
        }

        return itemList;
    }

    private List<Product> ChildItemList(List<Product> productList, String code) {
        List<Product> childItemList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getCode().equals(code)) {
/*                int id = productList.get(i).getId();
                String type = productList.get(i).getType();
                String price = productList.get(i).getPrice();
                String size = productList.get(i).getSize();
                String image = productList.get(i).getImage();
                ChildItemList.add(new ChildItem(id, type, price, size, image));*/
                childItemList.add(productList.get(i));
            }
        }
        return childItemList;
    }

    public interface NotificableProductSell {
        void insertProduct(Product product);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (NotificableProductSell) context;
    }

}