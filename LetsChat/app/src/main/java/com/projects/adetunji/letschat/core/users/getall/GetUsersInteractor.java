package com.projects.adetunji.letschat.core.users.getall;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projects.adetunji.letschat.models.Chat;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by adetunji on 07/01/2018.
 *
 * ============ CHAT ROOM ======================
 */

public class GetUsersInteractor implements GetUsersContract.Interactor{

    private static final String TAG = "GetUsersInteractor";

    private UserEntity user;
    public List<UserEntity> loginUser;


    private GetUsersContract.OnGetAllUsersListener appOnGetAllUsersListener;

    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.appOnGetAllUsersListener = onGetAllUsersListener;
    }


    @Override
    public void getAllUsersFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();

                List<UserEntity> users = new ArrayList<>();
                loginUser = new ArrayList<>();

                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    user = dataSnapshotChild.getValue(UserEntity.class);
                    if (!TextUtils.equals(user.Email, FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        users.add(user);

                    }
                    if (TextUtils.equals(user.Email, FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        loginUser.add(user);//Get profile of login user
                    }
                }
                appOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                appOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getChatUsersFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_CHAT_ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<UserEntity> users = new ArrayList<>();
                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    dataSnapshotChild.getRef();
                    Chat chat = dataSnapshotChild.getValue(Chat.class);
                    //if(chat.)
                    if(!TextUtils.equals(user.Email,FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    
    private void observableGetUsers(DataSnapshot dataSnapshot){
        Observable<DataSnapshot> observeData = Observable.create(new Observable.OnSubscribe<DataSnapshot>() { 
            @Override
            public void call(Subscriber<? super DataSnapshot> subscriber) {
                if (!subscriber.isUnsubscribed()) { 
                    try{
                        DataSnapshot datasnapshotChild = dataSnapshot.next(); 
                        subscriber.onNext(datasnapshotChild); 
                        subscriber.onCompleted(); //5
                    } catch (Exception e) {
                        subscriber.onError(e); //6
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        
        Subscriber<DataSnapshot> dataSubscriber = new Subscriber<DataSnapshot>() {
            List<UserEntity> users = new ArrayList<>();
            @Override
            public void onNext(DataSnapshot dataSnapshotChild) { 
                user = dataSnapshotChild.getValue(UserEntity.class);
                if (!TextUtils.equals(user.Email, FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    users.add(user);
                }
            }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }
        };
        
        observeData.subscribe(dataSubscriber);
    }
/*
    public List<UserEntity> getUserData(){

        List<UserEntity> loginUser = this.loginUser;

       // if (TextUtils.equals(user.email, FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
        //    loginUser.add(user);//Get profile of login user
       // }
        if (!loginUser.isEmpty()) return loginUser;

        return null;
    }*/
}
