package com.projects.adetunji.letschat.core.users.getall;

import com.projects.adetunji.letschat.models.UserEntity;

import java.util.List;

/**
 * Created by adetunji on 07/01/2018.
 */

public interface GetUsersContract {

    interface View {
        void onGetAllUsersSuccess(List<UserEntity> users);

        void onGetAllUsersFailure(String message);

        void onGetChatUsersSuccess(List<UserEntity> users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers();

        void getChatUsers();
    }

    interface Interactor {
        void getAllUsersFromFirebase();

        void getChatUsersFromFirebase();
    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<UserEntity> users);

        void onGetAllUsersFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(List<UserEntity> users);

        void onGetChatUsersFailure(String message);
    }
}
