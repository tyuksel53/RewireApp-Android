package com.example.taha.sigraylamcadele.Library

import android.app.Notification
import android.content.ContentValues
import android.content.Context
import android.util.Log
import br.com.goncalves.pugnotification.notification.PugNotification
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Database.DatabaseHelper
import com.example.taha.sigraylamcadele.Database.DbContract
import com.example.taha.sigraylamcadele.Model.User
import com.example.taha.sigraylamcadele.Model.UserSettings
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper
import com.example.taha.sigraylamcadele.R
import com.example.taha.sigraylamcadele.SplashActivity
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
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
            val context = LocaleHelper.setLocale(context, Paper.book().read<String>("language"))
            PugNotification.with(context)
                    .load()
                    .title(context.getString(R.string.Check_Up_Time))
                    .message(context.getString(R.string.checkUp_message_notfiy))
                    .smallIcon(R.mipmap.rewire_launcher)
                    .largeIcon(R.mipmap.rewire_launcher)
                    .flags(Notification.DEFAULT_ALL)
                    .autoCancel(true)
                    .click(SplashActivity::class.java)
                    .simple()
                    .build()
        }


    }

}