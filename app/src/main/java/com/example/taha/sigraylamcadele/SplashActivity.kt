package com.example.taha.sigraylamcadele

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var myUser = Portal.autoLogin(this)

        if(myUser != null)
        {
            val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

            var result = apiInterface?.tokenAl(myUser.Username!! ,myUser.Password!!,"password")

            result?.enqueue(object: Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                    if(response?.message()?.toString() == "OK")
                    {
                        var body = response?.body()
                        Toast.makeText(this@SplashActivity,"Giris basarili", Toast.LENGTH_LONG).show()
                        var intent = Intent(this@SplashActivity,AnaEkranActivity::class.java)
                        myUser.AccessToken = body?.access_token
                        UserPortal.loggedInUser = myUser
                        startActivity(intent)
                        finish()
                    }
                }

            })
        }else
        {
            var intent = Intent(this@SplashActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
