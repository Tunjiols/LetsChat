package com.projects.adetunji.letschat.uis.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.utils.Constants;

/**
 * Created by adetunji on 02/03/2018.
 */

public class InternetConnectionStatusFragment extends Fragment {

    private static final String NO_INTERNET = "Not Connected!";
    private static final String INTERNET	= "Connected!";
    private static final String CONNECTING	= "Connecting...";
    public static final String TAG			= "InternetConnectionStatusFragment";
    public static String isNoInternet				= "";
    private TextView text;


    public static InternetConnectionStatusFragment newInstance() {
        InternetConnectionStatusFragment fragment = new InternetConnectionStatusFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Need to define the child fragment layout
        View fragmentView = inflater.inflate(R.layout.internet_connection_status, container, false);
        bindView(fragmentView);
        return fragmentView;
    }

    private void bindView(View fragmentView){
        text = (TextView)fragmentView.findViewById(R.id.internet_connection_status);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isNoInternet.equals("not")){
            text.setText(NO_INTERNET);
            text.setBackgroundColor(getResources().getColor( R.color.red_800));
        }else if(isNoInternet.equals("connected")){
            text.setText(INTERNET);
            text.setBackgroundColor(getResources().getColor( R.color.green_500));
        }else if(isNoInternet.equals("connecting")){
            text.setText(CONNECTING);
            text.setBackgroundColor(getResources().getColor( R.color.orange_800));
        }
    }
}
