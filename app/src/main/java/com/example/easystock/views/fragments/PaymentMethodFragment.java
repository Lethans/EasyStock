package com.example.easystock.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.models.Product;

import java.io.Serializable;
import java.util.List;

public class PaymentMethodFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Product> mProductList;


    public static PaymentMethodFragment newInstance(List<Product> productList) {
        PaymentMethodFragment myFragment = new PaymentMethodFragment();

        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) productList);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_method, container, false);
        RecyclerView ParentRecyclerViewItem = view.findViewById(R.id.parent_recyclerview);

        mProductList = (List<Product>) getArguments().getSerializable("products");

        return view;

    }

    public interface NotificableMenuFragment {
        void insertProduct(Product product);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //notificableMenuFragment = (NotificableMenuFragment) context;
    }

}
