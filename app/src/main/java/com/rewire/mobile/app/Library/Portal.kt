package com.rewire.mobile.app.Library

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.rewire.mobile.app.API.ApiClient
import com.rewire.mobile.app.API.ApiInterface
import com.rewire.mobile.app.Database.DatabaseHelper
import com.rewire.mobile.app.Database.DbContract
import com.rewire.mobile.app.Model.User
import com.rewire.mobile.app.Model.UserSettings
import com.rewire.mobile.app.PaperHelper.LocaleHelper
import com.rewire.mobile.app.R
import com.rewire.mobile.app.SplashActivity
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Portal {

    companion object {


        fun isEmailValid(input:String):Boolean
        {
            var expression:String = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            var pattern =  Pattern.compile(expression,Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(input)
            return matcher.matches()
        }
        fun autoLogin(context:Context):User?
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase

            val protection = arrayOf(DbContract.UserEntry.COLUMN_ACCESSTOKEN,
                    DbContract.UserEntry.COLUMN_USERNAME,
                    DbContract.UserEntry.COLUMN_PASSWORD,
                    DbContract.UserEntry.COLUMN_ROLE,
                    DbContract.UserEntry.COLUMN_EMAIL)

            val selection = DbContract.UserEntry._ID + " = ?"
            val selectionAgrs = arrayOf("1")
            val myCorsor = db.query(DbContract.UserEntry.TABLE_NAME,protection,selection,selectionAgrs,
                    null,null,null)
            val count = myCorsor.count
            val myUser = User()
            if(count >= 1)
            {

                while(myCorsor.moveToNext())
                {
                    myUser.AccessToken = myCorsor.getString(0)
                    myUser.Username = myCorsor.getString(1)
                    myUser.Password = myCorsor.getString(2)
                    myUser.Role = myCorsor.getString(3)
                    myUser.Email = myCorsor.getString(4)
                    break
                }
                return myUser
            }else
                return null

        }

        fun textToDate(text:String):Date{
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val date = parser.parse(text)
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val formattedDate = formatter.format(date).split('-')

            return Date(formattedDate[2].toInt()-1900,formattedDate[1].toInt()-1,formattedDate[0].toInt())
        }

        fun dateToText(date:Date):String
        {
            val formatter = SimpleDateFormat("yyyy-MM-dd")

            return formatter.format(date)
        }

        fun textDateToFormatted(text:String):String
        {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val date = parser.parse(text)
            val formatter = SimpleDateFormat("d MMMM yyyy, EEEE")
            return formatter.format(date)
        }

        fun sifreDegisDb(context:Context, yeniSifre:String) {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase

            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.UserEntry.COLUMN_PASSWORD,yeniSifre)

            val args = arrayOf("1")

            var resultCount =  db.update(DbContract.UserEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.UserEntry._ID + " = ?",args)
        }
        fun updateUserSettingsTimeZone(context:Context,timeZone:String,username: String)
        {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase

            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.SettingsEntry.COLUMN_TIMEZONENAME,timeZone)

            val args = arrayOf(username)

            var resultCount =  db.update(DbContract.SettingsEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.SettingsEntry.COLUMN_USERNAME + " = ?",args)
        }
        fun updateUserSettingsCheckUpTime(context:Context,checkUpTime:String,username:String)
        {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase

            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.SettingsEntry.COLUMN_CHECKUPTIME,checkUpTime)

            val args = arrayOf(username)

            var resultCount =  db.update(DbContract.SettingsEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.SettingsEntry.COLUMN_USERNAME + " = ?",args)
        }

        fun updateUserSettingsNotification(context:Context,notificationSettings:String,username:String)
        {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase
            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.SettingsEntry.COLUMN_NOTFICATION,notificationSettings)

            val args = arrayOf(username)

            var resultCount =  db.update(DbContract.SettingsEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.SettingsEntry.COLUMN_USERNAME + " = ?",args)
        }
        fun insertUserSettings(context:Context,zoneName:String,checkUpTime:String,username:String)
        {
            val helper = DatabaseHelper(context)
            val db = helper.writableDatabase

            val yeniKayit = ContentValues()
            yeniKayit.put(DbContract.SettingsEntry.COLUMN_NOTFICATION,"YES")
            yeniKayit.put(DbContract.SettingsEntry.COLUMN_TIMEZONENAME,zoneName)
            yeniKayit.put(DbContract.SettingsEntry.COLUMN_CHECKUPTIME,checkUpTime)
            yeniKayit.put(DbContract.SettingsEntry.COLUMN_USERNAME,username)

            var id = db.insert(DbContract.SettingsEntry.TABLE_NAME,null,yeniKayit)
        }

        fun insertNotfiy(context:Context,username:String,date:String,isSent:String)
        {
            val helper = DatabaseHelper(context)
            val db = helper.writableDatabase

            val yeniKayit = ContentValues()
            yeniKayit.put(DbContract.NotifyEntry.COLUMN_DATE,date)
            yeniKayit.put(DbContract.NotifyEntry.COLUM_NOTIFICATON_SENT,isSent)
            yeniKayit.put(DbContract.NotifyEntry.COLUM_USERNAME,username)

            var id = db.insert(DbContract.NotifyEntry.TABLE_NAME,null,yeniKayit)
        }

        fun updateNotfiy(context:Context,username:String,date:String,isSent: String)
        {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase
            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.NotifyEntry.COLUM_NOTIFICATON_SENT,isSent)

            val args = arrayOf(username)

            var resultCount =  db.update(DbContract.NotifyEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.NotifyEntry.COLUM_USERNAME + " = ? and " +
                            DbContract.NotifyEntry.COLUMN_DATE + " = ?",args)
        }

        fun getNotfiy(context:Context,username: String,date:String):String?
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase

            val protection = arrayOf(DbContract.NotifyEntry.COLUM_NOTIFICATON_SENT)

            val selection = DbContract.NotifyEntry.COLUM_USERNAME + " = ? and " +
                    DbContract.NotifyEntry.COLUMN_DATE + " = ? "
            val selectionAgrs = arrayOf(username,date)
            val myCorsor = db.query(DbContract.NotifyEntry.TABLE_NAME
                    ,protection
                    ,selection,
                    selectionAgrs,
                    null,null,null)
            val count = myCorsor.count
            var result = ""
            if(count >= 1)
            {
                while(myCorsor.moveToNext())
                {
                    result = myCorsor.getString(0)
                    break
                }
                return result

            }else
                return null
        }
        fun deleteUserSettings(context:Context,username:String):Boolean
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val args = arrayOf(username)

            val resultCount = db.delete(DbContract.SettingsEntry.TABLE_NAME,
                    DbContract.UserEntry.COLUMN_USERNAME + " < ?",
                    args)

            if(resultCount > 0)
            {
                return true
            }

            return false
        }

        fun deleteNotfiy(context:Context):Boolean
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val args = arrayOf("1000")

            val resultCount = db.delete(DbContract.NotifyEntry.TABLE_NAME,
                    DbContract.NotifyEntry._ID + " < ?",
                    args)

            if(resultCount > 0)
            {
                return true
            }

            return false
        }
        fun getSettings(context:Context,username:String):UserSettings?
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase

            val protection = arrayOf(DbContract.SettingsEntry.COLUMN_NOTFICATION,
                    DbContract.SettingsEntry.COLUMN_TIMEZONENAME,
                    DbContract.SettingsEntry.COLUMN_CHECKUPTIME)

            val selection = DbContract.SettingsEntry.COLUMN_USERNAME + " = ?"
            val selectionAgrs = arrayOf(username)
            val myCorsor = db.query(DbContract.SettingsEntry.TABLE_NAME
                    ,protection
                    ,selection,
                    selectionAgrs,
                    null,null,null)
            val count = myCorsor.count
            val setttings = UserSettings()
            if(count >= 1)
            {

                while(myCorsor.moveToNext())
                {
                    setttings.Notification = myCorsor.getString(0)
                    setttings.TimeZoneName = myCorsor.getString(1)
                    setttings.UserCheckUpTime = myCorsor.getString(2)
                    break
                }
                return setttings
            }else
                return null
        }

        fun updateUserTimeZone(context:Context,username:String)
        {
            val userSettings = getSettings(context,username)
            if(userSettings != null)
            {
                if(userSettings!!.TimeZoneName != TimeZone.getDefault().id)
                {
                    UserPortal.updateUserTimeZone()
                    Portal.updateUserSettingsTimeZone(context
                            ,TimeZone.getDefault().id,username)
                }
            }
        }

        fun raiseUp()
        {
            val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
            val result = apiInterface?.raiseUp()
            result?.clone()?.enqueue(object: Callback<String> {
                override fun onFailure(call: Call<String>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<String>?, response: Response<String>?) {

                }

            })
        }

        fun isTimeHasCome(checkUpTime:String):Boolean
        {
            val parser = SimpleDateFormat("HH:mm")
            val currentDate = Calendar.getInstance().getTime()
            val currentTime = parser.parse( parser.format(currentDate) )
            val checkUpTime = parser.parse(checkUpTime)
            if(currentTime.after(checkUpTime))
            {
                return true
            }

            return false
        }

        fun sendNotify(context:Context)
        {
            Paper.init(context)
            val lang = Paper.book().read<String>("language")
            if(lang == null)
            {
                Paper.book().write("language","en")
            }

            val intent = Intent(context,SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            var bildirimPendding = PendingIntent.getActivity(context
                    ,10,intent,PendingIntent.FLAG_UPDATE_CURRENT)

            val myLangcontext = LocaleHelper.setLocale(context, Paper.book().read<String>("language"))
            showNotification(myLangcontext.getString(R.string.Check_Up_Time),
                    myLangcontext.getString(R.string.notfiy_mesaj),
                    context)
        }

        fun showNotification(title:String, content:String,context:Context) {
            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mBuilder = NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.rewire_launcher)
                    .setBadgeIconType(R.mipmap.rewire_launcher)// notification icon
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.rewire_launcher))
                    .setContentTitle(title) // title for notification
                    .setContentText(content)// message for notification
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // set alarm sound for notification
                    .setAutoCancel(true) // clear notification after click

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                val name = "RewireApp Notification"
                val description = "RewireApp's Notification"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("RewireApp-Quit Smoke Channel", name, importance)
                channel.description = description
                val notificationManager = context.
                        getSystemService(NotificationManager::class.java)

                notificationManager.createNotificationChannel(channel)
                mBuilder.setChannelId("RewireApp-Quit Smoke Channel")
            }

            val intent = Intent(context, SplashActivity::class.java)
            val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(1, mBuilder.build())
        }


    }

}