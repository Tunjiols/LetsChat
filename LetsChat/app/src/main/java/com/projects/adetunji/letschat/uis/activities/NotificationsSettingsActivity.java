package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;


/**
 * Created by adetunji on 01/02/2018.
 */

public class NotificationsSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = NotificationsSettingsActivity.class.getSimpleName();
    private		Toolbar			mToolbar;
    private     TextView	    mSound;
    private     TextView	    mPopups;
    private     TextView	    mVibrate;
    private     TextView	    mRingtone;
    private     TextView	    mLight;
    private     CheckBox        mSound_box;
    private		CheckBox		mPopups_box;
    private		CheckBox		mVibrate_box;


    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, NotificationsSettingsActivity.class);
        intent.setFlags(flags);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_settings_activity);

        bindViews();

        init();
    }

    private void bindViews() {
        mToolbar            = (Toolbar) findViewById(R.id.toolbar);
        mSound				= (TextView)findViewById(R.id.sound);
        mPopups				= (TextView)findViewById(R.id.popups);
        mVibrate			= (TextView)findViewById(R.id.vibrate);
        mRingtone			= (TextView)findViewById(R.id.ringtone);
        mLight				= (TextView)findViewById(R.id.light);
        mSound_box			= (CheckBox)findViewById(R.id.sound_box);
        mPopups_box			= (CheckBox)findViewById(R.id.popups_box);
        mVibrate_box		= (CheckBox)findViewById(R.id.vibrate_box);
    }

    private void init(){
        mToolbar.setTitle("Notifications");

        mSound_box.setOnClickListener(this);
        mPopups_box.setOnClickListener(this);
        mVibrate_box.setOnClickListener(this);;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.sound_box:
                break;
            case R.id.popups_box:
                break;
            case R.id.vibrate_box:
                break;
        }

    }
}
