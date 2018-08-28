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

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.uis.activities.AboutUsSettingsActivity;
import com.projects.adetunji.letschat.uis.activities.AccountSettingsActivity;
import com.projects.adetunji.letschat.uis.activities.ChatSettingsActivity;
import com.projects.adetunji.letschat.uis.activities.HelpSettingsActivity;
import com.projects.adetunji.letschat.uis.activities.NotificationsSettingsActivity;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

/**
 * Created by adetunji on 01/02/2018.
 */

public class SettingsFragment extends Fragment{

    private ImageView   mProfilePhoto;
    private TextView    mProfileName;
    private TextView 	mAccount;
    private TextView 	mStatus;
    private TextView 	mChats;
    private TextView 	mNotifications;
    private TextView 	mHelp;
    private TextView 	mAboutUs;

    public SettingsFragment(){}

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment mFragment = new SettingsFragment();
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mFragmentView = inflater.inflate(R.layout.settings_fragment, container, false);
        bindViews(mFragmentView);

        getActivity().setTitle("Settings");

        return mFragmentView;
    }

    private void bindViews(View fragmentView){
        mProfilePhoto 		= (ImageView) fragmentView.findViewById(R.id.profile_picture_circleView);
        mProfileName 		= (TextView)  fragmentView.findViewById(R.id.profile_name);
        mAccount			= (TextView)  fragmentView.findViewById(R.id.account);
        mStatus				= (TextView)  fragmentView.findViewById(R.id.profile_status);
        mChats				= (TextView)  fragmentView.findViewById(R.id.chats);
        mNotifications		= (TextView)  fragmentView.findViewById(R.id.notifications);
        mHelp			 	= (TextView)  fragmentView.findViewById(R.id.help);
        mAboutUs			= (TextView)  fragmentView.findViewById(R.id.about_us);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        final SharedPrefUtil sharedPref = new SharedPrefUtil(getContext());

        mProfileName.setText(sharedPref.getString(Helper.PREF_USERNAME));
        mStatus.setText(sharedPref.getString(Helper.PREF_STATUS));

        mAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountSettingsActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        mChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatSettingsActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        mNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationsSettingsActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpSettingsActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        mAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutUsSettingsActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
