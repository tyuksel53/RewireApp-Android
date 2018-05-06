package com.example.taha.sigraylamcadele

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Library.UserPortal
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_sifre_degis.*
import kotlinx.android.synthetic.main.register_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sifre_degis)


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
            this@ChangePasswordActivity.finish()
        }

        pbSifreDegis.visibility = View.INVISIBLE

        edSifreGuncelleEski.setHint(UserPortal.myLangResource!!.getString(R.string.eski_sifre))
        edSifreGuncelleYeni.setHint(UserPortal.myLangResource!!.getString(R.string.yeni_sifre))
        edSifreGuncelleYeniTekrar.setHint(UserPortal.myLangResource!!.getString(R.string.yeni_sifre_tekrar))
        btnSifreDegisKaydet.setText(UserPortal.myLangResource!!.getString(R.string.kaydet))

        btnSifreDegisKaydet.setOnClickListener {

            var registerControl = true
            if(edSifreGuncelleEski.text.toString() != UserPortal.loggedInUser!!.Password)
            {
                edSifreGuncelleEski.setError("Şifre doğru değil")
                registerControl = false
            }

            if(edSifreGuncelleYeni.text.isNullOrEmpty() || edSifreGuncelleYeni.text.isBlank())
            {
                edSifreGuncelleYeni.error = UserPortal.myLangResource!!.getString(R.string.hataGirisSifre)
                registerControl = false
            }

            if(edSifreGuncelleYeniTekrar.text.isNullOrEmpty() || edSifreGuncelleYeniTekrar.text.isBlank())
            {
                edSifreGuncelleYeniTekrar.error = UserPortal.myLangResource!!.getString(R.string.hataGirisSifre)
                registerControl = false
            }else
            {
                if(edSifreGuncelleYeni.text.toString().length < 4)
                {
                    edSifreGuncelleYeni.error = UserPortal.myLangResource!!.getString(R.string.hataSifreUzunluk)
                    registerControl = false
                }
                else
                    if(edSifreGuncelleYeniTekrar.text.isNotBlank() && edSifreGuncelleYeniTekrar.text.isNotEmpty())
                    {
                        if(edSifreGuncelleYeniTekrar.text.toString() != edSifreGuncelleYeni.text.toString())
                        {
                            edSifreGuncelleYeniTekrar.error = UserPortal.myLangResource!!.getString(R.string.hataSifreEslesme)
                            registerControl = false
                        }
                    }
            }

            if(registerControl)
            {
                pbSifreDegis.visibility = View.VISIBLE
                val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
                val result = apiInterface?.sifreGuncelle("Bearer ${UserPortal.loggedInUser!!.AccessToken}",
                        edSifreGuncelleYeni.text.toString())
                result?.clone()?.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        pbSifreDegis.visibility = View.INVISIBLE
                        Toasty.error(this@ChangePasswordActivity,
                                UserPortal.myLangResource!!.getString(R.string.hataBaglantiBozuk),
                                Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        pbSifreDegis.visibility = View.INVISIBLE
                        if(response?.code() == 200)
                        {
                            Portal.sifreDegisDb(this@ChangePasswordActivity,edSifreGuncelleYeni.text.toString())
                            Toasty.success(this@ChangePasswordActivity,
                                    UserPortal.myLangResource!!.getString(R.string.islem_basarili),
                                    Toast.LENGTH_SHORT).show()
                            finish()
                        }else
                        {
                            Toasty.error(this@ChangePasswordActivity,
                                    UserPortal.myLangResource!!.getString(R.string.hataBirSeylerTers),
                                    Toast.LENGTH_LONG).show()
                        }
                    }

                })
            }


        }
    }
}
