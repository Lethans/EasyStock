package com.example.easystock.views.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.ActivityUsersBinding;
import com.example.easystock.models.User;
import com.example.easystock.views.fragments.UsersFragment;
import com.example.easystock.views.fragments.UsersFragmentDirections;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem;

public class UserActivity extends AppCompatActivity implements UsersFragment.NotificableUsersFragment {

    public static final String USER_PROFILE = "USER_PROFILE";
    public static final String USERS = "USERS";
    public static final String USER_TO_UPDATE = "USER_TO_UPDATE";

    private ActivityUsersBinding mBinding;
    private UserViewModel mUserViewModel;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        mBinding.setLifecycleOwner(this);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        navController = Navigation.findNavController(this, R.id.nav_users_container);

        CbnMenuItem cbn1 = new CbnMenuItem(R.drawable.ic_baseline_groups_24, R.drawable.avd_group, 0);
        CbnMenuItem cbn2 = new CbnMenuItem(R.drawable.ic_baseline_add_24, R.drawable.avd_plus, 1);
        CbnMenuItem cbn3 = new CbnMenuItem(R.drawable.ic_baseline_person_white, R.drawable.avd_profile, 2);

        CbnMenuItem[] menuItems = new CbnMenuItem[]{cbn1, cbn2, cbn3};
/*                new CbnMenuItem(R.drawable.ic_baseline_groups_24, R.drawable.avd_group, 0),
                new CbnMenuItem(R.drawable.ic_baseline_add_24, R.drawable.avd_plus, 1),
                new CbnMenuItem(R.drawable.ic_baseline_person_white, R.drawable.avd_profile, 2)};*/

        mBinding.navView.setMenuItems(menuItems, 0);
        setActiveBottomMenuItem(0);

        mBinding.navView.setOnMenuItemClickListener(new Function2<CbnMenuItem, Integer, Unit>() {
            @Override
            public Unit invoke(CbnMenuItem cbnMenuItem, Integer integer) {
                switch (integer) {
                    case 0:
                        navController.navigate(R.id.action_to_users_fragment);
                        break;
                    case 1:
                        navController.navigate(R.id.action_to_crud_fragment);
                        break;
                    case 2:
                        navController.navigate(R.id.action_to_profile_fragment);
                        break;
                }
                return Unit.INSTANCE;
            }
        });

        /*app:cbn_selectedColor	Tint for the icon in selected state	#000000
        app:cbn_unSelectedColor	Tint for the icon in unselected state	#8F8F8F
        app:cbn_animDuration	Duration in millisecond for the curve animation	300L
        app:cbn_fabElevation	Elevation for the Floating Action Button	4dp
        app:cbn_elevation	Elevaton for the Curved Bottom Navigation View	6dp
        app:cbn_fabBg	Background color of the Floating Action Button	#FFFFFF
        app:cbn_bg	Background color of the Curved Bottom Navigation	#FFFFFF*/

    }

    private void setActiveBottomMenuItem(int index) {
        mBinding.navView.onMenuItemClick(index);
    }


    @Override
    public void updateUser(User user) {
        //Se hizo con una interfaz porque llamando al navigation con argumentos desde el mismo fragment
        //no activaba el curved bottom navigation, entonces funcionaba pero sin hacer el cambio de item
        setActiveBottomMenuItem(1);
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", user);
        navController.navigate(R.id.action_to_crud_fragment, bundle);

        //UsersFragmentDirections.ActionUsersToCrudFragment action = UsersFragmentDirections.actionUsersToCrudFragment();
        //action.setUser(user);
        //navController.navigate(action);
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
}