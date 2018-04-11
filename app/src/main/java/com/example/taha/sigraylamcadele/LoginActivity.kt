package com.example.taha.sigraylamcadele

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.LoginResponse
import com.example.taha.sigraylamcadele.Model.User
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var myResources: Resources
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttacth(newBase!!,"en"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Paper.init(this)
        val lang = Paper.book().read<String>("language")
        if(lang == null)
        {
            Paper.book().write("language","en")
        }

        updateView(Paper.book().read("language"))

        btnLoginRegister.setOnClickListener {
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
                        Toast.makeText(this@LoginActivity,myResources.getString(R.string.hataBaglantiBozuk)
                                ,Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                        btnLogin.setEnabled(true)
                        pbLogin.visibility = View.INVISIBLE
                        if(response?.message()?.toString() == "OK")
                        {
                            val body = response?.body()
                            val intent = Intent(this@LoginActivity,AnaEkranActivity::class.java)

                            val loggedInUser = User(edLoginUsername.text.toString(),
                                    edLoginPassword.text.toString(),
                                    "user",
                                    null,
                                    body?.access_token,
                                    null,
                                    null)

                            UserPortal.loggedInUser = loggedInUser
                            UserPortal.insertNewUser(this@LoginActivity,loggedInUser)
                            startActivity(intent)
                            finish()
                        }else if(response?.code() == 500)
                        {
                            Toast.makeText(this@LoginActivity,myResources.getString(R.string.hataBirSeylerTers),
                                    Toast.LENGTH_LONG).show()
                        }else
                        {
                            tvLoginError.visibility = View.VISIBLE
                        }

                    }

                })

            }else
            {
                if(edLoginUsername.text.toString().isEmpty())
                    edLoginUsername.setError(myResources.getString(R.string.hataGirisKullanıcıadi))
                if(edLoginPassword.text.toString().isEmpty())
                    edLoginPassword.setError(myResources.getString(R.string.hataGirisSifre))

                pbLogin.visibility = View.INVISIBLE
                btnLogin.setEnabled(true)
            }


        }

    }

    fun updateView(lang:String)
    {
        var context = LocaleHelper.setLocale(this@LoginActivity,lang)
        myResources = context.resources

        edLoginUsername.setHint(myResources.getString(R.string.kullanici_adi))
        edLoginPassword.setHint(myResources.getString(R.string.sifre))
        tvForgetPassword.setText(myResources.getString(R.string.sifremi_unuttum))
        btnLogin.setText(myResources.getString(R.string.giris_yap))
        btnLoginRegister.setText(myResources.getString(R.string.kayit_ol))
        tvLoginError.setText(myResources.getString(R.string.yanlis_kullanici_adi_veya_sifre))
        tvLoginYada.setText(myResources.getString(R.string.veya))

    }


}
