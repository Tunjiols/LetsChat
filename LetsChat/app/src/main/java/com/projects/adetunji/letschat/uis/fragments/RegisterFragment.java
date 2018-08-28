package com.projects.adetunji.letschat.uis.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.registration.RegisterContract;
import com.projects.adetunji.letschat.core.registration.RegisterPresenter;
import com.projects.adetunji.letschat.core.users.add.AddUserContract;
import com.projects.adetunji.letschat.core.users.add.AddUserPresenter;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.uis.activities.UserListingActivity;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

/**
 * Created by adetunji on 07/01/2018.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener, RegisterContract.View,
                                                                            AddUserContract.View {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    private RegisterPresenter       mpRegisterPresenter;
    private AddUserPresenter        mpAddUserPresenter;
    private DialogInfoPageFragment  dialogInfoPageFragment = new DialogInfoPageFragment();

    private String      username;
    private EditText    mpTxtEmail;
    private EditText    mpTxtDisplayName;
    private EditText    mpTxtPassword;
    private	EditText    mpTxtConfirmPassword;
    private Button 	    mpBtnRegister;
    private TextView    mpTxtErrorMessage;
    private TextView    mTerms;
    private TextView    mPolicies;
    private TextView    mHelp;

    private ProgressDialog mpProgressDialog;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_register, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mpTxtEmail              = (EditText) view.findViewById(R.id.edit_text_email_id);
        mpTxtDisplayName        = (EditText) view.findViewById(R.id.edit_text_username);
        mpTxtPassword           = (EditText) view.findViewById(R.id.edit_text_password);
        mpTxtConfirmPassword    = (EditText) view.findViewById(R.id.edit_text_confirm_password);
        mpBtnRegister           = (Button) view.findViewById(R.id.button_register);
        mpTxtErrorMessage       = (TextView) view.findViewById(R.id.text_error_message);

        mTerms 		            = (TextView) view.findViewById(R.id.terms);
        mHelp 		            = (TextView) view.findViewById(R.id.help);
        mPolicies 	            = (TextView) view.findViewById(R.id.policies);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mpRegisterPresenter     = new RegisterPresenter(this);
        mpAddUserPresenter      = new AddUserPresenter(this);
        mpProgressDialog        = new ProgressDialog(getActivity());

        mpProgressDialog.setTitle(getString(R.string.loading));
        mpProgressDialog.setMessage(getString(R.string.please_wait));
        mpProgressDialog.setIndeterminate(true);

        mpBtnRegister.setOnClickListener(this);
        mTerms.setOnClickListener(this);
        mPolicies.setOnClickListener(this);
        mHelp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_register:
                onRegister(view);
                break;

            case R.id.terms:

                dialogInfoPageFragment.setMessage(R.string.terms_title, R.string.terms);
                dialogInfoPageFragment.show(getFragmentManager(),"");
                dialogInfoPageFragment.mVisible("terms");
                break;

            case R.id.policies:

                dialogInfoPageFragment.setMessage(R.string.policies_title, R.string.policies);
                dialogInfoPageFragment.show(getFragmentManager(),"");
                dialogInfoPageFragment.mVisible("policies");
                break;

            case R.id.help:
                dialogInfoPageFragment.setMessage(R.string.help_title, R.string.help);
                dialogInfoPageFragment.show(getFragmentManager(),"");
                dialogInfoPageFragment.mVisible("help");

                break;
        }
    }



    private void onRegister(View view) {

        if (!dialogInfoPageFragment.isYes()){
            dialogInfoPageFragment.setMessage(R.string.terms_title, R.string.terms);
            dialogInfoPageFragment.mVisible("terms");
            dialogInfoPageFragment.show(getFragmentManager(),"");
        }else {
            String emailId = mpTxtEmail.getText().toString();
            username = mpTxtDisplayName.getText().toString();
            String password = mpTxtPassword.getText().toString();
            String confirm_password = mpTxtConfirmPassword.getText().toString();

            mDialogFragment mpDialogFragment;

            if (emailId.isEmpty()
                    || password.isEmpty()
                    || confirm_password.isEmpty()
                    || username.isEmpty()) {
                //create a new dialog box to inform user that something is missing
                mpDialogFragment = new mDialogFragment();
                mpDialogFragment.setMessage = "All fields are required!";
                mpDialogFragment.show(getFragmentManager(), " ");
            } else {
                if (password.contentEquals(confirm_password)) {
                    mpRegisterPresenter.register(getActivity(), emailId, password);
                    mpProgressDialog.show();
                } else {
                    mpDialogFragment = new mDialogFragment();
                    mpDialogFragment.setMessage = "Your passwords are different!";
                    mpDialogFragment.show(getFragmentManager(), " ");
                }

            }
        }
    }

    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mpProgressDialog.setMessage(getString(R.string.adding_user_to_db));

        Helper.displayToastMessage(getActivity(), "Registration Successful!");

        UserEntity userEntity = new UserEntity(firebaseUser.getUid(), firebaseUser.getEmail(), username,
                new SharedPrefUtil(getActivity().getApplicationContext()).getString(Constants.ARG_FIREBASE_TOKEN));

        mpAddUserPresenter.addUser(getActivity().getApplicationContext(), firebaseUser, userEntity);
    }

    @Override
    public void onRegistrationFailure(String message) {
        mpProgressDialog.dismiss();

        mpTxtErrorMessage.setText(message);

        mpProgressDialog.setMessage(getString(R.string.please_wait));
        Log.e(TAG, "onRegistrationFailure: " + message);
        Helper.displayToastMessage(getActivity(), "Registration failed!+\n" + message);
        }

    @Override
    public void onAddUserSuccess(String message) {
        mpProgressDialog.dismiss();
        Helper.displayToastMessage(getActivity(), message);

        //Set default value
        SharedPrefUtil sharedPref = new SharedPrefUtil(getContext());
        sharedPref.saveString(Helper.PREF_STATUS, "Available");
        sharedPref.saveString(Helper.PREF_USERNAME, username);
        sharedPref.saveString(Helper.PREF_EMAIL, mpTxtEmail.getText().toString());
        sharedPref.saveInt("SYNC_DATA_STATE", 1);

        UserListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onAddUserFailure(String message) {
        mpProgressDialog.dismiss();
        Helper.displayToastMessage(getActivity(), message);
    }
}
