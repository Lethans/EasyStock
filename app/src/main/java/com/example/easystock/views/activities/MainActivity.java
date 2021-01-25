package com.example.easystock.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.ActivityMainBinding;
import com.example.easystock.models.User;
import com.example.easystock.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    private UserViewModel mUserViewModel;
    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);

        mUserViewModel = new ViewModelProvider(MainActivity.this).get(UserViewModel.class);


        mBinding.usersMainBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserActivity.class)));

        //Fixme snackbar para implementar en delet de productos o usuarios
/*        text.setOnClickListener(v -> {
            Snackbar snackbar = Snackbar
                    .make(constraint, "Message is deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar snackbar1 = Snackbar.make(constraint, "Message is restored!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });

            snackbar.show();
        });*/

    }
}