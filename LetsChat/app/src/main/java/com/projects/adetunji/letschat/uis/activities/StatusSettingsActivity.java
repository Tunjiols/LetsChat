package com.projects.adetunji.letschat.uis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.projects.adetunji.letschat.LetsChatMainApp;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.models.StringModel;
import com.projects.adetunji.letschat.uis.adapters.StringModelViewAdapter;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adetunji on 01/02/2018.
 */

public class StatusSettingsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mCurrentStatusTitle;
    private TextView mCurrentStatus;
    private EditText mEditStatus;
    private ImageView mEditBtn;
    private ListView listView;
    private SharedPrefUtil sharedPref;

    private boolean toggle = true;

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, StatusSettingsActivity.class);
        intent.setFlags(flags);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_settings_activity);

        bindView();
        init();
    }

    private void bindView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCurrentStatusTitle = (TextView) findViewById(R.id.current_status_title);
        mCurrentStatus = (TextView) findViewById(R.id.current_status);
        mEditStatus = (EditText) findViewById(R.id.editable_status);
        mEditBtn = (ImageView) findViewById(R.id.edit_username);
        listView = (ListView) findViewById(R.id.status_list);
    }

    private void init() {

        // set the toolbar
        setSupportActionBar(mToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar action_bar = getSupportActionBar();

        // Enable the Up button
        action_bar.setDisplayHomeAsUpEnabled(true);

        mToolbar.setTitle("Status");
        sharedPref = new SharedPrefUtil(getApplicationContext());

        //mEditStatus.setEnabled(false);
        InputMethodManager soft_key_hide = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        soft_key_hide.hideSoftInputFromInputMethod(mEditStatus.getWindowToken(), 0);


        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mEditStatus.setEnabled(true);
                mEditStatus.setCursorVisible(true);

                if (!toggle) {
                    String status_text = mEditStatus.getText().toString();
                    if (!status_text.isEmpty()) {
                        saveToSharePrefUtil(status_text);
                        mEditStatus.setText("");
                        mEditStatus.setCursorVisible(false);
                    }
                }
                toggle = !toggle;
            }
        });

        String[] statusList = {"Busy", "Available", "In a meeting", "At work", "At the movie", "At the club", "Sleeping"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item_formater, R.id.text1, statusList);

        List<StringModel> mStringObjList = new ArrayList<>();

        for (int i = 0; i < statusList.length; i++) {
            StringModel mStrObj = new StringModel();
            mStrObj.text = statusList[i];
            mStringObjList.add(mStrObj);
        }

        StringModelViewAdapter mArrayViewAdapter = new StringModelViewAdapter(this, mStringObjList);
        listView.setAdapter(mArrayViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringModel itemValue = (StringModel) listView.getItemAtPosition(position);
                saveToSharePrefUtil(itemValue.getText());
            }

        });

        if (sharedPref.getString(Helper.PREF_STATUS) != null) {
            mCurrentStatus.setText(sharedPref.getString(Helper.PREF_STATUS));
        } else {
            mCurrentStatus.setText("No status");
            sharedPref.saveString(Helper.PREF_STATUS, " ");
        }

    }

    private void saveToSharePrefUtil(String text) {
        mCurrentStatus.setText(text);
        sharedPref.saveString(Helper.PREF_STATUS, text);
        sharedPref.saveInt("SYNC_DATA_STATE", 1);
    }
}


