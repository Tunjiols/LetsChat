package com.projects.adetunji.letschat.uis.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 08/02/2018.
 */

public class FavouriteFragment01 extends Fragment {

    TextView textView;

    public static FavouriteFragment01 newInstance() {
        Bundle args = new Bundle();
        FavouriteFragment01 fragment = new FavouriteFragment01();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mFragmentView = inflater.inflate(R.layout.fragment_favourite_01, container, false);
        bindViews(mFragmentView);

        getActivity().setTitle("Favourites");

        return mFragmentView;
    }

    private void bindViews(View fragmentView){
        textView = (TextView)fragmentView.findViewById(R.id.no_favourite_added);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
