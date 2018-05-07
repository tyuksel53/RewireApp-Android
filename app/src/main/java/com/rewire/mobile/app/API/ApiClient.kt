package com.rewire.mobile.app.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient
{

    val BASE_URL = "http://api.yukselalgoritma.net/"

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
