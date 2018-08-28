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

public class ChatSettingsActivity extends AppCompatActivity {
    private static final String TAG = ChatSettingsActivity.class.getSimpleName();
    private		Toolbar			mToolbar;
    private     TextView	    mWall_paper;
    private     TextView	    mFont_size;
    private     TextView	    mFont_size_chose;



    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, ChatSettingsActivity.class);
        intent.setFlags(flags);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_settings_fragment);

        bindViews();

        init();
    }

    private void bindViews() {
        mToolbar				= (Toolbar) findViewById(R.id.toolbar);
        mWall_paper             = (TextView) findViewById(R.id.wall_paper);
        mFont_size				= (TextView)findViewById(R.id.font_size);
        mFont_size_chose		= (TextView)findViewById(R.id.font_size_chose);
    }

    private void init(){
        mToolbar.setTitle("Chat Settings");

        mWall_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WallpaperActivity.startIntent(getBaseContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        mFont_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFont_size_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
