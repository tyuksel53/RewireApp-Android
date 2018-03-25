package com.example.taha.sigraylamcadele

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.LoginResponse
import com.example.taha.sigraylamcadele.Model.User
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        tvNewUser.setOnClickListener {
            var intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

        tvLoginError.visibility = View.INVISIBLE
        pbLogin.visibility = View.INVISIBLE

        var apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        btnLogin.setOnClickListener {
            btnLogin.setEnabled(false)
            tvLoginError.visibility = View.INVISIBLE
            pbLogin.visibility = View.VISIBLE
            if( edLoginUsername.text.toString().isNotEmpty() && edLoginPassword.text.isNotEmpty() )
            {
                var result = apiInterface?.tokenAl(edLoginUsername.text.toString(),
                        edLoginPassword.text.toString(),"password")

                result?.enqueue(object: Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                        btnLogin.setEnabled(true)
                        pbLogin.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity,"Bir şeyler ters gitti",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                        btnLogin.setEnabled(true)
                        pbLogin.visibility = View.INVISIBLE
                        if(response?.message()?.toString() == "OK")
                        {
                            val body = response?.body()
                            Toast.makeText(this@LoginActivity,"Giris basarili",Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity,AnaEkranActivity::class.java)

                            val loggedInUser = User(edLoginUsername.text.toString(),
                                    edLoginPassword.text.toString(),"user",null,body?.access_token)

                            UserPortal.loggedInUser = loggedInUser
                            UserPortal.insertNewUser(this@LoginActivity,loggedInUser)
                            startActivity(intent)
                            finish()
                        }else
                        {
                            tvLoginError.visibility = View.VISIBLE
                        }

                    }

                })

            }else
            {
                if(edLoginUsername.text.toString().isEmpty())
                    edLoginUsername.setError("Kullanici Adi boş olamaz")
                if(edLoginPassword.text.toString().isEmpty())
                    edLoginPassword.setError("Şifre boş olamaz")

                pbLogin.visibility = View.INVISIBLE
                btnLogin.setEnabled(true)
            }


        }

    }


}
