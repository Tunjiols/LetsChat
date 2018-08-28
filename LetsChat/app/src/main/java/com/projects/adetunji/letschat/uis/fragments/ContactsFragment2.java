package com.projects.adetunji.letschat.uis.fragments;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.models.ContactsModel;
import com.projects.adetunji.letschat.uis.activities.UserListingActivity;
import com.projects.adetunji.letschat.uis.adapters.ArrayViewAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;




/**
 * This Fragment is only used to illustrate that access to the Contacts ContentProvider API has
 * been granted (or denied) as part of the runtime permissions model. It is not relevant for the
 * use of the permissions API.
 * <p>
 * This fragments demonstrates a basic use case for accessing the Contacts Provider. The
 * implementation is based on the training guide available here:
 * https://developer.android.com/training/contacts-provider/retrieve-names.html

 /**
 * Created by adetunji on 13/03/2018.
 */

public class ContactsFragment2 extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ContactsFragment2";
    private ListView mContactsList;
    private FloatingActionButton fbutton;

    /**This Projection constant contains the columns returned from query.
     * Each item in the ListView displays the contact's display name
     */
    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.PHOTO_URI,
    };

    private static final String SELECTION =
            "(("
                    + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + " != '' ))";

    /**
     * Sort order for the query. Sorted by primary name in ascending order.
     */
    private static final String ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";
    private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";


    /**
     * Creates a new instance of a ContactsFragment2.
     */
    public ContactsFragment2() {}
    public static ContactsFragment2 newInstance() {
        return new ContactsFragment2();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        CircleImageView photo = (CircleImageView) rootView.findViewById(R.id.photo);
        ImageView call = (ImageView)rootView.findViewById(R.id.call);
        fbutton = (FloatingActionButton) rootView.findViewById(R.id.contact_add);
        mContactsList =(ListView) rootView.findViewById(R.id.list);

        init(rootView);

        return rootView;
    }

    private void init(View rootView){
        // Register a listener to display the first contact when a button is clicked.
        loadContact();

        // Register a listener to add a dummy contact when a button is clicked.
        fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertDummyContact();
            }
        });

        // An adapter that binds the result Cursor to the ListView
        SimpleCursorAdapter mCursorAdapter;
        // Gets a CursorAdapter
        //mCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_formater, null, FROM_COLUMNS, TO_IDS, 0);

        // Sets the adapter for the ListView
       // mContactsList.setAdapter(mCursorAdapter);
        // mContactsList.setOnItemClickListener(this);

        // Initializes the loader
        //getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Restart the Loader to query the Contacts content provider to display the first contact.
     */
    private void loadContact() {
        if(UserListingActivity.getContactPermission()){
            getLoaderManager().restartLoader(0, null, this);
        }else Helper.displayToastMessage(getContext(),"Couldn't Load Contacts");
    }

    /**
     * Initialises a new {@link CursorLoader} that queries the {@link ContactsContract}.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, null, ORDER);
    }


    /**
     * Dislays either the name of the first contact or a message.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null) {
            int totalCount = cursor.getCount();
            if (cursor.getCount() > 0) {
                List<ContactsModel> contactsList = getContacts(getContext(), cursor, totalCount);

                ArrayViewAdapter arrayViewAdapter = new ArrayViewAdapter (getContext(), contactsList );
                mContactsList.setAdapter(arrayViewAdapter);

            } else {
                //mMessageText.setText("contacts empty");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mMessageText.setText("Contacts empty");
    }

    /**
     * Accesses the Contacts content provider directly to insert a new contact.
     * <p>
     * The contact is called "__DUMMY ENTRY" and only contains a name.
     */
    private void insertDummyContact() {
        // Two operations are needed to insert a new contact.
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

        // First, set up a new raw contact.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // Next, set the name for the contact.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        DUMMY_CONTACT_NAME);
        operations.add(op.build());

        // Apply the operations.
        ContentResolver resolver = getActivity().getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            //Snackbar.make(mMessageText.getRootView(), "Could not add a new contact: " + e.getMessage(), Snackbar.LENGTH_LONG);
        }
    }




    public List<ContactsModel> getContacts(Context context, Cursor cursor, int totalCount) {
        List<ContactsModel> list = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();

        //if (cursor.getCount() > 0) {
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id)));

                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));
                Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                Bitmap photo = null;

                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream);
                }
                while (cursorInfo.moveToNext()) {
                    ContactsModel contactM = new ContactsModel();
                    contactM.id         = id;
                    contactM.name       = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contactM.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                   // contactM.email      = cursorInfo.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    contactM.photo      = photo;
                    contactM.photoURI   = pURI;

                    list.add(contactM);
                }

                cursorInfo.close();
            }
        }
        cursor.close();
        //}
        return list;
    }


    /**
     * ViewHolder for RecyclerViewAdapter
     * */

    private static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView photo;
        TextView name;
        ImageView call;
        TextView phone_number;

        public ViewHolder(final View view){
            super(view);
            photo        = (CircleImageView) view.findViewById(R.id.photo);
            name         = (TextView) view.findViewById(R.id.contact_name);
            phone_number = (TextView) view.findViewById(R.id.contact_number);
            call         = (ImageView)view.findViewById(R.id.call);

        }
    }

    /**
     * RecyclerViewAdapter Class
     *
     *  */
    private class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        private List<ContactsModel> contacts;
        protected Context context;


        public RecyclerViewAdapter(Context context, List<ContactsModel> contacts){
            this.contacts       = contacts;
            this.context        = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewholder;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_layout, parent, false);
            viewholder = new ViewHolder(view);
            return  viewholder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.name.setText(contacts.get(position).name);
            holder.phone_number.setText(contacts.get(position).mobileNumber);

            //holder.getAdapterPosition();

        }

        @Override
        public int getItemCount() {
            if (contacts != null) {
                return this.contacts.size();
            }else return 0;
        }

    }
}
