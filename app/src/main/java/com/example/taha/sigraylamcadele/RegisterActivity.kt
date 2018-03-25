package com.example.taha.sigraylamcadele

import android.content.Context
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taha.sigraylamcadele.Library.Portal
import kotlinx.android.synthetic.main.activity_register.*
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
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
                var newUser = User(edRegisterUserName.text.toString(),
                        edRegisterPass.text.toString(),
                        "user",
                        edRegisterMail.text.toString(),
                        accessToken = null)


                var apiInterface =  ApiClient.client?.create(ApiInterface::class.java)

                var result = apiInterface?.userRegister("application/json",newUser)

                result?.enqueue(object: Callback<String>{

                    override fun onFailure(call: Call<String>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        btnRegister.isEnabled = true
                        pbRegisterLoading.visibility = View.INVISIBLE
                        var mundi = response?.message()?.toString()
                        var body = response?.errorBody()?.string()

                        if(response?.message()?.toString() == "OK")
                        {
                            body = response?.body()
                            Toast.makeText(this@RegisterActivity,"Kayıt Başarılı",
                                    Toast.LENGTH_SHORT).show()
                            Portal.veriTabaniOlustur(this@RegisterActivity)
                            /* veri tabanı işlemleri */

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
