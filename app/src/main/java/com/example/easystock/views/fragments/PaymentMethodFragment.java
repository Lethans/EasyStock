package com.example.easystock.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.listeners.GetProductsListener;
import com.example.easystock.models.Payment;
import com.example.easystock.models.Product;
import com.example.easystock.utils.NumberHelper;
import com.example.easystock.utils.viewPagerEfects.StringHelper;
import com.example.easystock.views.activities.CheckoutActivity;
import com.example.easystock.views.adapters.ProductAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentMethodFragment extends Fragment {

    private RecyclerView mRecyclerChangeProducts;
    private ProductAdapter mProductChangeAdapter;
    private CheckBox chkIncludeStock;
    private List<Payment> mPaymentList = new ArrayList<>();
    private EditText paymentCash, paymentPlastic, paymentVoucher, productChangeCode;
    private NotificablePaymentFragment listener;
    private List<Product> mProductList;
    private ProductViewModel mProductViewModel;


    public static PaymentMethodFragment newInstance(List<Product> productList) {
        PaymentMethodFragment myFragment = new PaymentMethodFragment();

        Bundle args = new Bundle();
        args.putSerializable("productsToChange", (Serializable) productList);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_method, container, false);
        mProductViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);

        mProductList = (List<Product>) getArguments().getSerializable("productsToChange");

        init(view);

        Button btnAddPayments = view.findViewById(R.id.btnAddPayments);
        btnAddPayments.setOnClickListener(v -> {
            checkPayments();
            listener.insertPayment(mPaymentList);
            clearFields();
        });

        Button btnSearchChangeProduct = view.findViewById(R.id.btnSearchProductChange);
        btnSearchChangeProduct.setOnClickListener(v -> {
            String productCode = StringHelper.getEdittextValue(productChangeCode);
            if (chkIncludeStock.isChecked()) {
                mProductViewModel.getProductsByCode(productCode, new GetProductsListener() {
                    @Override
                    public void onReceivedProducts(List<Product> productList) {
                        mProductChangeAdapter.setProductList(productList);
                    }

                    @Override
                    public void onVoidProducts(String msj) {
                        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //fixme aca tengo que ver la manera de recuperar todos los productos que fueron vendidos.
            }
        });

        mProductChangeAdapter = new ProductAdapter(getContext(), new ProductAdapter.NotificableDelClickRecycler() {
            @Override
            public void showProduct(Product product) {
                addProductChange(product);
                listener.insertPayment(mPaymentList);
            }

            @Override
            public void notificaUpdate(Product product) {
            }

            @Override
            public void notificaDelete(Product product) {
            }
        });
        mRecyclerChangeProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerChangeProducts.setAdapter(mProductChangeAdapter);

        return view;
    }

    private void init(View v) {
        paymentCash = v.findViewById(R.id.paymentCash);
        paymentPlastic = v.findViewById(R.id.paymentPlastic);
        //plasticCode = v.findViewById(R.id.paymentPlasticCode);
        paymentVoucher = v.findViewById(R.id.paymentVoucher);
        productChangeCode = v.findViewById(R.id.producChangeCode);

        mRecyclerChangeProducts = v.findViewById(R.id.recyclerChangeProducts);

        chkIncludeStock = v.findViewById(R.id.chkIncludStock);

    }

    private void checkPayments() {
        String cashAmount = StringHelper.getEdittextValue(paymentCash);
        String plasticAmount = StringHelper.getEdittextValue(paymentPlastic);
        String voucherAmount = StringHelper.getEdittextValue(paymentVoucher);

        if (!cashAmount.isEmpty()) {
            if (!canJoinPaymentMethod(mPaymentList, "Efectivo", cashAmount))
                addPayment(paymentCash, "Efectivo");
        }

        if (!plasticAmount.isEmpty()) {
            if (!canJoinPaymentMethod(mPaymentList, "Tarjeta", plasticAmount))
                addPayment(paymentPlastic, "Tarjeta");
        }

        if (!voucherAmount.isEmpty()) {
            if (!canJoinPaymentMethod(mPaymentList, "Voucher", voucherAmount))
                addPayment(paymentVoucher, "Voucher");
        }

    }

    private void addPayment(EditText editextAmoun, String metodo) {
        Payment payment = new Payment(StringHelper.getEdittextValue(editextAmoun), metodo, null);
        mPaymentList.add(payment);
    }

    private void addProductChange(Product productChange) {
        Payment payment = new Payment(productChange.getPrice(), "Cambio", productChange);
        mPaymentList.add(payment);
    }

    private boolean canJoinPaymentMethod(List<Payment> paymentList, String method, String amount) {
        boolean existPayment = false;
        for (int i = 0; i < paymentList.size(); i++) {
            if (paymentList.get(i).getMethod().toUpperCase().equals(method.toUpperCase())) {
                paymentList.get(i).setAmount(String.valueOf(NumberHelper.getPlusOperation(paymentList.get(i).getAmount(), amount)));
                existPayment = true;
            }
        }
        return existPayment;
    }

    private void clearFields() {
        paymentCash.setText("");
        paymentPlastic.setText("");
        paymentVoucher.setText("");
        productChangeCode.setText("");
    }

    public interface NotificablePaymentFragment {
        void insertPayment(List<Payment> paymentList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (NotificablePaymentFragment) context;
    }

}
