package com.projects.adetunji.letschat.uis.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by adetunji on 13/01/2018.
 */

public class mDialogFragment extends DialogFragment {

    public String setMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class because this dialog has a simple UI
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(setMessage)

                // An OK button dismiss the dialog
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dismiss the dialog box
                        dialog.dismiss();
                    }
                })

                // A "Cancel" button that does nothing
                .setNegativeButton(" ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }

                });

        // Create the object and return it
        return builder.create();
    }

}
