package com.example.easystock.views.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.easystock.R;
import com.example.easystock.controllers.viewModel.UserViewModel;
import com.example.easystock.models.User;
import com.example.easystock.utils.Constants;

public class CrudUserFragment extends Fragment {

    public static final String USER = "USER";
    public static final String OPERATION = "OPERATION";

    private UserViewModel userViewModel;
    private EditText editName, editLastName, editPassword, editConfirmPassword, editPhone;
    private SwitchCompat rolSwitch;
    private Button btnAcept, btnCancel;
    private User mUser;


    public static CrudUserFragment newInstance() {
        CrudUserFragment fragment = new CrudUserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        init(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mUser = (User) bundle.getSerializable(USER);
            btnAcept.setText(bundle.getString(OPERATION) == null ? "Agregar" : "Modificar");
            setUserData(mUser);
        } else {
            btnAcept.setText("Agregar");
        }


        btnCancel.setOnClickListener(v -> {
        });


        btnAcept.setOnClickListener(v -> {
            if (validation()) {
                if (TextUtils.equals(btnAcept.getText().toString().toUpperCase(), "AGREGAR")) {
                    mUser = new User(getEditextValue(editName), getEditextValue(editLastName), getEditextValue(editPassword),
                            getEditextValue(editPhone), rolSwitch.isChecked() ? "9" : "1");
                    userViewModel.insertUser(mUser);
                } else if (TextUtils.equals(btnAcept.getText().toString().toUpperCase(), "MODIFICAR")) {
                    mUser.setName(getEditextValue(editName));
                    mUser.setLastName(getEditextValue(editLastName));
                    mUser.setPassword(getEditextValue(editPassword));
                    mUser.setPhone(getEditextValue(editPhone));
                    mUser.setRole(rolSwitch.isChecked() ? "9" : "1");
                    userViewModel.updateUser(mUser);
                }
            }
        });
        return view;
    }

    private void init(View layout) {
        editName = layout.findViewById(R.id.userName);
        editLastName = layout.findViewById(R.id.userLastName);
        editPassword = layout.findViewById(R.id.userPassword);
        editConfirmPassword = layout.findViewById(R.id.userRepeatPassword);
        editPhone = layout.findViewById(R.id.userPhone);
        rolSwitch = layout.findViewById(R.id.userRoleSwitch);
        btnAcept = layout.findViewById(R.id.btnAceptUser);
        btnCancel = layout.findViewById(R.id.btnCancelUser);

/*        rolSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rol = isChecked ? "9" : "1";
        });*/

    }


    private void setUserData(User mUser) {
        editName.setText(mUser.getName());
        editLastName.setText(mUser.getLastName());
        editPassword.setText(mUser.getPassword());
        editConfirmPassword.setText(mUser.getPassword());
        editPhone.setText(mUser.getPhone());
        rolSwitch.setChecked(TextUtils.equals(mUser.getRole(), Constants.ADMIN_ROLE));
    }

    private boolean validation() {

        String name = getEditextValue(editName);
        String lastName = getEditextValue(editLastName);
        String password = getEditextValue(editPassword);
        String passwordConfirm = getEditextValue(editConfirmPassword);
        String phone = getEditextValue(editPhone);

        boolean validados = true;

        if (name.isEmpty()) {
            editName.setError("Campo obligatorio");
            validados = false;
        }

        if (lastName.isEmpty()) {
            editLastName.setError("Campo obligatorio");
            validados = false;
        }

        if (password.isEmpty()) {
            editPassword.setError("Campo obligatorio");
            validados = false;
        }

        if (!password.equals(passwordConfirm)) {
            editConfirmPassword.setError("La contraseña no coincide");
            validados = false;
        }

        if (phone.isEmpty()) {
            editPhone.setError("La contraseña no coincide");
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

    private String getEditextValue(EditText editText) {
        return editText.getText().toString().trim();
    }


}