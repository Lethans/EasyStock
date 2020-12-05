package com.example.easystock.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.models.User;
import com.example.easystock.views.adapters.UserAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class UsersFragment extends Fragment {

    private UserViewModel mUserViewModel;
    private UserAdapter mAdapter;
    private RecyclerView mRecycler;
    private FloatingActionButton btnAddUser;
    private NotificableUsersFragment mListener;

    public UsersFragment() {
    }

    public static UsersFragment newInstance() {
        UsersFragment fragment = new UsersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        mUserViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            mAdapter.setUserList(users);
            mAdapter.notifyDataSetChanged();
        });
        init(view);
        setRecycler();
        btnAddUser.setOnClickListener(v -> {
            mListener.newUser();
        });
        return view;
    }

    private void init(View v) {
        mRecycler = v.findViewById(R.id.usersRecycler);
        btnAddUser = v.findViewById(R.id.addUserBtn);
    }

    private void setRecycler() {
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setFocusable(false);
        mAdapter = new UserAdapter(getContext(), new UserAdapter.NotificableDelClickRecycler() {
            @Override
            public void notificaUpdate(User user) {
                mListener.updateUser(user);
            }

            @Override
            public void notificaDelete(User user) {
                mListener.deleteUser(user);
            }
        });
        mRecycler.setAdapter(mAdapter);
    }

    public interface NotificableUsersFragment {
        void newUser();

        void updateUser(User user);

        void deleteUser(User user);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (UsersFragment.NotificableUsersFragment) context;
    }
}