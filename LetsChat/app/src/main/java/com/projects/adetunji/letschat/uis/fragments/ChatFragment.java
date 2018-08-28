package com.projects.adetunji.letschat.uis.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.chat.ChatContract;
import com.projects.adetunji.letschat.core.chat.ChatPresenter;
import com.projects.adetunji.letschat.helper.InterfaceChat;
import com.projects.adetunji.letschat.models.Chat;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.uis.activities.FriendListActivity;

import com.projects.adetunji.letschat.uis.adapters.PushNotificationEvent;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.ItemClickSupport;
import com.projects.adetunji.letschat.utils.NetworkConnectionUtil;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by adetunji on 10/01/2018.
 */

public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    private static final String TAG = " ChatFragment";
    private RecyclerView        mRecyclerViewChat;
    private EditText            mETxtMessage;
    private ImageView           mInEditTextIcon;
    private ImageView           mSendIcon;
    private ProgressDialog      mProgressDialog;
    private SwipeRefreshLayout  mSwipeRefreshLayout;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private ChatPresenter       mChatPresenter;
    boolean toggle = false;
    boolean background_clicked = false;
    private Toast mtoast;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);

        ChatFragment fragment = new ChatFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewChat   = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage        = (EditText) view.findViewById(R.id.edit_text_message);
        mInEditTextIcon		= (ImageView) view.findViewById(R.id.in_editText_icon);
        mSendIcon		    = (ImageView) view.findViewById(R.id.send_icon);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();



        final int DELAY_TIMER = 2000;//delay in millisecond
        Handler mHandler = new Handler();
        Runnable mRunable = new Runnable(){
            @Override
            public void run(){
                if (getContext() != null) {
                    if (!NetworkConnectionUtil.isConnectedToInternet(getContext())) {
                        mtoast = NetworkToast("not");
                        mtoast.show();
                    } else if (NetworkConnectionUtil.isConnectedToInternet(getContext())) {
                        mtoast = NetworkToast("connected");
                        mtoast.show();
                    }
                }
            }
        };

        mHandler.postDelayed(mRunable, DELAY_TIMER);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

        if(mtoast != null){
            mtoast.cancel();
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if(mtoast != null){
            mtoast.cancel();
        }
        //implement network service here to prevent memory leak when the activity
        //is destroyed before network connection completed.
    }

    private void init() {

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mETxtMessage.setOnEditorActionListener(this);

        mChatPresenter = new ChatPresenter(this);
        mSendIcon.setVisibility(View.GONE);

        /**
         * Get Messages from firebase/JSON
         * */

        getAllMessages();
        //OnGetMessageFromJSON();

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                //OnGetMessageFromJSON();
                //getAllMessages();
                mSwipeRefreshLayout.setRefreshing(false);
                return true;
            }

        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.canScrollVertically();

        OnInitClickListensers(); // all clickListeners
    }


    private void OnInitClickListensers(){
        //EditText Chang Listener
        mETxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){

            }

            @Override
            public void onTextChanged ( final CharSequence s, int start, int before, int count){
                mInEditTextIcon.setVisibility(View.GONE);
                mSendIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged ( final Editable s){
                String message = mETxtMessage.getText().toString().trim();
                if(TextUtils.isEmpty(message) ){
                    mSendIcon.setVisibility(View.GONE);
                    mInEditTextIcon.setVisibility(View.VISIBLE);
                }
            }
        });

        mSendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mETxtMessage.getText().toString().trim();

                if(!TextUtils.isEmpty(message) ){

                    if(!NetworkConnectionUtil.isConnectedToInternet(getContext())){

                        NetworkConnectionUtil.showNoInternetAvailableErrorDialog(getContext());
                    }
                    sendMessage(message);
                    mETxtMessage.setText("");
                    mSendIcon.setVisibility(View.GONE);
                    mInEditTextIcon.setVisibility(View.VISIBLE);
                }
            }
        });

        mInEditTextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mRecyclerViewChat.setSelection(mChatRecyclerAdapter.getCount()-1);

                toggle = !toggle;
                if (!toggle) {
                    mInEditTextIcon.setImageResource(R.drawable.ic_keyboard_black_24dp);
                }else { mInEditTextIcon.setImageResource(R.drawable.smile_emoji);}

            }
        });

    }


    @Override
    public void onRefresh() {

    }
    /**
     * Called to get message from Firebase
     *
     */
    private void getAllMessages(){
        //Get messages from database
        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
    }


    /**
     * Called to get message from JSON
     *
     * @param
     */
    private void OnGetMessageFromJSON(){
        //get messages from JSON
        Chat chat = getMessageFromJSON(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID) );

        createChatRecyclerAdapter(chat);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {

            String message = mETxtMessage.getText().toString().trim();

            if(!TextUtils.isEmpty(message) ){

                if(!NetworkConnectionUtil.isConnectedToInternet(getContext())){
                    NetworkConnectionUtil.showNoInternetAvailableErrorDialog(getContext());
                }
                sendMessage(message);
                mETxtMessage.setText("");
                mSendIcon.setVisibility(View.GONE);
                mInEditTextIcon.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return false;
    }

    private void CheckInternetConnections(){
        //----------------------------------------------------------------------------------------
        final int DELAY_TIMER = 3000;//delay in millisecond
        Handler mHandler = new Handler();
        Runnable mRunable = new Runnable(){
            boolean not = false; boolean connected = false;
            @Override
            public void run(){
                if(!NetworkConnectionUtil.isConnectedToInternet(getContext())){
                    InsertInternetConnectionStatusFragment("not");
                    not = true;
                }else if(NetworkConnectionUtil.isConnectedToInternet(getContext())){
                    InsertInternetConnectionStatusFragment("connected");
                    connected = true;
                }else if((not ) && NetworkConnectionUtil.isInternetConnected(getContext())){
                    InsertInternetConnectionStatusFragment("connecting");
                    not = false; connected = false;
                }
                //finish();
            }
        };
        Runnable mRunable2 = new Runnable(){
            @Override
            public void run(){
                if(NetworkConnectionUtil.isInternetConnected(getContext())){
                    InsertInternetConnectionStatusFragment("");
                }
            }
        };
        mHandler.postDelayed(mRunable, DELAY_TIMER);
        // mHandler.postAtTime(mRunable,InsertInternetConnectionStatusFragment(""), 500);
        //-------------------------------------------------------------------------------
    }

    private void sendMessage(String message) {
        boolean isRead = false;
        int LIKE_STATUS = 0;

        //String message = mETxtMessage.getText().toString();
        String receiver                 = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid              = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender                   = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String senderUid                = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverFirebaseToken    = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);

        Chat chat = new Chat(sender, receiver, senderUid, receiverUid, message, System.currentTimeMillis(), isRead, LIKE_STATUS);

        /*TO DO
        * write json file that takes and store the messages before sending to server
        * input ArrayList is Chat
        * output will be Chat object
        * send message to firebase*/
        mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat, receiverFirebaseToken);//send message to server
    }

    @Override
    public void onSendMessageSuccess() {
       /*TO DO
		* show the sent icon
		* set Chat isSend = true*/
    }

    @Override
    public void onSendMessageFailure(String message) {

        /* TO DO
        * get the saves Json message
        * add it to recycler adapter to display
        * show not send icon
        * set Chat,isSend = false
        * check internet connection
        * when connection comes back, resend the message
        * */
        Helper.displayToastMessage(getContext(), message);
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        createChatRecyclerAdapter(chat);

        /* TO DO
         * check Chat.LIKE_STATUS (values can be 0 1 2 3)
         * if set display the icon respectively
         * implement item click liistenner to select the message for forwarding
         * backup the whole messages, but ask for user permission first*/

    }

    @Override
    public void onGetMessagesFailure(String message) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void createChatRecyclerAdapter(Chat chat){
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>(),
                    getArguments().getString(Constants.ARG_FIREBASE_TOKEN), getContext());
        }

        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);

        mRecyclerViewChat.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
            }
        });

        // mRecyclerViewChat.setSelection(mChatRecyclerAdapter.getCount()-1);
        // mRecyclerViewChat.notifyAll();
        // mChatRecyclerAdapter.mtxtChatMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(
        //         null, null, getContext().getResources().getDrawable(R.drawable.all_done_icon), null);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        background_clicked = true;
    }

    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }

    // Embeds the child InternetConnectionStatus fragment dynamically
    private void InsertInternetConnectionStatusFragment(String isInternetConnection) {

        //Fragment internetConnectionStatusFragment = new InternetConnectionStatusFragment();
        InternetConnectionStatusFragment.isNoInternet = isInternetConnection;

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.entry_animatiom, R.anim.exit_animation);
        //transaction.replace(R.id.frame_layout_content_chat, internetConnectionStatusFragment).commit();
        //transaction.add(R.id.chat_relative, InternetConnectionStatusFragment.newInstance())
        transaction.add(R.id.chat_relative, InternetConnectionStatusFragment.newInstance())
                .addToBackStack(InternetConnectionStatusFragment.TAG)
                .commit();
    }

    private Toast NetworkToast(String isInternetConnection){
        LayoutInflater inflater =  LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.internet_connection_status, new ViewGroup(getContext()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        }, false);
        TextView text = (TextView) layout.findViewById(R.id.internet_connection_status);

        switch (isInternetConnection){
            case "not":
                text.setText("Not connected");
                // text.setBackgroundColor(getResources().getColor( R.color.red_800));
                break;
            case "connected":
                text.setText("Connected");
                //text.setBackgroundColor(getResources().getColor( R.color.green_500));
                break;
            case "connecting":
                text.setText("Connecting");
                // text.setBackgroundColor(getResources().getColor( R.color.orange_800));
                break;
        }

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        return toast;
    }

    /**
     * Get chat history from JSON file on the device*
     * string file path is supplied
     */
    private Chat getMessageFromJSON(String usenderUId, String receiverUId ){
        final String fileDir = "chatsfile_"+usenderUId ;
        Chat chat = null;

        try {
            chat = SaveAsJsonFile.getChatJSONArrayFromFile(fileDir, usenderUId, receiverUId );
        }catch (Exception e){
            Log.e(TAG,"Error retrieving FriendsList getMessageFromJSON ", e);
        }
        return chat;
    }










    //==============================================================================================
    //----------------------------------------------------------------------------
    public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
                                                 View.OnFocusChangeListener{
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
        private  Context 		context;
        private Chat 			chat;
        private String 			filePath;


        public ChatRecyclerAdapter(List<Chat> chats,String receiverFirebaseTOKEN, Context context) {

            mpChats = chats;
            this.context = context;
            this.receiverFirebaseTOKEN = receiverFirebaseTOKEN;
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
            saveToJSON(mpChats,  FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        boolean toast_toggle = false;

        /**     MyChatViewHolder Configuration  */
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



            myChatViewHolder.mlinearLayoutMsgBg.setOnFocusChangeListener(this);

            backgClickListener("myChatViewHolder");

            /*myChatViewHolder.mlinearLayoutMsgBg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (toast_toggle){
                        myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        toast_toggle = false;
                    }else{
                        myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.VISIBLE);
                        toast_toggle = true;
                    }
                    return true;
                }
            });
            myChatViewHolder.mlinearLayoutMsgBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toast_toggle){
                        myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                        toast_toggle = false;
                    }

                }
            });
            */

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
                    copyTextTOClipboard(context, message);
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


            /**
             * Called when the background is clicked and the View is vissible
             * */
            // if (mBackground_Clicked && myChatViewHolder.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
            //     myChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
            // }



        }

        MyChatViewHolder myChatViewHolder1;
        OtherChatViewHolder otherChatViewHolder1;

        /**     OtherChatViewHolder Configuration  */
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
                    otherChatViewHolder.mThumb_dwn_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down_frame));
                    otherChatViewHolder.mFavourite_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_frame_png));
                    break;
                case 2:
                    otherChatViewHolder.mThumb_dwn_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down));
                    otherChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down));
                    otherChatViewHolder.mThumb_up_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_frame));
                    otherChatViewHolder.mFavourite_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_frame_png));
                    break;
                case 3:
                    otherChatViewHolder.mFavourite_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_png));
                    otherChatViewHolder.mThumb_up.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favourite_png));
                    otherChatViewHolder.mThumb_up_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_frame));
                    otherChatViewHolder.mThumb_dwn_inner.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down_frame));
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

            backgClickListener("otherChatViewHolder");

           /* otherChatViewHolder.mlinearLayoutMsgBg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (toast_toggle){
                        otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.VISIBLE);
                    }
                    otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.VISIBLE);
                    if (myChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE)
                        myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);


                    return true;
                }
            });
            otherChatViewHolder.mlinearLayoutMsgBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.linear_layoutother :
                            if (otherChatViewHolder.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
                                otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);}
                            if (myChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
                                myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);}
                            break;
                    }
                }
            });*/
            otherChatViewHolder.mlinearLayoutMsgBg.setOnFocusChangeListener(this);

            // v.setBackgroundColor(context.getResources().getColor(R.color.grey_400) );//0x0000FF00


            otherChatViewHolder.mpForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FriendListActivity.startIntent(context, Intent.FLAG_ACTIVITY_NEW_TASK, TAG);
                    otherChatViewHolder.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                }
            });

            otherChatViewHolder.mpCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyTextTOClipboard(context, message);
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



            /**
             *
             * When click thumb_up icon
             *
             */

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

        private void backgClickListener(String chatTAG){
            if(chatTAG.equals("myChatViewHolder")){
               // mBodyBg.setBackground(getResources().getDrawable(R.drawable.forward_mine_dialog_background));
                String TAG = "myChatViewHolder";

            }else if(chatTAG.equals("otherChatViewHolder")){
               // mBodyBg.setBackground(getResources().getDrawable(R.drawable.forward_dialog_background));
                String TAG = "otherChatViewHolder";

            }
            mRecyclerViewChat.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    switch (v.getId()){
                        case R.id.linear_layout:
                            v.findViewById(R.id.message_dialog_linearlayout).setVisibility(View.VISIBLE);
                            if (v.findViewById( R.id.message_dialog_linearlayoutOther).getVisibility() == View.VISIBLE)
                                v.findViewById( R.id.message_dialog_linearlayoutOther).setVisibility(View.GONE);
                            break;
                        case R.id.linear_layoutother:
                            v.findViewById(R.id.message_dialog_linearlayoutOther).setVisibility(View.VISIBLE);
                            if (v.findViewById( R.id.message_dialog_linearlayout).getVisibility() == View.VISIBLE)
                                v.findViewById( R.id.message_dialog_linearlayout).setVisibility(View.GONE);
                            break;
                    }

                    return true;
                }
            });

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

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        /*@Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.linear_layoutother :
                    if (otherChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
                        otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);}
                    if (myChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
                        myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);}
                    break;

                case R.id.linear_layout:
                    if (otherChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
                        otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);}
                    if (myChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE){
                        myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);}
                    break;
                //default: transaction.detach(ToastChildFragment.newInstance(getContext(), chatTAG, chatTAG));
            }
        }*/

        /**
         * Called when a view has been clicked and held.
         *
         * @param v The view that was clicked and held.
         * @return true if the callback consumed the long click, false otherwise.
         */
        /*@Override
        public boolean onLongClick(View v) {

            switch (v.getId()){
                case R.id.linear_layoutother :
                    otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.VISIBLE);
                    if (myChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE)
                        myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                    break;

                case R.id.linear_layout:
                    myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.VISIBLE);
                    if (otherChatViewHolder1.mlinearLayoutMsgDialog.getVisibility() == View.VISIBLE)
                        otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                    break;
            }

            return true;
        }*/

        /**
         * Called when the focus state of a view has changed.
         *
         * @param v        The view whose state has changed.
         * @param hasFocus The new focus state of v.
         */
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.linear_layoutother:
                    v.getScrollX();
                    //v.getLocationOnScreen();
                    if (hasFocus) {
                        otherChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                    }
                    break;
                case R.id.linear_layout:
                    if (hasFocus) {
                        myChatViewHolder1.mlinearLayoutMsgDialog.setVisibility(View.GONE);
                    }
                    break;
            }
        }


        private  class MyChatViewHolder extends RecyclerView.ViewHolder {
            private TextView    mptxtChatMessage,
                    mpMessageTime,
                    mpForward,
                    mpDelete,
                    mpCopy;
            private ImageView   mSendStatusIcon,
                    mThumb_up;
            private LinearLayout mlinearLayoutMsgBg,
                    mlinearLayoutMsgDialog;
            private RelativeLayout relativeLayout_mainBody;
            private CircleImageView mptxtUserPic;

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
                relativeLayout_mainBody = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_mainBody);

                mlinearLayoutMsgDialog.setVisibility(View.GONE);
            }
        }

        private  class OtherChatViewHolder extends RecyclerView.ViewHolder {
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
            private RelativeLayout relativeLayout_mainBody;

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
                mlinearLayoutMsgBg		= (LinearLayout) itemView.findViewById(R.id.linear_layoutother);
                mlinearLayoutMsgDialog	= (LinearLayout) itemView.findViewById(R.id.message_dialog_linearlayoutOther);
                mpForward      			= (TextView) itemView.findViewById(R.id.forward_text);
                mpDelete			    = (TextView) itemView.findViewById(R.id.delete_text);
                mpCopy			    	= (TextView) itemView.findViewById(R.id.copy_text);
                relativeLayout_mainBody = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_mainBody);

                mlinearLayout.setVisibility(View.GONE);
                mlinearLayoutMsgDialog.setVisibility(View.GONE);
            }


        }

        private  class ChatBeginMessage extends RecyclerView.ViewHolder{
            private TextView mtext;
            public ChatBeginMessage(View view){
                super(view);
                mtext   = (TextView)view.findViewById(R.id.chat_begin);
            }
        }

        private  class ItemDateSeparator extends RecyclerView.ViewHolder{
            private TextView mtext;
            public ItemDateSeparator(View view){
                super(view);
                mtext   = (TextView)view.findViewById(R.id.item_separator);
            }
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
         *method to copy text to clipboard:
         */
        private void copyTextTOClipboard(Context context, String message) {

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
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

            dialog.setTitle("DELETE").setMessage("Delete the message!")
                    .setIcon(getContext(). getResources().getDrawable(R.drawable.delete_red_icon))
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


        /**
         * Method to handle touches outside the text area
         */

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

}


