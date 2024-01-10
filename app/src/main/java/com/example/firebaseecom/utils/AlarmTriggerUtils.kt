package com.example.firebaseecom.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.firebaseecom.broadcastReciever.AlarmReciever
import java.util.Calendar

class AlarmTriggerUtils {

    fun setAlarmTriggerForNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReciever::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            if (get(Calendar.HOUR_OF_DAY) >= 10 ) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        try{
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("ALARM","SET")
        }
        catch (e:Exception)
        {
            Log.d("ALARM",e.toString())
        }
    }
}