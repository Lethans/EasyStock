package com.example.easystock.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easystock.R;
import com.example.easystock.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter implements Filterable {

    private Context mContext;
    private List<Product> productList;
    private ProductFilter productFilter;
    private NotificableDelClickRecycler listener;

    public ProductAdapter(Context context, NotificableDelClickRecycler notificableDelClickRecycler) {
        this.mContext = context;
        this.listener = notificableDelClickRecycler;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_product = layoutInflater.inflate(R.layout.item_product, parent, false);

        return new ProductAdapter.ViewHolderProducts(item_product);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        ProductAdapter.ViewHolderProducts viewHolderProducts = (ProductAdapter.ViewHolderProducts) holder;
        viewHolderProducts.loadProduct(product);
    }

    @Override
    public Filter getFilter() {
        if (productFilter == null) {
            productFilter = new ProductFilter(this, productList);
        }
        return productFilter;
    }

    @Override
    public int getItemCount() {
        return (productList == null) ? 0 : productList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    private class ViewHolderProducts extends RecyclerView.ViewHolder {


        private TextView type;
        private TextView material;
        private TextView stock;
        private TextView descriptionColor;
        private TextView price;
        private ImageView image;
        private Button buttonUpdate;
        private Button buttonDelete;


        public ViewHolderProducts(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            material = itemView.findViewById(R.id.material);
            stock = itemView.findViewById(R.id.stock);
            descriptionColor = itemView.findViewById(R.id.colorDescription);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            buttonUpdate = itemView.findViewById(R.id.productUpdate);
            buttonDelete = itemView.findViewById(R.id.productDelete);

            itemView.setOnClickListener(view -> listener.showProduct(productList.get(getBindingAdapterPosition())));
            buttonUpdate.setOnClickListener(view -> listener.notificaUpdate(productList.get(getBindingAdapterPosition())));
            buttonDelete.setOnClickListener(view -> listener.notificaDelete(productList.get(getBindingAdapterPosition())));


        }

        private void loadProduct(Product product) {
            type.setText(product.getType());
            material.setText(product.getMaterial());
            stock.setText(product.getStock());
            descriptionColor.setText(product.getColorDescription());
            price.setText(product.getPrice());
            Glide.with(mContext).load(product.getImage()).into(image);
        }

    }

    public interface NotificableDelClickRecycler {
        void showProduct(Product product);

        void notificaUpdate(Product product);

        void notificaDelete(Product product);
    }

}
