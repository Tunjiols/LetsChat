package com.projects.adetunji.letschat.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by adetunji on 20/01/2018.
 */

public class Helper {

    public static final String NAME 			= "Full Name";
    public static final String EMAIL	 		= "Email";
    public static final String DISPLAY_NAME 	= "Profile name";
    public static final String BIRTHDAY 		= "Birthday";
    public static final String MOBILE_NUMBER 	= "Mobile Number";
    public static final String HOBBY_INTEREST 	= "Hobby & Interest";
    public static final String Full_Name 		= "Fullname";
    public static final String E_Mail 			= "email";
    public static final String Display_NAME 	= "userName";
    public static final String Birth_DAY 		= "birthday";
    public static final String Mobile_NUMBER 	= "phone";
    public static final String Hobby_INTEREST 	= "hobby";
    public static final String CURRENT_CITY     = "Current_city";
    public static final String LAST_SEEN_DATE   = "last_seen_date";

    public static final int SELECT_PICTURE 				= 2000;
    public static final int FRAGMENT_FLAG_PROFILE 		= 100;
    public static final int FRAGMENT_FLAG_FAVOURITE		= 200;
    public static final int FRAGMENT_FLAG_NEWGROUP		= 300;
    public static final int FRAGMENT_FLAG_ADDFRIEND		= 400;
    public static final int FRAGMENT_FLAG_SETTINGS		= 500;
    public static final int FRAGMENT_SHARE_LETSCHAT     = 600;

    public static final String PREF_USERNAME            = "Username";
    public static final String PREF_STATUS              = "status";
    public static final String PREF_EMAIL               = "Email";
    public static final String PREF_FULL_NAME           = "Full name";
    public static final String PREF_MOBILE_NUMBER       = "Mobile Number";
    public static final String PREF_BIRTHDAY            = "Birthday";
    public static final String PREF_CURRENT_CITY        = "Current City";
    public static final String PREF_RELATIONSHIP        = "Relationship";
    public static final String PREF_HOBBY_INTEREST      = "Hobby";

    public static boolean isValidEmail(String email){
        if(email.contains("@")){
            return true;
        }
        return false;
    }

    public static void displayToastMessage(Context context, String displayMessage){
        Toast.makeText(context, displayMessage, Toast.LENGTH_LONG).show();
    }
}
