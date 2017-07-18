package ru.worksolutions.loggingaction

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import android.content.Context.MODE_PRIVATE

object NotificationUtils {

    var isIncoming = false
    var isTalkingNow = false

    fun showNotification(context: Context, text: String) {
        val contentIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)
        val builder = Notification.Builder(context)
                .setContentTitle("LoggingAction")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setAutoCancel(true)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = builder.notification
        notificationManager.notify(android.R.drawable.ic_dialog_alert, notification)
    }

    fun addToLog(context: Context, content: String) {
        val sPref = context.getSharedPreferences("test", MODE_PRIVATE)
        val s = sPref.getString("test", "")
        sPref.edit().putString("test", s + content + "\n\n").apply()
    }

}
