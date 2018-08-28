package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.projects.adetunji.letschat.R;

import com.projects.adetunji.letschat.uis.fragments.RegisterFragment;

/**
 * Created by adetunji on 21/01/2018.
 */

public class RegisterActivity extends AppCompatActivity{

    private Toolbar mToolber;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
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
        mToolber.setTitle("Sign up");
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolber);

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_profile, RegisterFragment.newInstance(),
                RegisterFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
