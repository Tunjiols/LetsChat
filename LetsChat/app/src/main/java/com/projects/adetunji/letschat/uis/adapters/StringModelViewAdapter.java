package com.projects.adetunji.letschat.uis.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.StringModel;

import java.util.List;

/**
 * Created by adetunji on 19/03/2018.
 */

public class StringModelViewAdapter extends ArrayAdapter<StringModel> {
    private List<StringModel> mStringObjArray;
    protected Context context;

    public StringModelViewAdapter(Context context, List<StringModel> mStringObjArray) {
        super(context, R.layout.list_item_formater, mStringObjArray);
        this.mStringObjArray = mStringObjArray;
        this.context = context;
    }

    private class ViewHolder {
        TextView text;

        public ViewHolder(final View view) {
            text = (TextView) view.findViewById(R.id.text1);
        }
    }


    @Override
    public View getView(int position, View view, @Nullable ViewGroup parent) {

        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (view == null){
            //LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_formater, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        //view = inflater.inflate(R.layout.contacts_layout, parent, false);
        //viewHolder = new ViewHolder(view);

        // Populate the data
        viewHolder.text.setText(mStringObjArray.get(position).getText());

        return view;
    }


}

