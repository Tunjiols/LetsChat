package com.projects.adetunji.letschat.uis.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;

import com.projects.adetunji.letschat.models.UserData;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;

import java.util.List;

/**
 * Created by adetunji on 22/02/2018.
 */

public class FriendDetailsActivity extends AppCompatActivity {

    private static final String TAG = FriendDetailsActivity.class.getSimpleName();
    private TextView mShare;
    private LinearLayout mLinearLayoutPicture;
    private TextView mUsername;
    private  TextView mLast_seen;
    private RadioButton mMuteBtn;
    private TextView mStatus;
    private TextView mLastSeen2;
    private TextView mEmail;
    private ImageView mEmailBtn;
    private ImageView mChatBtn;
    private ImageView mPhoneCallBtn;
    private TextView mMobile_number;
    private TextView mAdd_Favourite;
    private TextView mdeleteChat;
    private TextView mFullname;

    private static String musername;
    private static String memail;
    private static String mstatus;
    private static String mmobile_number;
    private static long mlast_seen;
    private static String muid;
    private static String mtoken;
    private static String mfullname;
    private UserData data;

    static Context mcontext;

    public static void startActivity(Context context, int flags, String username,
                                     String email, String status, String mobile_number, long last_seen,
                                     String uid,String token , String fullname) {
        Intent intent = new Intent(context, FriendDetailsActivity.class);
        intent.setFlags(flags);
        mcontext = context;
        context.startActivity(intent);

        musername       = username;
        memail          = email;
        mstatus         = status;
        mmobile_number  = mobile_number;
        mlast_seen      = last_seen;
        muid			= uid;
        mtoken			= token;
        mfullname       = fullname;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.friend_details_activity);
        bindView();
        init();
    }

    private void bindView(){
        mShare                  = (TextView) findViewById(R.id.share);
        mLinearLayoutPicture    = (LinearLayout) findViewById(R.id.profile_header);
        mUsername               = (TextView) findViewById(R.id.usename);
        mLast_seen              = (TextView) findViewById(R.id.last_seen);
        mMuteBtn                = (RadioButton) findViewById(R.id.muteBtn);
        mStatus                 = (TextView)findViewById(R.id.status);
        mLastSeen2              = (TextView)findViewById(R.id.lastseen2);
        mEmail                  = (TextView)findViewById(R.id.email);
        mEmailBtn               = (ImageView)findViewById(R.id.emailBtn);
        mChatBtn                = (ImageView)findViewById(R.id.chatBtn);
        mMobile_number          = (TextView)findViewById(R.id.mobile);
        mAdd_Favourite       	= (TextView)findViewById(R.id.favourite);
        mdeleteChat             = (TextView)findViewById(R.id.delete_chat);
        mPhoneCallBtn			= (ImageView)findViewById(R.id.mobileBtn);
        mFullname               = (TextView)findViewById(R.id.fullname);
    }

    private void init(){

        mPhoneCallBtn.setVisibility(View.GONE);

        mLinearLayoutPicture.setBackgroundResource(R.drawable.profile_picture2);
        mUsername.setText(musername);
        mLast_seen.setText("Last seen: "+ DateFormat.format("dd, MMM (HH:mm)", mlast_seen));
        mStatus.setText(mstatus);
        mLastSeen2.setText(DateFormat.format("dd, MMM (HH:mm)", mlast_seen));
        mEmail.setText(memail);
        mFullname.setText(mfullname);

        if(mmobile_number != null){
            mMobile_number.setText(mmobile_number);
            mPhoneCallBtn.setVisibility(View.VISIBLE);
        }

        data = new UserData(memail,musername,muid,mtoken);

        mPhoneCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mMuteBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        mEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.startActivity(getBaseContext(),
                        memail,
                        muid,
                        musername,
                        mtoken);
            }
        });

        mShare.setOnClickListener(new View.OnClickListener() {
            final String shareSubject = "Here is "+musername+ "'s contact";
            final String shareBody	= "Email: "+memail;

            @Override
            public void onClick(View v) {
                shareIntent(shareSubject, shareBody);
            }
        });


        mAdd_Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = firebaseUser.getUid();
                SaveAsJsonFile saveAsJSONfile = new SaveAsJsonFile(getBaseContext());
                try{
                    saveAsJSONfile.saveObjectToFile(data, "favouritelist_"+uid);
                    Helper.displayToastMessage(getBaseContext(),"User added to favourite");
                }catch(
                        Exception e){ Log.e(TAG, e.toString());
                }

            }
        });

        mdeleteChat.setTextColor(getResources().getColor(R.color.red_800));
        mdeleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FriendDetailsActivity.this);
                dialog.setTitle("Delete Chat History").setMessage("Doing this will delete all the chat history and you will not be able to retrieve it.")
                        .setIcon(getResources().getDrawable(R.drawable.delete_red_icon))
                        .setPositiveButton("Delete anyway", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // dismiss the dialog box
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        });

    }

    private void shareIntent(String shareSubject, String shareBody){
        String[] extrasEmail = {"user5@test.com"};//for testing

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, extrasEmail);
        //shareIntent.putExtra(android.content.Intent.EXTRA_CC, email );

        PackageManager packageManager = FriendDetailsActivity.this. getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
        //PackageManager.resolveContentProvider();
        //getActivityIcon(Intent intent)
        //getApplicationIcon(ApplicationInfo info)
        //List<PackageInfo> list = getInstalledPackages(PackageManager.MATCH_DEFAULT_ONLY); Return a List of all packages that are installed on the device.
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_via)));
        }
    }
}
