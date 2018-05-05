package com.example.taha.sigraylamcadele.Service

import android.app.IntentService
import android.app.Notification
import android.content.Intent
import android.util.Log
import br.com.goncalves.pugnotification.notification.PugNotification
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper
import com.example.taha.sigraylamcadele.R
import io.paperdb.Paper
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
