package com.example.easystock.views.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.biometric.BiometricManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.models.User;
import com.example.easystock.views.adapters.LoginAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private AppCompatSpinner mSpinner;
    private EditText mPassword;
    private Button btnLogin;
    private UserViewModel mUserViewModel;
    private List<User> userList = new ArrayList<>();
    private LoginAdapter mAdapter;
    private User mUser;
    private String userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserViewModel = new ViewModelProvider(LoginActivity.this).get(UserViewModel.class);
        init();
        setObservers();
        setSpinner();


       Button btnFingerprint = findViewById(R.id.btnFingerprint);
        btnFingerprint.setOnClickListener(v -> {

        });


        btnLogin.setOnClickListener(v -> {
            if (TextUtils.equals(userPassword, mPassword.getText().toString())) {
                //Fixme query para pasar todos a login false y el que loguea en true
/*                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("ROLE", mUser.getRole());
                startActivity(intent);*/
                Dexter.withContext(this)
                        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.USE_FINGERPRINT)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("ROLE", mUser.getRole());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Se necesita activar los permisos para continuar", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).onSameThread().check();


            } else {
                mPassword.setText("");
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                //Borrar esto luego
/*                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("ROLE", mUser.getRole());
                startActivity(intent);*/
                //Borrar esto luego
            }
        });
    }

    private void init() {
        mSpinner = findViewById(R.id.usersSpinner);
        mPassword = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setObservers() {
        mUserViewModel.getAllUsers().observe(this, users -> {
            if (users != null) {
                //fixme Revisar usuarios de firebase 09-11
                if (users.size() != 0)
                    userList = users;
                else
                    createAdmin();

                mAdapter = new LoginAdapter(LoginActivity.this, R.layout.item_spinner_login, userList);
                mSpinner.setAdapter(mAdapter);
            }
        });
    }

    private void createAdmin() {
        User user = new User(0, "ADMIN", "ADMIN", "1234", "1111111111", "9");
        mUserViewModel.insertUser(user);
    }

    private void setSpinner() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUser = userList.get(position);
                userPassword = userList.get(position).getPassword();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}