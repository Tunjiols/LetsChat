package com.projects.adetunji.letschat.uis.fragments;


import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.adetunji.letschat.LetsChatMainApp;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.uis.activities.StatusSettingsActivity;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

/**
 * Created by adetunji on 01/02/2018.
 */

public class NavProfilePictureFragment extends Fragment {

    private ImageView   mProfilePhoto;
    private ImageView 	mFloatingActionBtn;
    private ImageView 	mEditUsernameBtn;
    private ImageView 	mEditStatusBtn;
    private EditText    mProfileName;
    private TextView    mEmail;
    private TextView 	mStatus;
    private TextView 	mUsernameCover;
    private static View mFragmentView;
    private boolean toggle = true;

    //public NavProfilePictureFragment(){}


    public static NavProfilePictureFragment newInstance() {
        Bundle args = new Bundle();
        NavProfilePictureFragment fragment = new NavProfilePictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.nav_profile_picture_fragment, container, false);
        bindViews(mFragmentView);

        getActivity().setTitle("Profile Status");

        return mFragmentView;
    }

    private void bindViews(View fragmentView){
        mProfilePhoto 		= (ImageView) fragmentView.findViewById(R.id.profile_picture_circleView);
        mFloatingActionBtn 	= (ImageView) fragmentView.findViewById(R.id.profile_camera_select);
        mEditUsernameBtn	= (ImageView) fragmentView.findViewById(R.id.edit_username);
        mEditStatusBtn		= (ImageView) fragmentView.findViewById(R.id.edit_status);
        mProfileName 		= (EditText)  fragmentView.findViewById(R.id.profile_username);
        mEmail				= (TextView)  fragmentView.findViewById(R.id.profile_email);
        mStatus				= (TextView)  fragmentView.findViewById(R.id.profile_status);
        mUsernameCover		= (TextView)  fragmentView.findViewById(R.id.text_username);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){

        mFloatingActionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                PhotoOptSelectDialogFragment mPhotoSelectOpt= new PhotoOptSelectDialogFragment();
                mPhotoSelectOpt.show(getFragmentManager(), "");
                mPhotoSelectOpt.setPhotoInto(mProfilePhoto);
                /*
                mPhotoSelectOpt.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };*/
            }
        });

        mProfileName.setVisibility(View.INVISIBLE);
        mUsernameCover.setVisibility(View.VISIBLE);

        final SharedPrefUtil sharedPref = new SharedPrefUtil(getContext());
        mUsernameCover.setText(sharedPref.getString(Helper.PREF_USERNAME));

        mEmail.setText(sharedPref.getString(Helper.PREF_FULL_NAME));

        mEditUsernameBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (toggle){
                    mProfileName.setVisibility(View.VISIBLE);
                    mUsernameCover.setVisibility(View.INVISIBLE);
                    mProfileName.setCursorVisible(true);

                    mProfileName.setMaxLines(1);
                    String mUserName = mProfileName.getText().toString().trim();

                    if(!mUserName.isEmpty()){
                        sharedPref.saveString(Helper.PREF_USERNAME, mUserName);
                        LetsChatMainApp.mUserName = mUserName;
                    }

                }else {
                    mProfileName.setVisibility(View.INVISIBLE);
                    mUsernameCover.setVisibility(View.VISIBLE);

                }

                toggle = !toggle;
            }
        });

        mStatus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                StatusSettingsActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        mEditStatusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                StatusSettingsActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    public static View getViews(){
        return mFragmentView;
    }
}
