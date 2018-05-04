package com.example.taha.sigraylamcadele.Library

import android.content.ContentValues
import android.content.Context
import com.example.taha.sigraylamcadele.Database.DatabaseHelper
import com.example.taha.sigraylamcadele.Database.DbContract
import com.example.taha.sigraylamcadele.Model.User
import com.example.taha.sigraylamcadele.Model.UserSettings
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
        fun updateUserSettingsTimeZone(context:Context,timeZone:String)
        {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase

            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.SettingsEntry.COLUMN_TIMEZONENAME,timeZone)

            val args = arrayOf("1")

            var resultCount =  db.update(DbContract.SettingsEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.SettingsEntry._ID + " = ?",args)
        }
        fun updateUserSettingsCheckUpTime(context:Context,checkUpTime:String)
        {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase

            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.SettingsEntry.COLUMN_CHECKUPTIME,checkUpTime)

            val args = arrayOf("1")

            var resultCount =  db.update(DbContract.SettingsEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.SettingsEntry._ID + " = ?",args)
        }

        fun updateUserSettingsNotification(context:Context,notificationSettings:String)
        {
            val helper = DatabaseHelper(context)
            val db =  helper.readableDatabase
            val guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.SettingsEntry.COLUMN_NOTFICATION,notificationSettings)

            val args = arrayOf("1")

            var resultCount =  db.update(DbContract.SettingsEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.SettingsEntry._ID + " = ?",args)
        }
        fun insertUserSettings(context:Context,zoneName:String,checkUpTime:String)
        {
            val helper = DatabaseHelper(context)
            val db = helper.writableDatabase

            val yeniKayit = ContentValues()
            yeniKayit.put(DbContract.SettingsEntry.COLUMN_NOTFICATION,"YES")
            yeniKayit.put(DbContract.SettingsEntry.COLUMN_TIMEZONENAME,zoneName)
            yeniKayit.put(DbContract.SettingsEntry.COLUMN_CHECKUPTIME,checkUpTime)

            var id = db.insert(DbContract.SettingsEntry.TABLE_NAME,null,yeniKayit)
        }

        fun deleteUserSettings(context:Context):Boolean
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val args = arrayOf("100")

            val resultCount = db.delete(DbContract.SettingsEntry.TABLE_NAME,
                    DbContract.UserEntry._ID + " < ?",
                    args)

            if(resultCount > 0)
            {
                return true
            }

            return false
        }

        fun getSettings(context:Context):UserSettings?
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase

            val protection = arrayOf(DbContract.SettingsEntry.COLUMN_NOTFICATION,
                    DbContract.SettingsEntry.COLUMN_TIMEZONENAME,
                    DbContract.SettingsEntry.COLUMN_CHECKUPTIME)

            val selection = DbContract.SettingsEntry._ID + " = ?"
            val selectionAgrs = arrayOf("1")
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

        fun updateUserTimeZone(context:Context)
        {
            val userSettings = getSettings(context)
            if(userSettings != null)
            {
                if(userSettings!!.TimeZoneName != TimeZone.getDefault().id)
                {
                    UserPortal.updateUserTimeZone()
                    Portal.updateUserSettingsTimeZone(context
                            ,TimeZone.getDefault().id)
                }
            }
        }



    }

}