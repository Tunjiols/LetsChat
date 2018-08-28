package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.users.getall.GetUsersContract;
import com.projects.adetunji.letschat.core.users.getall.GetUsersPresenter;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.RecyclerViewHolders;
import com.projects.adetunji.letschat.helper.SimpleItemDividerForDecoration;
import com.projects.adetunji.letschat.models.UserData;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.uis.adapters.UserListingRecyclerAdapter;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;
import com.projects.adetunji.letschat.utils.SendMessageClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by adetunji on 25/02/2018.
 */

public class FriendListActivity extends AppCompatActivity implements GetUsersContract.View{
    private static String TAG = FriendDetailsActivity.class.getSimpleName();
    private Button          mButton;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GetUsersPresenter mGetUsersPresenter;
    private RecyclerViewAdapter recyclerViewAdapter;
    private TextView selectedItemCountV;
    private TextView selectionTitle;
    private FirebaseUser user;
    private List<UserEntity> friend_list;
    //private boolean toggle = true;

    public static void startIntent(Context context, int flag, String TAG1){
        Intent intent = new Intent(context, FriendListActivity.class);
        intent.setFlags(flag);
        intent.putExtra(TAG, TAG1);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.friend_list_activity);
        bindView();
        init();
    }

    private void bindView(){
        mButton             = (Button)findViewById( R.id.button);
        selectedItemCountV  = (TextView)findViewById(R.id.selectedItemCount);
        selectionTitle      = (TextView)findViewById(R.id.selectTitle);
        recyclerView        = (RecyclerView) findViewById(R.id.friend_list);
    }

    private void init(){
        SaveAsJsonFile  saveAsJsonFile =  new SaveAsJsonFile(FriendListActivity.this);
        friend_list = new ArrayList<>();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        friend_list.addAll(saveAsJsonFile.loadUsersEntity(uid));

        mGetUsersPresenter = new GetUsersPresenter(this);
        linearLayoutManager = new LinearLayoutManager(FriendListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleItemDividerForDecoration(FriendListActivity.this));

        recyclerViewAdapter = new RecyclerViewAdapter(FriendListActivity.this, friend_list);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();


        List<UserData> friends_added_togroup = recyclerViewAdapter.friends_added_togroup;

        switch(getIntent().getExtras().getString(TAG)){
            case  "ChatRecyclerAdapter":
                selectionTitle.setText("Select receiver(s) ");
                if(friends_added_togroup != null){
                    for(UserData data: friends_added_togroup){
                      /*  SendMessageClass.forwardMessage(getBaseContext(),
                                message,
                                senderemail,
                                senderUid,
                                data.getEmail(),
                                data.getUid(),
                                data.getFirebaseToken()
                        );*/
                        if(SendMessageClass.isMessageSent()) {
                            Helper.displayToastMessage(getBaseContext(), "Message forwarded");
                        }
                    }
                }
                break;

            case "NewGroupFragment":
                selectionTitle.setText("Select Group members");
                //TO DO
                //get the name of the new created group to add to filename
                if(friends_added_togroup != null){
                    try {
                        //SaveAsJsonFile.saveObjectToFile(friends_added_togroup, "chat_group_newgroupname");
                    }catch (Exception e){

                    }

                }
                break;
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetUsersPresenter.getAllUsers();
                finish();
            }
        });

    }



    @Override
    public void onGetAllUsersSuccess(List<UserEntity> users) {

        UserListingRecyclerAdapter mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        for (int i = 0; i < users.size(); i++) {
            //friend_list.add(mUserListingRecyclerAdapter.getUser(1).mgetUsername());
        }
        Helper.displayToastMessage(FriendListActivity.this, "get users successful");

    }

    @Override
    public void onGetAllUsersFailure(String message) {
        Helper.displayToastMessage(FriendListActivity.this, "Error: " + message);
    }

    @Override
    public void onGetChatUsersSuccess(List<UserEntity> users) {}

    @Override
    public void onGetChatUsersFailure(String message) {}


    //Private classes
    private class ViewHolder extends RecyclerView.ViewHolder{
        public TextView        mFriend, mFriend_email,mFriend_status;
        public CheckBox        mCheckBox;
        public CircleImageView mProfile_img;


        public ViewHolder(final View itemView){
            super(itemView);
            mFriend 	    = (TextView)itemView.findViewById(R.id.friend_name);
            mFriend_email 	= (TextView)itemView.findViewById(R.id.friend_email);
            mFriend_status 	= (TextView)itemView.findViewById(R.id.friend_status);
            mCheckBox	    = (CheckBox)itemView.findViewById(R.id.checkbox);
            mProfile_img    = (CircleImageView) itemView.findViewById(R.id.profile_picture_circleView);
        }
    }

    /**
     * RecyclerViewAdapter Class
     *
     *  */
    private class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        protected Context context;
        public List<UserEntity> freind_list_Recycler;
        public List<UserData> friends_added_togroup = new ArrayList<>();



        public int selectedItemCount = 0;

        public RecyclerViewAdapter(Context context, List<UserEntity> friend_list){
            this.context 	    = context;
            this.freind_list_Recycler 	= friend_list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewholder = null;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list, parent, false);
            viewholder = new ViewHolder(view);
            return  viewholder;
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder,  int position) {
            holder.mFriend.setText(freind_list_Recycler.get(position).mgetUsername());
            holder.mFriend_email.setText(freind_list_Recycler.get(position).mgetEmail());
            holder.mFriend_status.setText(freind_list_Recycler.get(position).mgetStatus());
            holder.mCheckBox.setChecked(false);
            //holder.mProfile_img.setImageBitmap( );

            final int positions = position;
            holder.getAdapterPosition();

            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    UserData data1 = new UserData(freind_list_Recycler.get(positions).mgetEmail(),
                            freind_list_Recycler.get(positions).mgetUsername(),
                            freind_list_Recycler.get(positions).mgetuId(),
                            freind_list_Recycler.get(positions).mgetFirebaseToken());

                    if ( holder.mCheckBox.isChecked()){
                        //holder.mCheckBox.setChecked(true);
                        friends_added_togroup.add(data1);
                        selectedItemCount = friends_added_togroup.size();
                    }else {
                        if(friends_added_togroup != null){
                            for(int i = 0; i <= friends_added_togroup.size();i++ ){
                                if(friends_added_togroup.get(i).getEmail() == freind_list_Recycler.get(positions).mgetEmail() ) {
                                    if (friends_added_togroup.size() != 0) {
                                        friends_added_togroup.remove(i);
                                        selectedItemCount = friends_added_togroup.size();
                                    }
                                }
                            }
                        }
                    }
                    selectedItemCountV.setText(String.valueOf( recyclerViewAdapter.getSelectedItemCount()));
                   // recyclerView.notify();
                }
            });


        }

        public int getSelectedItemCount() {
            return selectedItemCount;
        }

        @Override
        public int getItemCount() {
            if (freind_list_Recycler != null) {
                return this.freind_list_Recycler.size();
            }else return 0;
        }

    }
}
