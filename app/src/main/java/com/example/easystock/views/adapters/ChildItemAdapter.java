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

public class ChildItemAdapter extends RecyclerView.Adapter/*<ChildItemAdapter.ChildViewHolder>*/ {

    private List<Product> mChildsList;
    private Context mCtx;
    private ListenerChildItem listener;

    public ChildItemAdapter(List<Product> childItemList, Context context, ListenerChildItem listenerChildItem) {
        this.mChildsList = childItemList;
        this.mCtx = context;
        this.listener = listenerChildItem;
    }

    /*    @NonNull
        @Override
        public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item, viewGroup, false);
            return new ChildViewHolder(view);
                *//*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View celda = layoutInflater.inflate(R.layout.item_cash_movement, parent, false);
            return new ViewHolderCaja(celda);*//*
    }*/
    /*@Override
    public void onBindViewHolder(@NonNull ChildViewHolder childViewHolder, int position) {
        ChildItem childItem = ChildItemList.get(position);
        ChildViewHolder viewHolderChild = (ChildViewHolder) childViewHolder;
        viewHolderChild.setChildInfo(childItem);
        //childViewHolder.ChildItemTitle.setText(childItem.getChildItemTitle());
    }*/
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View celda = layoutInflater.inflate(R.layout.child_item, parent, false);
        return new ChildViewHolder(celda);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = mChildsList.get(position);
        ChildViewHolder viewHolderChild = (ChildViewHolder) holder;
        viewHolderChild.setChildInfo(product);
        //childViewHolder.ChildItemTitle.setText(childItem.getChildItemTitle());
    }

    @Override
    public int getItemCount() {
        return (mChildsList == null) ? 0 : mChildsList.size();
    }


    private class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView childItemTitle;
        private ImageView mImage;

        public ChildViewHolder(View itemView) {
            super(itemView);
            childItemTitle = itemView.findViewById(R.id.child_item_title);
            mImage = itemView.findViewById(R.id.img_child_item);
            itemView.setOnClickListener(view -> listener.onChildItemClick(mChildsList.get(getBindingAdapterPosition())));
        }

        private void setChildInfo(Product product) {
            childItemTitle.setText(String.valueOf(product.getId()));
            Glide.with(mCtx).load(product.getImage()).into(mImage);
        }
    }

    public interface ListenerChildItem {
        void onChildItemClick(Product product);
    }
} 