package me.dontnotify.notify;

import android.support.v7.app.ActionBar;
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

public class NotifyAddAppActivity extends ActionBarActivity {
    private Button addButton;
    private ListView itemList;
    private NoDefaultSpinner selectApp;
    private NoDefaultSpinner selectAction;

    private ArrayList<AppInfo> AppInfoArray;
    public static class AppInfo {
        public AppInfo(String startAppName,
                       String startPackageName) {
            appName = startAppName;
            packageName = startPackageName;
        }
        public String appName; //Display Name
        public String packageName; //Unique Identifier!
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_app);

        AppInfoArray = new ArrayList<AppInfo>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Link local objects with UI
        addButton = (Button) findViewById(R.id.addButton);
        itemList = (ListView) findViewById(R.id.itemList);
        selectApp = (NoDefaultSpinner)
                findViewById(R.id.selectAppSpinner);
        selectAction = (NoDefaultSpinner)
                findViewById(R.id.selectActionSpinner);
        fillSelectApp();
    }

    public void onAddClicked(View view) {
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
        //items.add(new NotifyItem(appIn, actionIn, input));
        //adapter.notifyDataSetChanged();

        Intent i = new Intent(this, NotifyAppActivity.class);
        startActivity(i);
    }

    // Add items into App spinner dynamically from currently
    //      installed programs
    private void fillSelectApp() {
        List<String> list = new ArrayList<String>();
        AppInfoArray.clear();

        //get a list of installed apps
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //Fill App Spinner value Array and linked AppInfo Array
        for (ApplicationInfo packageInfo : packages) {
            if (null != pm.getLaunchIntentForPackage(packageInfo.packageName)) {
                String app_name = (String) pm.getApplicationLabel(packageInfo);
                list.add(app_name);
                AppInfoArray.add(new AppInfo(app_name, packageInfo.packageName));
            }
        }

        // Update App Spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectApp.setAdapter(dataAdapter);
    }
}
