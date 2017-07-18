package ru.worksolutions.loggingaction

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast

import android.content.Context.MODE_PRIVATE

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Get the SMS map from Intent
        val extras = intent.extras

        val messages = ""

        if (extras == null) {
            return
        }

        // Get received SMS array
        val smsExtra = extras.get(SMS_EXTRA_NAME) as Array<Any>

        // Get ContentResolver object for pushing encrypted SMS to the incoming folder
        val contentResolver = context.contentResolver

        for (i in smsExtra.indices) {
            val sms = SmsMessage.createFromPdu(smsExtra[i] as ByteArray)

            val body = sms.messageBody.toString()
            val address = sms.originatingAddress

            //            messages += "SMS from " + address + " :\n";
            //            messages += body + "\n";

            val content = "Получено сообщение от $address: $body"

            // Here you can add any your code to work with incoming SMS
            // I added encrypting of all received SMS

            NotificationUtils.addToLog(context, content)

            //                putSmsToDatabase( contentResolver, sms );
        }

        val body = messages
        val mIntent = Intent(context, SmsService::class.java)
        mIntent.putExtra("sms_body", body)
        context.startService(mIntent)

        // WARNING!!!
        // If you uncomment the next line then received SMS will not be put to incoming.
        // Be careful!
        // this.abortBroadcast();
    }

    companion object {
        val SMS_EXTRA_NAME = "pdus"
    }
}
