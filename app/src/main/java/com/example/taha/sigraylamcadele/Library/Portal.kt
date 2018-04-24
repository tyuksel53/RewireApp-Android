package com.example.taha.sigraylamcadele.Library

import android.content.ContentValues
import android.content.Context
import com.example.taha.sigraylamcadele.Database.DatabaseHelper
import com.example.taha.sigraylamcadele.Database.DbContract
import com.example.taha.sigraylamcadele.Model.User
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
            var myUser = User()
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

    }

}