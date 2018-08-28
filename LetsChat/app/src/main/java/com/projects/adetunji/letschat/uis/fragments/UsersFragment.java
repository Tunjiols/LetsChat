package com.projects.adetunji.letschat.uis.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.users.getall.GetUsersContract;
import com.projects.adetunji.letschat.core.users.getall.GetUsersPresenter;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.SimpleItemDividerForDecoration;
import com.projects.adetunji.letschat.uis.activities.ChatActivity;
import com.projects.adetunji.letschat.uis.activities.FriendDetailsActivity;
import com.projects.adetunji.letschat.uis.activities.UserListingActivity;
import com.projects.adetunji.letschat.uis.adapters.UserListingRecyclerAdapter;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.ItemClickSupport;
import com.projects.adetunji.letschat.utils.NetworkConnectionUtil;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;

import java.util.List;

/**
 * Created by adetunji on 07/01/2018.
 */

public class UsersFragment extends Fragment implements GetUsersContract.View,
        ItemClickSupport.OnItemClickListener,
        ItemClickSupport.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout          mSwipeRefreshLayout;
    private RecyclerView                mRecyclerViewAllUserListing;
    private UserListingRecyclerAdapter  mUserListingRecyclerAdapter;
    private GetUsersPresenter           mGetUsersPresenter;

    private static String uid;
    private Toast mtoast;


    public static UsersFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_TYPE, type);
        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_users, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout             = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing     = (RecyclerView) view.findViewById(R.id.recycle_view_all_user_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        mGetUsersPresenter = new GetUsersPresenter(this);

        //Get users from database
        getUsers();

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        mRecyclerViewAllUserListing.addItemDecoration(new SimpleItemDividerForDecoration(getActivity()));

        ItemClickSupport.addTo(mRecyclerViewAllUserListing).setOnItemClickListener(this).setOnItemLongClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);


    }

    @Override
    public void onRefresh() {
        getUsers();
        if(mtoast != null){
            mtoast.cancel();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getUsers();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if(mtoast != null){
            mtoast.cancel();
        }
    }

    private void getUsers() {

        //get users from saved JSONfile and apply to recyclerAdapter
        getUsersfromSavedJSONfile();

        //check the internet connection
        if(NetworkConnectionUtil.isConnectedToInternet(getContext())){
            if (TextUtils.equals(getArguments().getString(Constants.ARG_TYPE), Constants.TYPE_CHATS)) {
                mGetUsersPresenter.getChatUsers();
            } else if (TextUtils.equals(getArguments().getString(Constants.ARG_TYPE), Constants.TYPE_ALL)) {
                mGetUsersPresenter.getAllUsers();
            }
        }else{
            //Display a line of "No internet connection"
            mtoast = NetworkToast("not");
            mtoast.show();
        }
    }
    /**
     *	Retrieves saved users list from JSON file
     */
    private void getUsersfromSavedJSONfile(){
        SaveAsJsonFile saveAsJSONfile = new SaveAsJsonFile(getContext());
        List<UserEntity> usersEnt = saveAsJSONfile.loadUsersEntity(uid);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(usersEnt);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ChatActivity.startActivity(getActivity(),
                mUserListingRecyclerAdapter.getUser(position).mgetEmail(),
                mUserListingRecyclerAdapter.getUser(position).mgetuId(),
                mUserListingRecyclerAdapter.getUser(position).mgetUsername(),
                mUserListingRecyclerAdapter.getUser(position).mgetFirebaseToken());
    }

    @Override
    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

        mRecyclerViewAllUserListing.getChildAt(position).setBackgroundColor(getResources().getColor( R.color.grey_300));
        String email    = mUserListingRecyclerAdapter.getUser(position).mgetEmail();
        String status   = mUserListingRecyclerAdapter.getUser(position).mgetStatus();
        String username = mUserListingRecyclerAdapter.getUser(position).mgetUsername();
        String mobile   = mUserListingRecyclerAdapter.getUser(position).mgetPhone();
        long last_seen  = mUserListingRecyclerAdapter.getUser(position).mgetLastSeenDate();
        String uid		= mUserListingRecyclerAdapter.getUser(position).mgetuId();
        String token	= mUserListingRecyclerAdapter.getUser(position).mgetFirebaseToken();
        String fullname	= mUserListingRecyclerAdapter.getUser(position).mgetFullname();

        FriendDetailsActivity.startActivity(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK,
                username, email, status, mobile, last_seen, uid, token, fullname);


        return true;
    }

    @Override
    public void onGetAllUsersSuccess(List<UserEntity> users) {
         /*
	   mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
		*/
        SaveAsJsonFile saveAsJSONfile = new SaveAsJsonFile(getContext());
        saveAsJSONfile.saveUsersEntity(users, uid);
    }

    final String TAG = "UserFragment.";

    @Override
    public void onGetAllUsersFailure(String message) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Log.e(TAG, "onGetAllUsersFailure: "  + " failed to get user list from database");
    }

    @Override
    public void onGetChatUsersSuccess(List<UserEntity> users) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();

        SaveAsJsonFile saveAsJSONfile = new SaveAsJsonFile(getContext());
        saveAsJSONfile.saveUsersEntity(users, uid);
    }

    @Override
    public void onGetChatUsersFailure(String message) {

    }
    /**
     * Display a customized Toast of Internet connection at the Top of the screen
     *
     */
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
                //text.setBackgroundColor(getResources().getColor( R.color.red_800));
                break;
            case "connected":
                text.setText("Connected");
                //text.setBackgroundColor(getResources().getColor( R.color.green_500));
                break;
            case "connecting":
                text.setText("Connecting");
                //text.setBackgroundColor(getResources().getColor( R.color.orange_800));
                break;
        }

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        return  toast;
    }

    public static class PopUpFragment extends Fragment{
        LayoutInflater inflater =  LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.internet_connection_status, new ViewGroup(getContext()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        }, false);
        TextView text = (TextView) layout.findViewById(R.id.internet_connection_status);

    }
}
/*FragmentTransaction ft = getFragmentManager().beginTransaction()
ft.attach(fragment).commit();

 getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_container, listFragment)
                .commit();

*/