package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.projects.adetunji.letschat.LetsChatMainApp;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.InterfaceChat;
import com.projects.adetunji.letschat.models.Chat;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.uis.fragments.ChatFragment;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;

import java.util.List;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by adetunji on 05/01/2018.
 */

public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ShareActionProvider mShareActionProvider;

    private String extrasEmail;
    private String shareSubject;
    private String shareBody;

    public static boolean viewChatClick = false;

    public static void startActivity(Context context,
                                     String receiver_email,
                                     String receiverUid,
                                     String friendUsername,
                                     String firebaseToken) {

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver_email);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.putExtra(Constants.ARG_RECEIVER_USERNAME, friendUsername);

        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public static void viewSet(boolean viewSet){
        viewChatClick =  viewSet;
    }


    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar action_bar = getSupportActionBar();


        action_bar.setCustomView(R.layout.title_bar);
        CircleImageView action_bar_pic 	= (CircleImageView)action_bar.getCustomView().findViewById(R.id.action_bar_pic);
        TextView action_bar_title 		= (TextView)action_bar.getCustomView().findViewById(R.id.action_bar_title);
        TextView action_bar_subtitle 	= (TextView)action_bar.getCustomView().findViewById(R.id.action_bar_subtitle);
        action_bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        action_bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // set toolbar title
        extrasEmail = getIntent().getExtras().getString(Constants.ARG_RECEIVER);//get receiver's email
        String friendUsername = getIntent().getExtras().getString(Constants.ARG_RECEIVER_USERNAME);

        if(!friendUsername.equals("")){
            action_bar_title.setText(friendUsername);
        }else {
            action_bar_title.setText(extrasEmail);
        }
        action_bar_subtitle.setText("Online");


		/*
        //set profile picture onto ActionBar
        action_bar.setDisplayOptions(action_bar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView profile_ActionBar = new ImageView(action_bar.getThemedContext());
        profile_ActionBar.setScaleType(ImageView.ScaleType.CENTER);
        profile_ActionBar.setImageResource(R.drawable.profile_picture);
        //profile_ActionBar.setImageBitmap();
        ActionBar.LayoutParams  layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        profile_ActionBar.setLayoutParams(layoutParams);
        //action_bar.setCustomView(profile_ActionBar);
		*/

        //set details of information to be shared
        shareSubject = " Sharing info";

        // Enable the Up button
        action_bar.setDisplayHomeAsUpEnabled(true);

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                ChatFragment.newInstance(
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER),
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER_UID),
                        getIntent().getExtras().getString(Constants.ARG_FIREBASE_TOKEN)),
                ChatFragment.class.getSimpleName());

        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LetsChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LetsChatMainApp.setChatActivityOpen(false);
    }


    /**
     * Intent for sharing data with other apps
     */
    private Intent shareIntent(String shareSubject, String shareBody){
        String[] extrasEmail = {"user5@test.com"};//for testing

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //shareIntent.setType("image/jpeg");

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, extrasEmail);
        //shareIntent.putExtra(android.content.Intent.EXTRA_CC, email );


        return shareIntent;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            //Listener for when share item selected
            mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
                @Override
                public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                    return false;
                }
            });

            // if (IMAGE.equals(intent.getMimeType())) {
            //     mShareActionProvider.setHistoryFileName(SHARE_IMAGE_HISTORY_FILE_NAME);
            // } else if (TEXT.equals(intent.getMimeType())) {
            //      mShareActionProvider.setHistoryFileName(SHARE_TEXT_HISTORY_FILE_NAME);
            //  }
            //mShareActionProvider.setShareIntent(shareIntent);
            startActivity(shareIntent);
            invalidateOptionsMenu();
        }
    }

    //--------------Menu------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_activity, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareHistoryFileName("custom_share_history.xml");
        // Return true to display menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_contact:
                //FriendDetailsActivity.startActivity(getBaseContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
                break;

            case R.id.action_add_to_favourite:
                SaveAsJsonFile saveAsJsonFile = new SaveAsJsonFile(getBaseContext());
                List<UserEntity> userEntity = saveAsJsonFile.loadUsersEntity(Constants.ARG_RECEIVER_UID);
                saveAsJsonFile.saveUsersEntity(userEntity, "favourite_"+Constants.ARG_RECEIVER_UID);

                Helper.displayToastMessage(getBaseContext(), "User added to favourite list");

                break;

            case R.id.action_share:
                setShareIntent(shareIntent(shareSubject, shareBody));
                break;
            case R.id.action_forward:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem forward = menu.findItem(R.id.action_forward);
        MenuItem share = menu.findItem(R.id.action_share);

        if (viewChatClick){
            forward.setVisible(true);
            share.setVisible(true);
        }else{
            forward.setVisible(false);
            share.setVisible(false);
        }

        return true;
    }

}
