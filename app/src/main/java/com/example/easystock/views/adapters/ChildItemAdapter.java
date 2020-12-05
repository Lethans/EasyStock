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
import com.example.easystock.models.ChildItem;

import java.util.List;

public class ChildItemAdapter extends RecyclerView.Adapter/*<ChildItemAdapter.ChildViewHolder>*/ {

    private List<ChildItem> childItemList;
    private Context mCtx;
    private ListenerChildItem listener;

    public ChildItemAdapter(List<ChildItem> childItemList, Context context, ListenerChildItem listenerChildItem) {
        this.childItemList = childItemList;
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
        ChildItem childItem = childItemList.get(position);
        ChildViewHolder viewHolderChild = (ChildViewHolder) holder;
        viewHolderChild.setChildInfo(childItem);
        //childViewHolder.ChildItemTitle.setText(childItem.getChildItemTitle());
    }

    @Override
    public int getItemCount() {
        return (childItemList == null) ? 0 : childItemList.size();
    }


    private class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView childItemTitle;
        private ImageView mImage;

        public ChildViewHolder(View itemView) {
            super(itemView);
            childItemTitle = itemView.findViewById(R.id.child_item_title);
            mImage = itemView.findViewById(R.id.img_child_item);
            itemView.setOnClickListener(view -> listener.onChildItemClick(childItemList.get(getBindingAdapterPosition())));
        }

        private void setChildInfo(ChildItem childInfo) {
            childItemTitle.setText(String.valueOf(childInfo.getmProductId()));
            Glide.with(mCtx).load(childInfo.getmProductIdImage()).into(mImage);
        }
    }

    public interface ListenerChildItem {
        void onChildItemClick(ChildItem childInfo);
    }
} 