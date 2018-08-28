package com.projects.adetunji.letschat.uis.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by adetunji on 02/03/2018.
 */

public class ContactDetailsFragment extends Fragment {
//implements LoaderManager.LoaderCallbacks<Cursor>

    //--------------------Retrieving data-------------------------
    private static final String[] PROJECTION =
            {
                    ContactsContract.CommonDataKinds.Email._ID,
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.TYPE,
                    ContactsContract.CommonDataKinds.Email.LABEL

            };

    private static final String[] CONTACTS_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.LOOKUP_KEY
    };

    /*
     * Defines the selection clause. Search for a lookup key
     * and the Email MIME type
     */
    private static final String SELECTION =
            ContactsContract.Data.LOOKUP_KEY + " = ?" +
                    " AND " +
                    ContactsContract.Data.MIMETYPE + " = " +
                    "'" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";

    private static final String selection =
            "(("
                    + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + " != '' ))";


    // Defines the array to hold the search criteria
    private String[] mSelectionArgs = { "" };

    private static final String SORT_ORDER = ContactsContract.CommonDataKinds.Email.TYPE + " ASC ";

    // Defines a constant that identifies the loader
    final int DETAILS_QUERY_ID = 0;

    /*
     * Invoked when the parent Activity is instantiated
     * and the Fragment's UI is ready. Put final initialization
     * steps here.
     */
   // @Override
   // onActivityCreated(Bundle savedInstanceState) {

        // Initializes the loader framework
       // getLoaderManager().initLoader(DETAILS_QUERY_ID, null, this);

       // @Override
      /*  public Loader<Cursor> onCreateLoader( int loaderId, Bundle args){
            // Choose the proper action
            switch (loaderId) {
                case DETAILS_QUERY_ID:
                    // Assigns the selection parameter
                   // mSelectionArgs[0] = mLookupKey;
                    // Starts the query
                    CursorLoader mLoader =
                            new CursorLoader(
                                    getActivity(),
                                    ContactsContract.Data.CONTENT_URI,
                                    PROJECTION,
                                    SELECTION,
                                    mSelectionArgs,
                                    SORT_ORDER
                            );

           }*/
      //  }
   // }

        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            switch (loader.getId()) {
                case DETAILS_QUERY_ID:
                    /*
                     * Process the resulting Cursor here.
                     */
                    break;
            }

        }

       // @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            switch (loader.getId()) {
                case DETAILS_QUERY_ID:
                /*
                 * If you have current references to the Cursor,
                 * remove them here.
                 */
                    break;
            }
        }




    }
