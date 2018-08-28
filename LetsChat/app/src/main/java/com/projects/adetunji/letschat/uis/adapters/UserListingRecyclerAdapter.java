package com.projects.adetunji.letschat.uis.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.UserEntity;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by adetunji on 12/01/2018.
 */

public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder>{

    private List<UserEntity> mUsers;

    public UserListingRecyclerAdapter(List<UserEntity> users) {
        this.mUsers = users;
    }

    public void add(UserEntity user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserEntity user = mUsers.get(position);


        if (user.Email != null){

            String alphabet = user.Email.substring(0, 1);

            holder.username.setText(user.Username);
            //holder.txtUserAlphabet.setImageDrawable();
            holder.txtEmail.setText(user.Email);
            holder.lastSeenDate.setText(DateFormat.format("dd, MMM (HH:mm)", user.lastSeenDate));
        }
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public UserEntity getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username, txtEmail, lastSeenDate;
        private CircleImageView txtUserAlphabet;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (CircleImageView) itemView.findViewById(R.id.pic_circleView);
            username        = (TextView) itemView.findViewById(R.id.text_view_username);//NOTE:txtUsername is the same with userName
            txtEmail        = (TextView) itemView.findViewById(R.id.text_view_email);
            lastSeenDate    = (TextView) itemView.findViewById(R.id.last_seen_date);
        }
    }

}
