package ru.worksolutions.loggingaction

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ResolveInfo
import android.os.Build
import android.provider.Telephony
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        Intent serviceIntent = new Intent(this, NLService.class);
        //        startService(serviceIntent);

        val packageSMS = getDefaultSmsAppPackageName(this)
        Log.e("MainActivity", "package SMS: " + packageSMS!!)

        findViewById(R.id.btn_show_notification).setOnClickListener { NotificationUtils.showNotification(applicationContext, "SomeNotification") }

    }

    override fun onResume() {
        super.onResume()
        val s = getSharedPreferences("test", Context.MODE_PRIVATE).getString("test", "")
        (findViewById(R.id.tv_content) as TextView).text = s

    }

    companion object {

        fun getDefaultSmsAppPackageName(context: Context): String? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                return Telephony.Sms.getDefaultSmsPackage(context)
            else {
                val intent = Intent(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_DEFAULT).setType("vnd.android-dir/mms-sms")
                val resolveInfos = context.packageManager.queryIntentActivities(intent, 0)
                if (resolveInfos != null && !resolveInfos.isEmpty())
                    return resolveInfos[0].activityInfo.packageName
                return null
            }
        }
    }


}
