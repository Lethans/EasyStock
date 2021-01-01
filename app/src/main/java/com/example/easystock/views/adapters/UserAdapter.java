package com.example.easystock.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.databinding.UserItemBinding;
import com.example.easystock.databinding.UserItemMenuBinding;
import com.example.easystock.interfaces.IUsersActivity;
import com.example.easystock.models.User;
import com.example.easystock.views.fragments.UsersFragment;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<User> userList;
    private final int SHOW_MENU = 1;
    private final int HIDE_MENU = 2;
    private UsersFragment.NotificableUsersFragment mListener;

    public UserAdapter(Context context, UsersFragment.NotificableUsersFragment listener) {
        this.mContext = context;
        this.mListener = listener;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (userList.get(position).isShowMenu()) {
            return SHOW_MENU;
        } else {
            return HIDE_MENU;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SHOW_MENU) {
            UserItemMenuBinding bindingMenu = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext), R.layout.user_item_menu, parent, false);
            return new BindingHolderMenu(bindingMenu.getRoot());
        } else {
            UserItemBinding bindingItem = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.user_item, parent, false);
            return new BindingHolderUsers(bindingItem.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userList.get(position);
        if (holder instanceof BindingHolderUsers) {
            ((BindingHolderUsers) holder).mBinding.setUser(user);
            ((BindingHolderUsers) holder).mBinding.executePendingBindings();
        }
        if (holder instanceof BindingHolderMenu) {
            ((BindingHolderMenu) holder).mBinding.setUser(user);
            ((BindingHolderMenu) holder).mBinding.setCallback(mListener);
        }
    }

    @Override
    public int getItemCount() {
        return (userList == null) ? 0 : userList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        closeMenu();
        notifyDataSetChanged();
    }


    public void showMenu(int position) {
        boolean isMenuShow = userList.get(position).isShowMenu();

        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setShowMenu(false);
        }
        userList.get(position).setShowMenu(!isMenuShow);
        notifyDataSetChanged();
    }

    /*    public boolean isMenuShown() {
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).isShowMenu()) {
                    return true;
                }
            }
            return false;
        }

        */
    public void closeMenu() {
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setShowMenu(false);
        }
    }

    public class BindingHolderUsers extends RecyclerView.ViewHolder {

        UserItemBinding mBinding;
        //ViewDataBinding mBinding;

        public BindingHolderUsers(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

    }

    public class BindingHolderMenu extends RecyclerView.ViewHolder {


        private UserItemMenuBinding mBinding;

        public BindingHolderMenu(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }
    }

    public interface NotificableRecyclerUsers {

        void updateUser(User user);

        void deleteUser(User user);
    }
}
