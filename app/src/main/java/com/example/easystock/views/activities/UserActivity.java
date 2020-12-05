package com.example.easystock.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.models.User;
import com.example.easystock.views.fragments.CrudUserFragment;
import com.example.easystock.views.fragments.UserProfileFragment;
import com.example.easystock.views.fragments.UsersFragment;

public class UserActivity extends AppCompatActivity implements UsersFragment.NotificableUsersFragment {

    public static final String USER_PROFILE = "USER_PROFILE";
    public static final String USERS = "USERS";
    public static final String USER_TO_UPDATE = "USER_TO_UPDATE";

    private Fragment frameContainer;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        frameContainer = getSupportFragmentManager().findFragmentById(R.id.usersContainer);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        if (intent.getExtras().getString(USERS) != null) {
            frameContainer = UsersFragment.newInstance();

        } else if (intent.getExtras().getString(USER_PROFILE) != null) {

            frameContainer = UserProfileFragment.newInstance();
            User user = (User) intent.getExtras().getSerializable(USER_TO_UPDATE);
            bundle.putSerializable(UserProfileFragment.USER, user);

        }
        loadFragment(frameContainer, bundle);

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
        //Toast.makeText(this,getSupportFragmentManager().getFragments().size(), Toast.LENGTH_SHORT).show();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment frag = mFragmentManager.findFragmentById(R.id.usersContainer);
        if (frag instanceof UsersFragment || frag instanceof UserProfileFragment)
            clearStack();
        super.onBackPressed();
    }

    @Override
    public void newUser() {
        frameContainer = CrudUserFragment.newInstance();
        loadFragment(frameContainer, null);
    }

    @Override
    public void updateUser(User user) {
        frameContainer = CrudUserFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CrudUserFragment.USER, user);
        bundle.putString(CrudUserFragment.OPERATION, "UPDATE");
        loadFragment(frameContainer, bundle);
    }

    @Override
    public void deleteUser(User user) {
        if (!TextUtils.equals(String.valueOf(user.getId()), "1")) {
            new AlertDialog.Builder(UserActivity.this)
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
            Toast.makeText(UserActivity.this, "No se puede eliminar el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment, Bundle bundle) {
        //frameContainer = CrudUserFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Bundle bundle = new Bundle();
        //bundle.putSerializable(CrudUserFragment.USER, user);
        //bundle.putString(CrudUserFragment.OPERATION, "UPDATE");
        fragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.replace(R.id.usersContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}