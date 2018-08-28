package com.projects.adetunji.letschat.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.HelperFirebaseDatabase;

/**
 * Created by adetunji on 20/01/2018.
 */

public class EditProfileActivity extends AppCompatActivity{


        private static final String TAG = EditProfileActivity.class.getSimpleName();
        private     FirebaseAuth.AuthStateListener mAutheticationStateListener;

        private     EditText    mEditFullName;
        private     EditText    mEditProfileDisplayName;
        private     EditText    mEditProfilePhoneNumber;
        private     EditText    mEditProfileHobby;
        private     EditText    mEditProfileBirthday;
        private     Button 	    mpBtnSave;
        private     Toolbar     mtoolbar;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_activity_edit);

            bindViews();

            init();
        }

    private void bindViews() {

        mtoolbar                = (Toolbar) findViewById(R.id.toolbar);
        mpBtnSave               = (Button) findViewById(R.id.save_edit_button) ;
        mEditFullName           = (EditText)findViewById(R.id.profile_name);
        mEditProfileDisplayName = (EditText)findViewById(R.id.profile_display_name);
        mEditProfilePhoneNumber = (EditText)findViewById(R.id.profile_phone);
        mEditProfileHobby       = (EditText)findViewById(R.id.profile_hobby);
        mEditProfileBirthday    = (EditText)findViewById(R.id.profile_birth);
    }

    private void init(){
        mtoolbar.setTitle("Edit Profile");

        mpBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName             = mEditFullName.getText().toString().trim();
                String profileDisplayName   = mEditProfileDisplayName.getText().toString().trim();
                String profilePhoneNumber   = mEditProfilePhoneNumber.getText().toString().trim();
                String profileHobby         = mEditProfileHobby.getText().toString().trim();
                String profileBirthday      = mEditProfileBirthday.getText().toString().trim();

                //Get current user data
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // update the user profile information in Firebase database.
                if(!TextUtils.isEmpty(fullName)
                        || !TextUtils.isEmpty(profileDisplayName)
                        || TextUtils.isEmpty(profilePhoneNumber)
                        || TextUtils.isEmpty(profileHobby)
                        || TextUtils.isEmpty(profileBirthday)
                        ){

                    if(TextUtils.isEmpty(profilePhoneNumber)) profilePhoneNumber = null;
                    if(TextUtils.isEmpty(profileHobby)) profileHobby             = null;
                    if(TextUtils.isEmpty(profileBirthday)) profileBirthday       = null;

                    if (user == null) {
                        Intent firebaseUserIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
                        startActivity(firebaseUserIntent);
                        finish();
                    } else {
                        String userId       = user.getProviderId();
                        String id           = user.getUid();
                        String profileEmail = user.getEmail();
                        long lastSeenDate   = 0;

                        UserEntity userEntity = new UserEntity(id, profileEmail, profileDisplayName, fullName,
                                 profilePhoneNumber, profileBirthday, profileHobby, "","", "",lastSeenDate, "");

                        HelperFirebaseDatabase helperFirebaseDatabase = new HelperFirebaseDatabase();
                        helperFirebaseDatabase.addUserToDatabase(id, userEntity);


                    }

                }else {Helper.displayToastMessage(getApplicationContext(),"Full name or Profile name must not be blank");}
                finish();
            }

        });
    }
}
