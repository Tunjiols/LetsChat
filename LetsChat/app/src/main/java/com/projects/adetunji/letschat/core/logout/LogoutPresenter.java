package com.projects.adetunji.letschat.core.logout;

/**
 * Created by adetunji on 07/01/2018.
 */

public class LogoutPresenter implements LogoutContract.Presenter, LogoutContract.OnLogoutListener{
    private LogoutContract.View appLogoutView;
    private LogoutInteractor appLogoutInteractor;

    public LogoutPresenter(LogoutContract.View logoutView) {
        appLogoutView = logoutView;
        appLogoutInteractor = new LogoutInteractor(this);
    }

    @Override
    public void logout() {
        appLogoutInteractor.performFirebaseLogout();
    }

    @Override
    public void onSuccess(String message) {
        appLogoutView.onLogoutSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        appLogoutView.onLogoutFailure(message);
    }
}
