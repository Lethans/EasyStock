package com.example.easystock.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.ActivityUsersBinding;
import com.example.easystock.models.User;
import com.example.easystock.views.fragments.CrudUserFragment;
import com.example.easystock.views.fragments.UserProfileFragment;
import com.example.easystock.views.fragments.UsersFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UserActivity extends AppCompatActivity implements UsersFragment.NotificableUsersFragment {

    public static final String USER_PROFILE = "USER_PROFILE";
    public static final String USERS = "USERS";
    public static final String USER_TO_UPDATE = "USER_TO_UPDATE";

    private ActivityUsersBinding mBinding;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        mBinding.setLifecycleOwner(this);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mBinding.setIsNewUser(true);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        String tag = null;
        if (intent.getExtras().getString(USERS) != null) {
            fragment = UsersFragment.newInstance();
            tag = getString(R.string.fragment_users);
            mBinding.setIsNewUser(true);
        } else if (intent.getExtras().getString(USER_PROFILE) != null) {
            fragment = UserProfileFragment.newInstance();
            tag = getString(R.string.fragment_user_profile);
            User user = (User) intent.getExtras().getSerializable(USER_TO_UPDATE);
            bundle.putSerializable(UserProfileFragment.USER, user);
            mBinding.setIsNewUser(false);
        }
        loadFragment(fragment, bundle, tag);

        mBinding.bottomNavigationUsers.show(1, true);
        mBinding.bottomNavigationUsers.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_groups_24));
        mBinding.bottomNavigationUsers.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_group_add_24));
        mBinding.bottomNavigationUsers.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_person_white));


        mBinding.bottomNavigationUsers.setOnClickMenuListener(model -> {
            //aca las acciones que pasa con los cloicks
            if (model.getId() == 1)
                Toast.makeText(UserActivity.this, "1", Toast.LENGTH_SHORT).show();
            else if (model.getId() == 2)
                Toast.makeText(UserActivity.this, "2", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(UserActivity.this, "3", Toast.LENGTH_SHORT).show();
            return null;
        });

        mBinding.bottomNavigationUsers.setOnShowListener(model -> {
            //Aca va el menu qe abre por doefecto
            return null;
        });

            /*mBinding.addUserBtn.setOnClickListener(v -> {
                if (!(loadedFragment() instanceof CrudUserFragment)) {
                    CrudUserFragment crudFragment = CrudUserFragment.newInstance();
                    loadFragment(crudFragment, null, getString(R.string.fragment_new_user));
                }
            });*/


    }

    private void loadFragment(Fragment fragment, Bundle bundle, String fragmentTag) {
        if (bundle != null)
            fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.replace(R.id.userFrameContainer, fragment, fragmentTag);
        if (fragment instanceof CrudUserFragment)
            transaction.addToBackStack(getString(R.string.fragment_new_user));
        transaction.commit();
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
/*        if (loadedFragment() instanceof UsersFragment || loadedFragment() instanceof UserProfileFragment) {
            mBinding.setIsNewUser(true);
            clearStack();
        }*/
        super.onBackPressed();
    }

    @Override
    public void updateUser(User user) {
/*        CrudUserFragment crudFragment = CrudUserFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CrudUserFragment.USER, user);
        bundle.putString(CrudUserFragment.OPERATION, "UPDATE");
        mBinding.setIsNewUser(false);
        loadFragment(crudFragment, bundle, getString(R.string.fragment_new_user));*/
    }

    @Override
    public void deleteUser(User user) {
        /*mBinding.setIsNewUser(true);
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
        }*/
    }

    private Fragment loadedFragment() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        return mFragmentManager.findFragmentById(R.id.userFrameContainer);
    }
}