package com.projects.adetunji.letschat.core.registration;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by adetunji on 07/01/2018.
 */

public class RegisterPresenter implements RegisterContract.Presenter, RegisterContract.OnRegistrationListener{
    private RegisterContract.View mpRegisterView;
    private RegisterInteractor mpRegisterInteractor;

    public RegisterPresenter(RegisterContract.View registerView) {
        this.mpRegisterView = registerView;
        mpRegisterInteractor = new RegisterInteractor(this);
    }

    @Override
    public void register(Activity activity, String email, String password) {
        mpRegisterInteractor.performFirebaseRegistration(activity, email, password);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mpRegisterView.onRegistrationSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        mpRegisterView.onRegistrationFailure(message);
    }
}
