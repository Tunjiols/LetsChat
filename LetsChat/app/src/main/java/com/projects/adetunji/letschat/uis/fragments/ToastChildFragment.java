package com.projects.adetunji.letschat.uis.fragments;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.uis.activities.FriendListActivity;


/**
 * Created by adetunji on 10/03/2018.
 */

public class ToastChildFragment extends Fragment implements View.OnClickListener{


    private TextView mForward;
    private TextView mCopy;
    private TextView mDelete;
    private LinearLayout mBodyBg;

    private static String chatTAG;
    private static String message;
    private static Context mcontext;

    public static ToastChildFragment newInstance(Context context, String chatTAGs, String message1) {
        ToastChildFragment fragment = new ToastChildFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        chatTAG = chatTAGs;
        message =message1;
        mcontext = context;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Need to define the child fragment layout
        View fragmentView = inflater.inflate(R.layout.toast_child_fragment, container, false);
        bindView(fragmentView);
        return fragmentView;
    }

    private void bindView(View fragmentView){
        mForward      			= (TextView) fragmentView.findViewById(R.id.forward_text);
        mDelete			        = (TextView) fragmentView.findViewById(R.id.delete_text);
        mCopy			    	= (TextView) fragmentView.findViewById(R.id.copy_text);
        mBodyBg					= (LinearLayout) fragmentView.findViewById(R.id.message_dialog_linearlayout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(chatTAG.equals("myChatViewHolder")){
            mBodyBg.setBackground(getResources().getDrawable(R.drawable.forward_mine_dialog_background));
            String TAG = "myChatViewHolder";

        }else if(chatTAG.equals("otherChatViewHolder")){
            mBodyBg.setBackground(getResources().getDrawable(R.drawable.forward_dialog_background));
            String TAG = "otherChatViewHolder";

        }

        mForward.setOnClickListener(this);
        mCopy.setOnClickListener(this);
        mDelete.setOnClickListener(this);

                //if (!getSupportFragmentManager().findFragmentByTag(TAG).isEmpty()) {
               // transaction.detach(ChatRecyclerAdapter.ToastChildFragment.newInstance(chatTAG, message));
                //}

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forward_text:
                FriendListActivity.startIntent(mcontext, Intent.FLAG_ACTIVITY_NEW_TASK, chatTAG);
                //dismiss();
                break;
            case R.id.copy_text:
                copyTextTOClipboard(mcontext, message);
                Helper.displayToastMessage(mcontext, "Message copied");
                //dismiss();
                break;
            case R.id.delete_text:
                //Show Alert box and delete the message if OK is selected
                //SetAlertDialog(position);
                //dismiss();
                break;

            default: break;
        }
    }



    /**
     *method to copy text to clipboard:
     */
    private static void copyTextTOClipboard(Context context, String message) {

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(message);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Message", message);
            clipboard.setPrimaryClip(clip);
        }
    }


    private void SetAlertDialog(final int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);

        dialog.setTitle("DELETE").setMessage("Delete the message!")
                .setIcon(mcontext. getResources().getDrawable(R.drawable.delete_red_icon))
                .setPositiveButton("Yes, delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //removeItem(position);
                        //TO DO
                        //Update the firebase databse
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dismiss the dialog box
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }


}
