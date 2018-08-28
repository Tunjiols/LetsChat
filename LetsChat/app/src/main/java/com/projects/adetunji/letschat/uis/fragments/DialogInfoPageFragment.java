package com.projects.adetunji.letschat.uis.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 24/01/2018.
 */

public class DialogInfoPageFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener{

    private boolean     mVisible;
    private int         setMessages;
    private int         messageBody;
    private boolean     yes ;
    private CheckBox    mDisagreeBox;
    private CheckBox    mAgreeBox;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View mDalogView = inflater.inflate(R.layout.dialog_info_page_fragment, null);

        final TextView mTitle = (TextView) mDalogView.findViewById(R.id.Title);

        final TextView mDescription = (TextView) mDalogView.findViewById(R.id.Description);

        mDisagreeBox = (CheckBox) mDalogView.findViewById(R.id.DisagreeBox);
        mAgreeBox = (CheckBox) mDalogView.findViewById(R.id.AgreeBox);

        final Button btnOK = (Button) mDalogView.findViewById(R.id.btnOK);


        builder.setView(mDalogView);

        mTitle.setText(setMessages);

        mDescription.setText(messageBody);

        if (!mVisible) {
            mAgreeBox.setVisibility(View.INVISIBLE);
            mDisagreeBox.setVisibility(View.INVISIBLE);
        }


        mDisagreeBox.setOnCheckedChangeListener(this);
        mAgreeBox.setOnCheckedChangeListener(this);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked){
        switch (button.getId()) {
            case R.id.DisagreeBox:
                if (isChecked){
                    yes = false;
                    mAgreeBox.setChecked(false);
                }
            case R.id.AgreeBox:
                if (isChecked){
                    yes = true;
                    mDisagreeBox.setChecked(false);
                }
             break;
        }
    }
    public void setMessage(int messageTitle, int messageBody){
        this.setMessages = messageTitle;
        this.messageBody = messageBody;
    }

    public boolean isYes(){return yes;}

    public void mVisible(String type){

        if (type == "terms") this.mVisible = true;
        else this.mVisible = false;
    }

}
