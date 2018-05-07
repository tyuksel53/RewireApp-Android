package com.rewire.mobile.app.Service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.rewire.mobile.app.Library.Portal
import java.text.SimpleDateFormat
import java.util.*


class CheckUpService : IntentService {

    constructor() : super("CheckUpService") {
        Log.e("FIRE","SERVICE RUN")
    }
    override fun onHandleIntent(intent: Intent?) {
        Log.e("FIRE","SERVICE RUN")
        Portal.raiseUp()
        val user = Portal.autoLogin(this)
        if(user != null)
        {
            val ayarlar = Portal.getSettings(this,user.Username!!)
            if(ayarlar!=null)
            {
                if(ayarlar.Notification == "YES")
                {
                    if(Portal.isTimeHasCome(ayarlar.UserCheckUpTime!!)) // bildirim gönder
                    {
                        val formatter = SimpleDateFormat("dd-MM-yyyy")
                        val date = formatter.format(Calendar.getInstance().time)
                        val check = Portal.getNotfiy(this, user.Username!!,date)
                        if(check == null) // hiç girmemiş
                        {
                            Portal.insertNotfiy(this,user.Username!!,date,"YES")
                            Portal.sendNotify(this)
                        }else if(check == "NO")
                        {
                            Portal.updateNotfiy(this,user.Username!!,date,"YES")
                            Portal.sendNotify(this)
                        }

                    }
                }
            }
        }

    }


}
