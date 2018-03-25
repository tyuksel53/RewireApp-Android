package com.example.taha.sigraylamcadele.Library

import android.content.ContentValues
import android.content.Context
import com.example.taha.sigraylamcadele.Database.DatabaseHelper
import com.example.taha.sigraylamcadele.Database.DbContract
import com.example.taha.sigraylamcadele.Model.User
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
        fun veriTabaniOlustur(context:Context)
        {
            try{
                val myDb = context.openOrCreateDatabase("users",Context.MODE_PRIVATE,null)

                /*myDb.execSQL("Create Table if not exists users (name VARCHAR, age INT)")
                myDb.execSQL("Insert into users (name,age) values('taha','12')")*/

                var query = myDb.rawQuery("Select * from users",null)

                val nameIndex = query.getColumnIndex("name")
                var ageIndex = query.getColumnIndex("age")

                query.moveToFirst()

                while(query != null)
                {
                    var name = query.getString(nameIndex)
                    var age = query.getInt(ageIndex)
                    query.moveToNext()
                }

                query?.close()

            }catch (e:Exception)
            {
                e.printStackTrace()
            }
        }


        fun kullaniciGuncelle(context:Context)
        {
            var helper = DatabaseHelper(context)
            var db =  helper.readableDatabase

            var guncellenenDegerler = ContentValues()
            guncellenenDegerler.put(DbContract.UserEntry.COLUMN_ROLE,"admin")

            var args = arrayOf("1")

            var resultCount =  db.update(DbContract.UserEntry.TABLE_NAME,guncellenenDegerler,
                    DbContract.UserEntry._ID + " = ?",args)


        }

        fun kullanicilariSil(context:Context)
        {
            val helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            var args = arrayOf("100")

            var resultCount = db.delete(DbContract.UserEntry.TABLE_NAME,
                    DbContract.UserEntry._ID + " < ?",
                    args)
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

        fun kullanicilariOku(context:Context) {

            var helper = DatabaseHelper(context)
            var db = helper.readableDatabase

            var protection = arrayOf(DbContract.UserEntry.COLUMN_USERNAME,
                    DbContract.UserEntry.COLUMN_EMAIL,
                    DbContract.UserEntry.COLUMN_PASSWORD,
                    DbContract.UserEntry.COLUMN_ROLE)

            var selection = DbContract.UserEntry._ID + " = 1"

            var selectionAgs = arrayOf("100")

            var myCorsor = db.query(DbContract.UserEntry.TABLE_NAME,protection,selection,selectionAgs,null,null,null)

            var count = myCorsor.count

            var myString:String = ""

            while(myCorsor.moveToNext())
            {
                for(i in 0 until myCorsor.columnCount)
                {
                    myString += myCorsor.getString(i) + " "
                }

                myString += "\n"
            }


            myCorsor.close()
            db.close()
        }

         fun yeniKullaniciEkle(username:String,password:String,email:String,role:String,context:Context) {

            var helper = DatabaseHelper(context)
            var db = helper.writableDatabase


            /*yöntem 1*/
            /*var query:String = "Insert INTO users("+
                    DbContract.UserEntry.COLUMN_USERNAME + "," +
                    DbContract.UserEntry.COLUMN_PASSWORD + "," +
                    DbContract.UserEntry.COLUMN_EMAIL + "," +
                    DbContract.UserEntry.COLUMN_ROLE + ")" +
                    " values('$username', '$password' , '$email' , '$role' )"

            db.execSQL(query)*/

            /*yöntem 2*/
            var yeniKayit = ContentValues()
            yeniKayit.put(DbContract.UserEntry.COLUMN_USERNAME,username)
            yeniKayit.put(DbContract.UserEntry.COLUMN_PASSWORD,password)
            yeniKayit.put(DbContract.UserEntry.COLUMN_EMAIL,email)
            yeniKayit.put(DbContract.UserEntry.COLUMN_ROLE,role)

            var id = db.insert(DbContract.UserEntry.TABLE_NAME,null,yeniKayit)


        }
    }

}