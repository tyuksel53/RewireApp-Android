package com.example.taha.sigraylamcadele.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient
{

    val BASE_URL = "http://192.168.1.105/api/"

    private var retrofit: Retrofit? = null

    val client:Retrofit?
        get() {
            if(retrofit == null)
            {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }

            return retrofit
        }



}
