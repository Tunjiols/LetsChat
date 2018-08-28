package com.projects.adetunji.letschat.uis.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.StringModel;
import com.projects.adetunji.letschat.uis.adapters.ArrayViewAdapter;
import com.projects.adetunji.letschat.uis.adapters.StringModelViewAdapter;
import com.projects.adetunji.letschat.uis.fragments.DialogInfoPageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adetunji on 01/02/2018.
 */

public class HelpSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = HelpSettingsActivity.class.getSimpleName();

    private		Toolbar			mToolbar;
    private     TextView	    mFaq;
    private     TextView	    mApp_info;
    private     TextView	    mGot_question;

    private DialogInfoPageFragment dialogInfoPageFragment = new DialogInfoPageFragment();

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, HelpSettingsActivity.class);
        intent.setFlags(flags);

        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_settings_activity);

        bindViews();

        init();
    }

    private void bindViews() {
        mToolbar            = (Toolbar) findViewById(R.id.toolbar);
        mFaq				= (TextView)findViewById(R.id.faq);
        mApp_info			= (TextView)findViewById(R.id.app_info);
        mGot_question		= (TextView)findViewById(R.id.got_question);

    }

    private void init(){
        mToolbar.setTitle("Help");

        mFaq.setOnClickListener(this);
        mApp_info.setOnClickListener(this);
        mGot_question.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.faq :

                ItemList();
                break;

            case R.id.app_info:
                AppInformationActivity.startIntent(getBaseContext(), Intent. FLAG_ACTIVITY_NEW_TASK);
                break;
            case R.id.got_question:

                break;
        }
    }

    private void ItemList() {

        final ListView        listView;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.toast_list_view_list);
        dialog.setTitle("FAQ");

        listView = (ListView) dialog.findViewById(R.id.list);
        dialog.show();

        String[] values = new String[]{
                "Download and Installation",
                "Managing Profile",
                "Verifying Account",
                "Chats",
                "Privacy",
                "Changing Status",
                "Troubleshooting the app"};

        List<StringModel> mStringObjArray = new ArrayList<StringModel>();

        for(int i = 0; i < values.length; i++){
            StringModel mStrObj = new StringModel();
            mStrObj.text = values[i];
            mStringObjArray.add(mStrObj);
        }

        StringModelViewAdapter mArrayViewAdapter = new StringModelViewAdapter(HelpSettingsActivity.this, mStringObjArray);
        listView.setAdapter(mArrayViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);


                dialog.cancel();
            }

        });
    }
}
