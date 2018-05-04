package com.example.taha.sigraylamcadele.API

import com.example.taha.sigraylamcadele.Model.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Taha on 28-Feb-18.
 */
interface ApiInterface {

    @FormUrlEncoded
    @POST("getToken")
    fun tokenAl(@Field("username") username:String,
                @Field("password") password:String,
                @Field("grant_type") grant_type:String):Call<LoginResponse>


    @POST("Register/RegisterUser")
    fun userRegister(@Header("Content-Type") content_type:String,
                     @Body user: User):Call<String>

    @GET("Share/GetShares")
    fun getShares(@Header("Authorization") access_token: String,
                  @Query("skip") skip:Int,
                  @Query("orderby")orderby:String,
                  @Query("time") time:String):Call<ArrayList<Shares>>

    @POST("Share/AddShare")
    fun postShare(@Header("Authorization") access_token:String,
                  @Body share:Shares,
                  @Header("Content-type") content_type:String = "application/json"):Call<String>

    @GET("Comment/GetComments")
    fun getComments(@Query("shareId") shareId:Int,
                    @Header("Authorization") access_token:String):Call<ArrayList<Comment>>

    @POST("Comment/PostComment")
    fun postComment(@Header("Authorization") access_token: String,
                    @Body comment:Comment,
                    @Header("Content-type") content_type:String = "application/json"):Call<String>

    @PUT("User/UpdateUserLanguage")
    fun updateUserLang(@Header("Authorization") access_token:String,
                       @Query("lang") language:String):Call<String>

    @GET("Share/GetLikes")
    fun getLikes(@Header("Authorization") access_token:String):Call<ArrayList<ShareLike>>

    @POST("Share/UserLiked")
    fun userLiked(@Header("Authorization") access_token:String,
                  @Body like:ShareLike):Call<String>

    @POST("Share/ShareReport")
    fun shareReport(@Header("Authorization") access_token:String,
                    @Query("shareId") shareId:String):Call<String>

    @POST("Comment/ReportComment")
    fun commentReport(@Header("Authorization") access_token:String,
                      @Query("commentId") commnetId:String):Call<String>

    @GET("User/getUserTime")
    fun getUserTime(@Header("Authorization") access_token:String):Call<String>

    @GET("User/getUserInfo")
    fun getUserInfo(@Header("Authorization") access_toke:String):Call<User>

    @GET("Date/GetDates")
    fun getDates(@Header("Authorization") access_token:String):Call<ArrayList<UserDate>>

    @POST("Date/InsertDates")
    fun insertDates(@Header("Authorization") access_token:String,
                    @Header("Content-type") content_type:String = "application/json",
                    @Body dates:ArrayList<UserDate>):Call<String>

    @PUT("Date/UpdateDate")
    fun updateDates(@Header("Authorization") access_token:String,
                    @Header("Content-type") content_type:String = "application/json",
                    @Body dates:ArrayList<UserDate>):Call<String>

    @GET("User/GetLeaderboard")
    fun getLeaderBoard(@Header("Authorization") access_token:String):Call<ArrayList<User>>

    @GET("Share/GetUserLikedShares")
    fun getUserLikes(@Header("Authorization") access_token:String):Call<ArrayList<Shares>>


    @GET("Share/GetUserShares")
    fun getUserPosts(@Header("Authorization") access_token:String):Call<ArrayList<Shares>>

    @DELETE("Share/DeleteShare")
    fun deleteShare(@Header("Authorization") access_token:String,
                    @Query("shareId") shareId:String):Call<String>

    @PUT("Share/UpdateShare")
    fun updateShare(@Header("Authorization") access_token:String,
                    @Header("Content-type") content_type:String = "application/json",
                    @Body updateShare:Shares):Call<String>

    @DELETE("Date/DeleteAllDates")
    fun deleteAllDates(@Header("Authorization") access_token:String):Call<String>

    @PUT("User/SifreGuncelleme")
    fun sifreGuncelle(@Header("Authorization") access_token:String,
                      @Query("yeniSifre") yeniSifre:String):Call<String>
    @PUT("User/UpdateUserZone")
    fun updateTimeZone(@Header("Authorization") access_token:String,
                       @Query("userZone") userZone:String):Call<String>

}