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

public class AboutUsSettingsActivity extends AppCompatActivity {

    private static final String TAG = AboutUsSettingsActivity.class.getSimpleName();
    private		Toolbar			mToolbar;
    private     TextView	    mAbout_us;


    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, AboutUsSettingsActivity.class);
        intent.setFlags(flags);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_settings_activity);

        bindViews();

        init();
    }

    private void bindViews() {
        mToolbar            = (Toolbar) findViewById(R.id.toolbar);
        mAbout_us			= (TextView)findViewById(R.id.about);
    }

    private void init(){
        mToolbar.setTitle("About us");

        mAbout_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInformationActivity.startIntent(getBaseContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });


    }
}
