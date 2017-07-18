package ru.worksolutions.loggingaction;

import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.Date;

public class NLService extends NotificationListenerService {
    private final String TAG = NLService.class.getSimpleName();

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
//        Date date = new Date(sbn.getPostTime());
//        String context = "ID :" + sbn.getId() + " " + sbn.getNotification().tickerText + " " + sbn.getPackageName() + " " + date.toString() + "\n";
//        NotificationUtils.addToLog(this, context);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        super.onNotificationRemoved(sbn);
    }
}
