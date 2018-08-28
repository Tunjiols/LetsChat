package com.projects.adetunji.letschat.utils;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.Users;
import com.projects.adetunji.letschat.uis.activities.ShareActivity;

import java.util.ArrayList;

/**
 * Created by adetunji on 19/03/2018.
 */

public class SearchableUtil extends ListActivity implements SearchView.OnQueryTextListener{
   // private SenzorApplication application;
    private ListView friendListView;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    //private FriendListAdapter friendListAdapter;
    private ArrayList<Users> friendList;

    /**
     * {@inheritDoc}
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        //application = (SenzorApplication) this.getApplication();
        setActionBar();
        initFriendList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
       // inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       // searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //FriendListActivity.this.finish();
                //FriendListActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
               // ActivityUtils.hideSoftKeyboard(this);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set action bar
     *      1. properties
     *      2. title with custom font
     */
    private void setActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Friends");
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/vegur_2.otf");
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) (this.findViewById(titleId));
        actionBarTitle.setTextColor(getResources().getColor(R.color.white));
        actionBarTitle.setTypeface(typeface);
    }
    /**
     * Initialize friend list
     */
    private void initFriendList() {
        //friendList = application.getContactList();
        friendListView = (ListView) findViewById(R.id.friend_list);
       // friendListAdapter = new FriendListAdapter(this, friendList);
        // add header and footer for list
        //View headerView = View.inflate(this, R.layout.list_header, null);
        //View footerView = View.inflate(this, R.layout.list_header, null);
        //friendListView.addHeaderView(headerView);
        //friendListView.addFooterView(footerView);
        //friendListView.setAdapter(friendListAdapter);
        friendListView.setTextFilterEnabled(false);
        // use to enable search view popup text
        //friendListView.setTextFilterEnabled(true);
        // set up click listener
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0 && position <= friendList.size()) {
                   // handelListItemClick((Users)friendListAdapter.getItem(position - 1));
                }
            }
        });
    }

    /**
     * Navigate to share activity form here
     * @param user user
     */
    private void handelListItemClick(Users user) {
        // close search view if its visible
        if (searchView.isShown()) {
            searchMenuItem.collapseActionView();
            searchView.setQuery("", false);
        }
        // pass selected user and sensor to share activity
        Intent intent = new Intent(this, ShareActivity.class);
        //intent.putExtra("com.score.senzors.pojos.User", user);
       // intent.putExtra("com.score.senzors.pojos.Sensor", application.getCurrentSensor());
        this.startActivity(intent);
        //this.overridePendingTransition(R.anim.right_in, R.anim.stay_in);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        //friendListAdapter.getFilter().filter(newText);

        // use to enable search view popup text
//        if (TextUtils.isEmpty(newText)) {
//            friendListView.clearTextFilter();
//        }
//        else {
//            friendListView.setFilterText(newText.toString());

//        }
        return true;
    }
}
/*
*
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.score.senzors.R;
import com.score.senzors.pojos.User;
import java.util.ArrayList;

**
 * Display friend list
 *
 * @author eranga herath(erangaeb@gmail.com)
 *

public class FriendListAdapter extends BaseAdapter implements Filterable {

    private FriendListActivity activity;
    private FriendFilter friendFilter;
    private Typeface typeface;
    private ArrayList<User> friendList;
    private ArrayList<User> filteredList;

    **
     * Initialize context variables
     * @param activity friend list activity
     * @param friendList friend list
     *

    public FriendListAdapter(FriendListActivity activity, ArrayList<User> friendList) {
        this.activity = activity;
        this.friendList = friendList;
        this.filteredList = friendList;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/vegur_2.otf");
        getFilter();
    }

    **
     * Get size of user list
     * @return userList size
     *
    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Get specific item from user list
     * @param i item index
     * @return list item
     *

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    /**
     * Get user list item id
     * @param i item index
     * @return current item id
     *
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     * @param position index
     * @param view current list item view
     * @param parent parent
     * @return view
     *
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;
        final User user = (User) getItem(position);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.friend_list_row_layout, parent, false);
            holder = new ViewHolder();
            holder.iconText = (TextView) view.findViewById(R.id.icon_text);
            holder.name = (TextView) view.findViewById(R.id.friend_list_row_layout_name);
            holder.iconText.setTypeface(typeface, Typeface.BOLD);
            holder.iconText.setTextColor(activity.getResources().getColor(R.color.white));
            holder.name.setTypeface(typeface, Typeface.NORMAL);
            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }
        // bind text with view holder content view for efficient use
        holder.iconText.setText("#");
        holder.name.setText(user.getEmail());
        view.setBackgroundResource(R.drawable.friend_list_selector);

        return view;
    }

    /**
     * Get custom filter
     * @return filter
     *
    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     *
    static class ViewHolder {
        TextView iconText;
        TextView name;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     *
    private class FriendFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<User> tempList = new ArrayList<User>();
                // search content in friend list
                for (User user : friendList) {
                    if (user.getEmail().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(user);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = friendList.size();
                filterResults.values = friendList;
            }

            return filterResults;
        }

        **
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         *
       // @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }

}*/