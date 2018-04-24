package com.example.taha.sigraylamcadele

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.LoginResponse
import com.example.taha.sigraylamcadele.Model.User
import com.example.taha.sigraylamcadele.Model.UserDate
import com.example.taha.sigraylamcadele.PaperHelper.LocaleHelper
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        updateView(Paper.book().read("language"))
        val user = Portal.autoLogin(this)
        if( user != null)
        {
            edLoginUsername.setText(user.Username)
            edLoginPassword.setText(user.Password)
        }

        btnLoginRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

        tvLoginError.visibility = View.INVISIBLE
        pbLogin.visibility = View.INVISIBLE

        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        btnLogin.setOnClickListener {
            btnLogin.setEnabled(false)
            tvLoginError.visibility = View.INVISIBLE
            pbLogin.visibility = View.VISIBLE
            if( edLoginUsername.text.toString().isNotEmpty() && edLoginPassword.text.isNotEmpty() )
            {
                val result = apiInterface?.tokenAl(edLoginUsername.text.toString(),
                        edLoginPassword.text.toString(),"password")

                result?.enqueue(object: Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                        btnLogin.setEnabled(true)
                        pbLogin.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity,UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk)
                                ,Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                        when {
                            response?.message()?.toString() == "OK" -> {
                                val body = response.body()

                                val loggedInUser = User(edLoginUsername.text.toString(),
                                        edLoginPassword.text.toString(),
                                        "user",
                                        null,
                                        body?.access_token,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null)

                                UserPortal.loggedInUser = loggedInUser
                                UserPortal.updateUserInfo()
                                UserPortal.getLikes()
                                UserPortal.insertNewUser(this@LoginActivity,loggedInUser)
                                val getDates = apiInterface.getDates("Bearer ${body?.access_token}")
                                getDates.enqueue(object:Callback<ArrayList<UserDate>>{
                                    override fun onFailure(call: Call<ArrayList<UserDate>>?, t: Throwable?) {
                                        btnLogin.setEnabled(true)
                                        pbLogin.visibility = View.INVISIBLE
                                        Toasty.info(this@LoginActivity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                                Toast.LENGTH_LONG).show()
                                    }

                                    override fun onResponse(call: Call<ArrayList<UserDate>>?, response: Response<ArrayList<UserDate>>?) {
                                        btnLogin.setEnabled(true)
                                        pbLogin.visibility = View.INVISIBLE
                                        if(response?.code() == 200) {
                                            UserPortal.userDates = response.body()
                                            val intent = Intent(this@LoginActivity,AnaEkranActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            finish()
                                        }else {
                                            Toasty.info(this@LoginActivity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                                    Toast.LENGTH_LONG).show()
                                        }
                                    }

                                })

                            }
                            response?.code() == 500 ->{
                                btnLogin.setEnabled(true)
                                pbLogin.visibility = View.INVISIBLE
                                Toasty.info(this@LoginActivity,UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                        Toast.LENGTH_LONG).show()
                            }
                            else ->{
                                btnLogin.setEnabled(true)
                                pbLogin.visibility = View.INVISIBLE
                                tvLoginError.visibility = View.VISIBLE
                            }
                        }

                    }

                })

            }else
            {
                if(edLoginUsername.text.toString().isEmpty())
                    edLoginUsername.setError(UserPortal.myLangResource!!.getString(R.string.hataGirisKullanıcıadi))
                if(edLoginPassword.text.toString().isEmpty())
                    edLoginPassword.setError(UserPortal.myLangResource!!.getString(R.string.hataGirisSifre))

                pbLogin.visibility = View.INVISIBLE
                btnLogin.setEnabled(true)
            }


        }

    }

    fun updateView(lang:String)
    {
        val context = LocaleHelper.setLocale(this@LoginActivity,lang)
        UserPortal.myLangResource = context.resources

        edLoginUsername.setHint(UserPortal.myLangResource!!.getString(R.string.kullanici_adi))
        edLoginPassword.setHint(UserPortal.myLangResource!!.getString(R.string.sifre))
        tvForgetPassword.setText(UserPortal.myLangResource!!.getString(R.string.sifremi_unuttum))
        btnLogin.setText(UserPortal.myLangResource!!.getString(R.string.giris_yap))
        btnLoginRegister.setText(UserPortal.myLangResource!!.getString(R.string.kayit_ol))
        tvLoginError.setText(UserPortal.myLangResource!!.getString(R.string.yanlis_kullanici_adi_veya_sifre))
        tvLoginYada.setText(UserPortal.myLangResource!!.getString(R.string.veya))

    }


}
