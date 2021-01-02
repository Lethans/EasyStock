package com.example.easystock.views.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSendEmailRecoveryBinding.inflate(inflater);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        setCancelable(false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }


        mBinding.btnSendRecoveryNumber.setOnClickListener(v -> {
            String email = mBinding.userEmail.getText().toString().trim();

            if (isEmailValid(email)) {
                mUserViewModel.getExistingUserEmail(email, new GetExistingUserEmailListener() {
                    @Override
                    public void onValidEmail(User user) {
                        Random random = new Random();
                        @SuppressLint("DefaultLocale") String recoveryNumber = String.format("%04d", random.nextInt(10000));
                        user.setRecoveryPasswordNumber(recoveryNumber);
                        //mUserViewModel.updateUser(user);
                        UserRepository userRepository = new UserRepository(getActivity());
                        userRepository.updateUser(user);
                        sendEmail(email, recoveryNumber);
                        Intent intent = new Intent(getContext(), RecoveryPasswordActivity.class);
                        intent.putExtra(RecoveryPasswordActivity.RECOVERY_NUMBER, recoveryNumber);
                        intent.putExtra(RecoveryPasswordActivity.RECOVERY_USER, user);
                        startActivity(intent);
                    }

                    @Override
                    public void onInvalidEmail() {
                        Toast.makeText(getContext(), "No existe el correo ingresado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return mBinding.getRoot();
    }

    private boolean isEmailValid(String email) {

        if (email.isEmpty())
            return false;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.recoveryEmailTextInputLayout.setError("Formato de correo incorrecto");
            return false;
        }

        return true;
    }

    public void show(FragmentManager supportFragmentManager) {
        this.show(supportFragmentManager, "PasswordRecoveryDialog");
    }

    protected void sendEmail(String userEmail, String recoveryNumber) {
        new Thread(() -> {
            try {
                GMailSender sender = new GMailSender(getResources().getString(R.string.senderName),
                        getResources().getString(R.string.senderSecret));
                sender.sendMail("Recupera tu contraseña numero", "Ingresa este numero en la aplicacion para generar una nueva clave: " + recoveryNumber,
                        getResources().getString(R.string.senderName), userEmail);
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }
        }).start();
    }

}