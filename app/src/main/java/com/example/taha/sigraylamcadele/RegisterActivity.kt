package com.example.taha.sigraylamcadele

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taha.sigraylamcadele.Library.Portal
import kotlinx.android.synthetic.main.activity_register.*
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.UserPortal
import com.example.taha.sigraylamcadele.Model.LoginResponse
import com.example.taha.sigraylamcadele.Model.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.register_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
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
        val timezones = resources.getStringArray(R.array.timezones)
        val languageAdapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line,languages)
        val timeZoneAdapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,
                timezones)
        spLanguage.adapter = languageAdapter
        spTimeZones.adapter = timeZoneAdapter


        ivRegisterCancel.setOnClickListener {
            this@RegisterActivity.finish()
        }

        btnRegister.setOnClickListener {
            tvRegisterError.visibility = View.INVISIBLE
            btnRegister.isEnabled = false
            pbRegisterLoading.visibility = View.VISIBLE

            var registerControl:Boolean = true
            if(edRegisterMail.text.isNullOrEmpty() || edRegisterMail.text.isBlank())
            {
                edRegisterMail.error = "Mail adresi boş olamaz"
                registerControl = false
            }else
            {
                if(!Portal.isEmailValid(edRegisterMail.text.toString()))
                {
                    registerControl = false
                    edRegisterMail.error = "Lütfen düzgün formatta mail adresi giriniz"
                }
            }

            if(edRegisterUserName.text.isNullOrEmpty() || edRegisterUserName.text.isBlank())
            {
                edRegisterUserName.error = "Kullanici adi boş olamaz"
                registerControl = false
            }else
            {
                if(edRegisterUserName.text.toString().length < 4)
                {
                    edRegisterUserName.error = "Kullanıcı adi 4 küçük karakterden olmamalıdır"
                    registerControl = false
                }
            }

            if(edRegisterPassConfirm.text.isNullOrEmpty() || edRegisterPassConfirm.text.isBlank())
            {
                edRegisterPassConfirm.error = "Şifre boş olamaz"
                registerControl = false
            }

            if(edRegisterPass.text.isNullOrEmpty() || edRegisterPass.text.isBlank())
            {
                edRegisterPass.error = "Şifre boş olamaz"
                registerControl = false
            }else
            {
                if(edRegisterPass.text.toString().length < 4)
                {
                    edRegisterPass.error = "Şifreniz 4 karakterden küçük olmamalıdır"
                    registerControl = false
                }
                else
                if(edRegisterPassConfirm.text.isNotBlank() && edRegisterPassConfirm.text.isNotEmpty())
                {
                    if(edRegisterPassConfirm.text.toString() != edRegisterPass.text.toString())
                    {
                        edRegisterPassConfirm.error = "Şifreler eşleşmiyor"
                        registerControl = false
                    }
                }
            }

            if(registerControl)
            {
                var newUser = User(username = edRegisterUserName.text.toString(),
                        password = edRegisterPass.text.toString(),
                        role = "user",
                        email = edRegisterMail.text.toString(),
                        accessToken = null,
                        timeZoneId = spTimeZones.getSelectedItem().toString(),
                        language = spLanguage.getSelectedItem().toString())


                var apiInterface =  ApiClient.client?.create(ApiInterface::class.java)

                var result = apiInterface?.userRegister("application/json",newUser)

                result?.enqueue(object: Callback<String>{

                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Toast.makeText(this@RegisterActivity,
                                "Lütfen internet bağlantınızı kontrol edin",
                                Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {

                        var mundi = response?.message()?.toString()
                        var body = response?.errorBody()?.string()

                        if(response?.message()?.toString() == "OK")
                        {

                            val getToken = apiInterface?.tokenAl(newUser.Username!!,newUser.Password!!
                                    ,"password")
                            getToken?.enqueue(object:Callback<LoginResponse>{
                                override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                                    Toast.makeText(this@RegisterActivity,
                                            "Lütfen internet bağlantınızı kontrol edin",
                                            Toast.LENGTH_SHORT).show()
                                    btnRegister.isEnabled = true
                                    pbRegisterLoading.visibility = View.INVISIBLE
                                }

                                override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                                    if(response?.message()?.toString() == "OK")
                                    {
                                        val tokenBody = response.body()
                                        newUser.AccessToken = tokenBody?.access_token

                                        Toast.makeText(this@RegisterActivity,"Kayıt Başarılı",
                                                Toast.LENGTH_SHORT).show()
                                        UserPortal.deleteLoggedInUser(this@RegisterActivity)
                                        UserPortal.loggedInUser = newUser
                                        UserPortal.insertNewUser(this@RegisterActivity, newUser)

                                        var intent = Intent(this@RegisterActivity,AnaEkranActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()

                                    }else
                                    {
                                        Toast.makeText(this@RegisterActivity,
                                                "Bir şeyler ters gitti",
                                                Toast.LENGTH_SHORT).show()
                                    }
                                    btnRegister.isEnabled = true
                                    pbRegisterLoading.visibility = View.INVISIBLE
                                }

                            })


                        }else
                        {
                            if(body == "\"Bu kullanıcı adı zaten alınmış\"")
                            {
                                edRegisterUserName.error = "Bu kullanıcı adı zaten alınmış"
                                Toast.makeText(this@RegisterActivity,"Bu kullanıcı adı zaten alınmış",
                                        Toast.LENGTH_SHORT).show()
                            }else
                            {
                                tvRegisterError.visibility = View.VISIBLE
                                Toast.makeText(this@RegisterActivity,"Bir şeyler ters gitti",
                                        Toast.LENGTH_SHORT).show()
                            }

                            btnRegister.isEnabled = true
                            pbRegisterLoading.visibility = View.INVISIBLE
                        }
                    }

                })
            }else
            {
                btnRegister.isEnabled = true
                pbRegisterLoading.visibility = View.INVISIBLE
            }

        }

    }

}
