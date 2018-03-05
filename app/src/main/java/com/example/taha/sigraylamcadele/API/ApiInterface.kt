package com.example.taha.sigraylamcadele.API

import com.example.taha.sigraylamcadele.Model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Taha on 28-Feb-18.
 */
interface ApiInterface {

    @FormUrlEncoded
    @POST("getToken")
    fun tokenAl(@Field("username") username:String,
                @Field("password") password:String,
                @Field("grant_type") grant_type:String):Call<LoginResponse>



}