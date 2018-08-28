package com.projects.adetunji.letschat.uis.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.LetsChatMainApp;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.HelperFirebaseStorage;
import com.projects.adetunji.letschat.helper.SimpleItemDividerForDecoration;
import com.projects.adetunji.letschat.models.UserProfile;
import com.projects.adetunji.letschat.uis.adapters.RecyclerViewAdapter;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adetunji on 21/01/2018.
 */

public class ProfileFragment extends Fragment{
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ImageView           mProfilePhoto;
    private TextView            mProfileName;
    private TextView            mProfileEmail;
    private TextView            mUserStatus;
    private RecyclerView        recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String              uid;
    private FirebaseUser        user;

    private static final int REQUEST_READ_PERMISSION = 120;
    private SharedPrefUtil sharedPref;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        bindViews(fragmentView);

        getActivity().setTitle("My Profile");

        return fragmentView;
    }

    private void bindViews(View view) {
        mProfileName        = (TextView)view.findViewById(R.id.profile_name);
        mProfileEmail       = (TextView)view.findViewById(R.id.profile_email);
        mProfilePhoto       = (ImageView)view.findViewById(R.id.circleView);
        recyclerView        = (RecyclerView)view.findViewById(R.id.profile_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume(){
        super.onResume();

        List<UserProfile> userData = getSourceData(sharedPref);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), userData);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void init() {
        sharedPref = new SharedPrefUtil(getContext());
        SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(getContext());

        //Get current user data
        FirebaseUser user = ((LetsChatMainApp)getActivity().getApplication()).getFirebaseAuth().getCurrentUser();
        uid = ((LetsChatMainApp)getActivity().getApplication()).getFirebaseUserAuthenticateId();

        final PhotoOptSelectDialogFragment mPhotoSelectOpt= new PhotoOptSelectDialogFragment();
        this.user = user;

        //Display Profile Title and Email
        mProfileEmail.setText(user.getEmail());
        mProfileName.setText(sharedPref.getString(Helper.PREF_USERNAME));

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoSelectOpt.show(getFragmentManager(), "");
                mPhotoSelectOpt.setPhotoInto(mProfilePhoto);
            }
        });

        //mPhotoSelectOpt.setImgView(mProfilePhoto);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleItemDividerForDecoration(getActivity()));


        List<UserProfile> userData = getSourceData(sharedPref);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), userData);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    //------------------------------------------------------------------------------------------
    public List<UserProfile> getSourceData(SharedPrefUtil sharedPref){
        ArrayList<UserProfile> allUserData = new ArrayList<>();

        allUserData.add(new UserProfile(Helper.PREF_FULL_NAME, 		sharedPref.getString(Helper.PREF_FULL_NAME)));
        allUserData.add(new UserProfile(Helper.PREF_EMAIL, 			user.getEmail()));
        allUserData.add(new UserProfile(Helper.PREF_USERNAME, 	    sharedPref.getString(Helper.PREF_USERNAME)));
        allUserData.add(new UserProfile(Helper.PREF_MOBILE_NUMBER, 	sharedPref.getString(Helper.PREF_MOBILE_NUMBER)));
        allUserData.add(new UserProfile(Helper.PREF_BIRTHDAY, 		sharedPref.getString(Helper.PREF_BIRTHDAY)));
        allUserData.add(new UserProfile(Helper.PREF_CURRENT_CITY, 	sharedPref.getString(Helper.PREF_CURRENT_CITY)));
        allUserData.add(new UserProfile(Helper.PREF_RELATIONSHIP, 	sharedPref.getString(Helper.PREF_RELATIONSHIP)));
        allUserData.add(new UserProfile(Helper.PREF_HOBBY_INTEREST, sharedPref.getString(Helper.PREF_HOBBY_INTEREST)));

        return allUserData;
    }

    //--------------------------------------------------------------------------------------------
    //Check user permission and save profile picture to external storage
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Helper.SELECT_PICTURE) {

            Uri profileImageUri = data.getData();
            String imagePath = getPath(profileImageUri);

            HelperFirebaseStorage storageHelper = new HelperFirebaseStorage(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_PERMISSION);
                return;
            }
            //storageHelper.saveProfileImageToCloud(uid, profileImageUri, mProfilePhoto);
        }
    }

    //get directory path of the photo
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Helper.displayToastMessage(getActivity(), "Permission needed!");
            }
        }
    }
}
