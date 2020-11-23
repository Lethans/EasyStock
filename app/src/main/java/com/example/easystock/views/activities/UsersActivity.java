package com.example.easystock.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.models.User;
import com.example.easystock.views.adapters.UserAdapter;
import com.example.easystock.views.fragments.CrudUserFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UsersActivity extends AppCompatActivity implements CrudUserFragment.NotificableNewUserFragment {

    private UserViewModel mUserViewModel;
    private UserAdapter mAdapter;
    private RecyclerView mRecycler;
    private Fragment frameNewUser;
    private FloatingActionButton btnAddUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mUserViewModel = new ViewModelProvider(UsersActivity.this).get(UserViewModel.class);
        mUserViewModel.getAllUsers().observe(this, users -> {
            mAdapter.setUserList(users);
            mAdapter.notifyDataSetChanged();
        });
        init();
        setRecycler();


        btnAddUser.setOnClickListener(v -> {
            frameNewUser = CrudUserFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
            transaction.replace(R.id.new_user_container, frameNewUser);
            transaction.addToBackStack(null);
            transaction.commit();
            btnAddUser.setVisibility(View.GONE);
        });

    }


    private void init() {
        mRecycler = findViewById(R.id.usersRecycler);
        frameNewUser = getSupportFragmentManager().findFragmentById(R.id.new_user_container);
        btnAddUser = findViewById(R.id.addUserBtn);
    }

    private void setRecycler() {
        mRecycler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setFocusable(false);
        mAdapter = new UserAdapter(this, new UserAdapter.NotificableDelClickRecycler() {
            @Override
            public void notificaUpdate(User user) {
                    frameNewUser = CrudUserFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CrudUserFragment.USER, user);
                    bundle.putString(CrudUserFragment.OPERATION, "UPDATE");
                    frameNewUser.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.replace(R.id.new_user_container, frameNewUser);
                    transaction.addToBackStack(null);
                    transaction.commit();
            }

            @Override
            public void notificaDelete(User user) {
                if (!TextUtils.equals(String.valueOf(user.getId()), "1")) {
                    new AlertDialog.Builder(UsersActivity.this)
                            .setIcon(R.drawable.ic_baseline_arrow_right_24)
                            .setTitle("Borrar usuario")
                            .setMessage("¿ Desea eliminar al usuario: " + user.getName() + " ?")
                            .setPositiveButton("Si", (dialog, which) -> {
                                mUserViewModel.deleteUser(user);
                                //Fixme 12-11 Agregar actualizacion firebase
                                //if(settingRepository!=null)settingRepository.updateSettingSyncUserData("true");
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    Toast.makeText(UsersActivity.this, "No se puede eliminar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRecycler.setAdapter(mAdapter);


    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        btnAddUser.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRemoveFragment() {
        clearStack();
        btnAddUser.setVisibility(View.VISIBLE);
    }
}