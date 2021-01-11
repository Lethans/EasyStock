package com.example.easystock.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.ActivityRecoveryPasswordBinding;
import com.example.easystock.models.User;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easystock.R;

public class RecoveryPasswordActivity extends AppCompatActivity {

    public static final String RECOVERY_NUMBER = "RECOVERY_NUMBER";
    public static final String RECOVERY_USER = "RECOVERY_USER";

    private ActivityRecoveryPasswordBinding mBinding;
    private UserViewModel mUserViewModel;
    private String recoveryCode = "";
    private User recoveryPasswordUser;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recovery_password);

        mBinding.setLifecycleOwner(this);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding.setIsRecoveryCorrect(false);
        mBinding.setIsCodeError(false);

        recoveryCode = getIntent().getExtras().getString(RECOVERY_NUMBER);
        recoveryPasswordUser = (User) getIntent().getExtras().getSerializable(RECOVERY_USER);


        mBinding.recoveryFirstDigit.addTextChangedListener(firstInputWatcher);
        mBinding.recoverySecondDigit.addTextChangedListener(secondInputWatcher);
        mBinding.recoveryThirdDigit.addTextChangedListener(thirdInputWatcher);
        mBinding.recoveryFourthDigit.addTextChangedListener(validRecoveryCodeWatcher);


        String[] splitEmail = recoveryPasswordUser.getEmail().split("[@]");
        if (splitEmail[0].length() >= 4)
            mBinding.setUserEmail(splitEmail[0].substring(0, 3) + "******" + "@" + splitEmail[1]);
        else
            mBinding.setUserEmail(splitEmail[0].substring(0, 2) + "******" + "@" + splitEmail[1]);


        mBinding.btnConfirmeUpdatePassword.setOnClickListener(v -> {
            if (TextUtils.equals(mBinding.newresetpassword.getText().toString().trim(), mBinding.newConfirmResetpassword.getText().toString().trim())) {
                recoveryPasswordUser.setPassword(mBinding.newresetpassword.getText().toString().trim());
                mUserViewModel.updateUser(recoveryPasswordUser);
                Toast.makeText(this, "Contraseña actualizada con exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
    }


    private TextWatcher firstInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().isEmpty())
                mBinding.recoverySecondDigit.requestFocus();
            else
                setPasswordBoxBackground(R.drawable.custom_recovery_box);
        }
    };
    private TextWatcher secondInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().isEmpty())
                mBinding.recoveryThirdDigit.requestFocus();
            else
                setPasswordBoxBackground(R.drawable.custom_recovery_box);

        }
    };
    private TextWatcher thirdInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().isEmpty())
                mBinding.recoveryFourthDigit.requestFocus();
            else
                setPasswordBoxBackground(R.drawable.custom_recovery_box);

        }
    };
    private TextWatcher validRecoveryCodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String code = mBinding.recoveryFirstDigit.getText().toString() + mBinding.recoverySecondDigit.getText().toString() + mBinding.recoveryThirdDigit.getText().toString() + s.toString();
            if (!s.toString().isEmpty()) {
                if (TextUtils.equals(recoveryCode, code)) {

                    mBinding.lockAnimation.setVisibility(View.VISIBLE);
                    mBinding.lockAnimation.setAnimation("unlock.json");
                    mBinding.lockAnimation.playAnimation();

                    new Handler().postDelayed(() -> {
                        mBinding.lockAnimation.setVisibility(View.GONE);
                        mBinding.setIsRecoveryCorrect(true);
                    }, 5000);


                } else
                    setPasswordBoxBackground(R.drawable.recovery_code_error);
            } else
                setPasswordBoxBackground(R.drawable.custom_recovery_box);
        }
    };


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setPasswordBoxBackground(int customDrawableReference) {
        mBinding.recoveryFirstDigit.setBackground(getResources().getDrawable(customDrawableReference));
        mBinding.recoverySecondDigit.setBackground(getResources().getDrawable(customDrawableReference));
        mBinding.recoveryThirdDigit.setBackground(getResources().getDrawable(customDrawableReference));
        mBinding.recoveryFourthDigit.setBackground(getResources().getDrawable(customDrawableReference));
        mBinding.setIsCodeError(customDrawableReference == R.drawable.recovery_code_error);
    }


}