package com.rewire.mobile.app

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rewire.mobile.app.Library.Portal
import kotlinx.android.synthetic.main.activity_register.*
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.rewire.mobile.app.API.ApiClient
import com.rewire.mobile.app.API.ApiInterface
import com.rewire.mobile.app.Library.UserPortal
import com.rewire.mobile.app.Model.LoginResponse
import com.rewire.mobile.app.Model.User
import com.rewire.mobile.app.PaperHelper.LocaleHelper
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.android.synthetic.main.register_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class RegisterActivity : AppCompatActivity() {

    var isUserCanClick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        isUserCanClick = true
        updateView(Paper.book().read("language"))


        tvRegisterError.visibility = View.INVISIBLE


        val inflator = this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflator.inflate(R.layout.register_toolbar, null)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.customView = v

        val languages = resources.getStringArray(R.array.languages)
        val languageAdapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line,languages)
        spLanguage.adapter = languageAdapter


        tvRegisterSelectLanugage.setText(UserPortal.myLangResource!!.getString(R.string.dil_secin))

        ivRegisterCancel.setOnClickListener {
            this@RegisterActivity.finish()
        }

        btnRegister.setOnClickListener {
            if(isUserCanClick)
            {
                tvRegisterError.visibility = View.INVISIBLE
                isUserCanClick = false
                pbRegisterLoading.visibility = View.VISIBLE

                var registerControl = true
                if(edRegisterMail.text.isNullOrEmpty() || edRegisterMail.text.isBlank())
                {
                    edRegisterMail.error = UserPortal.myLangResource!!.getString(R.string.mailHataBos)
                    registerControl = false
                }else
                {
                    if(!Portal.isEmailValid(edRegisterMail.text.toString()))
                    {
                        registerControl = false
                        edRegisterMail.error = UserPortal.myLangResource!!.getString(R.string.mailHataFormat)
                    }else if(edRegisterMail.text.toString().length > 254)
                    {
                        registerControl = false
                        edRegisterMail.error = UserPortal.myLangResource!!.getString(R.string.mailHataUzunluk)
                    }
                }

                if(edRegisterUserName.text.isNullOrEmpty() || edRegisterUserName.text.isBlank())
                {
                    edRegisterUserName.error = UserPortal.myLangResource!!.getString(R.string.hataGirisKullanıcıadi)
                    registerControl = false
                }else
                {
                    if(edRegisterUserName.text.toString().length < 4)
                    {
                        edRegisterUserName.error = UserPortal.myLangResource!!.getString(R.string.hataKullaniciAdiMinUzunluk)
                        registerControl = false
                    }else if(edRegisterUserName.text.toString().length > 50)
                    {
                        edRegisterUserName.error = UserPortal.myLangResource!!.getString(R.string.hataKullaniciAdiMaxUzunluk)
                        registerControl = false
                    }
                }

                if(edRegisterPassConfirm.text.isNullOrEmpty() || edRegisterPassConfirm.text.isBlank())
                {
                    edRegisterPassConfirm.error = UserPortal.myLangResource!!.getString(R.string.hataGirisSifre)
                    registerControl = false
                }

                if(edRegisterPass.text.isNullOrEmpty() || edRegisterPass.text.isBlank())
                {
                    edRegisterPass.error = UserPortal.myLangResource!!.getString(R.string.hataGirisSifre)
                    registerControl = false
                }else
                {
                    if(edRegisterPass.text.toString().length < 4)
                    {
                        edRegisterPass.error = UserPortal.myLangResource!!.getString(R.string.hataSifreUzunluk)
                        registerControl = false
                    }
                    else
                        if(edRegisterPassConfirm.text.isNotBlank() && edRegisterPassConfirm.text.isNotEmpty())
                        {
                            if(edRegisterPassConfirm.text.toString() != edRegisterPass.text.toString())
                            {
                                edRegisterPassConfirm.error = UserPortal.myLangResource!!.getString(R.string.hataSifreEslesme)
                                registerControl = false
                            }
                        }
                }
                val timeZone = TimeZone.getDefault()

                if(registerControl)
                {
                    val newUser = User(username = edRegisterUserName.text.toString().trim(),
                            password = edRegisterPass.text.toString(),
                            role = "user",
                            email = edRegisterMail.text.toString(),
                            accessToken = null,
                            timeZoneId = timeZone.id ,
                            language = spLanguage.getSelectedItem().toString(),
                            lastLoginTime = null,
                            registeredDate = null,
                            clearDayCount = null)


                    val apiInterface =  ApiClient.client?.create(ApiInterface::class.java)

                    val result = apiInterface?.userRegister("application/json",newUser)

                    result?.enqueue(object: Callback<String>{

                        override fun onFailure(call: Call<String>?, t: Throwable?) {
                            Toasty.error(this@RegisterActivity,
                                    UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),
                                    Toast.LENGTH_SHORT).show()
                            isUserCanClick = true
                            pbRegisterLoading.visibility = View.INVISIBLE

                        }

                        override fun onResponse(call: Call<String>?, response: Response<String>?) {

                            val body = response?.errorBody()?.string()

                            if(response?.message()?.toString() == "OK")
                            {
                                val getToken = apiInterface?.tokenAl(newUser.Username!!,newUser.Password!!
                                        ,"password")
                                getToken?.enqueue(object:Callback<LoginResponse>{
                                    override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                                        Toasty.error(this@RegisterActivity,
                                                UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),
                                                Toast.LENGTH_SHORT).show()
                                        isUserCanClick = true
                                        pbRegisterLoading.visibility = View.INVISIBLE
                                    }

                                    override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                                        if(response?.message()?.toString() == "OK")
                                        {
                                            setLanguage(newUser.Language!!)
                                            val tokenBody = response.body()
                                            newUser.AccessToken = tokenBody?.access_token

                                            Toasty.success(this@RegisterActivity,
                                                    UserPortal.myLangResource!!.getString(R.string.kayitBasarili),
                                                    Toast.LENGTH_SHORT).show()
                                            UserPortal.deleteLoggedInUser(this@RegisterActivity)
                                            UserPortal.loggedInUser = newUser
                                            UserPortal.insertNewUser(this@RegisterActivity, newUser)
                                            UserPortal.updateUserInfo()
                                            UserPortal.getLikes()
                                            UserPortal.userDates = ArrayList()
                                            Portal.insertUserSettings(this@RegisterActivity,
                                                    newUser.TimeZoneId!!,"20:00",
                                                    UserPortal.loggedInUser!!.Username!!)
                                            val intent = Intent(this@RegisterActivity,HomeActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            finish()

                                        }else
                                        {
                                            Toast.makeText(this@RegisterActivity,
                                                    UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                                    Toast.LENGTH_SHORT).show()
                                        }
                                        isUserCanClick = true
                                        pbRegisterLoading.visibility = View.INVISIBLE
                                    }

                                })


                            }else
                            {
                                if(body == "\"Bu kullanıcı adı zaten alınmış\"")
                                {
                                    edRegisterUserName.error = UserPortal.myLangResource!!.getString(R.string.kullaniciAdiAlinmis)
                                    Toasty.error(this@RegisterActivity,
                                            UserPortal.myLangResource!!.getString(R.string.kullaniciAdiAlinmis),
                                            Toast.LENGTH_SHORT).show()

                                }else
                                {
                                    tvRegisterError.visibility = View.VISIBLE
                                    Toasty.error(this@RegisterActivity,
                                            UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                            Toast.LENGTH_SHORT).show()
                                }

                                isUserCanClick = true
                                pbRegisterLoading.visibility = View.INVISIBLE
                            }
                        }

                    })
                }else
                {
                    Toasty.error(this@RegisterActivity,
                            UserPortal.myLangResource!!.getString(R.string.bosluklari_doldur_adamgibi),
                            Toast.LENGTH_SHORT).show()
                    isUserCanClick = true
                    pbRegisterLoading.visibility = View.INVISIBLE
                }
            }


        }

    }

    private fun setLanguage(lang:String) {
        when(lang)
        {
            "türkçe" ->
            {
                Paper.book().write("language","tr")
                LocaleHelper.setLocale(this@RegisterActivity,Paper.book().read<String>("language"))
            }
            "english" ->
            {
                Paper.book().write("language","en")
                LocaleHelper.setLocale(this@RegisterActivity,Paper.book().read<String>("language"))
            }
            "русский" ->
            {
                Paper.book().write("language","ru")
                LocaleHelper.setLocale(this@RegisterActivity,Paper.book().read<String>("language"))
            }
            "français" ->
            {
                Paper.book().write("language","fr")
                LocaleHelper.setLocale(this@RegisterActivity,Paper.book().read<String>("language"))
            }
            "italiano" ->
            {
                Paper.book().write("language","it")
                LocaleHelper.setLocale(this@RegisterActivity,Paper.book().read<String>("language"))
            }
            "español" ->
            {
                Paper.book().write("language","es")
                LocaleHelper.setLocale(this@RegisterActivity,Paper.book().read<String>("language"))
            }
            "deutsch" ->
            {
                Paper.book().write("language","de")
                LocaleHelper.setLocale(this@RegisterActivity,Paper.book().read<String>("language"))
            }

        }
    }

    private fun updateView(lang: String) {
        val context = LocaleHelper.setLocale(this@RegisterActivity,lang)
        UserPortal.myLangResource = context.resources

        edRegisterMail.setHint(UserPortal.myLangResource!!.getString(R.string.mail_adresiniz))
        edRegisterPass.setHint(UserPortal.myLangResource!!.getString(R.string.sifrenizi_girin))
        edRegisterPassConfirm.setHint(UserPortal.myLangResource!!.getString(R.string.sifre_tekrar))
        edRegisterUserName.setHint(UserPortal.myLangResource!!.getString(R.string.kullanici_adi))
        tvRegisterError.setText(UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers))
        btnRegister.setText(UserPortal.myLangResource!!.getString(R.string.yeni_hesap))
        tvRegisterYeniHesap.setText(UserPortal.myLangResource!!.getString(R.string.yeni_hesap))
    }

}
