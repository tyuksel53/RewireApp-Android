package com.example.taha.sigraylamcadele.Library

import android.content.Context
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
    }

}