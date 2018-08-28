package com.projects.adetunji.letschat.uis.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.StringModel;
import com.projects.adetunji.letschat.uis.activities.FriendListActivity;
import com.projects.adetunji.letschat.uis.adapters.StringModelViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by adetunji on 25/02/2018.
 */

public class NewGroupFragment extends Fragment implements View.OnClickListener{
    private final String TAG = "NewGroupFragment";

    private ListView mListView;
    private TextView    mNewgroup, mNot_in_group;
    private RelativeLayout relativeLayout;
    private EditText mNewgroupEdittxt;
    private Button  addGroupBtn;
    private Button  addFriendTogoupBtn;

    private ArrayList<StringModel> chat_grouplist;
    private StringModelViewAdapter adapter;
    private boolean toggle = true;


    public static NewGroupFragment newInstance() {
        Bundle args = new Bundle();
        NewGroupFragment mFragment = new NewGroupFragment();
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();

        mNewgroupEdittxt.setText(" ");
        addGroupBtn.setVisibility(View.VISIBLE);
        addFriendTogoupBtn.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mFragmentView = inflater.inflate(R.layout.group_chatgroup_fragment, container, false);
        bindViews(mFragmentView);

        getActivity().setTitle("New Group");

        return mFragmentView;
    }

    private void bindViews(View fragmentView){
        mListView				= (ListView)  fragmentView.findViewById(R.id.list_view);
        mNewgroup			= (TextView)  fragmentView.findViewById(R.id.new_group);
        relativeLayout      = (RelativeLayout) fragmentView.findViewById(R.id.relativeLayout);
        mNewgroupEdittxt        = (EditText) fragmentView.findViewById(R.id.group_name);
        addGroupBtn         = (Button) fragmentView.findViewById(R.id.newgroupBtn);
        addFriendTogoupBtn  = (Button) fragmentView.findViewById(R.id.add_friendBtn);
        mNot_in_group       = (TextView) fragmentView.findViewById(R.id.not_in_group);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        relativeLayout.setVisibility(View.GONE);

        chat_grouplist = new ArrayList<>();

        adapter = new StringModelViewAdapter(getContext(),  chat_grouplist);
        mListView.setAdapter(adapter);


        mNewgroup.setOnClickListener(this);
        addGroupBtn.setOnClickListener(this);
        addFriendTogoupBtn.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = mListView.getItemAtPosition(position).toString();

            }
        });

        adapter.notifyDataSetChanged();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newgroupBtn:
                if (!toggle) {
                    StringModel new_group_name = new StringModel();
                    if (!mNewgroupEdittxt.getText().toString().trim().isEmpty()) {
                        new_group_name.text = mNewgroupEdittxt.getText().toString().trim();
                        addGroupBtn.setVisibility(View.GONE);
                        addFriendTogoupBtn.setVisibility(View.VISIBLE);

                        //Add to the group list
                        chat_grouplist.add(new_group_name);

                        adapter.notifyDataSetChanged();

                        if (!chat_grouplist.isEmpty()){
                            mNot_in_group.setVisibility(View.GONE);
                        }else{mNot_in_group.setVisibility(View.VISIBLE);}

                    }
                }
                break;
            case R.id.new_group:
                toggle = !toggle;
                if (!toggle){
                    relativeLayout.setVisibility(View.VISIBLE);
                    mNewgroupEdittxt.setFocusable(true);
                }else{
                    relativeLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.add_friendBtn:
                FriendListActivity.startIntent(getContext(), Intent.FLAG_ACTIVITY_NEW_TASK, TAG);
                mNewgroupEdittxt.setText("");
                break;
        }
    }
}
