package com.projects.adetunji.letschat.core.users.add;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

/**
 * Created by adetunji on 07/01/2018.
 */

public class AddUserInteractor implements AddUserContract.Interactor{

    private AddUserContract.OnUserDatabaseListener appOnUserDatabaseListener;

    public AddUserInteractor(AddUserContract.OnUserDatabaseListener onUserDatabaseListener) {
        this.appOnUserDatabaseListener = onUserDatabaseListener;
    }

    @Override
    public void addUserToDatabase(final Context context, FirebaseUser firebaseUser, UserEntity userEntity) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child(Constants.ARG_USERS).child(firebaseUser.getUid()).setValue(userEntity)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            appOnUserDatabaseListener.onSuccess(context.getString(R.string.user_successfully_added));
                        } else {
                            appOnUserDatabaseListener.onFailure(context.getString(R.string.user_unable_to_add));
                        }
                    }
                });
    }
}
