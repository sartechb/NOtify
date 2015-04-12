package me.dontnotify.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

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
        sendNotification((String) ((ai != null) ? pm.getApplicationLabel(ai) : "(unknown)"), (String) sbn.getNotification().tickerText);
        onNotification(sbn, "Posted");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        onNotification(sbn, "Removed");
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

    private void onNotification(StatusBarNotification sbn, String suffix) {
        Log.i(TAG, "ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
        //Intent i = new Intent("pw.mjs.NOtify.NOTIFICATION_LISTENER_NOTIFY");
        //i.putExtra("notification_event", "onNotification" + suffix + ":" + sbn.getPackageName() + "n");
        //sendBroadcast(i);
    }
}

// vim: ts=4 sw=4 expandtab : 
