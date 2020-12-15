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
import com.example.easystock.models.Payment;

import java.util.List;


public class PaymentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_CHANGE = 1;
    private static int TYPE_PAYMENT = 2;
    private Context mContext;
    private List<Payment> mPaymentList;
    private NotificablePaymentsAdapter listener;

    public PaymentsAdapter(Context context) {
        this.mContext = context;
        //this.listener = notificableDelClickRecycler;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mPaymentList.get(position).getProductChange() != null) {
            return TYPE_CHANGE;
        } else {
            return TYPE_PAYMENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_CHANGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_change, parent, false);
            return new ViewHolderPaymentsChange(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
            return new ViewHolderPayments(view);
        }

       /* LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_payment = layoutInflater.inflate(R.layout.cart_shop_item, parent, false);
        return new ViewHolderPayments(item_payment);*/
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Payment payment = mPaymentList.get(position);

        if (holder instanceof ViewHolderPaymentsChange) {
            ViewHolderPaymentsChange viewHolderPaymentsChange = (ViewHolderPaymentsChange) holder;
            viewHolderPaymentsChange.loadProductChange(payment);
        }
        if (holder instanceof ViewHolderPayments) {
            ViewHolderPayments viewHolderPayments = (ViewHolderPayments) holder;
            viewHolderPayments.loadPayment(payment);
        }
/*        if (getItemViewType(position) == TYPE_CHANGE) {
        } else {
        }*/
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
        return (mPaymentList == null) ? 0 : mPaymentList.size();
    }

    public void setProductList(List<Payment> paymentList) {
        this.mPaymentList = paymentList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mPaymentList.remove(position);
        notifyDataSetChanged();
    }

    public Payment getPayment(int adapterPosition) {
        return mPaymentList.get(adapterPosition);
    }

    public void undoDelete(Payment payment, int position) {
        mPaymentList.add(position, payment);
        notifyDataSetChanged();
    }

    private class ViewHolderPayments extends RecyclerView.ViewHolder {

        private TextView method;
        private TextView amount;

        public ViewHolderPayments(View itemView) {
            super(itemView);
            method = itemView.findViewById(R.id.paymentMethod);
            amount = itemView.findViewById(R.id.paymentAmount);

        }

        private void loadPayment(Payment payment) {
            method.setText(payment.getMethod());
            amount.setText(payment.getAmount());
        }

    }

    private class ViewHolderPaymentsChange extends RecyclerView.ViewHolder {

        private TextView type;
        private TextView code;
        private TextView price;

        public ViewHolderPaymentsChange(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.paymentProductType);
            code = itemView.findViewById(R.id.paymentProductCode);
            price = itemView.findViewById(R.id.paymentProductAmount);

        }

        private void loadProductChange(Payment payment) {
            type.setText(payment.getProductChange().getType());
            code.setText(payment.getProductChange().getCode());
            price.setText(payment.getProductChange().getPrice());
        }

    }

    public interface NotificablePaymentsAdapter {
        void deleteProduct(Payment payment);
    }

}