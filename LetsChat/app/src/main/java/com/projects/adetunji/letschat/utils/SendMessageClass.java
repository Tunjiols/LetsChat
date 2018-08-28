package com.projects.adetunji.letschat.utils;

import android.content.Context;

import com.projects.adetunji.letschat.core.chat.ChatContract;
import com.projects.adetunji.letschat.core.chat.ChatPresenter;
import com.projects.adetunji.letschat.models.Chat;

/**
 * Created by adetunji on 08/03/2018.
 */

public class SendMessageClass implements ChatContract.View{

    private static ChatPresenter mChatPresenter;
    private static boolean messageSent ;

    public static void forwardMessage(
                            Context context,
							String message,
                            String sender,
                            String senderUid,
                            String receiver,
                            String receiverUid,
                            String receiverFirebaseToken) {

        boolean isRead = false;
        int LIKE_STATUS = 0;

        Chat chat = new Chat(
                            sender,
                            receiver,
                            senderUid,
                            receiverUid,
                            message,
                            System.currentTimeMillis(),
                            isRead,
                            LIKE_STATUS);

        /*TO DO
        * write json file that takes and store the messages before sending to server
        * input ArrayList is Chat
        * output will be Chat object
        * send message to firebase*/
        mChatPresenter.sendMessage(context, chat, receiverFirebaseToken);//send message to server

    }

    public static boolean isMessageSent(){
        return messageSent;
    }

    @Override
    public void onSendMessageSuccess() {
        messageSent = true;
    }

    @Override
    public void onSendMessageFailure(String message) {
        messageSent = false;
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {

    }

    @Override
    public void onGetMessagesFailure(String message) {

    }

}
