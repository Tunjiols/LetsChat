package com.projects.adetunji.letschat.core.login;

import android.app.Activity;

/**
 * Created by adetunji on 07/01/2018.
 */

public interface LoginContract {
     interface View {
        void onLoginSuccess(String message);

        void onLoginFailure(String message);
    }

    interface Presenter {
        void login(Activity activity, String email, String password);
    }

    interface Interactor {
        void performFirebaseLogin(Activity activity, String email, String password);
    }

    interface OnLoginListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
