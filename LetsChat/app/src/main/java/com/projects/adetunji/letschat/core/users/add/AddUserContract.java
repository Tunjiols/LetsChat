package com.projects.adetunji.letschat.core.users.add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.models.UserEntity;

/**
 * Created by adetunji on 07/01/2018.
 */

public interface AddUserContract {
    interface View {
        void onAddUserSuccess(String message);

        void onAddUserFailure(String message);
    }

    interface Presenter {
        void addUser(Context context, FirebaseUser firebaseUser, UserEntity userEntity);
    }

    interface Interactor {
        void addUserToDatabase(Context context, FirebaseUser firebaseUser, UserEntity userEntity);
    }

    interface OnUserDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
