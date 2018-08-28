package com.projects.adetunji.letschat;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adetunji on 05/01/2018.
 */

public class LetsChatMainApp extends Application{

    private static boolean IsChatActivityOpen = false;

    /*saved into the sharedPref*/
    public  static  String 	status_text;
    public  static  String 	mUserName;
    public  static  String 	mEmail;
    public static   Uri     mImageUri;

    public static Bitmap    imageBitmap;
    public FirebaseAuth 	firebaseAuth;


    private static   Map<String, String> userInformation;

    public static boolean isChatActivityOpen() {
        return IsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        LetsChatMainApp.IsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getFirebaseUserAuthenticateId() {
        String userId = null;
        if(firebaseAuth.getCurrentUser() != null){
            userId = firebaseAuth.getCurrentUser().getUid();
        }
        return userId;
    }


    public static void getUserDataFromDatabase(final String uid, final Context context){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        userInformation = new HashMap<>();

        databaseReference.child(Constants.ARG_USERS).child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                userInformation.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                userInformation.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public static String getHelperValue(String HelperDataToGet){

        if(HelperDataToGet.equals(Helper.Full_Name)){
            if (!userInformation.get(Helper.Full_Name).isEmpty()) {return userInformation.get(Helper.Full_Name);}
            else { return "";}
        }
        else if(HelperDataToGet .equals( Helper.Display_NAME)){
            if (userInformation.get(Helper.Display_NAME) != "") {return userInformation.get(Helper.Display_NAME);}
            else { return "";}
        }
        else if(HelperDataToGet.equals( Helper.E_Mail)){
            if (userInformation.get(Helper.E_Mail) != "") {return userInformation.get(Helper.E_Mail);}
            else { return "";}
        }
        else if(HelperDataToGet .equals( Helper.Birth_DAY)){
            if (!userInformation.get(Helper.Birth_DAY).isEmpty()) {return userInformation.get(Helper.Birth_DAY);}
            else { return "";}
        }
        else if(HelperDataToGet .equals( Helper.Mobile_NUMBER)){
            if (!userInformation.get(Helper.Mobile_NUMBER).isEmpty()) {return userInformation.get(Helper.Mobile_NUMBER);}
            else { return "";}
        }
        else if(HelperDataToGet .equals( Helper.Hobby_INTEREST)){
            if (!userInformation.get(Helper.Hobby_INTEREST).isEmpty()) {return userInformation.get(Helper.Hobby_INTEREST);}
            else { return "";}
        }

        else return null;
    }

}
