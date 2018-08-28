package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 01/02/2018.
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = AccountSettingsActivity.class.getSimpleName();
    private     Toolbar         mToolbar;
    private     TextView        mPrivacy;
    private     TextView	    mSecurity;
    private     TextView	    mTwo_steps_verification;
    private     TextView	    mDelete_account;

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, AccountSettingsActivity.class);
        intent.setFlags(flags);

        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings_activity);

        bindViews();

        init();
    }

    private void bindViews() {
        mToolbar                = (Toolbar) findViewById(R.id.toolbar);
        mPrivacy				= (TextView)findViewById(R.id.privacy);
        mSecurity				= (TextView)findViewById(R.id.security);
        mTwo_steps_verification	= (TextView)findViewById(R.id.two_steps_verification);
        mDelete_account			= (TextView)findViewById(R.id.delete_account);

    }

    private void init(){
        mToolbar.setTitle("Account");

        mPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mTwo_steps_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDelete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
