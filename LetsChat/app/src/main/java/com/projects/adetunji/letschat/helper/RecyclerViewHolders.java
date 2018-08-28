package com.projects.adetunji.letschat.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 21/01/2018.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder{

    private static final String TAG = RecyclerViewHolders.class.getSimpleName();

    public TextView profileHeader;
    public TextView profileContent;
    public ImageView imageButton;

    public RecyclerViewHolders(final View itemView) {
        super(itemView);

        profileHeader = (TextView)itemView.findViewById(R.id.heading);
        profileContent = (TextView) itemView.findViewById(R.id.profile_content);
        imageButton		= (ImageView) itemView.findViewById(R.id.edit_button);
    }
}
