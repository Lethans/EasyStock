package com.example.easystock.views.fragments;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.R;
import com.example.easystock.controllers.emailSender.GMailSender;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.FragmentSendEmailRecoveryBinding;
import com.example.easystock.listeners.GetExistingUserEmailListener;
import com.example.easystock.models.User;
import com.example.easystock.models.repositories.UserRepository;
import com.example.easystock.views.activities.RecoveryPasswordActivity;

import java.util.Objects;
import java.util.Random;

public class SendEmailRecoveryFragment extends DialogFragment {

    private FragmentSendEmailRecoveryBinding mBinding;
    private UserViewModel mUserViewModel;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSendEmailRecoveryBinding.inflate(inflater);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);


        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        mBinding.userEmail.addTextChangedListener(validEmailWatcher);

        mBinding.btnSendRecoveryNumber.setOnClickListener(v -> {
            String email = mBinding.userEmail.getText().toString().trim();
            if (isEmailValid(email)) {
                mUserViewModel.getExistingUserEmail(email, new GetExistingUserEmailListener() {
                    @Override
                    public void onValidEmail(User user) {
                        Random random = new Random();
                        @SuppressLint("DefaultLocale") String recoveryNumber = String.format("%04d", random.nextInt(10000));
                        sendEmail(email, recoveryNumber);
                        Intent intent = new Intent(getContext(), RecoveryPasswordActivity.class);
                        intent.putExtra(RecoveryPasswordActivity.RECOVERY_NUMBER, recoveryNumber);
                        intent.putExtra(RecoveryPasswordActivity.RECOVERY_USER, user);
                        dismiss();
                        startActivity(intent);
                    }

                    @Override
                    public void onInvalidEmail() {
                        mBinding.recoveryEmailTextInputLayout.setError("Verifique la dirección ingresada");
                    }
                });
            }
        });

        return mBinding.getRoot();
    }

    private boolean isEmailValid(String email) {
        mBinding.recoveryEmailTextInputLayout.setErrorEnabled(true);

        if (email.isEmpty()) {
            mBinding.recoveryEmailTextInputLayout.setError("Debe ingresar una direccion");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.recoveryEmailTextInputLayout.setError("Formato de correo incorrecto");
            return false;
        }

        return true;
    }

    private TextWatcher validEmailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mBinding.recoveryEmailTextInputLayout.setErrorEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void show(FragmentManager supportFragmentManager) {
        this.show(supportFragmentManager, "PasswordRecoveryDialog");
    }

    protected void sendEmail(String userEmail, String recoveryNumber) {
        String messege = "Hola,\n" +
                "\n" +
                "Ingresa el siguiente código en la aplicación para cambiar tu contraseña.\n" +
                "\n" +
                "Código de recuperación: " + recoveryNumber + "\n" +
                "\n" +
                "Si no pediste recordar tu contraseña, podes ignorar este email.\n" +
                "\n" +
                "Muchas gracias,\n" +
                "\n" +
                "El equipo de EasyStock.";
        new Thread(() -> {
            try {
                GMailSender sender = new GMailSender(getResources().getString(R.string.senderName),
                        getResources().getString(R.string.senderSecret));
                sender.sendMail("EasyStock- Recuperar Contraseña", messege,
                        getResources().getString(R.string.senderName), userEmail);
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }
        }).start();
    }

    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), (int) (size.y * 0.45));
        window.setGravity(Gravity.CENTER);
    }

}