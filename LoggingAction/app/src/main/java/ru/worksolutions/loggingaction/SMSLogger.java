package ru.worksolutions.loggingaction;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SMSLogger extends Service {
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationUtils.INSTANCE.showNotification(getApplicationContext(), "SMSLogger.onStartCommand()");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("SMSLogger", "onCreate()");

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addCategory("android.intent.category.DEFAULT");

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NotificationUtils.INSTANCE.showNotification(context, "OnReceive");
            }
        }, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SMSLogger", "onDestroy()");
    }
}
