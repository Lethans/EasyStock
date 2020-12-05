package com.example.easystock.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easystock.R;
import com.example.easystock.models.Product;

import java.util.List;

public class SimilarProductAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Product> productList;
    private NotificableDelClickRecycler listener;

    public SimilarProductAdapter(Context context, NotificableDelClickRecycler notificableDelClickRecycler) {
        this.mContext = context;
        this.listener = notificableDelClickRecycler;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_product = layoutInflater.inflate(R.layout.item_similar_product, parent, false);

        return new SimilarProductAdapter.ViewHolderProducts(item_product);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        SimilarProductAdapter.ViewHolderProducts viewHolderProducts = (SimilarProductAdapter.ViewHolderProducts) holder;
        viewHolderProducts.loadSimilarProduct(product);
    }

    @Override
    public int getItemCount() {
        return (productList == null) ? 0 : productList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    private class ViewHolderProducts extends RecyclerView.ViewHolder {


        private TextView type;
        private TextView material;
        private TextView stock;
        private ImageView image;


        public ViewHolderProducts(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.typeSimilar);
            material = itemView.findViewById(R.id.materialSimilar);
            stock = itemView.findViewById(R.id.stockSimilar);
            image = itemView.findViewById(R.id.imageSimilar);
            itemView.setOnClickListener(view -> listener.showProduct(productList.get(getBindingAdapterPosition())));
        }

        private void loadSimilarProduct(Product product) {
            type.setText(product.getType());
            material.setText(product.getMaterial());
            stock.setText(product.getStock());
            Glide.with(mContext).load(product.getImage()).into(image);
        }

    }

    public interface NotificableDelClickRecycler {
        void showProduct(Product product);
    }

}
