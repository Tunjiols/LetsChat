package com.projects.adetunji.letschat.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.users.add.AddUserContract;
import com.projects.adetunji.letschat.models.ContactsModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by adetunji on 16/03/2018.
 */

public class ArrayViewAdapter extends ArrayAdapter<ContactsModel> {

    private List<ContactsModel> contacts;
    protected Context context;

    public ArrayViewAdapter(Context context, List<ContactsModel> contacts) {
        super(context, R.layout.contacts_layout, contacts);
        this.contacts       = contacts;
        this.context        = context;
    }

    private static class ViewHolder {
        CircleImageView photo;
        TextView name, invite,  phone_number;
        ImageView call;

        public ViewHolder(final View view){

            photo        = (CircleImageView) view.findViewById(R.id.photo);
            name         = (TextView) view.findViewById(R.id.contact_name);
            phone_number = (TextView) view.findViewById(R.id.contact_number);
            invite       = (TextView) view.findViewById(R.id.invite);
            call         = (ImageView)view.findViewById(R.id.call);

        }
    }


    @Override
    public View getView(int position, View view, @Nullable ViewGroup parent) {
        ViewHolder viewHolder;

        RecyclerView.LayoutManager layoutManager = new RecyclerView.LayoutManager() {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return null;
            }
        };
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(layoutManager);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (view == null){

            view = inflater.inflate(R.layout.contacts_layout, recyclerView, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(contacts.get(position).name);
        viewHolder.phone_number.setText(contacts.get(position).mobileNumber);
        //viewHolder.photo.setImageBitmap(contacts.get(position).photo);

        // Return the completed view to render on screen
        return view;
    }
}
