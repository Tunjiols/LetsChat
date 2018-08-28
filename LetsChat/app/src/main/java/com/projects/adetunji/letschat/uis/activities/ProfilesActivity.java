package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.uis.fragments.ProfileFragment;

/**
 * Created by adetunji on 05/01/2018.
 */

public class ProfilesActivity extends AppCompatActivity {
    private Toolbar mToolber;

    public static void startActivity(Context context, int flags){
        Intent intent = new Intent(context, ProfilesActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();
        init();
    }

    private void bindViews(){
        mToolber = (Toolbar) findViewById(R.id.toolbar);
        //mToolber.setTitle("Profile");
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolber);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar action_bar = getSupportActionBar();

        // Enable the Up button
        action_bar.setDisplayHomeAsUpEnabled(true);

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_profile, ProfileFragment.newInstance(),
                ProfileFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
       //getFragmentManager().beginTransaction().detach(ProfileFragment.newInstance()).attach(ProfileFragment.newInstance()).commit();
    }
}
