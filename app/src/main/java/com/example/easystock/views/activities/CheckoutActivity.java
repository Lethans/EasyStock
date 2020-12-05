package com.example.easystock.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.easystock.R;
import com.example.easystock.controllers.ProductViewModel;
import com.example.easystock.models.Product;
import com.example.easystock.utils.DepthPageTransformer;
import com.example.easystock.utils.ZoomOutPageTransformer;
import com.example.easystock.utils.viewPagerEfects.RotateUpPageTransformer;
import com.example.easystock.views.adapters.CheckoutViewPagerAdapter;
import com.example.easystock.views.fragments.FinalSellDetailsFragment;
import com.example.easystock.views.fragments.PaymentMethodFragment;
import com.example.easystock.views.fragments.ProductSellFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    /*    ExpandableListView expandableListView;
        ExpandableListAdapter expandableListAdapter;
        List<String> expandableListTitle;
        HashMap<String, List<String>> expandableListDetail;
        */


    private ProductViewModel mProductViewModel;
    //private  mViewPager;
    private List<Product> mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mProductViewModel = new ViewModelProvider(CheckoutActivity.this).get(ProductViewModel.class);

        ViewPager mViewPager = findViewById(R.id.checkout_ViewPager);
        //TabLayout tabLayout = findViewById(R.id.checkout_tab_layout);

        List<Fragment> checkoutFragmentList = new ArrayList<>();


        //List<String> fragmentsTitles = new ArrayList<>();

        mProductViewModel.getAllProducts().observe(this, productList -> {
            this.mProductList = productList;
            checkoutFragmentList.add(ProductSellFragment.newInstance(mProductList));
            checkoutFragmentList.add(PaymentMethodFragment.newInstance(mProductList));
            checkoutFragmentList.add(FinalSellDetailsFragment.newInstance(mProductList));

            //checkoutFragmentList.add(new BistroConfigPaymentsFragment());
/*            fragmentsTitles.add("GENERAL");
            fragmentsTitles.add("PRINT");
            fragmentsTitles.add("PAYMENTS_METHOD");*/

            CheckoutViewPagerAdapter viewPagerAdapter = new CheckoutViewPagerAdapter(
                    getSupportFragmentManager(), CheckoutViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, checkoutFragmentList);

            mViewPager.setAdapter(viewPagerAdapter);

            //Animaciones sin clases extras
            /*mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {

                public void transformPage(View page, float position) {
                    final float normalizedposition = Math.abs(Math.abs(position) - 1);
                    page.setScaleX(normalizedposition / 2 + 0.5f);
                    page.setScaleY(normalizedposition / 2 + 0.5f);

                    *//*final float normalizedposition = Math.abs(Math.abs(position) - 1);
                    page.setAlpha(normalizedposition);*//*

                    //page.setRotationY(position * -30); // animation style... change as you want..
                }
            });*/

            mViewPager.setPageTransformer(true, new RotateUpPageTransformer());

            //tabLayout.setupWithViewPager(mViewPager);

            /*tabLayout.getTabAt(0).setText("FIRST");
            tabLayout.getTabAt(1).setText("SECOND");
            tabLayout.getTabAt(2).setText("THIRD");*/

/*            tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_search_black_24dp);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_apps_black_24dp);
            tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black_24dp);
            tabLayout.getTabAt(4).setIcon(R.drawable.ic_person_black_24dp);*/
        });

        //fixme tendria que hacer otro observer hacia todos las ordenes para conseguir los productos que se vendieron

        //tabLayout.setupWithViewPager(viewPager);


        // Pass the arguments
        // to the parentItemAdapter.
        // These arguments are passed
        // using a method ParentItemList()
        /*ParentItemAdapter parentItemAdapter = new ParentItemAdapter(ParentItemList());

        // Set the layout manager
        // and adapter for items
        // of the parent recyclerview
        ParentRecyclerViewItem.setAdapter(parentItemAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);*/
    }
}

        /*cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);

        expandableListView = findViewById(R.id.expandableListView);
        expandableListDetail = getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });*/


