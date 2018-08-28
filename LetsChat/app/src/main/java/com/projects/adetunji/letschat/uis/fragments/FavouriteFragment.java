package com.projects.adetunji.letschat.uis.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.users.getall.GetUsersContract;
import com.projects.adetunji.letschat.core.users.getall.GetUsersPresenter;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.SimpleItemDividerForDecoration;
import com.projects.adetunji.letschat.models.UserData;
import com.projects.adetunji.letschat.models.UserEntity;
import com.projects.adetunji.letschat.uis.activities.ChatActivity;
import com.projects.adetunji.letschat.uis.adapters.UserListingRecyclerAdapter;
import com.projects.adetunji.letschat.utils.ItemClickSupport;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SaveAsJsonFile;

import java.util.List;

/**
 * Created by adetunji on 08/02/2018.
 */

public class FavouriteFragment extends Fragment implements GetUsersContract.View,
        ItemClickSupport.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = FavouriteFragment.class.getSimpleName();

    private RecyclerView mRecyclerViewFavouriteListing;
    private UserListingRecyclerAdapter mUserListingRecyclerAdapter;
    private GetUsersPresenter mGetUsersPresenter;
    private static List<UserData> mfavouriteList;


    public static FavouriteFragment newInstance(List<UserData> favouriteList) {
        Bundle args = new Bundle();
       // args.putString(Constants.ARG_TYPE, type);
        FavouriteFragment fragment = new FavouriteFragment();
        fragment.setArguments(args);
        mfavouriteList = favouriteList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_favourite, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mRecyclerViewFavouriteListing     = (RecyclerView) view.findViewById(R.id.recycle_view_favourite_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        getActivity().setTitle("Favourites");
        mGetUsersPresenter = new GetUsersPresenter(this);
        getUsersfromSavedJSONfile();

        mRecyclerViewFavouriteListing.addItemDecoration(new SimpleItemDividerForDecoration(getActivity()));
        ItemClickSupport.addTo(mRecyclerViewFavouriteListing).setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        getUsersfromSavedJSONfile();
    }

    /**
     *	Retrieves saved users list from JSON file
     */
    private void getUsersfromSavedJSONfile() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        SaveAsJsonFile saveAsJsonFile = new SaveAsJsonFile(getContext());
        List<UserEntity> usersEnt = saveAsJsonFile.loadUsersEntity(uid);


       // try {
            //mfavouriteList = saveAsJsonFile.objectFromFile("favouritelist_" + uid);
       // } catch (Exception e) {
       //     Log.e(TAG, e.toString());
       // }

        List<UserEntity> favouriteUsers = null;

        for (UserData data : mfavouriteList) {
            for (UserEntity user : usersEnt) {
                if (data.getUid().equals( user.mgetuId()))
                   favouriteUsers.add(user);
           }
        }

       // if (favouriteUsers != null) {
            mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(favouriteUsers);
            mRecyclerViewFavouriteListing.setAdapter(mUserListingRecyclerAdapter);
            mUserListingRecyclerAdapter.notifyDataSetChanged();
       // }
    }


    private void getUsers() {
        if (TextUtils.equals(getArguments().getString(Constants.ARG_TYPE), Constants.TYPE_FAVOURITE)) {

        }
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
    public void onGetAllUsersSuccess(List<UserEntity> users) {

    }

    @Override
    public void onGetAllUsersFailure(String message) {

    }

    @Override
    public void onGetChatUsersSuccess(List<UserEntity> users) {

    }

    @Override
    public void onGetChatUsersFailure(String message) {

    }
}
