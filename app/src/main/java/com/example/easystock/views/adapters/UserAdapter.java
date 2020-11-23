package com.example.easystock.views.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<User> userList;
    private NotificableDelClickRecycler listener;

    public UserAdapter(Context context, NotificableDelClickRecycler notificableDelClickRecycler) {
        this.mContext = context;
        this.listener = notificableDelClickRecycler;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_user = layoutInflater.inflate(R.layout.item_user, parent, false);

        return new ViewHolderUsers(item_user);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userList.get(position);
        ViewHolderUsers viewHolderRecetas = (ViewHolderUsers) holder;
        viewHolderRecetas.loadUser(user);
    }

    @Override
    public int getItemCount() {
        return (userList == null) ? 0 : userList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    private class ViewHolderUsers extends RecyclerView.ViewHolder {


        private TextView textViewUsername;
        private TextView textViewName;
        private TextView textViewRole;
        private Button buttonUpdate;
        private Button buttonDelete;


        public ViewHolderUsers(View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.item_user_username);
            textViewName = itemView.findViewById(R.id.item_user_name);
            textViewRole = itemView.findViewById(R.id.item_user_rol);
             buttonUpdate= itemView.findViewById(R.id.buttonUpdate);
             buttonDelete= itemView.findViewById(R.id.buttonDelete);

            //itemView.setOnClickListener(view -> listener.notificaClick(userList.get(getAdapterPosition())));
            buttonUpdate.setOnClickListener(view -> listener.notificaUpdate(userList.get(getBindingAdapterPosition())));
            buttonDelete.setOnClickListener(view -> listener.notificaDelete(userList.get(getBindingAdapterPosition())));


        }

        private void loadUser(User user) {
            textViewUsername.setText(user.getName());
            textViewName.setText(user.getLastName());
            textViewRole.setText(TextUtils.equals(user.getRole(), "9") ? "Administrador" : "Vendedor");
        }

    }

    public interface NotificableDelClickRecycler {
        void notificaUpdate(User user);
        void notificaDelete(User user);
    }

}
