package com.example.taha.sigraylamcadele

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Model.LoginResponse
import com.example.taha.sigraylamcadele.Model.User
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Paper.init(this)
        val lang = Paper.book().read<String>("language")
        if(lang == null)
        {
            Paper.book().write("language","en")
        }
        val context = LocaleHelper.setLocale(this,Paper.book().read<String>("language"))
        UserPortal.myLangResource = context.resources


        var myUser = Portal.autoLogin(this)

        if(myUser != null)
        {
            val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

            var result = apiInterface?.tokenAl(myUser.Username!! ,myUser.Password!!,"password")

            result?.enqueue(object: Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                    redirectToLogin()
                }

                override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                    if(response?.message()?.toString() == "OK")
                    {
                        val body = response?.body()
                        var intent = Intent(this@SplashActivity,AnaEkranActivity::class.java)
                        myUser.AccessToken = body?.access_token
                        UserPortal.loggedInUser = myUser
                        startActivity(intent)
                        finish()
                    }else
                    {
                        redirectToLogin()
                        UserPortal.deleteLoggedInUser(this@SplashActivity)
                    }
                }

            })
        }else
        {
            redirectToLogin()
        }

    }


    private fun redirectToLogin()
    {
        object:CountDownTimer(1000,100)
        {
            override fun onFinish() {
                val intent = Intent(this@SplashActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()
    }
}
