package com.projects.adetunji.letschat.uis.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.projects.adetunji.letschat.uis.fragments.CameraFragment;
import com.projects.adetunji.letschat.uis.fragments.ContactsFragment2;
import com.projects.adetunji.letschat.uis.fragments.UsersFragment;
import com.projects.adetunji.letschat.utils.Constants;

/**
 * Created by adetunji on 07/01/2018.
 */

public class UserListingPagerAdapter extends FragmentPagerAdapter {

    private static final String[] sTitles = new String[]{"Chats Room"/* "All Users"*/, "Contacts", "Camera"};

    private static final Fragment[] sFragments = new Fragment[]{
            /*UsersFragment.newInstance(UsersFragment.TYPE_CHATS),*/
            UsersFragment.newInstance(Constants.TYPE_ALL),
            ContactsFragment2.newInstance(),
            CameraFragment.newInstance() };


    public UserListingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return sFragments[position];
    }

    @Override
    public int getCount() {
        return sFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sTitles[position];
    }
}
