package com.projects.adetunji.letschat.uis.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 17/03/2018.
 */

public class ListViewActivity extends ListActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            /*
            // storing string resources into Array
            String[] newList = {"one","two","three","four"}
            // here store array of strings from the database
            //setContentView(R.layout.custom_list_activity_view);
            // Binding Array to ListAdapter
            this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, newList));

            ListView listView = getListView();

            // listening to single list item on click
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // selected item
                    String itemClicked = ((TextView) view).getText().toString();

                    // Launching new Activity on selecting single List Item
                    Intent intent = new Intent(getApplicationContext(), SingleListItem.class);
                    // sending data to new activity
                    intent.putExtra("number", itemClicked);
                    startActivity(intent);

                }
            });*/
        }
}


/*
    public class MainActivity extends ListActivity {

        private TextView text;
        private List<String> listValues;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.list_activity);

            text = (TextView) findViewById(R.id.mainText);
            listValues = new ArrayList<String>();
            listValues.add("Android");
            listValues.add("iOS");
            listValues.add("Symbian");
            listValues.add("Blackberry");
            listValues.add("Windows Phone");

            // initiate the listadapter
            ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this,
                    R.layout.row_layout, R.id.listText, listValues);

            // assign the list adapter
            setListAdapter(myAdapter);
        }

        // when an item of the list is clicked
        @Override
        protected void onListItemClick(ListView list, View view, int position, long id) {
            super.onListItemClick(list, view, position, id);

            String selectedItem = (String) getListView().getItemAtPosition(position);
            //String selectedItem = (String) getListAdapter().getItem(position);

            text.setText("You clicked " + selectedItem + " at position " + position);
        }
    }

}
*/