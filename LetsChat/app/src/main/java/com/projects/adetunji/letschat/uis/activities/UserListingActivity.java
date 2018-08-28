package com.projects.adetunji.letschat.uis.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.LetsChatMainApp;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.core.logout.LogoutContract;
import com.projects.adetunji.letschat.core.logout.LogoutPresenter;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.HelperFirebaseDatabase;
import com.projects.adetunji.letschat.uis.adapters.UserListingPagerAdapter;
import com.projects.adetunji.letschat.uis.fragments.ContactsFragment;
import com.projects.adetunji.letschat.uis.fragments.PhotoOptSelectDialogFragment;
import com.projects.adetunji.letschat.utils.PermissionUtil;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

import java.util.Date;

/**
 * Created by adetunji on 05/01/2018.
 */

public class UserListingActivity extends AppCompatActivity implements LogoutContract.View,
        NavigationView.OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private			View			main_layout;
    private         Toolbar         mpToolbar;
    private         TabLayout       mpTabLayoutUserListing;
    private         ViewPager       mpViewPagerUserListing;
    private         NavigationView  navigationView;
    private         LogoutPresenter mpLogoutPresenter;
    private static  Uri             profileImageUri;
    private         SharedPrefUtil  sharedPref;
    private         String          username;
    private         String          set_status;
    private	static 	boolean			contacts_permission = false;
    private static boolean          camera_permission = false;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingActivity.class);
        context.startActivity(intent);
        profileImageUri = intent.getData();
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
        profileImageUri = intent.getData();
    }

    public void getSupportActionBarMine(){
        getSupportActionBar().hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        init();
    }

    @Override
    protected void onResume(){
        super.onResume();

        long lastSeenDate = new Date().getTime();
        if (lastSeenDate != sharedPref.getLongDate(Helper.LAST_SEEN_DATE)) {
            sharedPref.saveLongDate(Helper.LAST_SEEN_DATE, lastSeenDate);
            sharedPref.saveInt("SYNC_DATA_STATE", 1);
        }

        //Sync data to Firebase Database
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (sharedPref.getInt( "SYNC_DATA_STATE") != 0) {
            HelperFirebaseDatabase database = new HelperFirebaseDatabase();
            database.syncUserDataToFirebaseDtatbase(firebaseUser.getUid(), firebaseUser.getEmail(), getApplicationContext());
        }

        username                = sharedPref.getString(Helper.PREF_USERNAME);
        set_status              = sharedPref.getString(Helper.PREF_STATUS);
    }

    private void bindViews() {
        main_layout				=  findViewById(R.id.main_layout);
        mpToolbar               = (Toolbar) findViewById(R.id.toolbar);
        navigationView          = (NavigationView) findViewById(R.id.navigation_header__view);
        mpTabLayoutUserListing  = (TabLayout) findViewById(R.id.tab_layout_user_listing);
        mpViewPagerUserListing  = (ViewPager) findViewById(R.id.view_pager_user_listing);

    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mpToolbar);
        long lastSeenDate = new Date().getTime();
        sharedPref = new SharedPrefUtil(getApplicationContext());

        if (lastSeenDate != sharedPref.getLongDate(Helper.LAST_SEEN_DATE)) {
            sharedPref.saveLongDate(Helper.LAST_SEEN_DATE, lastSeenDate);
            sharedPref.saveInt("SYNC_DATA_STATE", 1);
        }
        //navigationView.setVisibility(View.GONE);

        navigationView.setNavigationItemSelectedListener(this);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        updateNavHeaderView(firebaseUser, profileImageUri);

        //setup for drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mpToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getSupportFragmentManager());
        mpViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mpTabLayoutUserListing.setupWithViewPager(mpViewPagerUserListing);
        mpLogoutPresenter = new LogoutPresenter(this);

        //Request for permissions
        showContacts();
        showCamera();
    }


    private void updateNavHeaderView(FirebaseUser firebaseUser, Uri profileImageUri){

        String uid = firebaseUser.getUid();

        username                = sharedPref.getString(Helper.PREF_USERNAME);
        set_status              = sharedPref.getString(Helper.PREF_STATUS);

        View headerView                 = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageView mProfilePhoto         = (ImageView) headerView.findViewById(R.id.navigate_display_pic);
        TextView displayName            = (TextView) headerView.findViewById(R.id.navigate_display_name);
        TextView status                 = (TextView) headerView.findViewById(R.id.navigate_status);
        TextView displayEmail           = (TextView) headerView.findViewById(R.id.navigate_display_email);

        LetsChatMainApp.getUserDataFromDatabase(uid, getApplicationContext());
        displayEmail.setText(firebaseUser.getEmail());
        displayName.setText(username);
        status.setText(set_status);

        /*Set click listener to the profile picture on navigation view*/
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralActivity.startIntent(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK, Helper.FRAGMENT_FLAG_PROFILE);
            }
        });

        //String imagePath = getPath(profileImageUri);
        PhotoOptSelectDialogFragment mPhotoSelectOpt= new PhotoOptSelectDialogFragment();

        if (mPhotoSelectOpt.getImgView() != null) {
            mPhotoSelectOpt.setPhotoInto(mProfilePhoto);

            //Picasso.with(this).load(Bitmap.Config.BASE_IMAGE_URL+"/"+imagePath)
            //        .placeholder(R.drawable.profile_picture)
            //        .resize(avatarSize, avatarSize)
            //        .centerCrop()
            //        .transform(new CircleTransformation())
            //        .into(navigateProfilePic);
        } else {
            mProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.profile_picture));
        }
    }

    @Override
    public void onLogoutSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(this, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_listing, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setSubmitButtonEnabled(true);
        //friendListView.setTextFilterEnabled(true);
        //searchView.setOnQueryTextListener(this);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                // filter recycler view when query submitted
                // mAdapter.getFilter().filter(queryText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQueryText) {
                // filter recycler view when text is changed
                // mAdapter.getFilter().filter(newQueryText);
                return false;
            }
        });
 /*
        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener =
                new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };
        // Get the MenuItem for the action item
        MenuItem actionMenuItem = menu.findItem(R.id.action_share);

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);

        // Any other things you have to do when creating the options menu…
        */
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigate_change_profile_pic) {
            // Handle the camera action
            ProfilesActivity.startActivity(this, Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (id == R.id.navigate_Favourite) {
            GeneralActivity.startIntent(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK, Helper.FRAGMENT_FLAG_FAVOURITE);
        }else if (id == R.id.navigate_new_group) {
            GeneralActivity.startIntent(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK, Helper.FRAGMENT_FLAG_NEWGROUP);
        }
        else if (id == R.id.navigate_addFriends) {
            GeneralActivity.startIntent(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK, Helper.FRAGMENT_FLAG_ADDFRIEND);
        } else if (id == R.id.navigate_letschat) {
            GeneralActivity.startIntent(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK, Helper.FRAGMENT_SHARE_LETSCHAT);
        } else if (id == R.id.navigate_settings) {
            GeneralActivity.startIntent(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK, Helper.FRAGMENT_FLAG_SETTINGS);
        } else if (id == R.id.navigate_logout) {
            new AlertDialog.Builder(this).setTitle(R.string.logout).setMessage(R.string.are_you_sure).setPositiveButton(R.string.logout,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mpLogoutPresenter.logout();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //==================================================================================================
    public static boolean getContactPermission(){
        return contacts_permission;
    }

    public static  boolean getCameraPermission(){
        return camera_permission;
    }

    private static final String TAG = com.projects.adetunji.letschat.utils.ContactsAndCameraUtil.class.getSimpleName();
    private static final int REQUEST_CAMERA = 0;	//Id to identify a camera permission request.
    private static final int REQUEST_CONTACTS = 1;//Id to identify a contacts permission request.

    /**
     * Permissions required to read and write contacts. Used by the {@link ContactsFragment}.
     */
    private static String[] PERMISSIONS_CONTACT = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};


    /**
     * Called when moved to Camera.
     */
    public void showCamera( ) {
        Log.i(TAG, "Checking Camera permission.");
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted. request camera permission
            requestCameraPermission();
        } else {
            // Camera permissions is already available, show the camera preview.
            Log.i(TAG,"Displaying camera preview.");
            //return true to show CameraPreview();
            camera_permission = true;
        }
    }

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG, "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(main_layout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(UserListingActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
            }).show();

        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);

        }
    }

    /**
     * Called to show contacts
     */
    public void showContacts() {
        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            requestContactsPermissions();
            Log.i(TAG, "Requesting Contact permissions.");

        } else {
            // Contact permissions have been granted. Show the contacts fragment.
            Log.i(TAG, "Displaying contact details.");
            contacts_permission = true; //return true to show Contact Details();
        }
    }

    /**
     * Requests the Contacts permissions.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestContactsPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_CONTACTS)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG, "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(main_layout, R.string.permission_contacts_rationale,Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(UserListingActivity.this, PERMISSIONS_CONTACT,REQUEST_CONTACTS);
                        }
                    }).show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for Camera permission request.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, camera can be displayed
                camera_permission = true;
                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
                Snackbar.make(main_layout, "Camera permission Available", Snackbar.LENGTH_SHORT).show();
            } else {
                camera_permission = false;
                Log.i(TAG, "CAMERA permission was NOT granted.");
                Snackbar.make(main_layout, "Camera permission not granted", Snackbar.LENGTH_LONG).show();
            }

        } else if (requestCode == REQUEST_CONTACTS) {
            Log.i(TAG, "Received response for contact permissions request.");
            // We have requested multiple permissions for contacts, so all of them need to bechecked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                contacts_permission = true;
                Snackbar.make(main_layout, "Contact permission available", Snackbar.LENGTH_SHORT).show();
            } else {
                contacts_permission = false;
                Log.i(TAG, "Contacts permissions were NOT granted.");
                Snackbar.make(main_layout, "Contact permissions not granted", Snackbar.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onBackClick(View view) {
        getSupportFragmentManager().popBackStack();
    }
}


