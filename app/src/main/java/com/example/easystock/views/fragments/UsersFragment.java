package com.example.easystock.views.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.FragmentUsersBinding;
import com.example.easystock.interfaces.IUsersActivity;
import com.example.easystock.models.User;
import com.example.easystock.views.activities.UserActivity;
import com.example.easystock.views.adapters.UserAdapter;

import java.util.Objects;

public class UsersFragment extends Fragment {

    private UserViewModel mUserViewModel;
    //private UserAdapter mAdapter;
    //private RecyclerView mRecycler;
    //private NotificableUsersFragment mListener;

    private FragmentUsersBinding mBinding;

    public UsersFragment() {
    }

    public static UsersFragment newInstance() {
        UsersFragment fragment = new UsersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUsersBinding.inflate(inflater);

        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> mBinding.setUsers(users));
        setRecyclerSwipe();
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        //binding.lifecycleOwner = this
        return mBinding.getRoot();
    }

    private void setRecyclerSwipe() {
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.purple_200));

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                ((UserAdapter) mBinding.usersRecycler.getAdapter()).showMenu(position);
/*                if (mAdapter.isMenuShown())
                    mAdapter.closeMenu();
                mAdapter.showMenu(viewHolder.getBindingAdapterPosition());*/
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                if (dX > 0) {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.usersRecycler);
    }


    /*    public interface NotificableUsersFragment {

        void updateUser(User user);

        void deleteUser(User user);
    }*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //mListener = (UsersFragment.NotificableUsersFragment) context;
    }

}