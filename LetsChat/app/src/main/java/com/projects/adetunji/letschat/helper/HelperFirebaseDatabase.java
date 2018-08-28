package com.projects.adetunji.letschat.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.models.UserProfile;
import com.projects.adetunji.letschat.uis.adapters.RecyclerViewAdapter;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adetunji on 20/01/2018.
 */

public class HelperFirebaseDatabase {

    private static final String TAG = HelperFirebaseDatabase.class.getSimpleName();

    private DatabaseReference databaseReference;


    public HelperFirebaseDatabase(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addUserToDatabase(String userId, UserEntity userEntity){

        /*//this will add nickname to the user children list--
		Map<String, Object> userUpdates = new HashMap<>();
		userUpdates.put("userId/nickname", "Alan The Machine");

		databaseReference.child(Constants.ARG_USERS).updateChildrenAsync(userUpdates);
		*/

        databaseReference.child(Constants.ARG_USERS).child(userId).setValue(userEntity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Helper.displayToastMessage(getActivity(), "Successfully added");
                        } else {
                            //Helper.displayToastMessage(getActivity(), "Not successfully added");
                        }
                    }
                });
    }

    private List<UserProfile> userData;
    private static Map<String , Object> mapData;

    public void getUserDataFromDatabase(final String uid, final Context context, final RecyclerView recyclerView){
        //DataSnapshot dataSnapshot = new DataSnapshot().child(uid).getValue();

        databaseReference.child(Constants.ARG_USERS).child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                List<UserProfile> userData = getSourceData(dataSnapshot, uid, context);

                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, userData);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                List<UserProfile> userData = getSourceData(dataSnapshot, uid, context);

                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, userData);
                recyclerView.setAdapter(recyclerViewAdapter);
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

    private List<UserProfile> getSourceData(DataSnapshot dataSnapshot, String uId, Context context){

        ArrayList<UserProfile> allUserData = new ArrayList<>();
        Map<String, String> userInformation = new HashMap<String, String>();

        userInformation.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());

        SharedPrefUtil sharedPref = new SharedPrefUtil(context);

        if (dataSnapshot.getKey().equals(Helper.PREF_USERNAME)) {
            String username = dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_USERNAME, username);
        }else if (dataSnapshot.getKey().equals(Helper.Full_Name)){
            String fullName 		= dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_FULL_NAME, fullName);
        }else if (dataSnapshot.getKey().equals(Helper.Mobile_NUMBER)){
            String mobileNumber 	= dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_MOBILE_NUMBER, mobileNumber);
        }else if (dataSnapshot.getKey().equals(Helper.PREF_BIRTHDAY)){
            String birthday 	= dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_BIRTHDAY, birthday);
        }else if (dataSnapshot.getKey().equals(Helper.PREF_HOBBY_INTEREST)){
            String hobby 	= dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_HOBBY_INTEREST, hobby);
        }else if (dataSnapshot.getKey().equals(Helper.PREF_RELATIONSHIP)){
            String relationship 	= dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_RELATIONSHIP, relationship);
        }else if (dataSnapshot.getKey().equals(Helper.CURRENT_CITY)){
            String current_city 	= dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_CURRENT_CITY, current_city);
        }else if (dataSnapshot.getKey().equals(Helper.PREF_STATUS)){
            String status 	= dataSnapshot.getValue().toString();
            sharedPref.saveString(Helper.PREF_STATUS, status);
        }else if (dataSnapshot.getKey().equals(Helper.LAST_SEEN_DATE)){
            long lastSeenDate 	= (long)dataSnapshot.getValue();
            sharedPref.saveLongDate(Helper.LAST_SEEN_DATE, lastSeenDate);
        }


        allUserData.add(new UserProfile(Helper.EMAIL,           userInformation.get(Helper.PREF_EMAIL)));
        allUserData.add(new UserProfile(Helper.DISPLAY_NAME,    userInformation.get(Helper.PREF_USERNAME)));
        allUserData.add(new UserProfile(Helper.BIRTHDAY,        userInformation.get(Helper.PREF_BIRTHDAY)));
        allUserData.add(new UserProfile(Helper.MOBILE_NUMBER,   userInformation.get(Helper.PREF_MOBILE_NUMBER)));
        allUserData.add(new UserProfile(Helper.PREF_CURRENT_CITY,   userInformation.get(Helper.PREF_CURRENT_CITY)));
        allUserData.add(new UserProfile(Helper.PREF_RELATIONSHIP,   userInformation.get(Helper.PREF_RELATIONSHIP)));
        allUserData.add(new UserProfile(Helper.HOBBY_INTEREST,      userInformation.get(Helper.PREF_HOBBY_INTEREST)));

        return allUserData;
    }


    //----------------------DATA SYNCHRONIZATION SECTION--------------------------------------

    public void syncUserDataFromFirebaseDtatbase(final String uid, final Context context){

        databaseReference.child(Constants.ARG_USERS).child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getSourceData(dataSnapshot, uid, context);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getSourceData(dataSnapshot, uid, context);
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

    public void syncUserDataToFirebaseDtatbase(String uid, String email, Context context){

        SharedPrefUtil sharedPref = new SharedPrefUtil(context);

        String username 	= sharedPref.getString(Helper.PREF_USERNAME);
        String fullName 	= sharedPref.getString(Helper.PREF_FULL_NAME);
        String mobileNumber = sharedPref.getString(Helper.PREF_MOBILE_NUMBER);
        String birthday 	= sharedPref.getString(Helper.PREF_BIRTHDAY);
        String hobby 		= sharedPref.getString(Helper.PREF_HOBBY_INTEREST);
        String relationship	= sharedPref.getString(Helper.PREF_RELATIONSHIP);
        String current_city	= sharedPref.getString(Helper.PREF_CURRENT_CITY);
        String status       = sharedPref.getString(Helper.PREF_STATUS);
        long lastSeenDate   = sharedPref.getLongDate(Helper.LAST_SEEN_DATE);

        UserEntity userEntity = new UserEntity(uid, email, username, fullName, mobileNumber, birthday,
                hobby, relationship, current_city, status, lastSeenDate, "");

        addUserToDatabase(uid, userEntity);

        sharedPref.saveInt("SYNC_DATA_STATE", 0);
    }

}
