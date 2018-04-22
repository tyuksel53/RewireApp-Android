package com.example.taha.sigraylamcadele.Library

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Database.DatabaseHelper
import com.example.taha.sigraylamcadele.Database.DbContract
import com.example.taha.sigraylamcadele.Model.ShareLike
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.Model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

/**
 * Created by Taha on 25-Mar-18.
 */
object UserPortal {

    var loggedInUser: User? = null
    var shares:ArrayList<Shares>? = null
    var newShare:Boolean = false
    var hasSharesChanged = false
    var myLangResource: Resources? = null
    private var userLikeds:ArrayList<ShareLike>? = null

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

    fun getLikes():ArrayList<ShareLike>?
    {

        if(UserPortal.userLikeds == null)
        {
            val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
            val likeResult = apiInterface
                    ?.getLikes("Bearer ${UserPortal.loggedInUser?.AccessToken}")
            likeResult?.enqueue(object: Callback<ArrayList<ShareLike>>{
                override fun onFailure(call: Call<ArrayList<ShareLike>>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<ArrayList<ShareLike>>?, response: Response<ArrayList<ShareLike>>?) {
                    if(response?.code() == 200)
                    {
                        userLikeds = response.body()
                    }
                }

            })
        }

        return userLikeds
    }

    fun insertLikes(shareLike:ShareLike):Boolean
    {
        if(userLikeds != null)
        {
            userLikeds!!.add(shareLike)
            return true
        }else
        {
            return false
        }
    }

    fun removeLikes(shareLike: ShareLike):Boolean
    {
        if(userLikeds != null)
        {
            userLikeds!!.remove(shareLike)
            return true
        }else
        {
            return false
        }
    }

    fun reset()
    {
        this.loggedInUser = null
        this.userLikeds = null
        this.hasSharesChanged = false
        this.newShare = false
        this.shares = null
    }

    fun fixDate(date:String):String
    {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = parser.parse(date)
        val formatter = SimpleDateFormat("dd-MM HH:mm")
        val formattedDate = formatter.format(date)

        return formattedDate
    }

    fun updateUserInfo()
    {
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        val result = apiInterface?.getUserInfo("Bearer ${loggedInUser!!.AccessToken}")
        result?.enqueue(object:Callback<User>{
            override fun onFailure(call: Call<User>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {

                if(response?.code() == 200)
                {
                    val body = response?.body()
                    loggedInUser?.LastLoginTime = body?.LastLoginTime
                    loggedInUser?.Email = body?.Email
                    loggedInUser?.TimeZoneId = body?.TimeZoneId
                    loggedInUser?.Role = body?.Role
                    loggedInUser?.RegisteredDate = body?.RegisteredDate
                }
            }

        })
    }
}