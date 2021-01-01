package com.example.easystock.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.FragmentUserProfileBinding;
import com.example.easystock.models.User;

public class UserProfileFragment extends Fragment {

    public static final String USER = "USER";
    private UserViewModel mUserViewModel;
    private FragmentUserProfileBinding mBinding;

    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            //mUser = (User) getArguments().getSerializable(USER);
            mBinding.setUser((User) getArguments().getSerializable(USER));
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserProfileBinding.inflate(inflater);
        //View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (getArguments() != null) {
            //mUser = (User) getArguments().getSerializable(USER);
            mBinding.setUser((User) getArguments().getSerializable(USER));
        }

        mBinding.btnVisibilityUpdatePassword.setOnClickListener(v -> mBinding.setIsUpdatePassword(true));


        mBinding.btnUpdateProfilePassword.setOnClickListener(v -> {
            if (newPasswordValidations()) {
                mUserViewModel.updateUser(mBinding.getUser());
                mBinding.newPassword.setText("");
                mBinding.oldPassword.setText("");
                mBinding.newConfirmPassword.setText("");
                mBinding.setIsUpdatePassword(false);
                //mPasswordLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Contraseña actualizada con exito", Toast.LENGTH_SHORT).show();
            }
        });
        return mBinding.getRoot();
    }


    private boolean newPasswordValidations() {
        mBinding.oldPasswordTextInputLayout.setErrorEnabled(false);
        mBinding.newPasswordTextInputLayout.setErrorEnabled(false);
        mBinding.newConfirmPasswordTextInputLayout.setErrorEnabled(false);


        if (!getEditextValue(mBinding.newPassword).equals(getEditextValue(mBinding.newConfirmPassword))) {
            mBinding.newPasswordTextInputLayout.setError("Las contraseñas no coinciden");
            mBinding.newConfirmPasswordTextInputLayout.setError("Las contraseñas no coinciden");
            return false;
        }

        if (!getEditextValue(mBinding.oldPassword).equals(mBinding.getUser().getPassword())) {
            mBinding.oldPasswordTextInputLayout.setError("Su antigua contraseña no coincide");
            return false;
        }
        mBinding.getUser().setPassword(getEditextValue(mBinding.newPassword));
        return true;
    }

    private String getEditextValue(EditText editText) {
        return editText.getText().toString().trim();
    }

}