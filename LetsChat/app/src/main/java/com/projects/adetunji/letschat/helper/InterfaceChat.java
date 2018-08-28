package com.projects.adetunji.letschat.helper;

import android.app.ListFragment;
import android.content.Context;

import com.projects.adetunji.letschat.models.Chat;

/**
 * Created by adetunji on 01/03/2018.
 */

public class InterfaceChat extends ListFragment{
    OnMessageHighlightedListener mCallback;

    //OnMessageSentSuccessfullyListener.mCallback;

    // Container Activity must implement this interface
    public interface OnMessageSentSuccessfullyListener {
        public void onSuccessMessageSent(int position);
    }

    public interface OnMessageHighlightedListener {
        public void onMessageHighlighted(Chat chat);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMessageHighlightedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement OnMessageHighlightedListener");
        }
    }
}
