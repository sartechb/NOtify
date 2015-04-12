package me.dontnotify.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.service.notification.NotificationListenerService;
import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;


public class NLService extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(sbn.getPackageName(), 0);
        } catch (final NameNotFoundException e) {
            ai = null;
        }
        String body  = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT, "").toString();
        body = (body != null) ? body : "";
        String title = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE, "").toString();
        title = (title != null) ? title : "";
        String full_text = title + " " + body;
        ArrayList<String> people = sbn.getNotification().extras.getStringArrayList(Notification.EXTRA_PEOPLE);
        if (people != null) {
            for (String person : people) {
                full_text = full_text + " " + people;
            }
        }
        if (!full_text.toLowerCase().contains("mark")) {
            sendNotification((String) ((ai != null) ? pm.getApplicationLabel(ai) : "(unknown)"), (String) sbn.getNotification().tickerText);
        }
        onNotification(sbn, "Posted", full_text);
    }

    public void sendNotification(String title, String body) {
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");

        final Map data = new HashMap();
        data.put("title", title);
        data.put("body", body);
        final JSONObject jsonData = new JSONObject(data);
        final String notificationData = new JSONArray().put(jsonData).toString();

        i.putExtra("messageType", "PEBBLE_ALERT");
        i.putExtra("sender", "NOtify");
        i.putExtra("notificationData", notificationData);

        Log.d("NLService", "About to send a modal alert to Pebble: " + notificationData);
        sendBroadcast(i);
    }

    private void onNotification(StatusBarNotification sbn, String suffix, String body) {
        Log.i(TAG, body);
        Log.i(TAG, "ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
    }
}

// vim: ts=4 sw=4 expandtab : 
