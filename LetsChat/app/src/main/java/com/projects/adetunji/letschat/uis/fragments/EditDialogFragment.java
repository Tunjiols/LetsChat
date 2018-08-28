package com.projects.adetunji.letschat.uis.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 10/03/2018.
 */

public class EditDialogFragment extends DialogFragment implements AlertDialog.OnClickListener{
    private Context mContext;
    private static String TITLE;
    private static String searchText;



    public void EditDialog(String TITLE1, String searchText1){
        this.TITLE = TITLE1;
        this.searchText = searchText1;
    }

    public static EditDialogFragment newInstance(String TITLE1, String searchText1) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, TITLE1);
        args.putString(searchText,searchText1);
        frag.setArguments(args);
        return frag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.editor_dialog_fragment, null);

        final TextView 	mTitle			= (TextView)dialogView.findViewById(R.id.title);
        final EditText 	mEditText			= (EditText)dialogView.findViewById(R.id.edit_text);
        final ImageButton 	mSearch				= (ImageButton)dialogView.findViewById(R.id.action_search);

        mTitle.setText( TITLE);
        mEditText.setHint(searchText);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String text = mEditText.getText().toString().trim();
                    mEditText.setText("");

                }
                return false;
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String text = mEditText.getText().toString().trim();
                if (!text.isEmpty()) {
                    switch(TITLE){
                        case "Add by Email":
                            searchByEmail(text);
                            break;
                        case "Add by Contact/Phone number":
                            int text1 = Integer.valueOf(mEditText.getText().toString().trim());
                            searchByContact(text1);
                            break;
                        case "Add by Username":
                            searchByusername(text);
                            break;
                    }
                    mEditText.setText("");
                    //now search firebase database
                    //search_list.add();

                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, search_list);
                    //listView.setAdapter(adapter);

                    dismiss();
                }
            }
        });

        builder.setView(dialogView);

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        // switch (which) {
        //     case mSearch:
        //         dismiss();
        //         break;
        //  }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void searchByEmail(String email){

    }

    private void searchByusername(String username){

    }

    private void searchByContact(int contact){

    }
}
