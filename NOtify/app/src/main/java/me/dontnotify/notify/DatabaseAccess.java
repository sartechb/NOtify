package me.dontnotify.notify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by timkenny on 4/10/2015.
 */
public class DatabaseAccess {

    private DatabaseAccessHelper dbHelper;
    private Integer curr_index;


    protected DatabaseAccess(Context context) {
        dbHelper = new DatabaseAccessHelper(context);
        curr_index = -1;
    }

    private void insertRow(int index,
                          String appName,
                          String packageName,
                          String action,
                          String addText) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(DatabaseEntry.COLUMN_NAME_INDEX, index);
        vals.put(DatabaseEntry.COLUMN_NAME_APP, appName);
        vals.put(DatabaseEntry.COLUMN_NAME_PACKAGE, packageName);
        vals.put(DatabaseEntry.COLUMN_NAME_ACTION, action);
        vals.put(DatabaseEntry.COLUMN_NAME_ADD, addText);

        db.insert(DatabaseEntry.TABLE_NAME,
                DatabaseEntry.COLUMN_NAME_NULLABLE,
                vals);
    }

    public void updateRows(ArrayList<MainActivity.NotifyItem> list){
        MainActivity.NotifyItem item;
        if( curr_index + 1 >= list.size()) return;
        int i = curr_index+1;
        for(; i < list.size(); i++){
            item = list.get(i);
            insertRow(i, item.getName(), item.getPackageName(),
                    item.getAction(), item.getAdd());
        }
        curr_index = i-1;
    }

    public String getAppString (String packageName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseEntry.COLUMN_NAME_APP,
                DatabaseEntry.COLUMN_NAME_PACKAGE,
                DatabaseEntry.COLUMN_NAME_ACTION,
                DatabaseEntry.COLUMN_NAME_ADD
        };
        String where = DatabaseEntry.COLUMN_NAME_PACKAGE +
                " = ?";
        String[] whereArgs = new String[] { packageName };

        Cursor c = db.query( DatabaseEntry.TABLE_NAME,
                            projection, where, whereArgs,
                            null, null, null);

        if(c.getCount() == 0) return null;

        c.moveToFirst();
        String ret = c.getString(c.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_ADD));

        c.close();
        return ret;
    }

    public ArrayList<MainActivity.NotifyItem> getAllData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseEntry.COLUMN_NAME_INDEX,
                DatabaseEntry.COLUMN_NAME_APP,
                DatabaseEntry.COLUMN_NAME_PACKAGE,
                DatabaseEntry.COLUMN_NAME_ACTION,
                DatabaseEntry.COLUMN_NAME_ADD
        };
        String orderBy = DatabaseEntry.COLUMN_NAME_INDEX +
                        " ASC";

        Cursor c = db.query( DatabaseEntry.TABLE_NAME,
                projection, null, null,
                null, null, orderBy);

        c.moveToFirst();
        Integer preIndex = -1, index = -1;
        MainActivity.NotifyItem[] retArray =
                new MainActivity.NotifyItem[c.getCount()];

        while( !c.isAfterLast() ) {
            index = c.getInt(
                    c.getColumnIndexOrThrow(
                            DatabaseEntry.COLUMN_NAME_INDEX));
            if(preIndex == index) {
                throw new RuntimeException("More than 1 entry per Index");
            }
            preIndex = index;

            MainActivity.AppInfo appInfo = new MainActivity.AppInfo(
                    c.getString(c.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_APP)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_PACKAGE))
            );
            MainActivity.NotifyItem notifyItem = new MainActivity.NotifyItem(
                    appInfo,
                    c.getString(c.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_ACTION)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_ADD))
            );

            retArray[index] = notifyItem;
            c.moveToNext();
        }
        curr_index = index;
        c.close();

        ArrayList<MainActivity.NotifyItem> retList = new ArrayList<MainActivity.NotifyItem>();
        for(int i = curr_index; i >= 0; i--){
            retList.add(0,retArray[i]);
        }
        return retList;
    }

    /* Inner class that defines the table contents */
    public static abstract class DatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_INDEX = "listIndex";
        public static final String COLUMN_NAME_APP = "appName";
        public static final String COLUMN_NAME_PACKAGE = "packageName";
        public static final String COLUMN_NAME_ACTION = "action";
        public static final String COLUMN_NAME_ADD = "addString";
        public static final String COLUMN_NAME_NULLABLE = "nullable";
    }

    public class DatabaseAccessHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";
        private static final String TEXT_TYPE = " TEXT";
        private static final String INT_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseEntry.TABLE_NAME + " (" +
                DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                DatabaseEntry.COLUMN_NAME_INDEX + INT_TYPE + COMMA_SEP +
                DatabaseEntry.COLUMN_NAME_APP + TEXT_TYPE + COMMA_SEP +
                DatabaseEntry.COLUMN_NAME_PACKAGE + TEXT_TYPE + COMMA_SEP +
                DatabaseEntry.COLUMN_NAME_ACTION + TEXT_TYPE + COMMA_SEP +
                DatabaseEntry.COLUMN_NAME_ADD + TEXT_TYPE + COMMA_SEP +
                DatabaseEntry.COLUMN_NAME_NULLABLE + TEXT_TYPE +
            " )";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;

        public DatabaseAccessHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }
}
