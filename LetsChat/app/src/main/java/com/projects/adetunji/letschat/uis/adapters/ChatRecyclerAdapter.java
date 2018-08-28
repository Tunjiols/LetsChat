package com.projects.adetunji.letschat.uis.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.chat.ChatContract;
import com.projects.adetunji.letschat.core.chat.ChatPresenter;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.InterfaceChat;
import com.projects.adetunji.letschat.models.Chat;
import com.projects.adetunji.letschat.uis.activities.ChatActivity;
import com.projects.adetunji.letschat.uis.activities.FriendListActivity;
import com.projects.adetunji.letschat.uis.fragments.ChatFragment;
import com.projects.adetunji.letschat.utils.NetworkConnectionUtil;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by adetunji on 10/01/2018.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG 			= "ChatRecyclerAdapter";
    private static final int VIEW_TYPE_ME 		= 1;
    private static final int VIEW_TYPE_OTHER 	= 2;
    private static final int VIEW_BEGIN_CHAT 	= 3;
    private static final int VIEW_SEPARATOR 	= 4;
    private int LIKE_STATUS 					= 0;
    private long mpMessageTime;
    public TextView mtxtChatMessage;

    private ChatPresenter 	mChatPresenter;
    private String 			receiverFirebaseTOKEN;
    private List<Chat> 		mpChats;
    private static Context 		context;
    private Chat 			chat;
    private String 			filePath;
    //private boolean 		mBackground_Clicked;

    private InterfaceChat.OnMessageHighlightedListener onDialogShow;

    public ChatRecyclerAdapter(List<Chat> chats,String receiverFirebaseTOKEN, Context context) {

        mpChats = chats;
        this.context = context;
        this.receiverFirebaseTOKEN = receiverFirebaseTOKEN;
        //this.mBackground_Clicked = backg_click;
    }


    public long getMessageTime() {
        return mpMessageTime;
    }


    public void add(Chat chat) {
        /*TO DO
        * Inspect each elements of Chat
        * inspect chat date
        * */
        mpChats.add(chat);

        //need default chat elements
        notifyItemInserted(mpChats.size() - 1);

        mpMessageTime = new Date().getTime();
        long chatTime = chat.timestamp;
        DateFormat.format("dd, MMM (HH:mm)", mpMessageTime);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);

                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);

                break;
            case VIEW_BEGIN_CHAT:
                View viewChatBegin = layoutInflater.inflate(R.layout.item_chat_beginning, parent, false);
                viewHolder = new ChatBeginMessage(viewChatBegin);
                break;

            case VIEW_SEPARATOR:
                View itemDateSeparator = layoutInflater.inflate(R.layout.item_chat_beginning, parent, false);
                viewHolder = new ItemDateSeparator(itemDateSeparator);
                break;

        }
        //save the entire chats on the device
        saveToJSON(mpChats,  FirebaseAuth.getInstance().getCurrentUser().getUid());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //ChatMessageConfigure(position);
        if (TextUtils.equals(mpChats.get(position).senderUid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    // Create an anonymous implementation of OnFocusChangeListener
    private View.OnFocusChangeListener mFocusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            // do something when user scroll out of focus
            //myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
            //otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
        }
    };



    private void configureMyChatViewHolder(final MyChatViewHolder myChatViewHolder, final int position) {
        myChatViewHolder1 = myChatViewHolder;

        final Chat chat = mpChats.get(position);
        String alphabet = chat.sender.substring(0, 1);
        myChatViewHolder.mptxtChatMessage.setText(chat.message);
        myChatViewHolder.mpMessageTime.setText(DateFormat.format("dd, MMM (HH:mm)", chat.timestamp));

        final int positions = position;
        if (chat.isRead){
            myChatViewHolder.mSendStatusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.all_done_icon));
        }

        switch (chat.LIKE_STATUS){
            case 1:
                myChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up));
                myChatViewHolder.mThumb_up.setVisibility(View.VISIBLE);
                break;
            case 2:
                myChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down));
                myChatViewHolder.mThumb_up.setVisibility(View.VISIBLE);
                break;
            case 3:
                myChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_png));
                myChatViewHolder.mThumb_up.setVisibility(View.VISIBLE);
                break;
            case 0:
                myChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_frame));
                myChatViewHolder.mThumb_up.setVisibility(View.GONE);
                break;
        }
        //Set profile image
        //myChatViewHolder.mptxtUserPic.setImageDrawable()(alphabet);

        //When long click message background
        final String message = chat.message;
        final String chatTAG = "myChatViewHolder";
        myChatViewHolder.mlinearLayoutMsgBg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //FragmentTransaction transaction = context. getChildFragmentManager().beginTransaction();
				/*Add to View*/
               // transaction.setCustomAnimations(R.anim.entry_animatiom, R.anim.exit_animation);
                //transaction.add(myChatViewHolder.mlinearLayoutMsgBg, ToastChildFragment.newInstance(chatTAG), chatTAG).commit();




                myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.VISIBLE);
                //ChatActivity.viewSet(true);
                // v.setBackgroundColor(context.getResources().getColor(R.color.grey_400));
                //v.invalidate();


                myChatViewHolder.mpForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendListActivity.startIntent(context, Intent.FLAG_ACTIVITY_NEW_TASK, TAG);
                        myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);

                        //Helper.displayToastMessage(context, message);
                    }
                });

                myChatViewHolder.mpCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //copyTextTOClipboard(context, message);
                        myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);

                        Helper.displayToastMessage(context, "Message copied");
                    }
                });

                myChatViewHolder.mpDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        //Show Alert box and delete the message if OK is selected
                        SetAlertDialog(position);
                    }

                });
                //do something
                return true;
            }
        });

        /**
         * Called when the background is clicked and the View is vissible
         * */
        // if (mBackground_Clicked && myChatViewHolder.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
        //     myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
        // }

        //set mlinearLayoutMsgBg invisible when focus changed
        myChatViewHolder.mlinearLayoutMsgBg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                   // transaction.detach(ToastChildFragment.newInstance(chatTAG, message));
                }
            }
        });

    }


    private void configureOtherChatViewHolder(final OtherChatViewHolder otherChatViewHolder, final int position) {
        otherChatViewHolder1 = otherChatViewHolder;

        chat = mpChats.get(position);

        final int positions = position;
        String alphabet = chat.sender.substring(0, 1);
        otherChatViewHolder.mptxtChatMessage.setText(chat.message);
        otherChatViewHolder.mpMessageTime.setText(DateFormat.format("dd, MMM (HH:mm)", chat.timestamp));

        switch (chat.LIKE_STATUS){
            case 1:
                otherChatViewHolder.mThumb_up_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up));
                otherChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up));
                break;
            case 2:
                otherChatViewHolder.mThumb_dwn_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down));
                otherChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down));
                break;
            case 3:
                otherChatViewHolder.mFavourite_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_png));
                otherChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_png));
                break;
            case 0:
                otherChatViewHolder.mThumb_up_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_frame));
                otherChatViewHolder.mThumb_dwn_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down_frame));
                otherChatViewHolder.mFavourite_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_frame_png));
                otherChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_frame));
                break;
        }

        //When long click message background
        final String message = chat.message;
        final String chatTAG = "otherChatViewHolder";
        otherChatViewHolder.mlinearLayoutMsgBg.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

				/*Add to View*/
               // transaction = getChildFragmentManager().beginTransaction();
               // transaction.setCustomAnimations(R.anim.entry_animatiom, R.anim.exit_animation);
                //transaction.add(otherChatViewHolder.mlinearLayoutMsgBg, ToastChildFragment.newInstance(chatTAG), chatTAG).commit();

                otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.VISIBLE);
                // v.setBackgroundColor(context.getResources().getColor(R.color.grey_400) );//0x0000FF00


                otherChatViewHolder.mpForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendListActivity.startIntent(context, Intent.FLAG_ACTIVITY_NEW_TASK, TAG);
                        otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        //Helper.displayToastMessage(context, message);
                    }
                });

                otherChatViewHolder.mpCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // copyTextTOClipboard(context, message);
                        otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);

                        Helper.displayToastMessage(context, "Message copied");
                    }
                });


                otherChatViewHolder.mpDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        //Show Alert box and delete the message if OK is selected
                        SetAlertDialog(position);
                    }

                });
                //do something
                return true;
            }
        });

        /**
         * Called when the background is clicked and the View is vissible
         * */
        //if (mBackground_Clicked && otherChatViewHolder.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
        //otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
        //}

        /**
         * set mlinearLayoutMsgBg invisible when focus changed
         * */
        otherChatViewHolder.mlinearLayoutMsgBg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                   // transaction.detach(ToastChildFragment.newInstance(chatTAG, message));
                }
            }
        });

        //When click thumb_up icon
        otherChatViewHolder.mThumb_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherChatViewHolder.mlinearLayout.getVisibility() == View.GONE) {
                    otherChatViewHolder.mlinearLayout.setVisibility(View.VISIBLE);
                }else if (otherChatViewHolder.mlinearLayout.getVisibility() == View.VISIBLE){
                    otherChatViewHolder.mlinearLayout.setVisibility(View.GONE);
                }
            }
        });


        //When click thumb_up icon
        otherChatViewHolder.mThumb_up_inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LIKE_STATUS = 1;
                //likeSatusMap.add(position, LIKE_STATUS);
                updateMessageInDatabase(chat, LIKE_STATUS,position);
                //update database
                v.invalidate();
            }
        });

        //When click thumb_down icon
        otherChatViewHolder.mThumb_dwn_inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LIKE_STATUS = 2;
                //likeSatusMap.add(position, LIKE_STATUS);
                updateMessageInDatabase(chat, LIKE_STATUS, position);
                v.invalidate();
            }
        });

        otherChatViewHolder.mFavourite_inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LIKE_STATUS = 3;
                //likeSatusMap.add(position, LIKE_STATUS);
                updateMessageInDatabase(chat, LIKE_STATUS,position);
                v.invalidate();
            }
        });

        //save to database
    }

    /**
     * Method to update Message in firebase database when the message is edited
     *
     */
    private void updateMessageInDatabase(Chat mchat, int LIKE_STATUS, int position){
        Chat chat = new Chat(mchat.sender, mchat.receiver, mchat.senderUid, mchat.receiverUid, mchat.message, mchat.timestamp, mchat.isRead, LIKE_STATUS);
        mChatPresenter = new ChatPresenter(new ChatContract.View() {
            @Override
            public void onSendMessageSuccess() {}
            @Override
            public void onSendMessageFailure(String message) {}
            @Override
            public void onGetMessagesSuccess(Chat chat) {}
            @Override
            public void onGetMessagesFailure(String message) {}
        });
        mChatPresenter.sendMessage(context, chat, receiverFirebaseTOKEN);//send message to server
        //notifyItemChanged to refresh the recyclerView
        refreshChangedItems(position);
    }

    private void configureChatBeginMessage(ChatBeginMessage chatBeginMessage, int position){
        Chat chat = mpChats.get(position);
        if (mpChats.size() <= 1)
            chatBeginMessage.mtext.setText("");

    }

    private void configureItemDateSeparator(ItemDateSeparator itemDateSeparator, int position){
        Chat chat = mpChats.get(position);

        // itemDateSeparator.mtext.setText(DateFormat.format("dd, MMM", chat.timestamp));
    }

    @Override
    public int getItemCount() {
        if (mpChats != null) {
            return mpChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mpChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
        /*TO DO
        * process each message time to determine if was in the past 24hour*/
    }



    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView    mptxtChatMessage,
                mpMessageTime,
                mpForward,
                mpDelete,
                mpCopy;
        private ImageView   mSendStatusIcon,
                mThumb_up;
        private LinearLayout mlinearLayoutMsgBg,
                mlinearLayoutMsgDialog;
        private  CircleImageView mptxtUserPic;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            mptxtChatMessage    	= (TextView) itemView.findViewById(R.id.text_view_chat_message);
            mptxtUserPic        	= (CircleImageView) itemView.findViewById(R.id.pic_circleView);
            mpMessageTime       	= (TextView)itemView.findViewById(R.id.message_time);
            mSendStatusIcon     	= (ImageView)itemView.findViewById(R.id.icon_send_status);
            mThumb_up           	= (ImageView)itemView.findViewById(R.id.icon_thumb_up);
            mlinearLayoutMsgDialog	= (LinearLayout) itemView.findViewById(R.id.message_dialog_linearlayout);
            mpForward      			= (TextView) itemView.findViewById(R.id.forward_text);
            mpDelete			    = (TextView) itemView.findViewById(R.id.delete_text);
            mpCopy			    	= (TextView) itemView.findViewById(R.id.copy_text);
            mlinearLayoutMsgBg		= (LinearLayout) itemView.findViewById(R.id.linear_layout);

            mlinearLayoutMsgDialog.setVisibility(View.GONE);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView    mptxtChatMessage,
                mpMessageTime,
                mpForward,
                mpDelete,
                mpCopy;
        private ImageView   mThumb_up,
                mThumb_dwn_inner,
                mThumb_up_inner,
                mFavourite_inner;
        private  CircleImageView mptxtUserPic;
        private LinearLayout    mlinearLayout,
                mlinearLayoutMsgBg,
                mlinearLayoutMsgDialog;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            mptxtChatMessage        = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            mptxtUserPic            = (CircleImageView) itemView.findViewById(R.id.pic_circleView);
            mpMessageTime           = (TextView) itemView.findViewById(R.id.message_time);
            mThumb_up               = (ImageView)itemView.findViewById(R.id.icon_thumb_up);
            mThumb_up_inner         = (ImageView)itemView.findViewById(R.id.icon_thumb_up_inner);
            mFavourite_inner        = (ImageView)itemView.findViewById(R.id.icon_favourite_inner);
            mThumb_dwn_inner        = (ImageView) itemView.findViewById(R.id.icon_thumb_down_inner);
            mlinearLayout           = (LinearLayout) itemView.findViewById(R.id.thumb_dialog_linearlayout);
            mlinearLayoutMsgBg		= (LinearLayout) itemView.findViewById(R.id.linear_layout);
            mlinearLayoutMsgDialog	= (LinearLayout) itemView.findViewById(R.id.message_dialog_linearlayout);
            mpForward      			= (TextView) itemView.findViewById(R.id.forward_text);
            mpDelete			    = (TextView) itemView.findViewById(R.id.delete_text);
            mpCopy			    	= (TextView) itemView.findViewById(R.id.copy_text);

            mlinearLayout.setVisibility(View.GONE);
            mlinearLayoutMsgDialog.setVisibility(View.GONE);
        }


    }

    private static class ChatBeginMessage extends RecyclerView.ViewHolder{
        private TextView mtext;
        public ChatBeginMessage(View view){
            super(view);
            mtext   = (TextView)view.findViewById(R.id.chat_begin);
        }
    }

    private static class ItemDateSeparator extends RecyclerView.ViewHolder{
        private TextView mtext;
        public ItemDateSeparator(View view){
            super(view);
            mtext   = (TextView)view.findViewById(R.id.item_separator);
        }
    }

    private void SetAlertDialog(final int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("DELETE").setMessage("Delete the message!")
                .setIcon(context. getResources().getDrawable(R.drawable.delete_red_icon))
                .setPositiveButton("Yes, delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeItem(position);
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

    private void refreshChangedItems(int position) {
        notifyItemChanged(position);
    }

    private void removeItem(int position){
        mpChats.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mpChats.size());

		/*TO DO
		method to remove item from database*/
    }


    private void addItem(int position, Chat chat){
        mpChats.add(position, chat);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mpChats.size());
    }

    /**
     * Save chat history into JSON file on the device*
     * string file path is returned
     */
    private void saveToJSON(List<Chat> data,  String filename){
        final String fileDir = "chatsfile_"+filename ;
        //SaveAsJsonFile saveJsonFile = new SaveAsJsonFile(context);
        try {
            SaveAsJsonFile.saveChatJSONArrayToFile(data, fileDir);
        }catch (Exception e){
            Log.e(TAG,"Error Saving FriendsList saveToJSON ", e);
        }
    }





    /**
     * Method to handle touches outside the text area
     */

    MyChatViewHolder myChatViewHolder1;
    OtherChatViewHolder otherChatViewHolder1;

    private void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof TextView)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    v.performClick();
                    if(myChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE
                            || otherChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){

                        myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                    }
                    //hideSoftKeyboard(MyActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }


}
