package com.example.easystock.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.FragmentNewUserBinding;
import com.example.easystock.models.User;

public class CrudUserFragment extends Fragment {

    public static final String USER = "USER";
    public static final String OPERATION = "OPERATION";

    private UserViewModel userViewModel;
    private User mUser;

    private FragmentNewUserBinding mBinding;


    public static CrudUserFragment newInstance() {
        return new CrudUserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewUserBinding.inflate(inflater);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mBinding.setIsUpdated(true);
            mBinding.setUser((User) bundle.getSerializable(USER));
            mBinding.setIsAdmin(((User) bundle.getSerializable(USER)).getRole().equals("9"));
            if (mBinding.getUser().getId() == 1) {
                mBinding.userRoleSwitch.setClickable(false);
            }
        } else {
            mBinding.setIsUpdated(false);
        }


        mBinding.btnCancelUser.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
        });

        mBinding.btnAceptUser.setOnClickListener(v -> {
            if (validation()) {
                if (mBinding.getIsUpdated()) {
                    mUser.setName(getValue(mBinding.nameEdit));
                    mUser.setLastName(getValue(mBinding.userLastName));
                    mUser.setPassword(getValue(mBinding.userPassword));
                    mUser.setPhone(getValue(mBinding.userPhone));
                    mUser.setRole(mBinding.userRoleSwitch.isChecked() ? "9" : "1");
                    userViewModel.updateUser(mUser);
                } else {
                    mUser = new User(getValue(mBinding.userNameEdit), getValue(mBinding.nameEdit), getValue(mBinding.userLastName), getValue(mBinding.userPassword), getValue(mBinding.userEmail),
                            getValue(mBinding.userPhone), mBinding.userRoleSwitch.isChecked() ? "9" : "1");
                    userViewModel.insertUser(mUser);
                }
            }
        });
        return mBinding.getRoot();
    }

    private boolean validation() {

        String username = getValue(mBinding.userNameEdit);
        String password = getValue(mBinding.userPassword);
        String passwordConfirm = getValue(mBinding.userRepeatPassword);
        String email = getValue(mBinding.userEmail);

        boolean validados = true;

        if (username.isEmpty()) {
            mBinding.userNameEdit.setError("Campo obligatorio");
            validados = false;
        }

        if (password.isEmpty()) {
            mBinding.userPassword.setError("Campo obligatorio");
            validados = false;
        }

        if (!password.equals(passwordConfirm)) {
            mBinding.userRepeatPassword.setError("La contraseña no coincide");
            validados = false;
        }

        if (email.isEmpty()) {
            mBinding.userEmail.setError("Campo obligatorio");
            validados = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.userEmail.setError("Formato de correo incorrecto");
            validados = false;
        }

        return validados;
    }

    private String removeBlankSpaces(String string) {
        return string.replaceAll("\\s+", "");
    }

    private String removeAllNoNumericCharacters(String string) {
        return string.replaceAll("[^\\d]", "");
    }

    private String getValue(EditText editText) {
        return editText.getText().toString().trim();
    }


}