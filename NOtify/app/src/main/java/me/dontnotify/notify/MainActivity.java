package me.dontnotify.notify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView itemList;

    //private MyArrayAdapter adapter;
    //private DatabaseAccess db;

    /* Activty bar code */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                openAdd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAdd() {
        Intent i = new Intent(this, NotifyAddAppActivity.class);
        startActivity(i);
    }
    /* End Activty bar code */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = (ListView) findViewById(R.id.itemList);

        //items = new ArrayList<NotifyItem>();
        //adapter = new MyArrayAdapter(this, items);
        //itemList.setAdapter(adapter);

        //db = new DatabaseAccess(this);

        // Fill App Spinner, load rules from disk(db)
        //fillSelectApp();
        //restoreDatafromDisk();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        //storeDataToDisk();
    }

    @Override
    public void onResume() {
        super.onResume();
        //fillSelectApp();
    }

    //
    // NotifyItem
    //
    // Container class for information about a scavenger hunt item.
    //
/*
    public static class NotifyItem {
        private Switch allowSwitch;
        private AppInfo appInfo;
        private String actionInfo;
        private String addInfo;

        public NotifyItem(AppInfo startApp, String startAction,
                          String startAdd){
            appInfo = startApp;
            actionInfo = startAction;
            addInfo = startAdd;
            allowSwitch = null;
        }

        public void setSW(Switch s){ allowSwitch = s;  }
        public boolean getSWvalue(){
            return allowSwitch.isChecked();
        }

        public String getName(){
            return appInfo.appName;
        }
        public String getPackageName() { return appInfo.packageName; }
        public String getAction() { return actionInfo; }
        public String getAdd() { return addInfo; }
    } // NotifyItem
*/

    //
    // MyArrayAdapter
    //
    // Custom adapter to populate listview rows with ScavengerItems.
    // Called through the notifyDataSetChanged calls
    //
/*
    private class MyArrayAdapter extends ArrayAdapter<NotifyItem> {
        private Context context;
        private ArrayList<NotifyItem> values;

        public MyArrayAdapter(Context c, ArrayList<NotifyItem> v){
            super(c, R.layout.row, v);
            context = c;
            values = v;
        }

        public View getView(int pos, View convertView, ViewGroup parent){
            // System service that handles creating UI elements
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Create the specific row's view
            View row = inflater.inflate(R.layout.row, parent, false);

            // Initialize UI for row
            TextView name = (TextView) row.findViewById(R.id.rowText);
            Switch sw = (Switch) row.findViewById(R.id.allowSwitch);

            // Assign UI data
            values.get(pos).setSW(sw);
            name.setText( values.get(pos).getAction() + " from " +
                            values.get(pos).getName() );

            return row;
        }
    } // MyArrayAdapter
*/

/*

    //Make Data Permanent to SQLite
    public void storeDataToDisk() {
        db.updateRows(items);
    }

    //Restore Data from SQLite
    public void restoreDatafromDisk() {
        ArrayList<NotifyItem> updatedItems = db.getAllData();
        if( updatedItems.size() != items.size() ) {
            // Must create and link b/c Javas copy by reference
            items = updatedItems;
            adapter = new MyArrayAdapter(this, items);
            itemList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
*/

}
