package com.example.taha.sigraylamcadele.Recivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.taha.sigraylamcadele.Service.CheckUpService

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context , CheckUpService::class.java)
        val pendingIntent = PendingIntent.getService(context,
                100,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                100000,150000,
                pendingIntent)
    }
}
