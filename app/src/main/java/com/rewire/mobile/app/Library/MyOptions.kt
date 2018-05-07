package com.rewire.mobile.app.Library

import android.content.ContentValues
import android.content.Context
import com.rewire.mobile.app.Database.DatabaseHelper
import com.rewire.mobile.app.Database.DbContract

class MyOptions {

    fun kullaniciGuncelle(context: Context)
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

}