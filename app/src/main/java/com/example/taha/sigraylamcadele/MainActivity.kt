package com.example.taha.sigraylamcadele

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Model.LoginResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLoginError.visibility = View.INVISIBLE
        pbLogin.visibility = View.INVISIBLE

        var apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        btnLogin.setOnClickListener {
            tvLoginError.visibility = View.INVISIBLE
            pbLogin.visibility = View.VISIBLE

            var result = apiInterface?.tokenAl(edLoginUsername.text.toString(),
                    edLoginPassword.text.toString(),"password")

            result?.enqueue(object: Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                    pbLogin.visibility = View.INVISIBLE
                    Toast.makeText(this@MainActivity,"Bir şeyler ters gitti",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                    pbLogin.visibility = View.INVISIBLE
                    if(response?.message()?.toString() == "OK")
                    {
                        var body = response?.body()
                        Toast.makeText(this@MainActivity,"Giris basarili",Toast.LENGTH_LONG).show()
                    }else
                    {
                        tvLoginError.visibility = View.VISIBLE
                    }

                }

            })
        }

    }
}
