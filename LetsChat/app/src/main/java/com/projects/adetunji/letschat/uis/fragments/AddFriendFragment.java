package com.projects.adetunji.letschat.uis.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;

import java.util.ArrayList;

/**
 * Created by adetunji on 10/03/2018.
 */

public class AddFriendFragment extends Fragment implements View.OnClickListener{
    private final static String TAG = AddFriendFragment.class.getSimpleName();

    private TextView mAddbyEmail;
    private TextView    mAddbyContac;
    private TextView    mAddbyUsername;
    private EditDialogFragment editDialogFragment;

    private boolean toggle = true;

    private ArrayList<String> search_list;


    public static AddFriendFragment newInstance() {
        Bundle args = new Bundle();
        AddFriendFragment mFragment = new AddFriendFragment();
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mFragmentView = inflater.inflate(R.layout.add_friend_fragment, container, false);
        bindViews(mFragmentView);

        getActivity().setTitle("Add Friend");

        return mFragmentView;
    }

    private void bindViews(View fragmentView){
        mAddbyEmail				= (TextView)  fragmentView.findViewById(R.id.add_by_email);
        mAddbyContac			= (TextView)  fragmentView.findViewById(R.id.add_by_contact);
        mAddbyUsername			= (TextView)  fragmentView.findViewById(R.id.add_by_username);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){


        search_list = new ArrayList<>();

        editDialogFragment = new EditDialogFragment();

        mAddbyEmail.setOnClickListener(this);
        mAddbyContac.setOnClickListener(this);
        mAddbyUsername.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_by_email :
                editDialogFragment.EditDialog("Add by Email", "Enter email to search");
                editDialogFragment.show(getFragmentManager(), TAG);
                break;

            case R.id.add_by_contact:
                editDialogFragment. EditDialog("Add by Contact/Phone number", "Enter mobile number to search");
                editDialogFragment.show(getFragmentManager(), TAG);
                break;
            case R.id.add_by_username:
                editDialogFragment.EditDialog("Add by Username", "Enter username to search");
                editDialogFragment.show(getFragmentManager(), TAG);
                break;
        }
    }



}
