package com.example.taha.sigraylamcadele.Library

import android.content.ContentValues
import android.content.Context
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.Database.DatabaseHelper
import com.example.taha.sigraylamcadele.Database.DbContract
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.Model.User

/**
 * Created by Taha on 25-Mar-18.
 */
object UserPortal {

    var loggedInUser: User? = null
    var shares:List<Shares>? = null
    var counter:Int = 0

    override  fun toString(): String {


        return "${loggedInUser?.Username} , " +
                "${loggedInUser?.Password}, " +
                "${loggedInUser?.AccessToken}, "+
                "${loggedInUser?.Role}"
    }
    fun insertNewUser(context:Context, newUser:User)
    {
        val helper = DatabaseHelper(context)
        val db = helper.writableDatabase

        val yeniKayit = ContentValues()
        yeniKayit.put(DbContract.UserEntry.COLUMN_USERNAME,newUser.Username)
        yeniKayit.put(DbContract.UserEntry.COLUMN_PASSWORD,newUser.Password)
        yeniKayit.put(DbContract.UserEntry.COLUMN_ROLE,newUser.Role)
        yeniKayit.put(DbContract.UserEntry.COLUMN_EMAIL,newUser.Email)
        yeniKayit.put(DbContract.UserEntry.COLUMN_ACCESSTOKEN,newUser.AccessToken)

        var id = db.insert(DbContract.UserEntry.TABLE_NAME,null,yeniKayit)

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
    fun deleteLoggedInUser(context:Context):Boolean
    {
        val helper = DatabaseHelper(context)
        val db = helper.readableDatabase
        val args = arrayOf("100")

        var resultCount = db.delete(DbContract.UserEntry.TABLE_NAME,
                DbContract.UserEntry._ID + " < ?",
                args)

        if(resultCount > 0)
        {
            return true
        }

        return false
    }
}