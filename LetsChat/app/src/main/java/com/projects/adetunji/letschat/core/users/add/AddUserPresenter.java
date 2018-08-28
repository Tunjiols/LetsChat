package com.projects.adetunji.letschat.core.users.add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.models.UserEntity;

/**
 * Created by adetunji on 07/01/2018.
 */

public class AddUserPresenter implements AddUserContract.Presenter, AddUserContract.OnUserDatabaseListener{
    private AddUserContract.View appView;
    private AddUserInteractor appAddUserInteractor;

    public AddUserPresenter(AddUserContract.View view) {
        this.appView = view;
        appAddUserInteractor = new AddUserInteractor(this);
    }

    @Override
    public void addUser(Context context, FirebaseUser firebaseUser, UserEntity userEntity) {
        appAddUserInteractor.addUserToDatabase(context, firebaseUser, userEntity);
    }

    @Override
    public void onSuccess(String message) {
        appView.onAddUserSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        appView.onAddUserFailure(message);
    }
}
