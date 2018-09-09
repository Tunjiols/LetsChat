package com.projects.adetunji.letschat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.adetunji.letschat.LetsChatMainApp;

/**
 * Created by adetunji on 07/01/2018. class SharedPrefUtil
 */

public class SharedPrefUtil {
    /**
     * Name of the preference file
     */
    private static final String APP_PREFS = "application_prefs";
    private static  String USER_PREFS;

    private FirebaseAuth firebaseAuth;
    private Context appContext;
    private SharedPreferences appSharedPreferences;
    private SharedPreferences.Editor appEditor;

    public SharedPrefUtil(Context appContext) {
        firebaseAuth        = FirebaseAuth.getInstance();
        String user_ID      = firebaseAuth.getCurrentUser().getUid();

        this.appContext     = appContext;
        this.USER_PREFS 	= user_ID+"_"+APP_PREFS;
    }

    /**
     * Save a string into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public void saveString(String key, String value) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        appEditor = appSharedPreferences.edit();
        appEditor.putString(key, value);
        appEditor.commit();
    }

    /**
     * Save a int into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public void saveInt(String key, int value) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        appEditor = appSharedPreferences.edit();
        appEditor.putInt(key, value);
        appEditor.commit();
    }

    /**
     * Save a long date into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public void saveLongDate(String key, long value) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        appEditor = appSharedPreferences.edit();
        appEditor.putLong(key, value);
        appEditor.commit();
    }

    /**
     * Save a boolean into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public void saveBoolean(String key, boolean value) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        appEditor = appSharedPreferences.edit();
        appEditor.putBoolean(key, value);
        appEditor.commit();
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or null.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    public String getString(String key) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return appSharedPreferences.getString(key, null);
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    public String getString(String key, String defaultValue) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return appSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public int getInt(String key) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return appSharedPreferences.getInt(key, 0);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public long getLongDate(String key) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return appSharedPreferences.getLong(key, 0);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public int getInt(String key, int defaultValue) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return appSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or false.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    public boolean getBoolean(String key) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return appSharedPreferences.getBoolean(key, false);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return appSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Clears the shared preference file
     */
    public void clear() {
        appSharedPreferences = appContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        appSharedPreferences.edit().clear().apply();
    }
}
