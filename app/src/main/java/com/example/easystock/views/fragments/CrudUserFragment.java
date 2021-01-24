package com.example.easystock.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.databinding.FragmentNewUserBinding;
import com.example.easystock.models.User;

public class CrudUserFragment extends Fragment {


    private UserViewModel userViewModel;
    private User mUser;
    private FragmentNewUserBinding mBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewUserBinding.inflate(inflater);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (getArguments() != null) {
            mBinding.setUser((User) getArguments().getSerializable("USER"));
            mUser = (User) getArguments().getSerializable("USER");
        }

        mBinding.btnAceptUser.setOnClickListener(v -> {
            if (!isAllDataValid()) {
                return;
            }

            if (mBinding.getUser() != null) {
                mUser.setUsername(getValue(mBinding.userNameEdit));
                mUser.setName(getValue(mBinding.nameEdit));
                mUser.setLastName(getValue(mBinding.userLastName));
                mUser.setPassword(getValue(mBinding.userPassword));
                mUser.setEmail(getValue(mBinding.userEmail));
                mUser.setPhone(getValue(mBinding.userPhone));
                mUser.setRole(mBinding.userRoleSwitch.isChecked() ? "9" : "1");
                userViewModel.updateUser(mUser);
            } else {
                mUser = new User(getValue(mBinding.userNameEdit), getValue(mBinding.nameEdit), getValue(mBinding.userLastName), getValue(mBinding.userPassword), getValue(mBinding.userEmail),
                        getValue(mBinding.userPhone), mBinding.userRoleSwitch.isChecked() ? "9" : "1");
                userViewModel.insertUser(mUser);
            }
            mUser = null;
            mBinding.setUser(null);
            clearAllFields();
            //navController.navigate(R.id.action_to_users_fragment);

        });
        return mBinding.getRoot();
    }

    private void clearAllFields() {
        mBinding.userNameEdit.setText("");
        mBinding.nameEdit.setText("");
        mBinding.userLastName.setText("");
        mBinding.userPassword.setText("");
        mBinding.userRepeatPassword.setText("");
        mBinding.userEmail.setText("");
        mBinding.userPhone.setText("");
        mBinding.userRoleSwitch.setChecked(false);
    }

    private boolean isAllDataValid() {

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
        if (editText.toString().isEmpty())
            return "";
        return editText.getText().toString().trim();
    }


}