package com.example.easystock.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easystock.R;
import com.example.easystock.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<User> userList;
    private NotificableDelClickRecycler listener;
    private final int SHOW_MENU = 1;
    private final int HIDE_MENU = 2;

    public UserAdapter(Context context, NotificableDelClickRecycler notificableDelClickRecycler) {
        this.mContext = context;
        this.listener = notificableDelClickRecycler;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (userList.get(position).isShowMenu()) {
            return SHOW_MENU;
        } else {
            return HIDE_MENU;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View user_item = layoutInflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolderUsers(user_item);*/
        View v;
        if (viewType == SHOW_MENU) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu, parent, false);
            return new UserMenuViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list, parent, false);
            return new ViewHolderUsers(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userList.get(position);

        if (holder instanceof ViewHolderUsers) {
            ViewHolderUsers viewHolderUsers = (ViewHolderUsers) holder;
            viewHolderUsers.loadUser(user);

            /*((MyViewHolder)holder).title.setText(entity.getTitle());
            ((MyViewHolder)holder).imageView.setImageDrawable(context.getResources().getDrawable(entity.getImage()));

            ((MyViewHolder)holder).container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(position);
                    return true;
                }
            });*/
        }
        if (holder instanceof UserMenuViewHolder) {
            UserMenuViewHolder userMenuViewHolder = (UserMenuViewHolder) holder;
            userMenuViewHolder.loadUserMenu(user);
        }
    }

    @Override
    public int getItemCount() {
        return (userList == null) ? 0 : userList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void showMenu(int position) {
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setShowMenu(false);
        }
        userList.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }

    public boolean isMenuShown() {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).isShowMenu()) {
                return true;
            }
        }
        return false;
    }

    public void closeMenu() {
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder {


        private TextView textViewUsername;
        private TextView textViewName;
        private TextView textViewRole;
        private Button buttonUpdate;
        private Button buttonDelete;


        public ViewHolderUsers(View itemView) {
            super(itemView);
            /*textViewUsername = itemView.findViewById(R.id.item_user_username);
            textViewName = itemView.findViewById(R.id.item_user_name);
            textViewRole = itemView.findViewById(R.id.item_user_rol);*/
            /*buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);*/
            textViewUsername = itemView.findViewById(R.id.title);
            textViewRole = itemView.findViewById(R.id.subtitle);

            //itemView.setOnClickListener(view -> listener.notificaClick(userList.get(getAdapterPosition())));
            /*buttonUpdate.setOnClickListener(view -> listener.notificaUpdate(userList.get(getBindingAdapterPosition())));
            buttonDelete.setOnClickListener(view -> listener.notificaDelete(userList.get(getBindingAdapterPosition())));*/
        }


        private void loadUser(User user) {
            textViewUsername.setText(user.getName());
            //textViewName.setText(user.getLastName());
            textViewRole.setText(TextUtils.equals(user.getRole(), "9") ? "Administrador" : "Vendedor");
        }
    }

    public class UserMenuViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mShare, mEdit, mDelete;
        private TextView mUserName;

        public UserMenuViewHolder(View view) {
            super(view);
            mShare = view.findViewById(R.id.linearLayout);
            mEdit = view.findViewById(R.id.linearLayout4);
            mDelete = view.findViewById(R.id.linearLayout2);
            mUserName = view.findViewById(R.id.swipeUserOptions);

            mEdit.setOnClickListener(v -> listener.notificaUpdate(userList.get(getBindingAdapterPosition())));
            mDelete.setOnClickListener(v -> listener.notificaDelete(userList.get(getBindingAdapterPosition())));
        }

        private void loadUserMenu(User user) {
            mUserName.setText(user.getName());
        }
    }

    public interface NotificableDelClickRecycler {
        void notificaUpdate(User user);

        void notificaDelete(User user);
    }

}
