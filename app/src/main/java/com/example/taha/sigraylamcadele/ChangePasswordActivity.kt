package com.example.taha.sigraylamcadele

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.taha.sigraylamcadele.API.ApiClient
import com.example.taha.sigraylamcadele.API.ApiInterface
import com.example.taha.sigraylamcadele.Library.Portal
import com.example.taha.sigraylamcadele.Library.UserPortal
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_sifre_degis.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sifre_degis)

        pbSifreDegis.visibility = View.INVISIBLE

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
