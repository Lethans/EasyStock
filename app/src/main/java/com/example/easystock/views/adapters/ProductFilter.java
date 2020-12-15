package com.example.easystock.views.adapters;

import android.widget.Filter;

import com.example.easystock.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductFilter extends Filter {

    private ProductAdapter productAdapter;
    private List<Product> filterList;

    public ProductFilter(ProductAdapter productAdapter, List<Product> filterList) {
        this.productAdapter = productAdapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence sequence) {

        FilterResults filterResults = new FilterResults();
        if (sequence != null && sequence.length() > 0) {

            sequence = sequence.toString().toLowerCase();

            List<Product> filteredList = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {

                if (filterList.get(i).getType().toLowerCase().contains(sequence) ||
                        filterList.get(i).getMaterial().toLowerCase().contains(sequence) ||
                        filterList.get(i).getSupplierBill().toLowerCase().contains(sequence) ||
                        filterList.get(i).getCode().toLowerCase().contains(sequence) ||
                        filterList.get(i).getSize().toLowerCase().contains(sequence) ||
                        filterList.get(i).getPrice().toLowerCase().contains(sequence) ||
                        filterList.get(i).getColor().toLowerCase().contains(sequence) ||
                        filterList.get(i).getCostPrice().toLowerCase().contains(sequence) ||
                        filterList.get(i).getColorDescription().toLowerCase().contains(sequence)) {
                    filteredList.add(filterList.get(i));
                }
            }

            filterResults.count = filteredList.size();
            filterResults.values = filteredList;

        } else {
            filterResults.count = filterList.size();
            filterResults.values = filterList;
        }

        return filterResults;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        productAdapter.setProductList((List<Product>) results.values);
        //productAdapter.notifyDataSetChanged();

    }

}