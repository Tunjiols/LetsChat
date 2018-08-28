package com.projects.adetunji.letschat.uis.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lamudi.phonefield.PhoneEditText;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.models.StringModel;
import com.projects.adetunji.letschat.uis.adapters.StringModelViewAdapter;
import com.projects.adetunji.letschat.utils.Constants;
import com.projects.adetunji.letschat.utils.SharedPrefUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by adetunji on 09/02/2018.
 */

public class UpdateProfileActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private static final String TAG = UpdateProfileActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private EditText mEdit_text;
    private Button mEditButton;
    private TextView mText;
    private ListView listView;
    private static String mHeaderKey;
    private SharedPrefUtil sharedPref;
    private PhoneEditText phoneEditText;

    public static void startIntent(Context context, int flags, String headerKey) {
        Intent intent = new Intent(context, UpdateProfileActivity.class);
        intent.setFlags(flags);
        intent.putExtra(Constants.HEADERKEY, headerKey);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_activity);

        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEdit_text = (EditText) findViewById(R.id.edit_text);
        mText = (TextView) findViewById(R.id.text);
        mEditButton = (Button) findViewById(R.id.edit_approve_button);
        phoneEditText = (PhoneEditText) findViewById(R.id.phone_edit_text);
    }

    private void init() {
        mHeaderKey = getIntent().getStringExtra(Constants.HEADERKEY);

        mEdit_text.setVisibility(View.GONE);
        phoneEditText.setVisibility(View.GONE);


        sharedPref = new SharedPrefUtil(getApplicationContext());

        mToolbar.setTitle("Update Profile");
        mText.setText("Edit " + mHeaderKey);

        mEdit_text.setMaxLines(1);
        mEdit_text.setText(sharedPref.getString(mHeaderKey));

        mEdit_text.setOnEditorActionListener(this);


        switch (mHeaderKey) {
            case Helper.PREF_FULL_NAME:
                mEdit_text.setVisibility(View.VISIBLE);
                mEdit_text.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                break;
            case Helper.PREF_USERNAME:
                mEdit_text.setVisibility(View.VISIBLE);
                mEdit_text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                break;
            case Helper.PREF_MOBILE_NUMBER:
                phoneEditText.setVisibility(View.VISIBLE);
                PhoneEditFomatter(phoneEditText);

                break;
            case Helper.PREF_BIRTHDAY:
                mEdit_text.setVisibility(View.VISIBLE);
                mEdit_text.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                mEdit_text.setCursorVisible(false);

                mEdit_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker(mEdit_text);
                    }
                });
                break;
            case Helper.PREF_CURRENT_CITY:
                mEdit_text.setVisibility(View.VISIBLE);
                mEdit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                break;
            case Helper.PREF_RELATIONSHIP:
                mEdit_text.setVisibility(View.VISIBLE);
                mEdit_text.setCursorVisible(false);
                mEdit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                //Hide softKey
                //mEdit_text.setEnabled(false);

                mEdit_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager soft_key_hide = (InputMethodManager) UpdateProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        soft_key_hide.hideSoftInputFromInputMethod(mEdit_text.getWindowToken(), 0);
                        RelationshipList(mEdit_text);
                    }
                });
				/*
                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.relationshipOption);
                String[] relationship = getResources().getStringArray(R.array.Relationship);
                if (mEdit_text.isHovered()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, relationship);
                    textView.setAdapter(adapter);
					*/

                break;
            case Helper.PREF_HOBBY_INTEREST:
                mEdit_text.setVisibility(View.VISIBLE);
                mEdit_text.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                mEdit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
        }


        mEditButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mEdit_text.getVisibility() != View.GONE) {
                    String editText = mEdit_text.getText().toString().trim();
                    sharedPref.saveString(mHeaderKey, editText);
                    sharedPref.saveInt("SYNC_DATA_STATE", 1);

                    finish();
                } else if (phoneEditText.getVisibility() != View.GONE) {
                    boolean valid = true;

                    // checks if the field is valid
                    if (phoneEditText.isValid()) {
                        phoneEditText.setError(null);
                    } else {
                        phoneEditText.setError("Invalid mobile number");
                        valid = false;
                    }

                    if (valid) {
                        // Return the phone number
                        String editText = phoneEditText.getPhoneNumber();
                        sharedPref.saveString(mHeaderKey, editText);
                        sharedPref.saveInt("SYNC_DATA_STATE", 1);

                        Helper.displayToastMessage(getBaseContext(), "Mobile number saved");
                        finish();
                    }

                }
            }
        });
    }

    @Override

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEND) {

            if (mEdit_text.getVisibility() != View.GONE) {
                String editText = mEdit_text.getText().toString().trim();
                sharedPref.saveString(mHeaderKey, editText);
                sharedPref.saveInt("SYNC_DATA_STATE", 1);

                finish();
            } else if (phoneEditText.getVisibility() != View.GONE) {
                boolean valid = true;

                // checks if the field is valid
                if (phoneEditText.isValid()) {
                    phoneEditText.setError(null);
                } else {
                    phoneEditText.setError("Invalid mobile number");
                    valid = false;
                }

                if (valid) {
                    // Return the phone number
                    String editText = phoneEditText.getPhoneNumber();
                    sharedPref.saveString(mHeaderKey, editText);
                    sharedPref.saveInt("SYNC_DATA_STATE", 1);

                    Helper.displayToastMessage(getBaseContext(), "Mobile number saved");
                    finish();
                }

            }


            return true;

        }

        return false;
    }

    private void RelationshipList(EditText mEdit_texts) {

        final EditText mEdit_text = mEdit_texts;
        mEdit_text.requestFocus();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.toast_list_view_list);
        dialog.setTitle("Select");

        listView = (ListView) dialog.findViewById(R.id.list);
        dialog.show();

        String[] values = new String[]{"_", "Single", "In a relationship", "Engaged", "Married", "Divorce", "Widow"};

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        List<StringModel> mStringObjArray = new ArrayList<StringModel>();

        for (int i = 0; i < values.length; i++) {
            StringModel mStrObj = new StringModel();
            mStrObj.text = values[i];
            mStringObjArray.add(mStrObj);
        }

        StringModelViewAdapter mArrayViewAdapter = new StringModelViewAdapter(this, mStringObjArray);

        listView.setAdapter(mArrayViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringModel  itemValue = (StringModel) listView.getItemAtPosition(position);

                mEdit_text.setText(itemValue.getText());
                dialog.cancel();
            }

        });
    }

    private void DatePicker(EditText mEdit_texts) {

        mEdit_texts.requestFocus();
        DatePickerDialog mDatePickerDialog;
        final EditText mEdit_text = mEdit_texts;
        final SimpleDateFormat mDateFormatter;

        mDateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);

        mEdit_text.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance(Locale.US);

        mDatePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                Calendar newDate = Calendar.getInstance();
                // do{
                newDate.set(year, monthOfYear, dayOfMonth);

                mEdit_text.setText(mDateFormatter.format(newDate.getTime()));
                //}while(year < ((newDate.get(Calendar.YEAR))-120) || year > newDate.get(Calendar.YEAR));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);

        mDatePickerDialog.setTitle("Calendar");

        mDatePickerDialog.show();
    }

    private void PhoneEditFomatter(PhoneEditText phoneEditText) {

        phoneEditText.requestFocus();

        //Set default country
        phoneEditText.setDefaultCountry("US");
        phoneEditText.setPadding(40, 12, 40, 12);
        phoneEditText.setHint(R.string.phone_hint);
    }

}