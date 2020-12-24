package com.example.easystock.databinding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.models.User;
import com.example.easystock.views.adapters.UserAdapter;

import java.util.List;

public class UsersFragmentBindingAdapters {

    @BindingAdapter("usersList")
    public static void setUsersList(RecyclerView view, List<User> users) {
        if (users == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        }
        UserAdapter adapter = (UserAdapter) view.getAdapter();

        if (adapter == null) {
            adapter = new UserAdapter(view.getContext(), users);
            view.setAdapter(adapter);
        }
    }
}













