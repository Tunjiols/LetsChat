package com.projects.adetunji.letschat.uis.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.UserProfile;
import com.projects.adetunji.letschat.helper.RecyclerViewHolders;
import com.projects.adetunji.letschat.uis.activities.UpdateProfileActivity;

import java.util.List;

/**
 * Created by adetunji on 20/01/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders>{

    private List<UserProfile> user;
    protected Context context;

    public RecyclerViewAdapter(Context context, List<UserProfile> user) {
        this.user       = user;
        this.context    = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_data_list, parent, false);
        viewHolder      = new RecyclerViewHolders(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        holder.profileHeader.setText(user.get(position).getHeader());
        holder.profileContent.setText(user.get(position).getProfileContent());

        if (user.get(position).getHeader().equals("Email")){
            holder.imageButton.setVisibility(View.INVISIBLE);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener(){
            final int pos = position;
            @Override
            public void onClick(View view){
                UpdateProfileActivity.startIntent(context, Intent.FLAG_ACTIVITY_NEW_TASK, user.get(pos).getHeader());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (user != null) {
            return this.user.size();
        }else return 0;
    }
}
