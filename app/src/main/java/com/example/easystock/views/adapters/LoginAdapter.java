package com.example.easystock.views.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.easystock.R;
import com.example.easystock.models.User;

import java.util.List;


public class LoginAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> mTypeList;
    private User mUsuario;
    private LayoutInflater mInflater;

    public LoginAdapter(Context context, int textViewResourceId, List<User> userList) {
        super(context, textViewResourceId, userList);
        this.mContext = context;
        this.mTypeList = userList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomview(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomview(position, convertView, parent);
    }

    private View getCustomview(int position, View customView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_spinner_login, parent, false);
        mUsuario = mTypeList.get(position);
        TextView role = view.findViewById(R.id.login_spinner_role);
        TextView name = view.findViewById(R.id.login_spinner_name);
        TextView lastName = view.findViewById(R.id.login_spinner_lastname);

        String rol;
        if (TextUtils.equals(mUsuario.getRole(), "9"))
            rol = "Administrador";
        else rol = "Vendedor";

        role.setText(rol);
        name.setText(mUsuario.getName());
        lastName.setText(mUsuario.getLastName());
        return view;
    }
}