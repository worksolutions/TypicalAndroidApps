package ru.worksolutions.loggingaction

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

import ru.worksolutions.loggingaction.NotificationUtils.isIncoming
import ru.worksolutions.loggingaction.NotificationUtils.isTalkingNow

class PhoneReceiver : BroadcastReceiver() {

    //    private boolean isIncoming = false;

    override fun onReceive(context: Context, intent: Intent) {

        val state = (context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager).callState
        //            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        val incoming_number = intent.extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
        if (state == TelephonyManager.CALL_STATE_IDLE) {

            if (isTalkingNow) {
                if (isIncoming)
                    NotificationUtils.addToLog(context, "Закончен входящий звонок с : " + incoming_number!!)
                else
                    NotificationUtils.addToLog(context, "Закончен исходящий звонок с : " + incoming_number!!)
                isTalkingNow = false
                isIncoming = false
            }


            return
        }

        if (state == TelephonyManager.CALL_STATE_RINGING) {
            isIncoming = true

            NotificationUtils.addToLog(context, "Входящий звонок от : " + incoming_number!!)

            return
        }

        if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
            isTalkingNow = true
            NotificationUtils.addToLog(context, "Начат разговор с : " + incoming_number!!)
        }
    }

}
