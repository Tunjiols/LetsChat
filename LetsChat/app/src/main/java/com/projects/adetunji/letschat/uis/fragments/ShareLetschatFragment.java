package com.projects.adetunji.letschat.uis.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by adetunji on 02/02/2018.
 */

public class ShareLetschatFragment extends Fragment{
    private TextView    mInviteAfriend;
    private LinearLayout    linearLayout;
    private TextView    title;
    private ListView    listView;

    private ArrayList<String> invit_list;

    private boolean toggle = true;


    public static ShareLetschatFragment newInstance() {
        Bundle args = new Bundle();
        ShareLetschatFragment mFragment = new ShareLetschatFragment();
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mFragmentView = inflater.inflate(R.layout.share_letschat_fragment, container, false);
        bindViews(mFragmentView);

        getActivity().setTitle("Share LetsChat");

        return mFragmentView;
    }

    private void bindViews(View fragmentView){
        mInviteAfriend				= (TextView)  fragmentView.findViewById(R.id.invite);
        title                       = (TextView) fragmentView.findViewById(R.id.title) ;

        linearLayout                = (LinearLayout) fragmentView.findViewById(R.id.linear_layout);
        listView                    = (ListView) fragmentView.findViewById(R.id.list_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){

        linearLayout.setVisibility(View.GONE);

        invit_list = new ArrayList<>();
        invit_list.add("Facebook");

        final String shareSubject = "You are invited to download 'LetsChat' ";
        final String shareBody	= "Start chatting for free. http://letschat.com/download_letschat";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, invit_list);
        listView.setAdapter(adapter);

        mInviteAfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInviteAfriend.setBackgroundColor(getResources().getColor(R.color.teal_100));
                shareIntent(shareSubject, shareBody);
            }
        });


        //set click listener to items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listView.getItemAtPosition(position).toString();
            }
        });
    }

    private void shareIntent(String shareSubject, String shareBody){
        String[] extrasEmail = {"user5@test.com"};//for testing

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, extrasEmail);
        //shareIntent.putExtra(android.content.Intent.EXTRA_CC, email );

        PackageManager packageManager = getContext(). getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
        //PackageManager.resolveContentProvider();
        //getActivityIcon(Intent intent)
        //getApplicationIcon(ApplicationInfo info)
        //List<PackageInfo> list = getInstalledPackages(PackageManager.MATCH_DEFAULT_ONLY); Return a List of all packages that are installed on the device.
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_via)));
        }
    }
}
