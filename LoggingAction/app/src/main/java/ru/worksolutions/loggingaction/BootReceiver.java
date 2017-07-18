package ru.worksolutions.loggingaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils.INSTANCE.showNotification(context, "BootReceiver.onReceive()");
        Intent serviceIntent = new Intent(context, SMSLogger.class);
        context.startService(serviceIntent);

    }
}
