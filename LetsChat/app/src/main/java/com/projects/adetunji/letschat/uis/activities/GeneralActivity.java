package com.projects.adetunji.letschat.uis.activities;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.models.UserData;
import com.projects.adetunji.letschat.uis.fragments.AddFriendFragment;
import com.projects.adetunji.letschat.uis.fragments.FavouriteFragment;
import com.projects.adetunji.letschat.uis.fragments.FavouriteFragment01;
import com.projects.adetunji.letschat.uis.fragments.NavProfilePictureFragment;
import com.projects.adetunji.letschat.uis.fragments.NewGroupFragment;
import com.projects.adetunji.letschat.uis.fragments.SettingsFragment;
import com.projects.adetunji.letschat.uis.fragments.ShareLetschatFragment;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;

import java.util.List;

/**
 * Created by adetunji on 24/01/2018.
 */

public class GeneralActivity extends AppCompatActivity{

    private  static final String TAG= GeneralActivity.class.getSimpleName();
    private Toolbar             mToolbar;
    private static int                 FRAGMENT_FLAG;
    private FragmentTransaction fragmentTransaction;

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, GeneralActivity.class);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int flags, int FRAGMENT_FLAGs) {
        Intent intent = new Intent(context, GeneralActivity.class);
        intent.setFlags(flags);
        FRAGMENT_FLAG = FRAGMENT_FLAGs;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity);
        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //mToolbar.setTitle("Sign In");
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar action_bar = getSupportActionBar();

        // Enable the Up button
        action_bar.setDisplayHomeAsUpEnabled(true);


        // set the login screen fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        /*Switch between the fragments based on the flag set*/
        fragmentSwitch(FRAGMENT_FLAG);

        fragmentTransaction.commit();
    }

    private void fragmentSwitch(int FRAGMENT_FLAG){

        switch(FRAGMENT_FLAG){
            case Helper.FRAGMENT_FLAG_PROFILE:
                fragmentTransaction.replace(R.id.frame_layout_content_login , NavProfilePictureFragment.newInstance(),
                        NavProfilePictureFragment.class.getSimpleName());

                break;

            case Helper.FRAGMENT_FLAG_FAVOURITE:
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = firebaseUser.getUid();
                SaveAsJsonFile saveAsJsonFile = new SaveAsJsonFile(getBaseContext());
                List<UserData> favouriteList = null;

                try{
                    favouriteList = saveAsJsonFile.objectFromFile("favouritelist_"+uid);
                }catch(Exception e){
                    Log.e(TAG, e.toString());
                }

                if(favouriteList != null){
                    fragmentTransaction.replace(R.id.frame_layout_content_login, FavouriteFragment.newInstance(favouriteList),
                            FavouriteFragment.class.getSimpleName());
                }else{
                    fragmentTransaction.replace(R.id.frame_layout_content_login, FavouriteFragment01.newInstance(),
                            FavouriteFragment01.class.getSimpleName());
                }


                break;

            case Helper.FRAGMENT_FLAG_NEWGROUP:
                fragmentTransaction.replace(R.id.frame_layout_content_login, NewGroupFragment.newInstance(),
                        NewGroupFragment.class.getSimpleName());
                break;

            case Helper.FRAGMENT_FLAG_ADDFRIEND:
                fragmentTransaction.replace(R.id.frame_layout_content_login, AddFriendFragment.newInstance(),
                        AddFriendFragment.class.getSimpleName());

                break;

            case Helper.FRAGMENT_SHARE_LETSCHAT:
               fragmentTransaction.replace(R.id.frame_layout_content_login, ShareLetschatFragment.newInstance(),
                    ShareLetschatFragment.class.getSimpleName());

                break;
            case Helper.FRAGMENT_FLAG_SETTINGS:
                fragmentTransaction.replace(R.id.frame_layout_content_login, SettingsFragment.newInstance(),
                        SettingsFragment.class.getSimpleName());

                break;
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
