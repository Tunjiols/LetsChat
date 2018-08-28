package com.projects.adetunji.letschat.uis.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.login.LoginContract;
import com.projects.adetunji.letschat.core.login.LoginPresenter;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.HelperFirebaseDatabase;
import com.projects.adetunji.letschat.uis.activities.RegisterActivity;
import com.projects.adetunji.letschat.uis.activities.UserListingActivity;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

/**
 * Created by adetunji on 07/01/2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, LoginContract.View {

    private LoginPresenter  mpLoginPresenter;
    private EditText        mpTxtEmail, mpTxtPassword;
    private Button          mpBtnLogin, mpBtnRegister;
    private ProgressDialog  mpProgressDialog;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mpTxtEmail      = (EditText) view.findViewById(R.id.edit_text_email_id);
        mpTxtPassword   = (EditText) view.findViewById(R.id.edit_text_password);
        mpBtnLogin      = (Button) view.findViewById(R.id.button_login);
        mpBtnRegister   = (Button) view.findViewById(R.id.button_register);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mpLoginPresenter = new LoginPresenter(this);
        mpProgressDialog = new ProgressDialog(getActivity())
        ;
        mpProgressDialog.setTitle(getString(R.string.loading));
        mpProgressDialog.setMessage(getString(R.string.please_wait));
        mpProgressDialog.setIndeterminate(true);

        mpBtnLogin.setOnClickListener(this);
        mpBtnRegister.setOnClickListener(this);

        //setDummyCredentials();
    }

    private void setDummyCredentials() {
        mpTxtEmail.setText("test@test.com");
        mpTxtPassword.setText("123456");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_login  : onLogin(view);
                break;
            case R.id.button_register: onRegister(view);
                break;
        }
    }

    private void onLogin(View view) {
        String emailId = mpTxtEmail.getText().toString();
        String password = mpTxtPassword.getText().toString();

        if (emailId.isEmpty() || password.isEmpty()){
            //create a new dialog box to inform user that something is missing
            mDialogFragment mpDialogFragment = new mDialogFragment();
            mpDialogFragment.setMessage ="Email or password is missing!";
            mpDialogFragment.show(getFragmentManager(), " ");
        }
        else {

            mpLoginPresenter.login(getActivity(), emailId, password);
            mpProgressDialog.show();
        }
    }

    private void onRegister(View view) {
        RegisterActivity.startActivity(getActivity());
    }

    @Override
    public void onLoginSuccess(String message) {
        mpProgressDialog.dismiss();
        Helper.displayToastMessage(getContext(), "Logged in successfully");

        SharedPrefUtil sharedPref = new SharedPrefUtil(getContext());
        sharedPref.saveInt("SYNC_DATA_STATE", 0);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        HelperFirebaseDatabase  database = new HelperFirebaseDatabase();
        database.syncUserDataFromFirebaseDtatbase(firebaseUser.getUid(), getContext());

        UserListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void onLoginFailure(String message) {
        mpProgressDialog.dismiss();
        Helper.displayToastMessage(getContext(), "Error: " + message);
    }


}
