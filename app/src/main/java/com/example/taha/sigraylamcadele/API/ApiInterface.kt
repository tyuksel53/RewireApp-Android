package com.example.taha.sigraylamcadele.API

import com.example.taha.sigraylamcadele.Model.Comment
import com.example.taha.sigraylamcadele.Model.LoginResponse
import com.example.taha.sigraylamcadele.Model.Shares
import com.example.taha.sigraylamcadele.Model.User
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

    @GET("User/GetUserInfo")
    fun userInfo(@Header("Authorization") access_token:String):Call<User>

    @GET("Share/GetShares")
    fun getShares(@Header("Authorization") access_token: String,
                  @Query("skip") skip:Int):Call<List<Shares>>

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



}