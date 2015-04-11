package me.dontnotify.notify;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
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

    private EditText inputField;
    private Button addButton;
    private ListView itemList;
    private NoDefaultSpinner selectApp;
    private NoDefaultSpinner selectAction;

    private ArrayList<AppInfo> AppInfoArray;
    // AppInfo Declaration
    //      Stores App information for App spinner and
    //      in the ListView values ArrayList
    public static class AppInfo {
        public AppInfo(String startAppName,
                       String startPackageName) {
            appName = startAppName;
            packageName = startPackageName;
        }
        public String appName; //Display Name
        public String packageName; //Unique Identifier!
    }


    private ArrayList<NotifyItem> items;
    private MyArrayAdapter adapter;
    private DatabaseAccess db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link local objects with UI
        inputField = (EditText) findViewById(R.id.inputText);
        addButton = (Button) findViewById(R.id.addButton);
        itemList = (ListView) findViewById(R.id.itemList);
        selectApp = (NoDefaultSpinner)
                findViewById(R.id.selectAppSpinner);
        selectAction = (NoDefaultSpinner)
                findViewById(R.id.selectActionSpinner);

        AppInfoArray = new ArrayList<AppInfo>();
        items = new ArrayList<NotifyItem>();
        adapter = new MyArrayAdapter(this, items);
        itemList.setAdapter(adapter);

        db = new DatabaseAccess(this);

        // Fill App Spinner, load rules from disk(db)
        fillSelectApp();
        restoreDatafromDisk();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        storeDataToDisk();
    }

    @Override
    public void onResume() {
        super.onResume();
        fillSelectApp();
    }

    public void onAddClicked(View view) {
        String input = inputField.getText().toString();
        if(selectApp.getSelectedItemPosition() == AdapterView.INVALID_POSITION){
            Toast toast = Toast.makeText(
                    this,"Please Select Application",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        AppInfo appIn = AppInfoArray.get(selectApp.getSelectedItemPosition());
        if(selectAction.getSelectedItem() == null) {
            Toast toast = Toast.makeText(
                    this,
                    "Please Select Action",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String actionIn = selectAction.getSelectedItem().toString();
        items.add(new NotifyItem(appIn, actionIn, input));
        adapter.notifyDataSetChanged();
    }

    public void onAllowClicked(View view) {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();

        if (on) {
            //Allow
        } else {
            // Ignore
        }
    }

    //
    // NotifyItem
    //
    // Container class for information about a scavenger hunt item.
    //
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

    //
    // MyArrayAdapter
    //
    // Custom adapter to populate listview rows with ScavengerItems.
    // Called through the notifyDataSetChanged calls
    //
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

    // Add items into App spinner dynamically from currently
    //      installed programs
    public void fillSelectApp() {
        List<String> list = new ArrayList<String>();
        AppInfoArray.clear();

        //get a list of installed apps
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //Fill App Spinner value Array and linked AppInfo Array
        for (ApplicationInfo packageInfo : packages) {
            String app_name = (String) pm.getApplicationLabel(packageInfo);
            list.add(app_name);
            AppInfoArray.add(new AppInfo(app_name, packageInfo.packageName));
        }

        // Update App Spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectApp.setAdapter(dataAdapter);
    }

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

}