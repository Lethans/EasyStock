package com.example.easystock.views.activities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.models.Payment;
import com.example.easystock.models.Product;
import com.example.easystock.utils.NumberHelper;
import com.example.easystock.utils.viewPagerEfects.RotateUpPageTransformer;
import com.example.easystock.views.adapters.CartShopItemsAdapter;
import com.example.easystock.views.adapters.CheckoutViewPagerAdapter;
import com.example.easystock.views.adapters.PaymentsAdapter;
import com.example.easystock.views.fragments.FinalSellDetailsFragment;
import com.example.easystock.views.fragments.PaymentMethodFragment;
import com.example.easystock.views.fragments.ProductSellFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements ProductSellFragment.NotificableProductSell, PaymentMethodFragment.NotificablePaymentFragment,FinalSellDetailsFragment.NotificableFinalOrderDetails {

    private TextView mTotal;
    private ProductViewModel mProductViewModel;
    private List<Product> mProductList;
    private List<Product> mProductsToSell = new ArrayList<>();
    private List<Payment> mPaymentList = new ArrayList<>();
    private RecyclerView mCartShopRecycler;
    private CartShopItemsAdapter mCartShopAdapter;
    private PaymentsAdapter mPaymentAdapter;
    private RecyclerView mPaymentListRecycler;
    private TextView mSubtotal;
    private TextView mDiscount;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mProductViewModel = new ViewModelProvider(CheckoutActivity.this).get(ProductViewModel.class);

        ViewPager mViewPager = findViewById(R.id.checkout_ViewPager);
        TabLayout tabLayout = findViewById(R.id.checkout_tab_layout);
        ConstraintLayout mainLayout = findViewById(R.id.constraintCheckout);
        mCartShopRecycler = findViewById(R.id.cartShopRecycler);
        mTotal = findViewById(R.id.checkoutTotalTextView);
        mPaymentListRecycler = findViewById(R.id.paymentListRecycler);

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

        mCartShopAdapter = new CartShopItemsAdapter(this, mProductsToSell, product -> {
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
            mCartShopAdapter.setProductList(newProductList);
            //mCartShopAdapter.notifyDataSetChanged();
            setTotalAmount(newProductList);
            mProductsToSell = newProductList;
        });

        mCartShopRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCartShopRecycler.setAdapter(mCartShopAdapter);

        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_delete);
            private final ColorDrawable background = new ColorDrawable(Color.BLUE);

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getBindingAdapterPosition();
                final Product product = mCartShopAdapter.getProduct(viewHolder.getBindingAdapterPosition());
                mCartShopAdapter.removeItem(viewHolder.getBindingAdapterPosition());

                Snackbar snackbar = Snackbar.make(mainLayout, "Producto borrado", Snackbar.LENGTH_LONG)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mCartShopAdapter.undoDelete(product, position);
                            }
                        });
                snackbar.show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                if (dX > 0) {
                    int iconLeft = itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getLeft() + iconMargin;

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                deleteIcon.draw(c);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mCartShopRecycler);

        mPaymentAdapter = new PaymentsAdapter(this);
        mPaymentListRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPaymentListRecycler.setAdapter(mPaymentAdapter);

        ItemTouchHelper.SimpleCallback touchHelperCallbackPayments = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_delete);
            private final ColorDrawable background = new ColorDrawable(Color.BLUE);

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getBindingAdapterPosition();
                final Payment payment = mPaymentAdapter.getPayment(viewHolder.getBindingAdapterPosition());
                mPaymentAdapter.removeItem(viewHolder.getBindingAdapterPosition());

                Snackbar snackbar = Snackbar.make(mainLayout, "Pago borrado", Snackbar.LENGTH_LONG)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPaymentAdapter.undoDelete(payment, position);
                            }
                        });
                snackbar.show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                if (dX > 0) {
                    int iconLeft = itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getLeft() + iconMargin;

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                deleteIcon.draw(c);
            }
        };
        ItemTouchHelper itemTouchHelperPayments = new ItemTouchHelper(touchHelperCallbackPayments);
        itemTouchHelperPayments.attachToRecyclerView(mPaymentListRecycler);


        mSubtotal = findViewById(R.id.checkoutSubTotalTextView);
        mTotal = findViewById(R.id.checkoutTotalTextView);
        mDiscount = findViewById(R.id.checkoutDiscountTextView);
        Button btnPdf = findViewById(R.id.btnPdf);

        //btnPdf.setOnClickListener(this::createMyPDF);
        btnPdf.setOnClickListener(v -> {
            //https://www.youtube.com/watch?v=KU8q7ZS86WA&ab_channel=SarthiTechnology
            PdfDocument pmyPdfDocument = new PdfDocument();
            Paint paint = new Paint();
            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
            PdfDocument.Page myPage = pmyPdfDocument.startPage(myPageInfo);
            Canvas canvas = myPage.getCanvas();

            paint.setTextSize(15.5f);
            paint.setColor(Color.rgb(0, 50, 250));

            canvas.drawText("Esto es una prueba del pdf", 20, 20, paint);
            paint.setTextSize(8.5f);
            canvas.drawText("PapaTwist, plotTwist", 20, 35, paint);

            pmyPdfDocument.finishPage(myPage);
            File file = new File(this.getExternalFilesDir("/"), "Prueba2" + ".pdf");

            try {
                pmyPdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                Toast.makeText(this, "Error 509", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            pmyPdfDocument.close();
        });

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
        mCartShopAdapter.notifyDataSetChanged();
    }

    @Override
    public void insertPayment(List<Payment> paymentList) {
        mPaymentList = paymentList;
        mPaymentAdapter.setProductList(paymentList);
        //mPaymentAdapter.notifyDataSetChanged();
    }

    @Override
    public void discountAvaiable(String discount) {

    }
}
