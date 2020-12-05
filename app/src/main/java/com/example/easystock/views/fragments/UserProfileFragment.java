package com.example.easystock.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.models.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class UserProfileFragment extends Fragment {

    public static final String USER = "USER";

    private TextView mName, mLastName, mPhone;
    private EditText editConfirmPassword, editPassword, editOldPassword;
    private Button btnUpdate, btnCancel, btnChangePassword;
    private ConstraintLayout mPasswordLayout;
    private UserViewModel mUserViewModel;
    private User mUser;

    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
/*        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mUserViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
        init(view);
        loadData(mUser);
        btnChangePassword.setOnClickListener(v -> mPasswordLayout.setVisibility(View.VISIBLE));
        btnUpdate.setOnClickListener(v -> {
            if (newPasswordValidations(view)) {
                mUserViewModel.updateUser(mUser);
                editPassword.setText("");
                editConfirmPassword.setText("");
                editOldPassword.setText("");
                mPasswordLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Contraseña actualizada con exito", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void init(View v) {
        mName = v.findViewById(R.id.sellerNameEdit);
        mLastName = v.findViewById(R.id.sellerLastnameEdit);
        mPhone = v.findViewById(R.id.sellerPhoneEdit);

        editPassword = v.findViewById(R.id.newPassword);
        editConfirmPassword = v.findViewById(R.id.newConfirmPassword);
        editOldPassword = v.findViewById(R.id.oldPassword);

        btnChangePassword = v.findViewById(R.id.updatePassword);
        btnCancel = v.findViewById(R.id.cancelUserProfile);
        btnUpdate = v.findViewById(R.id.updateProfilePassword);

        mPasswordLayout = v.findViewById(R.id.changePasswordConstraint);
    }

    private void loadData(User user) {
        mName.setText(user.getName());
        mLastName.setText(user.getLastName());
        mPhone.setText(user.getPhone());
    }

    private boolean newPasswordValidations(View v) {
        String password = getEditextValue(editPassword);
        String confirmPassword = getEditextValue(editConfirmPassword);
        String oldPassword = getEditextValue(editOldPassword);

        TextInputLayout oldInput = v.findViewById(R.id.oldPassword_text_input_layout);
        TextInputLayout newInput = v.findViewById(R.id.newPassword_text_input_layout);
        TextInputLayout confirmInput = v.findViewById(R.id.newConfirmPassword_text_input_layout);
        oldInput.setErrorEnabled(false);
        newInput.setErrorEnabled(false);
        confirmInput.setErrorEnabled(false);


        if (!password.equals(confirmPassword)) {
            newInput.setError("Las contraseñas no coinciden");
            confirmInput.setError("Las contraseñas no coinciden");
            return false;
        }

        if (!oldPassword.equals(mUser.getPassword())) {
            oldInput.setError("Su antigua contraseña no coincide");
            return false;
        }
        mUser.setPassword(password);
        return true;
    }

    private String getEditextValue(EditText editText) {
        return editText.getText().toString().trim();
    }

}