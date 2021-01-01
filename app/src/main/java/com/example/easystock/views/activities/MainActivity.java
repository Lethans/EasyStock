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
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.models.User;
import com.example.easystock.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private CardView btnUser;
    private CardView btnTwo;
    private CardView btnThird;
    private CardView btnFour;
    private CardView btnFifth;
    private String role;
    private UserViewModel mUserViewModel;
    private User mUser;
    private ConstraintLayout constraint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserViewModel = new ViewModelProvider(MainActivity.this).get(UserViewModel.class);

        constraint = findViewById(R.id.constraintMenu);

        Intent intent = getIntent();
        role = intent.getStringExtra("ROLE");
        init();

        TextView text = findViewById(R.id.textUser);
        mUserViewModel.getUserLogged().observe(this, user -> {
            text.setText(user.getName());
            mUser = user;
        });

        //Fixme snackbar para implementar en delet de productos o usuarios
        text.setOnClickListener(v -> {
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
        });


        btnUser.setOnClickListener(v -> {
            if (TextUtils.equals(role, Constants.ROLE_ADMIN)) {
                Intent intent1 = new Intent(MainActivity.this, UserActivity.class);
                intent1.putExtra(UserActivity.USERS, "ALL_USERS");
                startActivity(intent1);
            } else {
                Toast.makeText(this, "Vendedor no", Toast.LENGTH_SHORT).show();
            }


        });

        btnTwo.setOnClickListener(v -> {
            if (TextUtils.equals(role, Constants.ROLE_ADMIN)) {
                Intent intent1 = new Intent(MainActivity.this, StockActivity.class);
                startActivity(intent1);
            } else {
                Toast.makeText(MainActivity.this, "Función habilitada para administradores", Toast.LENGTH_SHORT).show();
            }
        });

        btnThird.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, ShowStock.class);
            startActivity(intent1);
        });

        btnFour.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, UserActivity.class);
            intent1.putExtra(UserActivity.USER_PROFILE, "USER_PROFILE");
            intent1.putExtra(UserActivity.USER_TO_UPDATE, mUser);
            startActivity(intent1);
        });

        btnFifth.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, CheckoutActivity.class);
            startActivity(intent1);
        });
    }

    private void init() {
        btnUser = findViewById(R.id.firstIcon);
        btnTwo = findViewById(R.id.secondIcon);
        btnThird = findViewById(R.id.thirdIcon);
        btnFour = findViewById(R.id.fourIcon);
        btnFifth = findViewById(R.id.fifthIcon);
    }
}