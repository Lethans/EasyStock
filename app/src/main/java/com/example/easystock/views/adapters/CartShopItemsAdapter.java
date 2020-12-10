package com.example.easystock.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.models.Product;

import java.util.List;

public class CartShopItemsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Product> productList;
    //private ProductFilter productFilter;
    private NotificableCartShopItemsAdapter listener;

    public CartShopItemsAdapter(Context context, List<Product> productList, NotificableCartShopItemsAdapter notificableDelClickRecycler) {
        this.mContext = context;
        this.listener = notificableDelClickRecycler;
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_product = layoutInflater.inflate(R.layout.cart_shop_item, parent, false);
        return new ViewHolderCartShopProducts(item_product);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        ViewHolderCartShopProducts viewHolderProducts = (ViewHolderCartShopProducts) holder;
        viewHolderProducts.loadShopCartItem(product);
    }

/*    @Override
    public Filter getFilter() {
        if (productFilter == null) {
            productFilter = new ProductFilter(this, productList);
        }
        return productFilter;
    }*/

    @Override
    public int getItemCount() {
        return (productList == null) ? 0 : productList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    private class ViewHolderCartShopProducts extends RecyclerView.ViewHolder {


        private TextView type;
        private TextView code;
        private TextView price;
        private ImageView deleteImage;


        public ViewHolderCartShopProducts(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.cartShopItemType);
            code = itemView.findViewById(R.id.cartShopItemCode);
            price = itemView.findViewById(R.id.cartShopItemCost);
            deleteImage = itemView.findViewById(R.id.cartShopItemDelete);

            deleteImage.setOnClickListener(view -> listener.deleteProduct(productList.get(getBindingAdapterPosition())));
        }

        private void loadShopCartItem(Product product) {
            type.setText(product.getType());
            code.setText(product.getCode());
            price.setText(product.getPrice());
        }

    }

    public interface NotificableCartShopItemsAdapter {
        void deleteProduct(Product product);
    }

}