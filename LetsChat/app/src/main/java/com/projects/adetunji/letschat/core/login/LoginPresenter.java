package com.projects.adetunji.letschat.core.login;

import android.app.Activity;

/**
 * Created by adetunji on 07/01/2018.
 */

public class LoginPresenter implements LoginContract.Presenter, LoginContract.OnLoginListener{
    private LoginContract.View mpLoginView;
    private LoginInteractor mpLoginInteractor;

    public LoginPresenter(LoginContract.View loginView) {
        this.mpLoginView = loginView;
        mpLoginInteractor = new LoginInteractor(this);
    }

    @Override
    public void login(Activity activity, String email, String password) {
        mpLoginInteractor.performFirebaseLogin(activity, email, password);
    }

    @Override
    public void onSuccess(String message) {
        mpLoginView.onLoginSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mpLoginView.onLoginFailure(message);
    }
}
