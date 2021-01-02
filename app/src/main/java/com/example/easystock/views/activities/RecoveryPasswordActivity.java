package com.example.easystock.views.activities;

import android.os.Bundle;

import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.ActivityRecoveryPasswordBinding;
import com.example.easystock.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Toast;

import com.example.easystock.R;

public class RecoveryPasswordActivity extends AppCompatActivity {

    public static final String RECOVERY_NUMBER = "RECOVERY_NUMBER";
    public static final String RECOVERY_USER = "RECOVERY_USER";

    private ActivityRecoveryPasswordBinding mBinding;
    private UserViewModel mUserViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recovery_password);
        mBinding.setLifecycleOwner(this);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        String recoveryNumber = getIntent().getExtras().getString(RECOVERY_NUMBER);
        User recoveryUser = (User) getIntent().getExtras().getSerializable(RECOVERY_USER);
        Toast.makeText(this, recoveryNumber, Toast.LENGTH_SHORT).show();

    }
}