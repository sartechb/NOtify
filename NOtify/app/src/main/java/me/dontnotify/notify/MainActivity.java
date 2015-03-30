package me.dontnotify.notify;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private EditText inputField;
    private Button addButton;
    private ListView itemList;

    private ArrayList<NotifyItem> items;
    private MyArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = (EditText) findViewById(R.id.inputText);
        addButton = (Button) findViewById(R.id.addButton);
        itemList = (ListView) findViewById(R.id.itemList);

        items = new ArrayList<NotifyItem>();
        adapter = new MyArrayAdapter(this, items);

        itemList.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputField.getText().toString();

                items.add(new NotifyItem(input));

                adapter.notifyDataSetChanged();
            }
        });

    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

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
    private class NotifyItem {
        private String name;
        private ToggleButton tog;
//        private Bitmap icon;

        public NotifyItem(String n){
            name = n;
            tog = null;
        }

        public NotifyItem(String n, ToggleButton b){
            name = n;
            tog = b;
        }

        public void setTB(ToggleButton b){
            tog = b;
        }

        public ToggleButton getTB(){
            return tog;
        }

        public String getName(){
            return name;
        }
    } // NotifyItem

    //
    // MyArrayAdapter
    //
    // Custom adapter to populate listview rows with ScavengerItems.
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
//            ImageView pict = (ImageView) row.findViewById(R.id.rowImage);
            ToggleButton tb = (ToggleButton) row.findViewById(R.id.togglebutton);

            // Assign UI data
            name.setText(values.get(pos).getName());

//            if (values.get(pos).getIcon() != null){
//                pict.setImageBitmap(values.get(pos).getIcon());
//            }

            return row;
        }
    } // MyArrayAdapter

}