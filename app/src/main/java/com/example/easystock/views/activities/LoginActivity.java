package com.example.easystock.views.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.biometric.BiometricManager;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.listeners.GetUserFingerprintListener;
import com.example.easystock.models.User;
import com.example.easystock.views.adapters.LoginAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LoginActivity extends AppCompatActivity {

    private AppCompatSpinner mSpinner;
    private EditText mPassword;
    private Button btnLogin;
    private UserViewModel mUserViewModel;
    private List<User> userList = new ArrayList<>();
    private LoginAdapter mAdapter;
    private User mUser;
    private String userPassword;
    private CheckBox fingerPrintCheck;
    private boolean fingerprintControl = false;
    /* Fingerprint*/
    public static final int BIOMETRIC_SUCCESS = 0;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserViewModel = new ViewModelProvider(LoginActivity.this).get(UserViewModel.class);
        init();
        setObservers();
        setSpinner();

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.USE_BIOMETRIC)
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


        if (BiometricManager.from(LoginActivity.this).canAuthenticate() == BIOMETRIC_SUCCESS) {
            fingerPrintCheck.setVisibility(View.VISIBLE);
        }
        /*Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                } else {
                    Log.d("TAG", "An unrecoverable error occurred");
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d("TAG", "Fingerprint recognised successfully");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("TAG", "Fingerprint not recognised");
            }
        });

//Create the BiometricPrompt instance//
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Title text goes here")
                .setSubtitle("Subtitle goes here")
                .setDescription("This is the description")
                .setNegativeButtonText("Cancel")
                .build();


        myBiometricPrompt.authenticate(promptInfo);*/

        ImageView btnFingerprint = findViewById(R.id.btnFingerprint);
        btnFingerprint.setOnClickListener(v -> {
            fingerprintControl = false;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Biometric authentication permission not granted", Toast.LENGTH_LONG).show();
            } else {
                FingerprintManagerCompat fmc = FingerprintManagerCompat.from(this);
                if (!fmc.isHardwareDetected()) {
                    Toast.makeText(this, "There is no fingerprint sensor hardware found", Toast.LENGTH_LONG).show();
                } else if (!fmc.hasEnrolledFingerprints()) {
                    Toast.makeText(this, "There are no fingerprints currently enrolled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Fingerprint authentication is ready for testing", Toast.LENGTH_LONG).show();

                    //final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder(this)
                    // Set up the authentication dialog
                    BiometricPrompt biometricPrompt = new BiometricPrompt
                            .Builder(this)
                            .setTitle("Biometric Authentication")
                            .setSubtitle("Please authenticate to continue")
                            .setDescription("Fingerprinting in biometric authentication API is being tested.")
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
                }
            }

        });

        btnLogin.setOnClickListener(v -> {
            if (TextUtils.equals(userPassword, mPassword.getText().toString())) {
                mUser.setFingerprint(fingerPrintCheck.isChecked() ? "YES" : "NO");
                mUser.setIsLogged(true);
                mUserViewModel.updateUsersLogged(mUser);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("ROLE", mUser.getRole());
                startActivity(intent);
            } else {
                mPassword.setText("");
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                //Borrar esto luego

                //Borrar esto luego
            }
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
                /*mUserViewModel.getUserWithFingerprint(new GetUserFingerprintListener() {
                    @Override
                    public void onGetUser(User user) {
                        mUser.setFingerprint(fingerPrintCheck.isActivated() ? "YES" : "NO");
                        mUser.setIsLogged(true);
                        mUserViewModel.updateUsersLogged(mUser);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("ROLE", mUser.getRole());
                        startActivity(intent);
                    }

                    @Override
                    public void onNotGetUser(String msj) {
                        Toast.makeText(LoginActivity.this, msj, Toast.LENGTH_SHORT).show();

                    }
                });*/
                mUserViewModel.getUserFingerPrint().observe(LoginActivity.this, user -> {
                    if (!fingerprintControl) {
                        if (user != null) {
                            fingerprintControl = true;
                            mUser.setFingerprint(fingerPrintCheck.isActivated() ? "YES" : "NO");
                            mUser.setIsLogged(true);
                            mUserViewModel.updateUsersLogged(mUser);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("ROLE", mUser.getRole());
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Ingrese su cuenta manual y vuelva a seleccionar Mantener sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*mUserViewModel.getUserWithFingerprint(user -> {
                    if (user != null) {
                        mUser.setFingerprint(fingerPrintCheck.isActivated() ? "YES" : "NO");
                        mUser.setIsLogged(true);
                        mUserViewModel.updateUsersLogged(mUser);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("ROLE", mUser.getRole());
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Ingrese su cuenta manual y vuelva a seleccionar Mantener sesión", Toast.LENGTH_SHORT).show();
                    }
                });*/


                super.onAuthenticationSucceeded(result);
            }
        };
    }

    private CancellationSignal cancellationSignal;

    private CancellationSignal getCancellationSignal() {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> Toast.makeText(LoginActivity.this, "5", Toast.LENGTH_SHORT).show());

        return cancellationSignal;
    }


    private void init() {
        mSpinner = findViewById(R.id.usersSpinner);
        mPassword = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        fingerPrintCheck = findViewById(R.id.fingerPrintCheck);
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
                fingerPrintCheck.setActivated(TextUtils.equals(mUser.getFingerprint(), "YES"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}