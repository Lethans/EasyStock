package com.example.easystock.views.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.ActivityLoginBinding;
import com.example.easystock.listeners.GetUserListener;
import com.example.easystock.listeners.GetUsersCountListener;
import com.example.easystock.models.User;
import com.example.easystock.controllers.emailSender.GMailSender;
import com.example.easystock.views.fragments.SendEmailRecoveryFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LoginActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private String androidId = "";
    public static final int BIOMETRIC_SUCCESS = 0;
    private ActivityLoginBinding mBinding;
    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.setLifecycleOwner(this);
        mAuth = FirebaseAuth.getInstance();

        mUserViewModel = new ViewModelProvider(LoginActivity.this).get(UserViewModel.class);
        try {
            androidId = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUserViewModel.getUsersCount(new GetUsersCountListener() {
            @Override
            public void onEmptyUsers() {
                createAdmin();
                mBinding.usernameEdit.setText("ADMIN");
            }

            @Override
            public void onExistUsers() {

            }
        });

        mBinding.passwordEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                searchUser();
            }
            return false;
        });

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (!multiplePermissionsReport.areAllPermissionsGranted()) {
                            finish();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).onSameThread().check();


        mBinding.setHasFingerPrintHardware(BiometricManager.from(LoginActivity.this).canAuthenticate() == BIOMETRIC_SUCCESS);


        mBinding.fingerPrintBtn.setOnClickListener(v -> {
            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Biometric authentication permission not granted", Toast.LENGTH_LONG).show();
            } else {
                FingerprintManagerCompat fmc = FingerprintManagerCompat.from(this);
                if (!fmc.isHardwareDetected()) {
                    Toast.makeText(this, "There is no fingerprint sensor hardware found", Toast.LENGTH_LONG).show();
                } else if (!fmc.hasEnrolledFingerprints()) {
                    Toast.makeText(this, "There are no fingerprints currently enrolled", Toast.LENGTH_LONG).show();
                } else {*/
            //Toast.makeText(this, "Fingerprint authentication is ready for testing", Toast.LENGTH_LONG).show();

            //final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder(this)
            // Set up the authentication dialog
            BiometricPrompt biometricPrompt = new BiometricPrompt
                    .Builder(this)
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Por favor, autentifique para continuar")
                    //.setDescription("Fingerprinting in biometric authentication API is being tested.")
                    .setNegativeButton("Cancel", this.getMainExecutor(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(LoginActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .build();
            // Authenticate with callback functions
            biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(), getAuthenticationCallback());
        });

        mBinding.loginBtn.setOnClickListener(v -> searchUser());

        mBinding.forgotPassword.setOnClickListener(v -> {
            new SendEmailRecoveryFragment().show(getSupportFragmentManager());
            mBinding.forgotPassword.setClickable(false);
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                Toast.makeText(LoginActivity.this, "Fingerprint authentication error", Toast.LENGTH_LONG).show();
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                mUserViewModel.getUserFingerprint(androidId, new GetUserListener() {
                    @Override
                    public void onGetUser(User user) {
                        user.setLogged(true);
                        mUserViewModel.updateUsersLogged(user);
                        login(user);
                    }

                    @Override
                    public void onNotGetUser(String msj) {
                        Toast.makeText(LoginActivity.this, msj, Toast.LENGTH_SHORT).show();
                    }
                });
                /*mUserViewModel.getUserFingerPrint().observe(LoginActivity.this, user -> {
                    if (!fingerprintControl) {
                        if (user != null) {
                            fingerprintControl = true;
                            login();
                        } else {
                            Toast.makeText(LoginActivity.this, "Ingrese su cuenta manual y vuelva a seleccionar Mantener sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
                super.onAuthenticationSucceeded(result);
            }
        };
    }

    private void searchUser() {
        mUserViewModel.getLoginUser(mBinding.usernameEdit.getText().toString().trim(), mBinding.passwordEdit.getText().toString().trim(), new GetUserListener() {
            @Override
            public void onGetUser(User user) {
                user.setAndroidIdFingerprint(androidId);
                user.setLogged(true);
                mUserViewModel.updateUsersLogged(user);
                login(user);
                //mUserViewModel.updateUser(user);
            }

            @Override
            public void onNotGetUser(String msj) {
                mBinding.passwordEdit.setText("");
                Toast.makeText(LoginActivity.this, "Usuario Inexistente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(User user) {
        mBinding.passwordEdit.setText("");
        //mUser.setFingerprint(mBinding.fingerPrintCheck.isChecked() ? "YES" : "NO");
        //mUser.setLogged(true);
        //mUserViewModel.updateUsersLogged(mUser);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("ROLE", user.getRole());
        startActivity(intent);
    }

    private CancellationSignal cancellationSignal;

    private CancellationSignal getCancellationSignal() {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> Toast.makeText(LoginActivity.this, "5", Toast.LENGTH_SHORT).show());
        return cancellationSignal;
    }

    private void createAdmin() {
        User user = new User(1, "ADMIN", "ADMIN", "ADMIN", "1234", "demian_1705@hotmail.com", "1111111111", "9");
        mUserViewModel.insertUser(user);
    }


}